package com.wtb.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	// Extras to be passed via Intent
	public final static String EXTRA_EMAIL = "com.wtb.loginscreen.EMAIL";
	public final static String EXTRA_PASS = "com.wtb.loginscreen.PASS";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Added this to have fonts match
        //Found here: http://stackoverflow.com/questions/3406534/password-hint-font-in-android
        final EditText password = (EditText) findViewById(R.id.pass_field);
        password.setTypeface(Typeface.DEFAULT);
        final EditText email = (EditText) findViewById(R.id.email_field);
        email.setOnFocusChangeListener( new OnFocusChangeListener() {
        	public void onFocusChange(View v, boolean hasFocus) {
        		email.setTextColor(Color.parseColor("#000000"));
        		if (!hasFocus && email.getText().length() != 0) {
        			if (!isValidEmail(email.getText())){
        				Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
        				email.setTextColor(Color.parseColor("#ff0000"));
        				}
        			} 
        		}       
        	});
        
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isValidEmail(email.getText())) {
            		Toast.makeText(getApplicationContext(), "Email address invalid.  Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            		email.setTextColor(Color.parseColor("#ff0000"));
            	}
				else if (password.getText().length() == 0) {
            		Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            	}
				else {
					submitCredentials(v, email.getText(), password.getText());
				}
			}
		});
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    
    public void submitCredentials(View view, Editable email, Editable pass){
    	Intent intent = new Intent (this, LoggedInActivity.class);
    	intent.putExtra(EXTRA_EMAIL, email.toString());
    	intent.putExtra(EXTRA_PASS, pass.toString());
    	startActivity(intent);
    }
    
}
