package com.parse.starter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;



/**
 * Created by TSTARKS on 4/9/2018.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if(state==null)
            {
                //Outgoing call
                        String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                 if(number.matches("8817730465")||number.matches("112")||number.matches("108")||number.matches("100")) {
                     Toast.makeText(context, "outgoing Number is - " + number, Toast.LENGTH_SHORT).show();
                     Intent intentone = new Intent(context.getApplicationContext(), reqmaker.class);
                     intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                     context.startActivity(intentone);



                 }
                //
                //      Log.i("tag","Outgoing number : "+number);
            }
                  if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                Toast.makeText(context, "Ringing State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}