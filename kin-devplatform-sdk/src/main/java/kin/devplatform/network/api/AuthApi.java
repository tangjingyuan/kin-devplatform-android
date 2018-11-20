/*
 * Kin Ecosystem
 * Apis for client to server interaction
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package kin.devplatform.network.api;

import static kin.devplatform.core.network.ApiClient.PATCH;
import static kin.devplatform.core.network.ApiClient.POST;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kin.devplatform.Configuration;
import kin.devplatform.core.network.ApiCallback;
import kin.devplatform.core.network.ApiClient;
import kin.devplatform.core.network.ApiException;
import kin.devplatform.core.network.ApiResponse;
import kin.devplatform.core.network.Pair;
import kin.devplatform.core.network.ProgressRequestBody;
import kin.devplatform.core.network.ProgressResponseBody;
import kin.devplatform.network.model.AuthToken;
import kin.devplatform.network.model.SignInData;
import kin.devplatform.network.model.UserProperties;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.Response;


public class AuthApi {

	private ApiClient apiClient;

	public AuthApi() {
		this(Configuration.getDefaultApiClient());
	}

	public AuthApi(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}


	/**
	 * Build call for activateAcount
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param progressListener Progress listener
	 * @param progressRequestListener Progress request listener
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call activateAcountCall(String X_REQUEST_ID, final ProgressResponseBody.ProgressListener progressListener,
		final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Object localVarPostBody = null;

		// create path and map variables
		String localVarPath = "/users/me/activate";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {

		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		if (progressListener != null) {
			apiClient.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {
					Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder()
						.body(new ProgressResponseBody(originalResponse.body(), progressListener))
						.build();
				}
			});
		}

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, POST, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
	}

	@SuppressWarnings("rawtypes")
	private Call activateAcountValidateBeforeCall(String X_REQUEST_ID,
		final ProgressResponseBody.ProgressListener progressListener,
		final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling activateAcount(Async)");
		}

		Call call = activateAcountCall(X_REQUEST_ID, progressListener, progressRequestListener);
		return call;


	}

	/**
	 * Activate account
	 * Activate account by accepting TOS
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return AuthToken
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public AuthToken activateAcount(String X_REQUEST_ID) throws ApiException {
		ApiResponse<AuthToken> resp = activateAcountWithHttpInfo(X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * Activate account
	 * Activate account by accepting TOS
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;AuthToken&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<AuthToken> activateAcountWithHttpInfo(String X_REQUEST_ID) throws ApiException {
		Call call = activateAcountValidateBeforeCall(X_REQUEST_ID, null, null);
		Type localVarReturnType = new TypeToken<AuthToken>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * Activate account (asynchronously)
	 * Activate account by accepting TOS
	 *
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call activateAcountAsync(String X_REQUEST_ID, final ApiCallback<AuthToken> callback) throws ApiException {

		ProgressResponseBody.ProgressListener progressListener = null;
		ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

		if (callback != null) {
			progressListener = new ProgressResponseBody.ProgressListener() {
				@Override
				public void update(long bytesRead, long contentLength, boolean done) {
					callback.onDownloadProgress(bytesRead, contentLength, done);
				}
			};

			progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
				@Override
				public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
					callback.onUploadProgress(bytesWritten, contentLength, done);
				}
			};
		}

		Call call = activateAcountValidateBeforeCall(X_REQUEST_ID, progressListener, progressRequestListener);
		Type localVarReturnType = new TypeToken<AuthToken>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Build call for signIn
	 *
	 * @param signindata (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param progressListener Progress listener
	 * @param progressRequestListener Progress request listener
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call signInCall(SignInData signindata, String X_REQUEST_ID,
		final ProgressResponseBody.ProgressListener progressListener,
		final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Object localVarPostBody = signindata;

		// create path and map variables
		String localVarPath = "/users";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();
		if (X_REQUEST_ID != null) {
			localVarHeaderParams.put("X-REQUEST-ID", apiClient.parameterToString(X_REQUEST_ID));
		}

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json", "application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		if (progressListener != null) {
			apiClient.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(Interceptor.Chain chain) throws IOException {
					Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder()
						.body(new ProgressResponseBody(originalResponse.body(), progressListener))
						.build();
				}
			});
		}

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, POST, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
	}

	@SuppressWarnings("rawtypes")
	private Call signInValidateBeforeCall(SignInData signindata, String X_REQUEST_ID,
		final ProgressResponseBody.ProgressListener progressListener,
		final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

		// verify the required parameter 'signindata' is set
		if (signindata == null) {
			throw new ApiException("Missing the required parameter 'signindata' when calling signIn(Async)");
		}

		// verify the required parameter 'X_REQUEST_ID' is set
		if (X_REQUEST_ID == null) {
			throw new ApiException("Missing the required parameter 'X_REQUEST_ID' when calling signIn(Async)");
		}

		Call call = signInCall(signindata, X_REQUEST_ID, progressListener, progressRequestListener);
		return call;


	}

	/**
	 * Sign in/ Log in
	 * Sign a user into kin marketplace
	 *
	 * @param signindata (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return AuthToken
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public AuthToken signIn(SignInData signindata, String X_REQUEST_ID) throws ApiException {
		ApiResponse<AuthToken> resp = signInWithHttpInfo(signindata, X_REQUEST_ID);
		return resp.getData();
	}

	/**
	 * Sign in/ Log in
	 * Sign a user into kin marketplace
	 *
	 * @param signindata (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @return ApiResponse&lt;AuthToken&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<AuthToken> signInWithHttpInfo(SignInData signindata, String X_REQUEST_ID) throws ApiException {
		Call call = signInValidateBeforeCall(signindata, X_REQUEST_ID, null, null);
		Type localVarReturnType = new TypeToken<AuthToken>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
	}

	/**
	 * Sign in/ Log in (asynchronously)
	 * Sign a user into kin marketplace
	 *
	 * @param signindata (required)
	 * @param X_REQUEST_ID A unique id for the request. A retransmitted request will have the same id  (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call signInAsync(SignInData signindata, String X_REQUEST_ID, final ApiCallback<AuthToken> callback)
		throws ApiException {

		ProgressResponseBody.ProgressListener progressListener = null;
		ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

		if (callback != null) {
			progressListener = new ProgressResponseBody.ProgressListener() {
				@Override
				public void update(long bytesRead, long contentLength, boolean done) {
					callback.onDownloadProgress(bytesRead, contentLength, done);
				}
			};

			progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
				@Override
				public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
					callback.onUploadProgress(bytesWritten, contentLength, done);
				}
			};
		}

		Call call = signInValidateBeforeCall(signindata, X_REQUEST_ID, progressListener, progressRequestListener);
		Type localVarReturnType = new TypeToken<AuthToken>() {
		}.getType();
		apiClient.executeAsync(call, localVarReturnType, callback);
		return call;
	}

	/**
	 * Update user (asynchronously) Update user - wallet address
	 *
	 * @param userproperties (required)
	 * @param callback The callback to be executed when the API call finishes
	 * @return The request call
	 * @throws ApiException If fail to process the API call, e.g. serializing the request body object
	 */
	public Call updateUserAsync(UserProperties userproperties, final ApiCallback<Void> callback) throws ApiException {
		Call call = updateUserValidateBeforeCall(userproperties);
		apiClient.executeAsync(call, callback);
		return call;
	}

	@SuppressWarnings("rawtypes")
	private Call updateUserValidateBeforeCall(UserProperties userproperties) throws ApiException {

		// verify the required parameter 'userproperties' is set
		if (userproperties == null) {
			throw new ApiException("Missing the required parameter 'userproperties' when calling updateUser(Async)");
		}

		Call call = updateUserCall(userproperties);
		return call;
	}

	/**
	 * Build call for updateUser
	 *
	 * @param userproperties (required)
	 * @return Call to execute
	 * @throws ApiException If fail to serialize the request body object
	 */
	public Call updateUserCall(UserProperties userproperties) throws ApiException {
		Object localVarPostBody = userproperties;

		// create path and map variables
		String localVarPath = "/users";

		List<Pair> localVarQueryParams = new ArrayList<Pair>();
		List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

		Map<String, String> localVarHeaderParams = new HashMap<String, String>();

		Map<String, Object> localVarFormParams = new HashMap<String, Object>();

		final String[] localVarAccepts = {
			"application/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}

		final String[] localVarContentTypes = {
			"application/json"
		};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		String[] localVarAuthNames = new String[]{};
		return apiClient
			.buildCall(localVarPath, PATCH, localVarQueryParams, localVarCollectionQueryParams, localVarPostBody,
				localVarHeaderParams, localVarFormParams, localVarAuthNames, null);
	}

}
