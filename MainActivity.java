package com.example.arshad.textmessager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int constant = 12345;
    String stateOne = "Greeting";
    String stateTwo = "Rejecting";
    String stateThree = "Explaining";
    String stateDeny = "Dismissing";
    int stateStatus = 0;
    String aiResponse = "";
    String[] oneState = {"Hey, how can I help you?","What do you want?","Hi","Hey, what's up?"};
    String[] twoState = {"What?! I don't even know you!","Um... who even  are you?","Sorry, I'm not interested.","Verizon msg: The number ending in 5554 has been changed, disconnected, or is no longer in service. You may visit stop stoptextingme.com for more information."};
    String[] threeState = {"I don't like you","Aren't you in jail?","How can I put this... ur ugly.","I have a boyfriend..."};
    String[] denyState = {"Bye","Leave me alone","I'm blocking you","K"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, constant);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, constant);
        }

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();
                Object[] arrayPdus = (Object[])bundle.get("pdus");
                SmsMessage[] smsMessages = new SmsMessage[arrayPdus.length];
                String messageBody="";
                for(int a = 0; a < arrayPdus.length; a++) {
                    smsMessages[a] = SmsMessage.createFromPdu((byte[])arrayPdus[a],bundle.getString("format"));
                    messageBody += smsMessages[a].getMessageBody().toLowerCase();
                }

                TextView currentState = (TextView) findViewById(R.id.id_State);

                if(stateStatus==0)
                {
                    if(messageBody.contains("hi")||messageBody.contains("hello")||messageBody.contains("up")||messageBody.contains("hey"))
                    {
                        currentState.setText(stateOne);
                        Handler handler = new Handler();
                        int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                        int randomResponse = (int)(Math.random()*4);
                        aiResponse = oneState[randomResponse];
                        stateStatus = 1;
                        handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);
                    }
                    else
                    {
                        aiResponse = "What do you mean "+messageBody+"?";
                        Handler handler = new Handler();
                        int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                        handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);
                    }
                }
                else if(stateStatus==1)
                {
                    if(messageBody.contains("marry")||messageBody.contains("love")||messageBody.contains("date")||messageBody.contains("out")||messageBody.contains("cute"))
                    {
                        currentState.setText(stateTwo);
                        Handler handler = new Handler();
                        int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                        int randomResponse = (int)(Math.random()*4);
                        aiResponse = twoState[randomResponse];
                        stateStatus = 2;
                        handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);
                    }
                    else
                    {
                        aiResponse = "What do you mean "+messageBody+"?";
                        Handler handler = new Handler();
                        int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                        handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);
                    }
                }
                else if(stateStatus==2)
                {
                    if(messageBody.contains("why")||messageBody.contains("but")||messageBody.contains("please")||messageBody.contains("need")||messageBody.contains("cmon"))
                    {
                        currentState.setText(stateThree);
                        Handler handler = new Handler();
                        int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                        int randomResponse = (int)(Math.random()*4);
                        aiResponse = threeState[randomResponse];
                        stateStatus = 3;
                        handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);
                    }
                    else
                    {
                        aiResponse = "What do you mean "+messageBody+"?";
                        Handler handler = new Handler();
                        int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                        handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);
                    }

                }
                else if(stateStatus==3)
                {

                    currentState.setText(stateDeny);
                    Handler handler = new Handler();
                    int randomAmountOfTime = (int)(Math.random()*5000)+1000;
                    int randomResponse = (int)(Math.random()*4);
                    aiResponse = denyState[randomResponse];
                    handler.postDelayed(createRunnable(smsMessages[0].getOriginatingAddress().substring(1), aiResponse), randomAmountOfTime);

                }

            }
        };

        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    public Runnable createRunnable(final String address, final String message)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(address,null,message,null,null);
            }
        };
        return runnable;
    }
}

