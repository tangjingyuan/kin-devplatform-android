package com.kin.ecosystem.data.blockchain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.kin.ecosystem.Callback;
import com.kin.ecosystem.CallbackAdapter;
import com.kin.ecosystem.base.ObservableData;
import com.kin.ecosystem.base.Observer;
import com.kin.ecosystem.data.model.BalanceUpdate;
import com.kin.ecosystem.data.model.Payment;
import com.kin.ecosystem.exception.InitializeException;
import com.kin.ecosystem.util.ExecutorsUtil.MainThreadExecutor;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import kin.core.Balance;
import kin.core.EventListener;
import kin.core.KinAccount;
import kin.core.KinClient;
import kin.core.ListenerRegistration;
import kin.core.PaymentInfo;
import kin.core.ResultCallback;
import kin.core.TransactionId;
import kin.core.exception.AccountNotFoundException;
import kin.core.exception.CreateAccountException;

public class BlockchainSourceImpl implements BlockchainSource {

    private static final String TAG = BlockchainSourceImpl.class.getSimpleName();

    private static volatile BlockchainSourceImpl instance;
    private final BlockchainSource.Local local;

    private KinClient kinClient;
    private KinAccount account;
    private ObservableData<BalanceUpdate> balance = ObservableData.create(new BalanceUpdate());
    /**
     * Listen for {@code completedPayment} in order to be notify about completed transaction sent to
     * the blockchain, it could failed or succeed.
     */
    private ObservableData<Payment> completedPayment = ObservableData.create();
    private static volatile AtomicInteger paymentObserversCount = new AtomicInteger(0);
    private static volatile AtomicInteger balanceObserversCount = new AtomicInteger(0);

    private ListenerRegistration paymentRegistration;
    private ListenerRegistration balanceRegistration;
    private ListenerRegistration accountCreationRegistration;
    private final MainThreadExecutor mainThread = new MainThreadExecutor();

    private String appID;
    private static final int MEMO_FORMAT_VERSION = 1;
    private static final String MEMO_DELIMITER = "-";
    private static final String MEMO_FORMAT =
        "%d" + MEMO_DELIMITER + "%s" + MEMO_DELIMITER + "%s"; // version-appID-orderID

    private static final int APP_ID_INDEX = 1;
    private static final int ORDER_ID_INDEX = 2;
    private static final int MEMO_SPLIT_LENGTH = 3;

    private BlockchainSourceImpl(@NonNull final Context context, @NonNull BlockchainSource.Local local)
        throws InitializeException {
        this.local = local;
        this.kinClient = new KinClient(context, Network.NETWORK_PRIVATE_TEST.getProvider());
        createKinAccountIfNeeded();
        getCurrentBalance();
    }

    public static void init(@NonNull final Context context, @NonNull BlockchainSource.Local local)
        throws InitializeException {
        if (instance == null) {
            synchronized (BlockchainSourceImpl.class) {
                if (instance == null) {
                    instance = new BlockchainSourceImpl(context, local);
                }
            }
        }
    }

    public static BlockchainSourceImpl getInstance() {
        return instance;
    }

    private void createKinAccountIfNeeded() throws InitializeException {
        account = kinClient.getAccount(0);
        if (account == null) {
            try {
                account = kinClient.addAccount();
                startAccountCreationListener();
            } catch (CreateAccountException e) {
                throw new InitializeException(e.getMessage());
            }
        }
    }

    @Override
    public void setAppID(String appID) {
        Log.d(TAG, "setAppID: " + appID);
        if (!TextUtils.isEmpty(appID)) {
            this.appID = appID;
        }
    }

    @Override
    public void sendTransaction(@NonNull String publicAddress, @NonNull BigDecimal amount,
        @NonNull final String orderID) {
        account.sendTransaction(publicAddress, amount, generateMemo(orderID)).run(
            new ResultCallback<TransactionId>() {
                @Override
                public void onResult(TransactionId result) {
                    Log.d(TAG, "sendTransaction onResult: " + result.id());
                }

                @Override
                public void onError(Exception e) {
                    completedPayment.setValue(new Payment(orderID, false, e.getMessage()));
                    Log.d(TAG, "sendTransaction onError: " + e.getMessage());
                }
            });
    }

    @SuppressLint("DefaultLocale")
    private String generateMemo(@NonNull final String orderID) {
        return String.format(MEMO_FORMAT, MEMO_FORMAT_VERSION, appID, orderID);
    }


    private void getCurrentBalance() {
        balance.postValue(getBalance());
        getBalance(new CallbackAdapter<BalanceUpdate>() {
        });
    }

    @Override
    public BalanceUpdate getBalance() {
        BalanceUpdate balanceUpdate = new BalanceUpdate();
        balanceUpdate.setAmount(new BigDecimal(local.getBalance()));
        return balanceUpdate;
    }

