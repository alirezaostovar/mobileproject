package edu.ut.mobileproject.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: Lukman
 * Date: 4/27/13
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class CloudService extends Service {

    private Thread serverthread;
    public static String changelabel = "CHANGE_LABEL";
    NetworkManager NM = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Retrieve the shared preferences
//    Context context = getApplicationContext();
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if(serverthread == null || !serverthread.isAlive()){
            serverthread = new Thread(new ServerThreadRunnable());
            serverthread.start();
        }
        return Service.START_STICKY;
    };

    private class ServerThreadRunnable implements Runnable {

        @Override
        public void run() {
//		Waitforcall();
        }
    };

    @Override
    public void onCreate() {
        serverthread = new Thread(new ServerThreadRunnable());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//  private void Waitforcall() {

//	  byte[] IPAddress = new byte[4];
//      IPAddress[0] = 10;
//      IPAddress[1] = 0;
//      IPAddress[2] = 2;
//      IPAddress[3] = 2;
//      if(NM == null){
//          NM = new NetworkManager(IPAddress, 6000);
////          NM.setNmf(this);
//      }
//      boolean isconnected = NM.makeconnection();
//	  /*while(true){
//		try {
//			Thread.currentThread().sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//
//		}
//		Intent intent = new Intent(changelabel);
//	    intent.putExtra("label", String.valueOf(System.currentTimeMillis()));
//
//	    sendBroadcast(intent);
//
//	  }*/
//
//  }

    public void notifytochangelable(String str){
        Intent intent = new Intent(changelabel);
        intent.putExtra("label", str);
        sendBroadcast(intent);
    }
}