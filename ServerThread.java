
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread {

    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private Log log = new Log("server");
    private int subastaConnected;

    ManejadorSubastas Subastas = ManejadorSubastas.INSTANCE.getInstance();

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
        subastaConnected = 0;
    }

    public int getsubastaConnected() {
        return subastaConnected;
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
                if (outputString.equals("exit-server")) {
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
        System.out.println("Server recibio conexión de: " + command);

        ArrayList<String> splitCmd = new ArrayList<>(Arrays.asList(command.split("!!")));

        switch (splitCmd.get(1)) {
            case "addName" -> {
                caseAddName(command);
            }

            case "menu-1" -> { //Suscripciones activas
                log.add(splitCmd.get(0) + " ingreso a menu-1");
                respondSubscripcionesActivas(command);
                respondToClient("Ingresa el ID de la sesion para entrar a una sala de puja, exit para salir");
            }

            case "sub-menu-1" -> { //Suscripciones activas
                log.add(splitCmd.get(0) + " ingreso a sub-menu-1");
                System.out.println("flag:" + splitCmd.get(2));

                if (splitCmd.get(2).equals("exit")) {
                    System.out.println("flag2");
                } else {
                    System.out.println("flag3");
                    int idSubasta = Integer.parseInt(splitCmd.get(2));
                    Subasta subastaSuscrita = null;
                    Boolean exito = false;
                    for (Entry<Integer, SubastaSession> entry : Subastas.getSubastaSessions().entrySet()) {
                        for (Subasta subasta : entry.getValue().getSubastas()) {
                            if (subasta.getId() == idSubasta && subasta.isUserSuscribed(splitCmd.get(0))) {
                                subastaConnected = idSubasta;
                                subastaSuscrita = subasta;
                                exito = true;
                            }
                        }
                    }
                    if (!exito) {
                        subastaConnected = 0;

                    }

                    if (subastaConnected != 0) {
                        respondToClient(subastaSuscrita.getLastMove());
                        //respondToClient("Conection to subasta stablished");
                        log.add(splitCmd.get(0) + " ingresó a la puja ID: " + subastaSuscrita.getId());
                        respondToClient("Ingresa el valor a pujar, exit para salir");
                    } else {
                        log.add(splitCmd.get(0) + " no pudo ingresar a la sale en sub-menu-1");
                        respondToClient("No se pudo ingresar a la sala, ingresa 'exit'");
                    }

                    var time_left_copy = subastaSuscrita.getHoraFin();

                    Runnable timeRunnable = () -> {
                        final var time_left = time_left_copy;
                        LocalDateTime current_time;
                        Duration duration;
                        long total_seconds;
                        long minutes;
                        long seconds;

                        while (time_left.isAfter(LocalDateTime.now()) && subastaConnected != 0) { // loop until time_left is in the past
                            current_time = LocalDateTime.now();
                            duration = Duration.between(current_time, time_left);
                            total_seconds = duration.getSeconds();
                            minutes = total_seconds / 60;
                            seconds = total_seconds % 60;

                            if (total_seconds > 60) {
                                respondToClient("Time left: " + minutes + " minutes " + seconds + " seconds");
                                try {
                                    Thread.sleep(60000); // sleep for 1 minute
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else if (total_seconds > 10) {
                                respondToClient("Time left: " + seconds + " seconds");
                                try {
                                    Thread.sleep(10000); // sleep for 10 seconds
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                respondToClient("Time left: " + seconds + " seconds");
                                try {
                                    Thread.sleep(1000); // sleep for 1 second
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            //time_left = LocalDateTime.now().plusMinutes(5); // replace with actual value obtained from getTimeLeft()
                        }

                    };

                    Thread timeThread = new Thread(timeRunnable);

                    if (timeThread.isAlive()) {
                        //System.out.println("thread time is already running");
                    } else {
                        //System.out.println("thread time started");
                        timeThread.start();
                    }
                }
            }
            case "sub-sub-menu-1" -> {

                log.add(splitCmd.get(0) + " ingreso a sub-sub-menu-1");
                if (splitCmd.get(2).equals("exit_bid")) {
                    subastaConnected = 0;
                    log.add(splitCmd.get(0) + " salió de la sala de puja");
                }

                if (subastaConnected != 0) {

                    Subasta subastaSuscrita = null;

                    for (Entry<Integer, SubastaSession> entry : Subastas.getSubastaSessions().entrySet()) {
                        for (Subasta subasta : entry.getValue().getSubastas()) {
                            if (subasta.getId() == subastaConnected && subasta.isUserSuscribed(splitCmd.get(0))) {
                                subastaSuscrita = subasta;
                            }
                        }
                    }

                    var time_left = subastaSuscrita.getHoraFin();

                    if (!time_left.isAfter(LocalDateTime.now())) {
                        System.out.println("time_left " + time_left);
                        System.out.println("time_now " + LocalDateTime.now());
                        subastaSuscrita.isOver();
                    }

                    if (!subastaSuscrita.getIsOver()) {
                        Boolean response = subastaSuscrita.Bet(Integer.valueOf(splitCmd.get(2)), splitCmd.get(0));
                        if (response) {
                            respondToClient("Puja exitosa");
                            log.add("-" + splitCmd.get(0) + " ha pujado " + formatMoney(Integer.valueOf(splitCmd.get(2))) + " por " + subastaSuscrita.getProduct().getName());
                            for (ServerThread sT : threadList) {
                                if (sT.getsubastaConnected() != 0 && sT.getsubastaConnected() == subastaSuscrita.getId()) {
                                    System.out.println(sT.socket.getRemoteSocketAddress());
                                    sT.output.println("-" + splitCmd.get(0) + " ha pujado " + formatMoney(Integer.valueOf(splitCmd.get(2))));

                                }
                            }
                        } else {
                            log.add(splitCmd.get(0) + ": puja fallida");
                            respondToClient("La puja no fue efectuada");

                        }
                    } else {
                        respondToClient("La subasta se ha cerrado");
                        respondToClient(subastaSuscrita.getWinner());
                    }
                    respondToClient("Ingresa el valor a pujar, exit para salir");
                } else {
                    respondToClient("No se pudo ingresar a la sala, ingresa 'exit'");
                }

            }

            case "menu-2" -> {//Subastas terminadas
                log.add(splitCmd.get(0) + "ingresó a menu-2");
                for (Entry<Integer, Subasta> entry : Subastas.getSubastas().entrySet()) {
                    if (entry.getValue().getIsOver()) {
                        respondToClient(entry.getValue().getWinner());
                    }
                }
                respondToClient("Presiona enter para continuar");
            }

            case "menu-3" -> {//Subastas activas
                log.add(splitCmd.get(0) + "ingresó a menu-3");
                for (Entry<Integer, Subasta> entry : Subastas.getSubastas().entrySet()) {
                    if (!entry.getValue().getIsOver()) {
                        respondToClient(entry.getValue().toString());
                    }

                }
                respondToClient("Ingresa el ID de la subasta para subscribirte, exit para salir");

            }

            case "sub-menu-3" -> {//Subastas activas
                log.add(splitCmd.get(0) + "ingresó a sub-menu-3");
                int responseInt = 0;
                responseInt = Subastas.subcribeToSubasta(splitCmd.get(0), Integer.valueOf(splitCmd.get(2)));
                responseSubMenu3(responseInt);
                respondToClient("Ingresa el ID de la subasta a subscribirte, exit para salir");

            }

            case "menu-4" -> {//Sesiones de Subastas activas
                //TODO add subastassesion. to string
                log.add(splitCmd.get(0) + "ingresó a menu-4");

                for (Entry<Integer, SubastaSession> entry : Subastas.getSubastaSessions().entrySet()) {
                    respondToClient(entry.getValue().toString());
                }
                respondToClient("Ingresa el ID de la sesion de subasta para subscribirte, exit para salir");

            }

            case "sub-menu-4" -> {//Session subastas activas
                log.add(splitCmd.get(0) + "ingresó a sub-menu-4");
                int responseInt = 0;
                responseInt = Subastas.subscribeToSubastaSession(splitCmd.get(0), Integer.valueOf(splitCmd.get(2)));
                responseSubMenu4(responseInt);
                respondToClient("Ingresa el ID de la subasta a subscribirte, exit para salir");

            }

            case "menu-5" -> {
                log.add(splitCmd.get(0) + "ingresó a sub-menu-5");
                respondSubscripcionesActivas(command);
                respondToClient("Ingresa el ID de la subasta a desubscribirte, exit para salir");
            }

            case "sub-menu-5" -> {
                int responseInt = 0;
                responseInt = Subastas.unsubscribeToSubasta(splitCmd.get(0), Integer.valueOf(splitCmd.get(2)));
                responseSubMenu5(responseInt);
                respondToClient("Ingresa el ID de la subasta a desubscribirte, exit para salir");
            }

            default -> {
            }
        }
        //for clientlist find splitCmd.get(0)
        //respondToClient("Hola cliente");
    }

    private void responseSubMenu3(int responseInt) {
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
    }

    private void responseSubMenu4(int responseInt) {

        switch (responseInt) {
            case 0:
                respondToClient("Error: La sesion de subasta no existe");
                break;
            case 1:
                respondToClient("Error: Debes estar suscrito a una subasta para suscribirte a la sesion");
                break;
            case 2:
                respondToClient("Error: Esta sesion de subasta ya a terminado");
                break;
            case 3:
                respondToClient("Subscripción exitosa");
                break;
        }
    }

    private void responseSubMenu5(int responseInt) {
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

    public static String formatMoney(int amount) {
        // Divide the amount by 100 to get the dollar value as a double
        double dollars = amount;

        // Use a NumberFormat object to format the dollar value with commas and a period
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(3); // Set the maximum number of digits after the decimal point to 3
        String formattedDollars = nf.format(dollars);

        // Add a dollar sign to the beginning of the formatted string and return it
        return "$" + formattedDollars;
    }
}
