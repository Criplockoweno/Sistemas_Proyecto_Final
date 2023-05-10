
import java.io.BufferedReader;
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

            ClientRunnable clientRun = new ClientRunnable(socket);

            new Thread(clientRun).start();
            //loop closes when user enters exit command

            do {

                if (clientName.equals("empty")) {
                    System.out.print("Ingresa tu nombre:  ");
                    userInput = scanner.nextLine();
                    clientName = userInput;
                    output.println(userInput+"!!addName!!"+userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                    log.setName(clientName);
                    log.add("Client added name:" + clientName);
                    log.add("Client connected to server");
                } else {
                    // Scanner input = new Scanner(System.in);
                    log.setName(clientName);

                    while (true) {
                        // Display menu options
                        System.out.println("\nIngrese un número para seleccionar una opción:");
                        System.out.println("\t1. Suscripciones activas");
                        System.out.println("\t2. Subastas terminadas");
                        System.out.println("\t3. Sesiones de Subasta disponibles");
                        System.out.println("\t0. Salir");
                        System.out.print("> ");
                        // Get user input
                        String userOpt = scanner.nextLine();

                        // Validate input
                        if (userOpt.matches("\\d+")) {
                            int option = Integer.parseInt(userOpt);
                            
                            if (option >= 0 && option <= 3) {
                                // Execute selected option
                                switch (option) {
                                    case 1:
                                        sendMessage(socket, scanner, clientName, output, input,"menu-"+option);
                                        log.add("Selected Suscripciones activas");
                                        System.out.println("\tSuscripciones activas");
                                        
                                        break;
                                    case 2:
                                        sendMessage(socket, scanner, clientName, output, input,"menu-"+option);
                                        log.add("Selected Subastas terminadas");
                                        System.out.println("\tSubastas terminadas");
                                        break;
                                    case 3:
                                        sendMessage(socket, scanner, clientName, output, input,"menu-"+option);
                                        log.add("\tSelected Subasta disponibles");
                                        System.out.println("Sesiones de Subasta disponibles");
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

    public static void sendMessage(Socket socket,Scanner scanner, String clientName, PrintWriter output, BufferedReader input) {
        String userInput;
        //String message = ("(" + clientName + ")" + " : ");
        String message = ( clientName + "!!" );
        System.out.print("> ");
        userInput = scanner.nextLine();
        output.println(message + userInput);
        if (userInput.equals("exit")) {
            //reading the input from server
        }
    }
    
    public static void sendMessage(Socket socket,Scanner scanner, String clientName, PrintWriter output, BufferedReader input,String usrMessage) {
        //String message = ("(" + clientName + ")" + " : ");
        String message = ( clientName + "!!" );
        output.println(message + usrMessage);
    }

}
