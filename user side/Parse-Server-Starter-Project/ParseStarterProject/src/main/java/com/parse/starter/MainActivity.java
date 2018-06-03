/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  public static final String PREFS_NAME = "MyPrefsFile";

  public void login(View view)
  {
    EditText username = (EditText)findViewById(R.id.editText);
    EditText phonenumber = (EditText)findViewById(R.id.editText2);
    if(username.getText().toString().matches("") || phonenumber.getText().toString().matches("")){

      Toast.makeText( this,"A username and mobile number are required",Toast.LENGTH_SHORT).show();
    }
    else
    {
      ParseUser user = new ParseUser();

      user.setUsername(phonenumber.getText().toString());
      user.setPassword(username.getText().toString());
      user.put("VicName",username.getText().toString());
      user.put("VicNumber",username.getText().toString());


      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if(e==null)
          {
            Toast.makeText(MainActivity.this, "succesfully logged in ", Toast.LENGTH_SHORT).show();
            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
            SharedPreferences.Editor editor = settings.edit();

//Set "hasLoggedIn" to true
            editor.putBoolean("hasLoggedIn", true);

// Commit the edits!
            editor.commit();
          }else{
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

          }
          Intent launchNextActivity;
          launchNextActivity = new Intent(MainActivity.this, LocatingActivity.class);
          startActivity(launchNextActivity);
          MainActivity.this.finish();



        }
      });

    }



  }




  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
    getSupportActionBar().hide();
//Get "hasLoggedIn" value. If the value doesn't exist yet false is returned

    boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

    if(hasLoggedIn)
    {
      Intent launchNextActivity;
      launchNextActivity = new Intent(MainActivity.this, LocatingActivity.class);
      startActivity(launchNextActivity);
      MainActivity.this.finish();

    }
//    ParseUser.logOut();




    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());

  }

}