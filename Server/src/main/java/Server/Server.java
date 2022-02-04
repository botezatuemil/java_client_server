package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 6543;

    public void start() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket clientSocket = null;
                boolean isClose = false;

                System.out.println("Server is running");
                while (!isClose) {
                    clientSocket = serverSocket.accept();
                    new Thread(new ServerThread(clientSocket)).start();
                }
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
