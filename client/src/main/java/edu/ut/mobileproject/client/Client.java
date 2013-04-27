package edu.ut.mobileproject.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Client extends Activity {


    MessageReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connect = (Button)this.findViewById(R.id.button1);
        connect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//        		changelabel("Client is trying to start connection!");
                RunCient();
//        		buttonInvisible();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void buttonInvisible() {
        Button connect = (Button)this.findViewById(R.id.button1);
        connect.setVisibility(View.INVISIBLE);
    }

    private void RunCient() {
        startService(new Intent(this, ClientService.class));
    }

    private void changelabel(String str){
        TextView tv = (TextView)this.findViewById(R.id.textView1);
        tv.setText(str);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String lab = intent.getStringExtra("label");
            changelabel(lab);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter;
        filter = new IntentFilter(ClientService.changelabel);
        receiver = new MessageReceiver();
        registerReceiver(receiver, filter);


    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

    }
}