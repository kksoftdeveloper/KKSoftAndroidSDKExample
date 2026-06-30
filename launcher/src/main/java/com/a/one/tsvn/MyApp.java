package com.a.one.tsvn;

import android.app.Application;

import com.appmb.sdk.mbauth.MbAuth;
import com.appmb.sdk.mbcore.MbSdk;
import com.appmb.sdk.mbcore.config.MbSdkConfig;
import com.appmb.sdk.mbcore.config.TrackingConfig;
import com.appmb.sdk.mbpayment.MbPayment;
import com.appmb.sdk.mbtracking.di.TrackingLoader;
import kotlin.jvm.functions.Function0;

public class MyApp extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		String serverId = "IOS1"; // S1, IOS1 or null
		
		TrackingConfig trackingConfig = new TrackingConfig.Builder()
				.enableFirebase(true, BuildConfig.firebaseAppID)
				.enableAdjust(true, BuildConfig.adjustId, BuildConfig.adjustToken)
				.enableTikTok(true, BuildConfig.tiktokAppID, BuildConfig.tiktokAccessToken)
				.enableMeta(true, BuildConfig.facebookAppID, BuildConfig.facebookClientToken, BuildConfig.facebookDisplayName)
				.enableAppFlyers(false, BuildConfig.appFlyersId, BuildConfig.appFlyersDevKey)
				.enableGid(BuildConfig.gidClientID)
				.build();

		MbSdkConfig config = new MbSdkConfig.Builder()
				.setServerClientId(serverId)
				.setAppId(BuildConfig.APPLICATION_ID)
				.setAppVersionName(BuildConfig.VERSION_NAME)
				.setTrackingConfig(trackingConfig)
				.build();

		MbSdk.INSTANCE.init(this, new Function0<MbSdkConfig>() {
			@Override
			public MbSdkConfig invoke() {
				return config;
			}
		});
		
		// Initialize Adjust
		TrackingLoader.INSTANCE.loadOnce();
		
		MbAuth.INSTANCE.init();
		MbPayment.INSTANCE.init();
	}
}
