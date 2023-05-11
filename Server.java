
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ServerSocket serverSocket;

     
    public static void main(String[] args) throws IOException {
        

        
        Log log = new Log("server");
        //using serversocket as argument to automatically close the socket
        //the port number is unique for each server

        //list to add all the clients thread
        ArrayList<ServerThread> threadList = new ArrayList<>();

        serverSocket = new ServerSocket(1234);
        log.add("Server created");
        
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, threadList);
            //starting the thread
            threadList.add(serverThread);
            serverThread.start();

            //get all the list of currently running thread
        }

    }
}
