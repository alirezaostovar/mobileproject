package edu.ut.mobileproject.client;

/**
 *
 * @author ALI
 */

import java.io.*;
import java.net.*;


public class NetworkManager {
    int portnum;
    Socket mysocket = null;
    InputStream in = null;
    OutputStream out = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    byte []serveraddress =new byte[4];
    ClientService nmfparent = null;

    public NetworkManager(byte []serveraddress, int port) {

        this.serveraddress = serveraddress;
        portnum = port;
    }

    public void setNmf(ClientService nmfparent) {
        this.nmfparent = nmfparent;
    }


    public boolean connect(){
//        if(mysocket == null || !mysocket.isConnected()){
        nmfparent.notifytochangelable("Requesting a new conection");
        mysocket = new Socket();
        try {
            mysocket.connect(new InetSocketAddress(Inet4Address.getByAddress(serveraddress), portnum), 0);
            in = mysocket.getInputStream();
            out = mysocket.getOutputStream();

            oos = new ObjectOutputStream(out);

            ois = new ObjectInputStream(in);
            return true;
        } catch (IOException ex) {
            return false;
        }
//        }else
//            return true;
    }



    public void send(String functionName, Class[] funcArgDataTypes, Object[] funcArgValues, Class stateDType, Object state){
        try{
            new Sending(new Pack(functionName, stateDType, state, funcArgValues, funcArgDataTypes)).send();
        }catch(Exception ex){
            nmfparent.notifytochangelable(ex.getCause().getClass().getName());
            ex.printStackTrace();
//            nmfparent.showError();
        }
    }



    class Sending implements  Runnable{
        Pack MyPack = null;
        ResultPack result = null;

        public Sending(Pack MyPack) {
            this.MyPack = MyPack;
        }


        public void send(){
            Thread t = new Thread(this);
            t.start();
        }
        @Override
        public void run() {
            try {

                oos.writeObject( MyPack );
                oos.flush();

                result = (ResultPack) ois.readObject();
                if(result == null){
                    nmfparent.notifytochangelable("unsuccessful execution");
                }else
                    nmfparent.notifytochangelable(String.valueOf(((Integer)result.getresult()).intValue()));

                oos.close();
                ois.close();

                in.close();
                out.close();

                mysocket.close();

                oos = null;
                ois = null;

                in = null;
                out = null;
                mysocket = null;

            } catch (IOException ex) {
                nmfparent.notifytochangelable(ex.getCause().getClass().getName());
                ex.printStackTrace();nmfparent.notifytochangelable(ex.getCause().getClass().getName());
            } catch (ClassNotFoundException ex) {
                nmfparent.notifytochangelable(ex.getCause().getClass().getName());
                ex.printStackTrace();nmfparent.notifytochangelable(ex.getCause().getClass().getName());
            }
        }

    }


}
