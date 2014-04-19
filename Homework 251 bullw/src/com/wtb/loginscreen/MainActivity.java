package com.wtb.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/* Login screen activity
 * 
 * This activity takes user input for email and password, validates email
 * and sends it to the LoggedInActivity class where it is displayed.
 * 
 * @author Wilson Bull
 * @version 1.0.0
 * 
 */

public class MainActivity extends Activity {
	// Extras to be passed via Intent
	public final static String EXTRA_EMAIL = "com.wtb.loginscreen.EMAIL";
	public final static String EXTRA_PASS = "com.wtb.loginscreen.PASS";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Added .setTypeface to default to have fonts match
        //Found here: http://stackoverflow.com/questions/3406534/password-hint-font-in-android
        final EditText password = (EditText) findViewById(R.id.pass_field);
        password.setTextColor(Color.WHITE);
        password.setTypeface(Typeface.DEFAULT);
        final EditText email = (EditText) findViewById (R.id.email_field);
        email.setTextColor(Color.WHITE);
        
        /*Set listener to make sure that email address is valid when user
         * changes focus away from the EditText field. Text changes to
         * red color if the email address is invalid.
         */
        email.setOnFocusChangeListener( new OnFocusChangeListener() {
        	public void onFocusChange(View v, boolean hasFocus) {
        		email.setTextColor(Color.WHITE);
        		if (!hasFocus && email.getText().length() != 0) {
        			if (!isValidEmail(email.getText())) {
        				Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
        				email.setTextColor(Color.parseColor("#ff0000"));
        				}
        			} 
        		}       
        	});
        
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setTextColor(Color.LTGRAY);
        
        /*This onClickListener checks email and password fields to verify they are
         * valid.  If not, the application displays a toast informing the user. 
         * If they are valid, they're submitted via submitCredentials method and
         * added to an intent received by the LoggedInActivity.§
         */
        signInButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (email.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "You must enter a valid email address.", Toast.LENGTH_SHORT).show();
				} else if (email.getText().toString().length() > 0 && !isValidEmail(email.getText())) {
            		Toast.makeText(getApplicationContext(), "Email address invalid.  Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            		email.setTextColor(Color.parseColor("#ff0000"));
            	} else if (password.getText().length() == 0) {
            		Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            	} else {
					submitCredentials(v, email.getText(), password.getText());
				}
			}
		});
    }
  
    /* Checks character sequence to see whether or not it's valid
     * 
     * @param target, character sequence that should be pulled from the email field
     * @return true if email is valid, otherwise false
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    
    /* Submits credentials intent to pass username and email address
     * 
     * @param view, current view
     * @param email, email text from textedit field
     * @param pass, password text from textedit field
     * 
     */
    public void submitCredentials(View view, Editable email, Editable pass){
    	Intent intent = new Intent (this, LoggedInActivity.class);
    	intent.putExtra(EXTRA_EMAIL, email.toString());
    	intent.putExtra(EXTRA_PASS, pass.toString());
    	startActivity(intent);
    }
}
