package edu.ut.mobileproject.server;




public class Main {

    /**
     * @param args
     */

    public static void main(String[] args) {
//
        NetworkManager NM = null;
        byte[] IPAddress = new byte[4];
//		IPAddress[0] = (byte) 10 ;
//	    IPAddress[1] = 0;
//	    IPAddress[2] = 2;
//	    IPAddress[3] = (byte) 2;
        if(NM == null){
            NM = new NetworkManager(6000);
        }
        boolean isconnected = NM.makeconnection();

    }

}