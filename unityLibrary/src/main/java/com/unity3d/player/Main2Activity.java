package com.unity3d.player;

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
import com.appmb.sdk.mbauth.model.UpdateServerIdResult;
import com.appmb.sdk.mbauth.ui.login.AuthResult;
import com.appmb.sdk.mbauth.worker.TimerWorker;
import com.appmb.sdk.mbcore.model.MbAuthData;
import com.appmb.sdk.mbpayment.MbPayment;
import com.appmb.sdk.mbpayment.model.PurchaseResult;
import com.unity3d.player.dialog.TokenManagerDialogFragment;
import com.unity3d.player.dialog.TokenManagerDialogListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class Main2Activity extends UnityPlayerActivity implements TokenManagerDialogListener {
	// Result from launcher SDK
	private String errorMessage = "";
	private MbAuthData mbAuthData = null;
	
	private int AUTHENTICATING_CODE = 1234;
	
	private SharedPreferences sharedPreferences;
	
	private BroadcastReceiver timerReceiver;
	private BroadcastReceiver purchaseReceiver;
	private BroadcastReceiver tokenExpiratoryReceiver;
	private BroadcastReceiver userBlockReceiver;
	private BroadcastReceiver serverMaintenanceReceiver;
	private boolean reopenMenuOnResume = false;

	private final Function1<AuthResult, Unit> onRefreshTokenResult = new Function1<AuthResult, Unit>() {
		@Override
		public Unit invoke(AuthResult authResult) {
			if (authResult instanceof AuthResult.AuthSuccess) {
				AuthResult.AuthSuccess authSuccess = (AuthResult.AuthSuccess) authResult;
				mbAuthData = authSuccess.getUser();
				errorMessage = "";
				SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
				showAuthenticationResult(
						mbAuthData.getAccessToken(),
						mbAuthData.getUserId(),
						mbAuthData.getGameUuid(),
						mbAuthData.getServerId(),
						false);
			} else if (authResult instanceof AuthResult.Failure) {
				AuthResult.Failure failure = (AuthResult.Failure) authResult;
				errorMessage = failure.getMsg();
				mbAuthData = null;
				SharePreferenceUtil.clear(sharedPreferences);
				showAuthenticationResult(errorMessage,
						null,
						null,
						null,
						true);
			}
			return null;
		}
	};
	
	private final Function1<AuthResult, Unit> onLatestSessionResult = new Function1<AuthResult, Unit>() {
		@Override
		public Unit invoke(AuthResult authResult) {
			if (authResult instanceof AuthResult.AuthSuccess) {
				AuthResult.AuthSuccess authSuccess = (AuthResult.AuthSuccess) authResult;
				mbAuthData = authSuccess.getUser();
				errorMessage = "";
				SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
				showAuthenticationResult(
						mbAuthData.getAccessToken(),
						mbAuthData.getUserId(),
						mbAuthData.getGameUuid(),
						mbAuthData.getServerId(),
						false);
			} else if (authResult instanceof AuthResult.Failure) {
				AuthResult.Failure failure = (AuthResult.Failure) authResult;
				errorMessage = failure.getMsg();
				mbAuthData = null;
				SharePreferenceUtil.clear(sharedPreferences);
				showAuthenticationResult(errorMessage,  null, null, null, true);
			}
			return null;
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == AUTHENTICATING_CODE) {
				AuthResult authResult = data != null ? data.getParcelableExtra("authResult") : null;
				Log.d("ClientApp", "Result from SDK: " + authResult);
				if (authResult instanceof AuthResult.Failure) {
					AuthResult.Failure failure = (AuthResult.Failure) authResult;
					errorMessage = failure.getMsg();
					mbAuthData = null;
					SharePreferenceUtil.clear(sharedPreferences);
					showAuthenticationResult(errorMessage, null, null, null,true);
				
				} else if (authResult instanceof AuthResult.RegisterSuccess) {
					AuthResult.RegisterSuccess registerSuccess = (AuthResult.RegisterSuccess) authResult;
					errorMessage = "";
					mbAuthData = registerSuccess.getUser();
					SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
					showAuthenticationResult(
							mbAuthData.getAccessToken(),
							mbAuthData.getUserId(),
							mbAuthData.getGameUuid(),
							mbAuthData.getServerId(),
							false);
				
				} else if (authResult instanceof AuthResult.AuthSuccess) {
					AuthResult.AuthSuccess authSuccess = (AuthResult.AuthSuccess) authResult;
					mbAuthData = authSuccess.getUser();
					if (Boolean.TRUE.equals(authSuccess.getUser().isGuest())) {
						MbAuth.countDownForGuestAsync(new Function0<Unit>() {
							@Override
							public Unit invoke() {
								return null;
							}
						});
					}
					errorMessage = "";
					SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
					showAuthenticationResult(
							mbAuthData.getAccessToken(),
							mbAuthData.getUserId(),
							mbAuthData.getGameUuid(),
							mbAuthData.getServerId(),
							false);
				} else if (authResult instanceof AuthResult.Logout) {
					mbAuthData = null;
					errorMessage = "";
					SharePreferenceUtil.clear(sharedPreferences);
					showAuthenticationResult("Logout Successfully", null,null,null,false);
				
				} else if (authResult instanceof AuthResult.DeactivateAccount) {
					AuthResult.DeactivateAccount deactivateAccount = (AuthResult.DeactivateAccount) authResult;
					if (deactivateAccount.isSuccess()) {
						mbAuthData = null;
						errorMessage = "";
						SharePreferenceUtil.clear(sharedPreferences);
						showAuthenticationResult("Deactivate account Successfully", null,null,null,false);
					} else {
						errorMessage = "Deactivate account failed";
						SharePreferenceUtil.clear(sharedPreferences);
						showAuthenticationResult(errorMessage, null,null,null,true);
					}
					
				} else if (authResult instanceof AuthResult.LinkAccount) {
					AuthResult.LinkAccount linkAccount = (AuthResult.LinkAccount) authResult;
					boolean isGuest = linkAccount.getUser().isGuest();
					SharePreferenceUtil.setIsGuest(isGuest, sharedPreferences.edit());
					
					if (isGuest) {
						MbAuth.countDownForGuestAsync(new Function0<Unit>() {
							@Override
							public Unit invoke() {
								return null;
							}
						});
					}
					mbAuthData = linkAccount.getUser();
					errorMessage = "";
					showAuthenticationResult(
							mbAuthData.getAccessToken(),
							mbAuthData.getUserId(),
							mbAuthData.getGameUuid(),
							mbAuthData.getServerId(),
							false);
				} else if(authResult instanceof AuthResult.RepeatableRemindLinkAccount) {
					if (((AuthResult.RepeatableRemindLinkAccount) authResult).isRepeated()) {
						MbAuth.countDownForGuestAsync(new Function0<Unit>() {
							@Override
							public Unit invoke() {
								return null;
							}
						});
					}
				}
			}
		}
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
//		MixpanelAPI.getInstance(this, AnalyticsProperties.Companion.getToken(), true);
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
		showTokenManagerDialog();
		
		MbAuth.updateServerClientId("IOS1", result -> {
			if (result instanceof UpdateServerIdResult.Success) {
				// Success - gameUuid and characterId are saved
				UpdateServerIdResult.Success success = (UpdateServerIdResult.Success) result;
				String gameUuid = success.getAuthData().getGameUuid();
				String serverId = success.getAuthData().getServerId();
				String characterId = success.getCharacterId();
				// Note: characterId is also saved to local storage automatically by the API

				Log.d("Main2Activity", "Update Server Client ID Success!");
				Log.d("Main2Activity", "GameUUID: " + gameUuid);
				Log.d("Main2Activity", "ServerID: " + serverId);
				Log.d("Main2Activity", "CharacterID: " + characterId);

			} else if (result instanceof UpdateServerIdResult.Error) {
				// Error - handle different error codes
				UpdateServerIdResult.Error error = (UpdateServerIdResult.Error) result;
				int code = error.getCode();
				String message = error.getMessage();

				Log.e("Main2Activity", "Update Server Client ID Error!");
				Log.e("Main2Activity", "Code: " + code);
				Log.e("Main2Activity", "Message: " + message);
			}
			return null;
		});
		
		// Initialize and register the receiver
		timerReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (TimerWorker.ACTION_TIMER_DONE.equals(intent.getAction())) {
					MbAuth.startLinkAccountScreenForResult(
							Main2Activity.this,
							AUTHENTICATING_CODE
					);
				}
			}
		};
		
		// Initialize and register the receiver
		tokenExpiratoryReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (MbAuth.ACTION_TOKEN_EXPIRATION.equals(intent.getAction())) {
					MbAuth.startTokenExpirationForResult(Main2Activity.this, AUTHENTICATING_CODE);
				}
			}
		};
		
		// Initialize and register the receiver
		userBlockReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (MbAuth.ACTION_USER_BLOCKED.equals(intent.getAction())) {
					MbAuth.startUserBlock(Main2Activity.this);
				}
			}
		};

		serverMaintenanceReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (MbAuth.ACTION_SERVER_MAINTENANCE.equals(intent.getAction())) {
					MbAuth.startServerMaintenance(Main2Activity.this);
				}
			}
		};
		
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
					showTokenManagerDialog();
				}
			}
		};
		
		MbAuth.startCheckingForceUpdateForResult(this, AUTHENTICATING_CODE);
		
		Log.d("Main2Activity", "onCreate: Calling MbAuth.countDownForGuest()");
		MbAuth.countDownForGuest();
		
		IntentFilter filter = new IntentFilter(TimerWorker.ACTION_TIMER_DONE);
		LocalBroadcastManager.getInstance(this).registerReceiver(timerReceiver, filter);
		
		IntentFilter tokenExpiratoryFilter = new IntentFilter(MbAuth.ACTION_TOKEN_EXPIRATION);
		LocalBroadcastManager.getInstance(this).registerReceiver(tokenExpiratoryReceiver, tokenExpiratoryFilter);
		
		IntentFilter userBlockFilter = new IntentFilter(MbAuth.ACTION_USER_BLOCKED);
		LocalBroadcastManager.getInstance(this).registerReceiver(userBlockReceiver, userBlockFilter);

		IntentFilter serverMaintenanceBlockFilter = new IntentFilter(MbAuth.ACTION_SERVER_MAINTENANCE);
		LocalBroadcastManager.getInstance(this).registerReceiver(serverMaintenanceReceiver, serverMaintenanceBlockFilter);

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
		if (timerReceiver != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(timerReceiver);
		}
		if (purchaseReceiver != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(purchaseReceiver);
		}
		if (tokenExpiratoryReceiver != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(tokenExpiratoryReceiver);
		}
		if (userBlockReceiver != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(userBlockReceiver);
		}
		if (serverMaintenanceReceiver != null) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(serverMaintenanceReceiver);
		}
	}
	
	private void showAuthenticationResult(String accessToken, String userId, String gameUid, String serverId, boolean isError) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (isError) {
			builder.setTitle("Error");
		} else {
			showTokenManagerDialog();
			return;
		}
		
		if (isError) {
			builder.setTitle("Authentication Error:\n" + accessToken);
		} else {
			String mTK = truncateAtWord(accessToken, 4);
			builder.setMessage("Your access token is:\n" + mTK + "...\n" +
					"User ID: " + userId + "\n" +
					"Game UUID: " + gameUid + "\n" +
					"Server ID: " + serverId);
		}
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
					errorMessage = "";
					SharePreferenceUtil.saveAccessToken(mbAuthData.getAccessToken(), sharedPreferences.edit());
					
					TokenManagerDialogFragment.newInstance(mbAuthData.getAccessToken())
							.show(getFragmentManager(), "tokenManager");
				} else {
					MbAuth.startAuthenticationForResult(Main2Activity.this, AUTHENTICATING_CODE);
				}
				return null;
			}
		});
	}
	
	@Override
	public void onDialogDismissed(String data) {
		if ("SignUp_Login".equals(data)) {
			Log.d("Main2Activity", "sign-up-login");
			MbAuth.startAuthenticationForResult(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Logout".equals(data)) {
			Log.d("Main2Activity", "logout");
			MbAuth.startLogoutForResult(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Deactivate_Account".equals(data)) {
			Log.d("Main2Activity", "deactivate");
			MbAuth.deactivateAccountForResult(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Change_Game_Server".equals(data)) {
			Log.d("Main2Activity", "change-game-server");
			MbAuth.changeGameServerForResult(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Refresh_Token".equals(data)) {
			Log.d("Main2Activity", "refresh-token");
			MbAuth.refreshTokenForResult(Main2Activity.this, AUTHENTICATING_CODE, onRefreshTokenResult);
		}
		if ("Latest_Session_Info".equals(data)) {
			Log.d("Main2Activity", "latest-session-info");
			MbAuth.getSessionDatas(onLatestSessionResult);
		}
		if ("PURCHASE".equals(data)) {
			Log.d("Main2Activity", "purchase");
			reopenMenuOnResume = true;
			MbPayment.startPayment(Main2Activity.this);
		}
		if ("USER_BLOCKED".equals(data)) {
			Log.d("Main2Activity", "user-blocked");
			MbAuth.startUserBlock(Main2Activity.this);
		}
		if ("Token_Expiration".equals(data)) {
			Log.d("Main2Activity", "token-expiration");
			MbAuth.startTokenExpirationForResult(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("Link_Account".equals(data)) {
			Log.d("Main2Activity", "link-account");
			MbAuth.startLinkAccountScreenForResult(Main2Activity.this, AUTHENTICATING_CODE);
		}
		if ("GAME_TRACKING_TEST".equals(data)) {
			Log.d("Main2Activity", "game-tracking-test");
			startGameTrackingTestActivity();
		}
		if ("Update_Server_Client_Id".equals(data)) {
			Log.d("Main2Activity", "update-server-client-id");
			handleUpdateServerClientId();
		}
	}
	
	/**
	 * Launches the Game Tracking Test Activity.
	 * Can be called from Unity or directly from Java code.
	 * Demo method showing how to use updateServerClientId API
	 * This demonstrates updating server ID with example server IDs: "1", "2", "3"
	 * Uses the static MbAuth.updateServerClientId() method - same pattern as changeGameServerForResult()
	 */
	public void startGameTrackingTestActivity() {
		Intent intent = new Intent(this, GameTrackingTestActivity.class);
		reopenMenuOnResume = true;
		startActivity(intent);
	}

	private void handleUpdateServerClientId() {
		// Show dialog to select server name
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Server Name");
		
		// Array of example server names
		final String[] serverNames = {"IOS1", "S1"};
		
		builder.setItems(serverNames, (dialog, which) -> {
			String selectedServerName = serverNames[which];
			Log.d("Main2Activity", "Selected Server Name: " + selectedServerName);
			
			// Call the updateServerClientId API using static method (same pattern as changeGameServerForResult)
			// The API will fetch server list and find serverId by serverName
			MbAuth.updateServerClientId(selectedServerName, result -> {
				if (result instanceof UpdateServerIdResult.Success) {
					// Success - gameUuid and characterId are saved
					UpdateServerIdResult.Success success = (UpdateServerIdResult.Success) result;
					String gameUuid = success.getAuthData().getGameUuid();
					String serverId = success.getAuthData().getServerId();
					String characterId = success.getCharacterId();
					// Note: characterId is also saved to local storage automatically by the API
					
					Log.d("Main2Activity", "Update Server Client ID Success!");
					Log.d("Main2Activity", "GameUUID: " + gameUuid);
					Log.d("Main2Activity", "ServerID: " + serverId);
					Log.d("Main2Activity", "CharacterID: " + characterId);
					
					String message = "Server ID updated successfully!\n\n" +
							"Server ID: " + serverId + "\n" +
							"Character ID: " + characterId + "\n" +
							"GameUUID: " + gameUuid;
					showUpdateServerResult(message, false);
					
				} else if (result instanceof UpdateServerIdResult.Error) {
					// Error - handle different error codes
					UpdateServerIdResult.Error error = (UpdateServerIdResult.Error) result;
					int code = error.getCode();
					String message = error.getMessage();
					
					Log.e("Main2Activity", "Update Server Client ID Error!");
					Log.e("Main2Activity", "Code: " + code);
					Log.e("Main2Activity", "Message: " + message);
					
					String errorMessage;
					if (code == 401) {
						// User not authenticated - server name saved locally
						errorMessage = "Not authenticated.\n" +
								"Server name saved locally.\n\n" +
								"Code: " + code + "\n" +
								"Message: " + message;
					} else if (code == 400) {
						// Invalid server name
						errorMessage = "Invalid server name.\n\n" +
								"Code: " + code + "\n" +
								"Message: " + message;
					} else if (code == 404) {
						// Server not found
						errorMessage = "Server not found.\n\n" +
								"Code: " + code + "\n" +
								"Message: " + message;
					} else {
						// Network or other error
						errorMessage = "Failed to update server.\n\n" +
								"Code: " + code + "\n" +
								"Message: " + message;
					}
					
					showUpdateServerResult(errorMessage, true);
				}
				return null;
			});
		});
		
		builder.setNegativeButton("Cancel", (dialog, which) -> {
			dialog.dismiss();
			showTokenManagerDialog();
		});
		
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	/**
	 * Show the result of updateServerClientId operation
	 */
	private void showUpdateServerResult(String message, boolean isError) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		if (isError) {
			builder.setTitle("Update Server Client ID Error");
		} else {
			builder.setTitle("Update Server Client ID Success");
		}
		
		builder.setMessage(message);
		
		builder.setPositiveButton("Menu", (dialog, which) -> {
			dialog.dismiss();
			showTokenManagerDialog();
		});
		
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	public static String truncateAtWord(String input, int maxLength) {
		if (input == null) return null;
		
		if (input.length() <= maxLength) {
			return input;
		}
		
		String truncated = input.substring(0, maxLength);
		int lastSpace = truncated.lastIndexOf(" ");
		
		if (lastSpace > 0) {
			truncated = truncated.substring(0, lastSpace);
		}
		
		return truncated + "...";
	}
}
