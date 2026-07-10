package com.unity3d.player.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.unity3d.player.R;

public class TokenManagerDialogFragment extends DialogFragment {
	private static final String ARG_ACCESS_TOKEN = "arg_access_token";
	
	public static TokenManagerDialogFragment newInstance(String myData) {
		TokenManagerDialogFragment fragment = new TokenManagerDialogFragment();
		Bundle args = new Bundle();
		args.putString(ARG_ACCESS_TOKEN, myData);
		fragment.setArguments(args);
		return fragment;
	}
	
	private String getAccessToken() {
		if (getArguments() != null) {
			return getArguments().getString(ARG_ACCESS_TOKEN);
		}
		return null;
	}
	
	private TokenManagerDialogListener listener;
	private String accessToken;
	private boolean actionHandled = false;

	private void notifyAction(String action) {
		actionHandled = true;
		if (listener != null) {
			listener.onDialogDismissed(action);
		}
		dismiss();
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof TokenManagerDialogListener) {
			listener = (TokenManagerDialogListener) context;
		}
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_fragment_token_manager, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		accessToken = getAccessToken();
		
		Boolean isAuthenticated = accessToken != null && !accessToken.isEmpty();
		if (!isAuthenticated) {
			view.findViewById(R.id.btnSignUpLogin).setVisibility(View.VISIBLE);
			view.findViewById(R.id.btnLogout).setVisibility(View.GONE);
			view.findViewById(R.id.btnDeleteAccount).setVisibility(View.GONE);
			view.findViewById(R.id.btnBuyItems).setVisibility(View.GONE);
			view.findViewById(R.id.btnChangeGameServer).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.btnSignUpLogin).setVisibility(View.GONE);
			view.findViewById(R.id.btnLogout).setVisibility(View.VISIBLE);
			view.findViewById(R.id.btnDeleteAccount).setVisibility(View.VISIBLE);
			view.findViewById(R.id.btnBuyItems).setVisibility(View.VISIBLE);
			view.findViewById(R.id.btnChangeGameServer).setVisibility(View.VISIBLE);
		}
		
		((TextView) view.findViewById(R.id.tvToken)).setText(accessToken != null ? accessToken : "");
		view.findViewById(R.id.btnSignUpLogin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("SignUp_Login");
			}
		});
		view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("Logout");
			}
		});
		view.findViewById(R.id.btnDeleteAccount).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("Deactivate_Account");
			}
		});
		view.findViewById(R.id.btnBuyItems).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("PURCHASE");
			}
		});
		view.findViewById(R.id.btnChangeGameServer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("Change_Game_Server");
			}
		});
		
		view.findViewById(R.id.btnLinkAccount).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("Link_Account");
			}
		});
		
		view.findViewById(R.id.btnGameTracking).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyAction("GAME_TRACKING_TEST");
			}
		});
		
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (!actionHandled && listener != null) {
			listener.onDialogDismissed("Dialog dismissed without action");
		}
	}
}
