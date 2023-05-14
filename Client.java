
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {
        Log log = new Log();

        try ( Socket socket = new Socket("localhost", 1234)) {
            //reading the input from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //returning the output to the server : true statement is to flush the buffer otherwise
            //we have to do it manuallyy
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            //taking the user input
            Scanner scanner = new Scanner(System.in);
            String userInput;
            String response;
            String clientName = "empty";
            int idSubasta = 0;
            // create a new thread to handle incoming messages from the server
            ClientRunnable clientRun = new ClientRunnable(socket);

            new Thread(clientRun).start();
            
            //loop closes when user enters exit command
            do {

                if (clientName.equals("empty")) {
                    System.out.print("Ingresa tu nombre:  ");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    // send the client name to the server
                    output.println(userInput + "!!addName!!" + userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                    // log the client name and connection to the server
                    log.setName(clientName);
                    log.add("Client added name:" + clientName);
                    log.add("Client connected to server");
                } else {

                    log.setName(clientName);

                    while (true) {
                        // Display menu options
                        System.out.println("\nIngrese un número para seleccionar una opción:");
                        System.out.println("\t1. Suscripciones activas");
                        System.out.println("\t2. Subastas terminadas");
                        System.out.println("\t3. Subastas disponibles");
                        System.out.println("\t4. Sesiones de Subasta disponibles");
                        System.out.println("\t5. Desubscribirme");
                        System.out.println("\t0. Salir");
                        System.out.print("> ");
                        // Get user input
                        String userOpt = scanner.nextLine();

                        // Validate input
                        if (userOpt.matches("\\d+")) {
                            int option = Integer.parseInt(userOpt);

                            if (option >= 0 && option <= 5) {
                                // Execute selected option
                                switch (option) {
                                    case 1:
                                        log.add("Selected Suscripciones activas");
                                        System.out.println("\tSuscripciones activas");
                                        sendMessage(socket, scanner, clientName, output, input, "menu-" + option,log);
                                        idSubasta = Integer.valueOf(sendResponseMenu1(socket, scanner, clientName, output, input, "sub-menu-" + option + "!!",log));
                                        sendBidResponse(socket, scanner, clientName, output, input, "sub-sub-menu-1!!",log);
                                        break;
                                    case 2:
                                        
                                        log.add("Selected Subastas terminadas");
                                        System.out.println("\tSubastas terminadas");
                                        sendMessage(socket, scanner, clientName, output, input, "menu-" + option,log);
                                        
                                        scanner.nextLine(); // waits for the user to press enter
                                        break;
                                    case 3:
                                        log.add("\tSelected Subasta disponibles");
                                        System.out.println("Subastas disponibles");
                                        sendMessage(socket, scanner, clientName, output, input, "menu-" + option,log);
                                        sendResponse(socket, scanner, clientName, output, input, "sub-menu-" + option + "!!",log);
                                        break;
                                    case 4:
                                        log.add("\tSelected Sesiones de Subasta disponibles");
                                        sendMessage(socket, scanner, clientName, output, input, "menu-" + option,log);
                                        sendResponse(socket, scanner, clientName, output, input, "sub-menu-" + option + "!!",log);
                                        break;
                                    case 5:
                                        log.add("\tSelected Desubscribirme");
                                        sendMessage(socket, scanner, clientName, output, input, "menu-" + option,log);
                                        sendResponse(socket, scanner, clientName, output, input, "sub-menu-" + option + "!!",log);
                                        break;
                                                                                
                                    case 0:
                                        log.add("Exiting program...");
                                        System.out.println("Exiting program...");
                                        System.exit(0);
                                }
                            } else {
                                log.add("Menu - Invalid option");
                                System.out.println("Invalid option. Please try again.");
                            }
                        } else {
                            log.add("Menu - Invalid input");
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }

                }

            } while (!userInput.equals("exit"));

        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }

    }
    
    //Leer y respuesta, sin pre-command
    public static void sendReadMessage(Socket socket, Scanner scanner, String clientName, PrintWriter output, BufferedReader input,Log log) throws IOException {
        String userInput;
        String message = (clientName + "!!");
        System.out.print("> ");
        userInput = scanner.nextLine();
        output.println(message + userInput);
        if (userInput.equals("exit")) {
            //reading the input from server
        }
        logMessage(log,message + userInput);
    }

    //Lerr en loop y enviar respuesta con un pre-command 
    public static void sendResponse(Socket socket, Scanner scanner, String clientName, PrintWriter output, BufferedReader input, String preCommand,Log log) throws IOException {
        String userInput;
        String message = (clientName + "!!");
        //System.out.print("> ");
        userInput = scanner.nextLine();
        while (!userInput.equals("exit")) {
            //System.out.print("> ");
            if (!userInput.equals("exit")) {
                output.println(message + preCommand + userInput);
                logMessage(log,message + preCommand + userInput);
            }
            userInput = scanner.nextLine();
            
        }

    }
    
    public static void sendBidResponse(Socket socket, Scanner scanner, String clientName, PrintWriter output, BufferedReader input, String preCommand,Log log) throws IOException {
        String userInput;
        String message = (clientName + "!!");
        //System.out.print("> ");
        userInput = scanner.nextLine();
        while (!userInput.equals("exit")) {
            //System.out.print("> ");
            if (!userInput.equals("exit")) {
                output.println(message + preCommand + userInput);
            }
            userInput = scanner.nextLine();
            logMessage(log,message + preCommand + userInput);
        }
        output.println(message + preCommand + "exit_bid");
        logMessage(log,message + preCommand + "exit_bid");
    }
    
    public static String sendResponseMenu1(Socket socket, Scanner scanner, String clientName, PrintWriter output, BufferedReader input, String preCommand,Log log) throws IOException {
        String userInput;
        String message = (clientName + "!!");
        userInput = scanner.nextLine();
        output.println(message + preCommand + userInput);
        logMessage(log,userInput);
        return userInput;
        
    }

    //Enviar respuesta
    public static void sendMessage(Socket socket, Scanner scanner, String clientName, PrintWriter output, BufferedReader input, String usrMessage,Log log) throws IOException {
        String message = (clientName + "!!");
        output.println(message + usrMessage);
        logMessage(log,usrMessage);
    }
    
    public static void logMessage(Log log,String message) throws IOException{
        log.add(" Sent to server:"+message);
    }
    
}
