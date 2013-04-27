package edu.ut.mobileproject.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ClientService extends Service {

    private Thread clientthread;
    public static String changelabel = "CHANGE_LABEL";
    private NetworkManager NM = null;
    byte[] IPAddress = new byte[4];
    int port;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Retrieve the shared preferences
        //    Context context = getApplicationContext();
        //    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        clientthread = new Thread(new ClientThreadRunnable());
        clientthread.start();
        return Service.START_STICKY;
    };

    private class ClientThreadRunnable implements Runnable {

        @Override
        public void run() {
            call();
        }
    };

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void call() {

         ClassA a = new ClassA();
        Class[] funcDataTypes = {int.class, int.class};
        Object[] funcArgValues = {10, 20};
        IPAddress[0] = (byte) 127 ;
        IPAddress[1] = 0;
        IPAddress[2] = 0;
        IPAddress[3] = (byte) 1;
        if(NM == null){
            NM = new NetworkManager(IPAddress, 6000);
            NM.setNmf(this);
        }
        boolean isconnected = NM.connect();
        if(isconnected){
            NM.send("add", funcDataTypes, funcArgValues, a.getClass(), a);
        }
    }



    public void notifytochangelable(String str){
        Intent intent = new Intent(changelabel);
        intent.putExtra("label", str);

        sendBroadcast(intent);
    }
}
