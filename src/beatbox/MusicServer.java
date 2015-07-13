/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beatbox;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Jonathan
 */
public class MusicServer {
    
    ArrayList<ObjectOutputStream> clientOutputStreams;
    
    public static void main(String[] args){
        new MusicServer().go();
    }
    
    public class ClientHandler implements Runnable{
        ObjectInputStream in;
        Socket clientSocket;
        
        public ClientHandler(Socket socket){
            try{
                clientSocket = socket;
                in = new ObjectInputStream(clientSocket.getInputStream());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        public void run(){
            Object o1 = null;
            Object o2 = null;
            try{
                while((o1 = in.readObject())!= null){
                    o2 = in.readObject();
                    System.out.println("Just read two objects.");
                    tellEveryone(o1, o2);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public void go(){
        clientOutputStreams = new ArrayList<ObjectOutputStream>();
        try{
            ServerSocket serverSock = new ServerSocket(4242);
            //this is to set up a "greeting" server socket for the clients to connect to and obtain the real socket
            while(true){
                Socket clientSocket = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(out);
                //this ArrayList keeps track of the lists of all the clients connected to the server
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void tellEveryone(Object one, Object two){
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()){
            try{
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(one);
                out.writeObject(two);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
