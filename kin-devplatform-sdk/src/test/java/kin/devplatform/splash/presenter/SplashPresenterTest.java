package kin.devplatform.splash.presenter;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import kin.devplatform.BaseTestClass;
import kin.devplatform.KinCallback;
import kin.devplatform.bi.EventLogger;
import kin.devplatform.bi.events.BackButtonOnWelcomeScreenPageTapped;
import kin.devplatform.bi.events.WelcomeScreenButtonTapped;
import kin.devplatform.bi.events.WelcomeScreenPageViewed;
import kin.devplatform.data.auth.AuthDataSource;
import kin.devplatform.splash.view.ISplashView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@RunWith(JUnit4.class)
public class SplashPresenterTest extends BaseTestClass {

	@Mock
	private AuthDataSource authRepository;

	@Mock
	private EventLogger eventLogger;

	@Mock
	private ISplashView splashView;

	@Captor
	private ArgumentCaptor<KinCallback<Void>> activateCapture;

	private SplashPresenter splashPresenter;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		MockitoAnnotations.initMocks(this);
		splashPresenter = new SplashPresenter(authRepository, eventLogger);
		splashPresenter.onAttach(splashView);
		assertNotNull(splashPresenter.getView());
		verify(eventLogger).send(any(WelcomeScreenPageViewed.class));
	}

	@After
	public void tearDown() throws Exception {
		splashPresenter.onDetach();
		assertNull(splashPresenter.getView());
	}

	@Test
	public void getStartedClicked_AnimationEndedNavigateMP() throws Exception {
		splashPresenter.getStartedClicked();
		verify(eventLogger).send(any(WelcomeScreenButtonTapped.class));
		verify(authRepository).activateAccount(activateCapture.capture());

		activateCapture.getValue().onResponse(null);
		verify(splashView, times(0)).navigateToMarketPlace();
		verify(splashView, times(1)).animateLoading();

		splashPresenter.onAnimationEnded();
		verify(splashView, times(1)).navigateToMarketPlace();
	}

	@Test
	public void getStartedClicked_CallbackSuccessNavigateMP() throws Exception {
		splashPresenter.getStartedClicked();
		verify(authRepository).activateAccount(activateCapture.capture());
		verify(splashView, times(1)).animateLoading();

		splashPresenter.onAnimationEnded();
		verify(splashView, times(0)).navigateToMarketPlace();

		activateCapture.getValue().onResponse(null);
		verify(splashView, times(1)).navigateToMarketPlace();
	}

	@Test
	public void getStartedClicked_CallbackFailed_Reset() throws Exception {
		splashPresenter.getStartedClicked();
		verify(authRepository).activateAccount(activateCapture.capture());

		splashPresenter.onAnimationEnded();
		activateCapture.getValue().onFailure(null);
		verify(splashView, times(1)).animateLoading();
		verify(splashView, times(0)).navigateToMarketPlace();

		verify(splashView, times(1)).showToast("Oops something went wrong...");
		verify(splashView, times(1)).stopLoading(true);
	}

	@Test
	public void backButtonPressed_NavigateBack() throws Exception {
		splashPresenter.backButtonPressed();
		verify(eventLogger).send(any(BackButtonOnWelcomeScreenPageTapped.class));
		verify(splashView, times(1)).navigateBack();
	}
}