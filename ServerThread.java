
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerThread extends Thread {

    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private Log log = new Log("server");

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
                //printToALlClients(outputString);
                //output.println("Server says " + outputString);
                receiveCommand(outputString);
            }

        } catch (Exception e) {
            System.out.println("Error occured " + e.getStackTrace());
        }
    }

    private void receiveCommand(String command) throws IOException {
        System.out.println("Server received " + command);

        ArrayList<String> splitCmd = new ArrayList<>(Arrays.asList(command.split("!!")));

        switch (splitCmd.get(1)) {
            case "addName" -> {
                respondToClient("optionAddName");
//                //if name exists, prin t welcome again Name
//                log.add("User re-conected: " + splitCmd.get(2));
//                respondToClient("Bienvenido de nuevo " + splitCmd.get(2));
//                //else:
//                //create new client, name splitCmd.get(2)
//                log.add("User created: " + splitCmd.get(2));
                
            }
            case "menu-1" -> {
                respondToClient("option1");
            }
            case "menu-2" -> {
                respondToClient("option2");
            }
            case "menu-3" -> {
                respondToClient("option3");
            }
            default -> {
            }
        }
        //for clientlist find splitCmd.get(0)
        //respondToClient("Hola cliente");
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
