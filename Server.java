
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ServerSocket serverSocket;

     
    public static void main(String[] args) throws IOException {
        
        Product product1 = new Product("pintura", 100);
        Product product2 = new Product("escultura", 200);
        Product product3 = new Product("joya", 300);

        Subasta subasta1 = new Subasta(product1, 1,5);
        Subasta subasta2 = new Subasta(product2, 2, 10);
        Subasta subasta3 = new Subasta(product3, 3, 15);

        SubastaSession subastaSession = new SubastaSession("Arte", 1);

        subastaSession.addSubasta(subasta1);
        subastaSession.addSubasta(subasta2);
        subastaSession.addSubasta(subasta3);

        SubastaSession subastaSession2 = new SubastaSession("Autos", 2);
        Product product4 = new Product("auto1", 100);
        Subasta subasta4 = new Subasta(product4, 1, 5);
        subastaSession2.addSubasta(subasta4);

        ManejadorSubastas Subastas = ManejadorSubastas.INSTANCE.getInstance();
        Subastas.addSubastaSession(subastaSession);
        Subastas.addSubastaSession(subastaSession2);
        
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
