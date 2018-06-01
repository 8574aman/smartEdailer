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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.parse.FindCallback;


import com.parse.ParseAnalytics;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MainActivity extends AppCompatActivity {
  boolean flag;
  EditText numb;
  Double reqlat,reqlon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();

    flag=true;


    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  public void findvic(View view)
  {
    numb=(EditText)findViewById(R.id.editText);
    String number = numb.getText().toString();
    if(number.length()==10) {
      if (flag) {

        ParseQuery<ParseObject> query = new ParseQuery("VictimRequest");
        query.whereEqualTo("Numberid", number);

        query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {
            if(e==null)
            {
              if(objects.size()>0)
              {
                for(ParseObject object : objects)
                {
                  ParseGeoPoint reqlocation = (ParseGeoPoint)object.get("Viclocation");
                  reqlat=reqlocation.getLatitude();
                  reqlon=reqlocation.getLongitude();
                  Intent i1 = new Intent(MainActivity.this,findvictim.class);
                  i1.putExtra("reqlat",reqlat);
                  i1.putExtra("reqlon",reqlon);
                  startActivity(i1);
                  MainActivity.this.finish();

                }
              }
            }
            else
            {
              Toast.makeText(MainActivity.this,"no record found",Toast.LENGTH_SHORT).show();
              //no such record
            }
          }
        });
      }
    }
    else
    {
      Toast.makeText(MainActivity.this,"please enter a valid number",Toast.LENGTH_SHORT).show();
      numb.setText("");
    }
  }

}