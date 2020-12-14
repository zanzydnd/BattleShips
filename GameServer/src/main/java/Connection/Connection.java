package Connection;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import mvc.Connection.Message;
import mvc.Connection.MessageType;

public class Connection implements Closeable{
    private final Socket socket;
    private String code;
    private Connection enemyConnection;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public void setEnemyConnection(Connection connection){
        enemyConnection = connection;
    }

    public Connection getEnemyConnection(){
        return enemyConnection;
    }

    public void send(Message message) throws IOException{
        synchronized (out){
            out.writeObject(message);
        }
    }

    public Message receive() throws IOException,ClassNotFoundException{
        synchronized (in){
            return (Message) in.readObject();
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
