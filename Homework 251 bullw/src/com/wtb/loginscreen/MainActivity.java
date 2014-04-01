package com.wtb.loginscreen;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Added this to have fonts match
        //Found here: http://stackoverflow.com/questions/3406534/password-hint-font-in-android
        EditText password = (EditText) findViewById( R.id.pass_field );
        password.setTypeface( Typeface.DEFAULT );
        
        final EditText email = (EditText) findViewById(R.id.email_field);
        
        email.setOnFocusChangeListener(new OnFocusChangeListener() {          

        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
            	if (!isValidEmail(email.getText())){
            		Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_LONG).show();
            		email.setTextColor(Color.parseColor("#ff0000"));
            	}
            }
 
        }
        
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    
}
