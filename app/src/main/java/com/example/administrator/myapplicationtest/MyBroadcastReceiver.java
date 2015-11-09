package com.example.administrator.myapplicationtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public MyBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            Constant.numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
        Intent service = new Intent(context,MyPhoneService.class);
        context.startService(service);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
