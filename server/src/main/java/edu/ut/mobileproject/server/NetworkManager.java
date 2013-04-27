package edu.ut.mobileproject.server;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.lang.reflect.Method;



public class NetworkManager {
    int portnum;
    Socket mysocket = null;
    InputStream in = null;
    OutputStream out = null;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    ServerSocket serversoc = null;
    //        CloudService nmfparent = null;
    byte []serveraddress =new byte[4];

    public NetworkManager(/*byte []serveraddress, */int port) {
        //this.serveraddress = serveraddress;
        portnum = port;
    }

//    public void setNmf(CloudService nmfparent) {
//        this.nmfparent = nmfparent;
//    }





    public boolean makeconnection(){

        if(serversoc == null || serversoc.isClosed()){
            try {
                serversoc = new ServerSocket(portnum);
                //serversoc.bind(new InetSocketAddress(Inet4Address.getByAddress(serveraddress), portnum));
                serversoc.setSoTimeout(0);
            } catch (IOException ex) {
            }
        }


        //        while(true){
        try {
            System.out.println("server waiting");
            mysocket = serversoc.accept();

            in = mysocket.getInputStream();
            out = mysocket.getOutputStream();

            oos = new ObjectOutputStream(out);
            ois = new ObjectInputStream(in);

            waitforreceivingdata();
            System.out.println("connection established");
            return true;
        } catch (SocketException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
        //        }
//        }else{
//            waitforreceivingdata();
//            return true;
//        }
    }




    private void waitforreceivingdata(){
        try{
            new Receiving().waitforreceivingdata();
        }catch(Exception ex){
        }
    }



    class Receiving implements Runnable{
        String functionName = null;
        Class[] funcArgDataTypes = null;
        Object[] funcArgValues = null;
        Object state = null;
        Class stateDType = null;
        Pack myPack = null;

        public Receiving() {
        }

        public void waitforreceivingdata(){
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            try {
                myPack = (Pack) ois.readObject();
                functionName = myPack.getfunctionName();
                funcArgDataTypes = myPack.getFuncDTypes();
                funcArgValues = myPack.getFuncArgValues();
                state = myPack.getstate();
                stateDType = myPack.getstateType();
                if(functionName !=null && functionName.length() > 0){
                    try {

                        Class cls = Class.forName(stateDType.getName());

                        Method method = cls.getDeclaredMethod(functionName, funcArgDataTypes);

                        Object[] para = new Object[funcArgValues.length];
                        for(int q = 0; q < funcArgValues.length; q++){
                            if(funcArgDataTypes[q].isPrimitive()){
                                if(funcArgDataTypes[q].getName().compareToIgnoreCase("int") == 0)
                                    para[q] = ((Integer)funcArgValues[q]).intValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("long") == 0)
                                    para[q] = ((Long)funcArgValues[q]).longValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("short") == 0)
                                    para[q] = ((Short)funcArgValues[q]).shortValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("float") == 0)
                                    para[q] = ((Float)funcArgValues[q]).floatValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("double") == 0)
                                    para[q] = ((Double)funcArgValues[q]).doubleValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("byte") == 0)
                                    para[q] = ((Byte)funcArgValues[q]).byteValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("boolean") == 0)
                                    para[q] = ((Boolean)funcArgValues[q]).booleanValue();
                                else if(funcArgDataTypes[q].getName().compareToIgnoreCase("char") == 0)
                                    para[q] = ((Character)funcArgValues[q]).charValue();

                            }else
                                para[q] = funcArgDataTypes[q].cast(funcArgValues[q]);
                        }

                        Object result = method.invoke(state, para);

                        ResultPack rp = new ResultPack(result, state);
                        oos.writeObject( rp );
                        oos.flush();
                    } catch (ClassNotFoundException ex) {
                        oos.writeObject(null);
                    } catch (IllegalAccessException ex) {
                        oos.writeObject(null);
                    } catch (IllegalArgumentException ex) {
                        oos.writeObject(null);
                    } catch (InvocationTargetException ex) {
                        oos.writeObject(null);
                    } catch (NoSuchMethodException ex) {
                        oos.writeObject(null);
                    } catch (SecurityException ex) {
                        oos.writeObject(null);
                    }finally{

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

                    }
                }else{
                    oos.writeObject(null);
                }
            } catch (IOException ex) {

            } catch (ClassNotFoundException ex) {

            } finally{
                makeconnection();
            }
        }
    }


}

