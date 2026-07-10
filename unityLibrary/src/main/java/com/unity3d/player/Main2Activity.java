package com.unity3d.player;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.appmb.sdk.mbauth.MbAuth;
import com.appmb.sdk.mbauth.ui.login.AuthResult;
import com.appmb.sdk.mbcore.config.MbSdkConfig;
import com.appmb.sdk.mbcore.config.TrackingConfig;
import com.appmb.sdk.mbcore.model.MbAuthData;
import com.appmb.sdk.mbpayment.MbPayment;
import com.appmb.sdk.mbpayment.model.PurchaseResult;
import com.kksoft.sdk.KKSoftAndroidSdk;
import com.unity3d.player.dialog.TokenManagerDialogFragment;
import com.unity3d.player.dialog.TokenManagerDialogListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Main2Activity extends UnityPlayerActivity implements TokenManagerDialogListener {
	private MbAuthData mbAuthData = null;
	
	private static final int AUTHENTICATING_CODE = 1234;
	private static final String DEFAULT_GAME_ID = "1";
	private static final String DEFAULT_SERVER_CLIENT_ID = "IOS1";
	
	private SharedPreferences sharedPreferences;
	
	private BroadcastReceiver purchaseReceiver;
	private boolean reopenMenuOnResume = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AUTHENTICATING_CODE && resultCode == RESULT_CANCELED) {
			showErrorResult("Cancelled");
			return;
		}
		if (requestCode != AUTHENTICATING_CODE || resultCode != RESULT_OK) {
			return;
		}

		AuthResult authResult = data != null ? data.getParcelableExtra("authResult") : null;
		Log.d("ClientApp", "Result from SDK: " + authResult);
		handleAuthResult(authResult);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (reopenMenuOnResume) {
			reopenMenuOnResume = false;
			showTokenManagerDialog();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
		initializeSdk();
		showTokenManagerDialog();

		purchaseReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (MbPayment.ACTION_PURCHASE_DONE.equals(intent.getAction())) {
					PurchaseResult purchaseResult = intent.getParcelableExtra(MbPayment.EXTRA_PURCHASE_RESULT);
					Log.d("ClientApp", "Purchase result: " + purchaseResult);
					if (purchaseResult instanceof PurchaseResult.PurchasedSuccess) {
						PurchaseResult.PurchasedSuccess purchasedSuccess = (PurchaseResult.PurchasedSuccess) purchaseResult;
						Log.d("ClientApp", "Purchase result successful: " + purchasedSuccess.getProductName());
					
					} else if (purchaseResult instanceof PurchaseResult.PurchasedError) {
						Log.d("ClientApp", "Purchase result error: " + purchaseResult);
						
					} else if (purchaseResult instanceof PurchaseResult.PurchasedFailure) {
						Log.d("ClientApp", "Purchase result failed: " + purchaseResult);
					
					} else if (purchaseResult instanceof PurchaseResult.ClosedProductList) {
						Log.d("ClientApp", "Purchase Closed. " + purchaseResult);
					
					} else if (purchaseResult instanceof PurchaseResult.PurchasedUserNotAuthenticated) {
						Log.d("ClientApp", "Purchase result failure as an user is not authenticated. " + purchaseResult);
						
					}
					showTokenManagerDialogFromCachedToken();
				}
			}
		};
		
		KKSoftAndroidSdk.startCheckingForceUpdate(this, AUTHENTICATING_CODE);

		IntentFilter purchaseFilter = new IntentFilter(MbPayment.ACTION_PURCHASE_DONE);
		LocalBroadcastManager.getInstance(this).registerReceiver(purchaseReceiver, purchaseFilter);
	}
	
	@Override
	public void recreate() {
		super.recreate();
		showTokenManagerDialog();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (purchaseReceiver != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(purchaseReceiver);
		}
	}
	
	private void handleAuthResult(AuthResult authResult) {
		if (authResult instanceof AuthResult.Failure) {
			handleFailure((AuthResult.Failure) authResult);
		} else if (authResult instanceof AuthResult.RegisterSuccess) {
			handleAuthenticatedUser(((AuthResult.RegisterSuccess) authResult).getUser());
		} else if (authResult instanceof AuthResult.AuthSuccess) {
			handleAuthenticatedUser(((AuthResult.AuthSuccess) authResult).getUser());
		} else if (authResult instanceof AuthResult.Logout) {
			AuthResult.Logout logout = (AuthResult.Logout) authResult;
			if (logout.isLogoutSuccess()) {
				clearSessionAndOpenLogin();
			} else {
				showErrorResult("Logout failed");
			}
		} else if (authResult instanceof AuthResult.DeactivateAccount) {
			AuthResult.DeactivateAccount deactivateAccount = (AuthResult.DeactivateAccount) authResult;
			if (deactivateAccount.isSuccess()) {
				clearSession();
				showTokenManagerDialog();
			} else {
				clearSession();
				showErrorResult("Deactivate account failed");
			}
		} else if (authResult instanceof AuthResult.LinkAccount) {
			AuthResult.LinkAccount linkAccount = (AuthResult.LinkAccount) authResult;
			boolean isGuest = linkAccount.getUser().isGuest();
			SharePreferenceUtil.setIsGuest(isGuest, sharedPreferences.edit());
			handleAuthenticatedUser(linkAccount.getUser());
		} else {
			showTokenManagerDialog();
		}
	}

	private void handleAuthenticatedUser(MbAuthData authData) {
		mbAuthData = authData;
		SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
		showTokenManagerDialog();
	}

	private void handleFailure(AuthResult.Failure failure) {
		clearSession();
		showErrorResult(failure.getMsg());
	}

	private void clearSession() {
		mbAuthData = null;
		SharePreferenceUtil.clear(sharedPreferences);
	}

	private void showErrorResult(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Authentication Error:\n" + message);
		builder.setPositiveButton("Menu", (dialog, which) -> {
			dialog.dismiss();
			showTokenManagerDialog();
		});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private void showTokenManagerDialog() {
		MbAuth.getSessionDatas(new Function1<AuthResult, Unit>() {
			@Override
			public Unit invoke(AuthResult authResult) {
				if (authResult instanceof AuthResult.AuthSuccess) {
					AuthResult.AuthSuccess authSuccess = (AuthResult.AuthSuccess) authResult;
					mbAuthData = authSuccess.getUser();
					SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
					
					TokenManagerDialogFragment.newInstance(mbAuthData.getAccessToken())
							.show(getFragmentManager(), "tokenManager");
				} else {
					KKSoftAndroidSdk.startAuthentication(Main2Activity.this, AUTHENTICATING_CODE);
				}
				return null;
			}
		});
	}

	private void showTokenManagerDialogFromCachedToken() {
		String accessToken = mbAuthData != null
				? mbAuthData.getAccessToken()
				: SharePreferenceUtil.getAccessToken(sharedPreferences);
		TokenManagerDialogFragment.newInstance(accessToken)
				.show(getFragmentManager(), "tokenManager");
	}

	private void clearSessionAndOpenLogin() {
		reopenMenuOnResume = false;
		clearSession();
		dismissTokenManagerDialog();
		KKSoftAndroidSdk.startAuthentication(Main2Activity.this, AUTHENTICATING_CODE);
	}

	private void dismissTokenManagerDialog() {
		DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag("tokenManager");
		if (dialogFragment != null) {
			dialogFragment.dismissAllowingStateLoss();
		}
	}
	
	@Override
	public void onDialogDismissed(String data) {
		if ("SignUp_Login".equals(data)) {
			Log.d("Main2Activity", "sign-up-login");
			KKSoftAndroidSdk.startAuthentication(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Logout".equals(data)) {
			Log.d("Main2Activity", "logout");
			KKSoftAndroidSdk.startLogout(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Deactivate_Account".equals(data)) {
			Log.d("Main2Activity", "deactivate");
			KKSoftAndroidSdk.startDeactivateAccount(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Change_Game_Server".equals(data)) {
			Log.d("Main2Activity", "change-game-server");
			KKSoftAndroidSdk.startChangeGameServer(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("PURCHASE".equals(data)) {
			Log.d("Main2Activity", "purchase");
			KKSoftAndroidSdk.startPayment(Main2Activity.this);
		}
		if ("Link_Account".equals(data)) {
			Log.d("Main2Activity", "link-account");
			KKSoftAndroidSdk.startLinkAccount(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("GAME_TRACKING_TEST".equals(data)) {
			Log.d("Main2Activity", "game-tracking-test");
			startGameTrackingTestActivity();
		}
	}

	private void initializeSdk() {
		TrackingConfig trackingConfig = new TrackingConfig.Builder()
				.enableFirebase(!isBlank(BuildConfig.firebaseAppID), BuildConfig.firebaseAppID)
				.enableAdjust(!isBlank(BuildConfig.adjustToken), BuildConfig.adjustId, BuildConfig.adjustToken)
				.enableTikTok(!isBlank(BuildConfig.tiktokAppID), BuildConfig.tiktokAppID, BuildConfig.tiktokAccessToken)
				.enableMeta(!isBlank(BuildConfig.facebookAppID), BuildConfig.facebookAppID, BuildConfig.facebookClientToken, BuildConfig.facebookDisplayName)
				.enableAppFlyers(!isBlank(BuildConfig.appFlyersDevKey), BuildConfig.appFlyersId, BuildConfig.appFlyersDevKey)
				.enableGid(BuildConfig.gidClientID)
				.build();

		MbSdkConfig config = new MbSdkConfig.Builder()
				.setBaseUrl(BuildConfig.isStaging ? "https://api-staging.kksoft.vn" : null)
				.setAppId(getPackageName())
				.setGameId(DEFAULT_GAME_ID)
				.setServerClientId(DEFAULT_SERVER_CLIENT_ID)
				.setAppVersionName(getVersionName())
				.setGoogleClientId(BuildConfig.gidClientID)
				.setFacebookAppId(BuildConfig.facebookAppID)
				.setFacebookClientToken(BuildConfig.facebookClientToken)
				.setTrackingConfig(trackingConfig)
				.build();

		KKSoftAndroidSdk.init(this, config);
	}

	private static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private String getVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "1.0.0";
		}
	}
	
	public void startGameTrackingTestActivity() {
		Intent intent = new Intent(this, GameTrackingTestActivity.class);
		reopenMenuOnResume = true;
		startActivity(intent);
	}

}
