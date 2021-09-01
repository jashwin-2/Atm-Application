package com.application.atm.view;

import com.application.atm.exception.AuthenticationFailedException;

interface ATMUserView {

	public void onAuthenticationFailed(AuthenticationFailedException exception);
	public void onExit();
	public boolean serviceController(UserMenuItems choice);
	public void sessionManager();
}
