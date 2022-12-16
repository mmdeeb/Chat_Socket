package sample;

import javafx.scene.layout.VBox;

import javax.naming.ldap.SortKey;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    public ServerSocket serverSocket;
    public Socket socket;
    public Socket socket_Server ;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;


    public Server (int port) {

         try {
             this.serverSocket=new ServerSocket(port);

            if (!available(3000)){
             socket = new Socket("localhost",3000);
            }

         }catch (IOException e){
             System.out.println("Error !! in Server");
             e.printStackTrace();
         }

    }
    private static boolean available(int port) {
        System.out.println("--------------Testing port " + port);
        Socket s = null;
        try { s = new Socket("localhost", port);
            System.out.println("--------------Port " + port + " is not available Found Server To Listen");
            return false;
        } catch (IOException e) {
            System.out.println("--------------Port " + port + " is available (NoT Found Server TO Listen");
            return true; }
        finally {
            if( s != null)
            {
                try { s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error." , e);
                } } } }
    /*public boolean Ping (String Ip_Address ,int Port ){
        try {
            InetAddress inetAddress = new InetAddress().getHostName(Ip_Address);
            boolean Result = inetAddress.isReachable(5000);
            return Result;
        }catch (IOException e){
            e.printStackTrace();
        }
    }*/
    public void connect() {
        new Thread(new Runnable() { //Thread To Dont Watting between Create Connection With Client
            @Override
            public void run() {
               try {
                  while (true){
                   socket_Server = serverSocket.accept();}

               }catch (IOException e){
                   e.printStackTrace();
                   System.out.println("Error in connection");
               }
            }
        }).start();

    }

    public void SendMessage(String MessageToClient){
        try {
            if (socket_Server != null) {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket_Server.getOutputStream()));
                bufferedWriter.write(MessageToClient);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println(" Done send the message");//WorkOut
            }
            if (socket!= null) {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write(MessageToClient);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println(" Done send the message");//WorkOut
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Finished The Connection ");
            Close_Connection(socket,socket_Server,bufferedWriter,bufferedReader);
        }
    }
    public void receiveMessage(VBox vBox){
        new Thread(new Runnable() { // Thread To Dont Wating Between Receive Message From Client
            @Override
            public void run() {

                while (true) {
                    try {
                            //  DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            //   String MessageFromClient = (String) dataInputStream.readUTF();
                            if (socket_Server != null && !socket_Server.isClosed()) {
                                bufferedReader = new BufferedReader(new InputStreamReader(socket_Server.getInputStream()));
                                System.out.println("1");// WorkOut
                                String MessageFromClient = bufferedReader.readLine();
                                System.out.println("2");//WorkOut
                                System.out.println(MessageFromClient);
                                if (MessageFromClient!=null)
                                Controller.add_Text_To_Chat(MessageFromClient, vBox);
                            }
                            if (socket != null && !socket.isClosed()) {
                                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                System.out.println("1");// WorkOut
                                String MessageFromClient = bufferedReader.readLine();
                                System.out.println("2");//WorkOut
                                System.out.println(MessageFromClient);
                                if (MessageFromClient!=null)
                                Controller.add_Text_To_Chat(MessageFromClient, vBox);
                            }

                               System.out.println("3"); // WorkOut

                        }catch(IOException e){
                            e.printStackTrace();
                        System.out.println("Finished The Connection ");
                        Close_Connection(socket,socket_Server,bufferedWriter,bufferedReader);

                        }
                    }
            }
        }).start();
    }
    public void Close_Connection (Socket socket ,Socket socket_Server, BufferedWriter bufferedWriter ,BufferedReader bufferedReader){
        try {
            if (bufferedReader!=null)
                bufferedReader.close();
                if (bufferedWriter!=null)
                bufferedWriter.close();
                if (socket!=null)
                socket.close();
                if (socket_Server!=null)
                socket_Server.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
