package Client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client {

    public static final int PORT = 6543;
    public Socket socket;

    public String sendMessageToServer(String message, String object) {

        System.out.println("Say something and the message will be sent to the server: ");

        boolean isClose = false;

        while (!isClose) {

            try {
                socket = new Socket("localhost", PORT);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeObject(message);
                outputStream.writeObject(object);

                String messageFromServer = (String) inputStream.readObject();
                String jsonReceived = (String) inputStream.readObject();

                System.out.println(messageFromServer);

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return jsonReceived;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
