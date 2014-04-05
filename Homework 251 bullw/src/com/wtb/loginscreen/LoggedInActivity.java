package com.wtb.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoggedInActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in);
		Intent intent = getIntent(); 
		String emailAddress = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
		String password = intent.getStringExtra(MainActivity.EXTRA_PASS);
		TextView emailText = (TextView) findViewById(R.id.logged_in_email_field);
		emailText.setText(emailAddress);
		TextView passwordText = (TextView) findViewById(R.id.logged_in_password_field);
		passwordText.setText(password);
	}

}