    @Override
    public void getBalance(@NonNull final Callback<BalanceUpdate> callback) {
        account.getBalance().run(new ResultCallback<Balance>() {
            @Override
            public void onResult(final Balance balanceObj) {
                setBalance(balanceObj);
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(balance.getValue());
                    }
                });
                Log.d(TAG, "getCurrentBalance onResult: " + balanceObj.value().intValue());
            }

            @Override
            public void onError(final Exception e) {
                mainThread.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (e instanceof AccountNotFoundException) {
                            callback.onResponse(balance.getValue());
                        } else {
                            callback.onFailure(e);
                        }
                    }
                });
                Log.d(TAG, "getCurrentBalance onError: " + e.getMessage());
            }
        });
    }

    private void setBalance(final Balance balanceObj) {
        BalanceUpdate balanceUpdate = balance.getValue();
        // if the values are not equals so we need to update,
        // no need to update for equal values.
        if (balanceUpdate.getAmount().compareTo(balanceObj.value()) != 0) {
            Log.d(TAG, "setBalance: Balance changed, should get update");
            balanceUpdate.setAmount(balanceObj.value());
            balance.postValue(balanceUpdate);
            local.setBalance(balanceObj.value().intValue());
        }
    }

    @Override
    public void addBalanceObserver(@NonNull Observer<BalanceUpdate> observer) {
        balance.addObserver(observer);
        observer.onChanged(balance.getValue());
    }

    @Override
    public void addBalanceObserverAndStartListen(@NonNull Observer<BalanceUpdate> observer) {
        addBalanceObserver(observer);
        Log.d(TAG, "addBalanceObserverAndStartListen: " + balanceObserversCount.get());
        if (balanceObserversCount.getAndIncrement() == 0) {
            startBalanceListener();
        }
    }

    private void startBalanceListener() {
        Log.d(TAG, "startBalanceListener: ");
        balanceRegistration = account.blockchainEvents()
            .addBalanceListener(new EventListener<Balance>() {
                @Override
                public void onEvent(Balance data) {
                    setBalance(data);
                }
            });
    }

    @Override
    public void removeBalanceObserver(@NonNull Observer<BalanceUpdate> observer) {
        Log.d(TAG, "removeBalanceObserver: ");
        balance.removeObserver(observer);
        decrementBalanceCount();
    }

    private void decrementBalanceCount() {
        if (balanceObserversCount.get() > 0 &&
            balanceObserversCount.decrementAndGet() == 0) {
            removeRegistration(balanceRegistration);
            Log.d(TAG, "decrementBalanceCount: removeRegistration");
        }
        Log.d(TAG, "decrementBalanceCount: " + balanceObserversCount.get());
    }

    @Override
    public void createTrustLine() {
        account.activate().run(new ResultCallback<Void>() {
            @Override
            public void onResult(Void result) {
                Log.d(TAG, "createTrustLine onResult");
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "createTrustLine onError");
            }
        });
    }

    @Override
    public String getPublicAddress() {
        if (account == null) {
            return null;
        }
        return account.getPublicAddress();
    }

    @Override
    public void addPaymentObservable(Observer<Payment> observer) {
        completedPayment.addObserver(observer);
        if (paymentObserversCount.getAndIncrement() == 0) {
            startPaymentListener();
        }
    }

    private void startPaymentListener() {
        paymentRegistration = account.blockchainEvents()
            .addPaymentListener(new EventListener<PaymentInfo>() {
                @Override
                public void onEvent(PaymentInfo data) {
                    String orderID = extractOrderId(data.memo());
                    Log.d(TAG, "startPaymentListener onEvent: the orderId: " + orderID + " with memo: " + data.memo());
                    if (orderID != null) {
                        completedPayment.setValue(new Payment(orderID, data.hash().id()));
                        Log.d(TAG, "completedPayment order id: " + orderID);
                    }
                    getCurrentBalance();
                }
            });
    }

    @Override
    public void removePaymentObserver(Observer<Payment> observer) {
        completedPayment.removeObserver(observer);
        decrementPaymentCount();
    }

    private void decrementPaymentCount() {
        if (paymentObserversCount.get() > 0 &&
            paymentObserversCount.decrementAndGet() == 0) {
            removeRegistration(paymentRegistration);
        }
    }

    private void removeRegistration(ListenerRegistration listenerRegistration) {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private void startAccountCreationListener() {
        Log.d(TAG, "startAccountCreationListener");
        accountCreationRegistration = account.blockchainEvents()
            .addAccountCreationListener(new EventListener<Void>() {
                @Override
                public void onEvent(Void data) {
                    createTrustLine();
                    removeRegistration(accountCreationRegistration);
                }
            });
    }

    private String extractOrderId(String memo) {
        String[] memoParts = memo.split(MEMO_DELIMITER);
        String orderID = null;
        if (memoParts.length == MEMO_SPLIT_LENGTH && memoParts[APP_ID_INDEX].equals(appID)) {
            orderID = memoParts[ORDER_ID_INDEX];
        }
        return orderID;
    }
}
