package com.example.its_akki.external3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import static android.telephony.SmsMessage.createFromPdu;

public class MyReceiver extends BroadcastReceiver {
    String body, number;
    Calendar calendar = Calendar.getInstance();

   public void onReceive(Context context, Intent intent) {
       try {
           Bundle bundle = intent.getExtras();
           if (bundle != null) {
               Object[] pdus = (Object[]) bundle.get("pdus");
               if ((pdus != null)) {
                   for (int i = 0; i < pdus.length; i++) {
                       SmsMessage[] messages = new SmsMessage[pdus.length];
                       messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                       body = messages[i].getMessageBody().toString();
                       number = messages[i].getDisplayOriginatingAddress().toString();
                       if (messages[i].getDisplayMessageBody() != null) ;
                       if (isExternalStorageWritable()) {
                           File txtfile = new File(Environment.getExternalStorageDirectory(), "smsbackup.txt");
                           String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                           try {
                               FileOutputStream fos = new FileOutputStream(txtfile, true);
                               fos.write(number.getBytes());
                               fos.write(body.getBytes());
                               fos.write(currentDate.getBytes());
                               fos.write("\n".getBytes());
                               fos.close();
                               Toast.makeText(context, "file saved", Toast.LENGTH_SHORT).show();
                           } catch (IOException e1) {
                               e1.printStackTrace();
                           }
                       } else {
                           Toast.makeText(context, "file cannot be created", Toast.LENGTH_SHORT).show();
                       }
                   }
               }
           }
       }catch (Exception e){
           Log.e("SMS Receive Error", "" + e);
       }
   }
    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes Writable");
            return true;
        } else {
            return false;
        }
    }
}
