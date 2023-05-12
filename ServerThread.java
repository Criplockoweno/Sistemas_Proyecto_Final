
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

public class ServerThread extends Thread {

    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private Log log = new Log("server");

    ManejadorSubastas Subastas = ManejadorSubastas.INSTANCE.getInstance();

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {

            //Reading the input from Client
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //returning the output to the client : true statement is to flush the buffer otherwise
            //we have to do it manuallyy
            output = new PrintWriter(socket.getOutputStream(), true);

            //inifite loop for server
            while (true) {
                String outputString = input.readLine();
                //if user types exit command
                if (outputString.equals("exit")) {
                    break;
                }
                receiveCommand(outputString);
            }

        } catch (Exception e) {
            System.out.println("Error occured " + e.getStackTrace());
            System.out.println("Error occured " + e.getMessage());
        }
    }

    private void receiveCommand(String command) throws IOException {
        System.out.println("Server received " + command);

        ArrayList<String> splitCmd = new ArrayList<>(Arrays.asList(command.split("!!")));

        switch (splitCmd.get(1)) {
            case "addName" -> {
                caseAddName(command);
            }

            case "menu-1" -> { //Suscripciones activas
                respondSubscripcionesActivas(command);
                //User tempUSr = findUser(command);
            }

            case "menu-2" -> {//Subastas terminadas
                for (Entry<Integer, Subasta> entry : Subastas.getSubastas().entrySet()) {
                    if (entry.getValue().getIsOver()) {
                        respondToClient(entry.toString());
                    }
                }
            }

            case "menu-3" -> {//Subastas activas
                for (Entry<Integer, Subasta> entry : Subastas.getSubastas().entrySet()) {
                    if (!entry.getValue().getIsOver()) {
                        respondToClient(entry.toString());
                    }

                }
                respondToClient("Ingresa el ID de la subasta para subscribirte, exit para salir");

            }
            
            case "menu-4" -> {//Subastas activas
                //TODO add subastassesion. to string
//                for (Entry<Integer, SubastaSession> entry : Subastas.getSubastaSessions().entrySet()) {
//                    if (!entry.getValue().getIsOver()) {
//                        respondToClient(entry.toString());
//                    }
//
//                }
//                respondToClient("Ingresa el ID de la session de subasta para subscribirte, exit para salir");

            }


            case "sub-menu-3" -> {//Subastas activas
                int responseInt = 0;
                responseInt = Subastas.subcribeToSubasta(splitCmd.get(0), Integer.valueOf(splitCmd.get(2)));

                switch (responseInt) {
                    case 0:
                        respondToClient("Error: La subasta no existe");
                        break;
                    case 1:
                        respondToClient("Error: Esta subasta ya comenzó");
                        break;
                    case 2:
                        respondToClient("Error: Esta subasta ya a terminado");
                        break;
                    case 3:
                        respondToClient("Subscripción exitosa");
                        break;
                }
                respondToClient("Ingresa el ID de la subasta a subscribirte, exit para salir");

            }
            case "menu-5" -> {
                respondSubscripcionesActivas(command);
                respondToClient("Ingresa el ID de la subasta a desubscribirte, exit para salir");
            }
            
            case "sub-menu-5" -> {
                int responseInt = 0;
                responseInt = Subastas.unsubscribeToSubasta(splitCmd.get(0), Integer.valueOf(splitCmd.get(2)));

                switch (responseInt) {
                    case 0:
                        respondToClient("Error: La subasta no existe");
                        break;
                    case 1:
                        respondToClient("Error: Esta subasta ya comenzó");
                        break;
                    case 2:
                        respondToClient("Error: Esta subasta ya a terminado");
                        break;
                    case 3:
                        respondToClient("Desubscripción exitosa");
                        break;
                }
                respondToClient("Ingresa el ID de la subasta a desubscribirte, exit para salir");
            }

            default -> {
            }
        }
        //for clientlist find splitCmd.get(0)
        //respondToClient("Hola cliente");
    }

    private void respondSubscripcionesActivas(String command) {
        ArrayList<String> splitCmd = new ArrayList<>(Arrays.asList(command.split("!!")));
        for (Entry<Integer, SubastaSession> entry : Subastas.getSubastaSessions().entrySet()) {
            for (Subasta subasta : entry.getValue().getSubastas()) {
                if (subasta.isUserSuscribed(splitCmd.get(0))) {
                    respondToClient(subasta.toString());
                }
            }
        }
    }

    private User findUser(String command) {
        ArrayList<String> splitCmd = new ArrayList<>(Arrays.asList(command.split("!!")));
        User temp = null;

        for (Entry<String, User> entry : Subastas.getSubscribers().entrySet()) {
            if (entry.getValue().getName().equals(splitCmd.get(1))) {
                temp = entry.getValue();
            }
        }
        return temp;

    }

    private void caseAddName(String command) throws IOException {
        ArrayList<String> splitCmd = new ArrayList<>(Arrays.asList(command.split("!!")));
        Boolean alreadyExisted = false;
        for (Entry<String, User> entry : Subastas.getSubscribers().entrySet()) {
            if (entry.getValue().getName().equals(splitCmd.get(2))) {
                alreadyExisted = true;
            }
        }
        if (alreadyExisted) {
            log.add("User re-conected: " + splitCmd.get(2));
            respondToClient("Bienvenido de nuevo " + splitCmd.get(2));
        } else {
            User newUsr = new User(splitCmd.get(2));
            Subastas.addSuscriber(newUsr);
            log.add("User created: " + splitCmd.get(2));
        }
    }

    private void respondToClient(String response) {
        output.println(response);
    }

    private void printToALlClients(String outputString) {
        for (ServerThread sT : threadList) {
            System.out.println(sT.socket.getRemoteSocketAddress());
            sT.output.println(outputString);
        }

    }

}
