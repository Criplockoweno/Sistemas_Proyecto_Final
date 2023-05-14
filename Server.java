
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ServerSocket serverSocket;

    
    public static  void initSubastaSession(){
        Product nocheEstP = new Product("La Noche Estrellada", 70000000);
        Product ferrariP = new Product("Ferrari clásico", 1000000);
        Product mercedesP = new Product("Mercedes clásico", 2000000);
        Product principiosMatP = new Product("Principios matemáticos de la filosofía natural", 40000000);

        Subasta subastaNocheEst = new Subasta(nocheEstP, 101,2);
        Subasta subastaFerrari = new Subasta(ferrariP, 201,5);
        Subasta subastaMercedes = new Subasta(mercedesP, 202,5);
        Subasta subastaPrincipiosMat = new Subasta(principiosMatP, 301, 7);

        SubastaSession sessionPinturas = new SubastaSession("Pinturas", 100);

        sessionPinturas.addSubasta(subastaNocheEst);

        SubastaSession sessionAutos = new SubastaSession("Autos", 200);
        sessionAutos.addSubasta(subastaFerrari);
        sessionAutos.addSubasta(subastaMercedes);
        
        SubastaSession sessionLiteratura = new SubastaSession("Literatura", 300);
        sessionLiteratura.addSubasta(subastaPrincipiosMat);
        
        

        ManejadorSubastas Subastas = ManejadorSubastas.INSTANCE.getInstance();

        Subastas.addSubastaSession(sessionPinturas);
        Subastas.addSubastaSession(sessionAutos);
        Subastas.addSubastaSession(sessionLiteratura);

    }
     
    public static void main(String[] args) throws IOException {
        

        Log log = new Log("server");
        

        //using serversocket as argument to automatically close the socket
        //the port number is unique for each server

        //list to add all the clients thread
        ArrayList<ServerThread> threadList = new ArrayList<>();

        serverSocket = new ServerSocket(1234);
        log.add("Servidor creado");
        
        initSubastaSession();
        log.add("Sesion inicializada");
        
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
