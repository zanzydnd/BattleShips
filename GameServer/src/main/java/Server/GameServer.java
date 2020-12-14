package Server;

import Connection.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mvc.Connection.Message;
import mvc.Connection.MessageType;

public class GameServer{
   static final int port = 8080;
   private List<Connection> listConnection = new ArrayList<>();


   public GameServer(){
       Socket clientSocket = null;
       ServerSocket serverSocket = null;

       try{
           serverSocket = new ServerSocket(port);
           System.out.println("Стартанули сервер");
           while(true){
               clientSocket = serverSocket.accept();
               Connection connection = new Connection(clientSocket);
               listConnection.add(connection);
               connection.send(new Message(MessageType.ACCEPTED));
               new ThreadConnection(connection).start();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   public class ThreadConnection extends Thread {
       private Connection connection;
       private volatile boolean stopCycle = false;

       public ThreadConnection(Connection connection){
           this.connection = connection;
       }

       public String generateCode(){
          String result = UUID.randomUUID().toString().substring(0,4);
          return result;
       }

      public void mainCycle(Connection connection){
          try {
              while (!stopCycle) {
                  Message message = connection.receive();
                  if(message.getMessageType() == MessageType.CREATE_ROOM){
                      System.out.println("++");
                      String code = generateCode();
                      connection.setCode(code);
                      connection.send(new Message(MessageType.CODE, code));
                  }
                  else if(message.getMessageType() == MessageType.CONNECT_TO_ROOM){
                      for(Connection con : listConnection){
                          if(con.getCode().equals(message.getCode()) && !con.equals(connection)){
                              connection.setCode(con.getCode());
                              connection.setEnemyConnection(con);
                              con.setEnemyConnection(connection);
                              System.out.println("00");
                          }
                      }
                  }
                  else if (message.getMessageType() == MessageType.DISCONNECT || message.getMessageType() == MessageType.DEFEAT) {
                      sendMessageEnemy(message, connection);
                      stopCycle = true;
                  } else if (message.getMessageType() == MessageType.MY_DISCONNECT) {
                      stopCycle = true;
                  } else {
                      System.out.println("--");
                      System.out.println(connection.getEnemyConnection());
                      sendMessageEnemy(message, connection);
                  }
              }
              connection.close();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }

       private void sendMessageEnemy(Message message, Connection connection) throws IOException {
           System.out.println(connection);
           System.out.println(message);
            connection.getEnemyConnection().send(message);
           System.out.println("отправили");
       }



       @Override
       public void run() {
           mainCycle(connection);
       }
   }
}
