// Purpose: Class that represents a subasta

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Subasta implements Subject {

    private Integer id;
    private Observer observer;
    private Product product;
    private int timeLeft;
    private int duration;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private int maxBid;
    private String maxBidClient;
    private boolean isOver;
    private boolean isStarted;
    private ArrayList<String> clientsInterested;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // create a formatter with a custom pattern

    private Log log;

    public Subasta(Product product, Integer id, int duration) {
        this.id = id;
        this.product = product;
        this.maxBid = product.getbasePrice();
        this.maxBidClient = "";
        this.isOver = false;
        this.isStarted = false;
        this.clientsInterested = new ArrayList<String>();
        this.horaInicio = LocalDateTime.now();
        this.horaFin = horaInicio.plusMinutes(duration);
        log = new Log("server");
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }
    
    
    
    public LocalDateTime getLocalDateTimeLeft(){
        Duration duration = Duration.between(LocalDateTime.now(), horaFin);
        long millis = duration.toMillis();
        if (millis < 0) {
            return LocalDateTime.MIN;
        } else {
            return horaFin.plus(Duration.ofMillis(millis));
        }
    }
    

    public String countdown() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, horaFin);
        long seconds = Math.max(duration.getSeconds(), 0);
        this.timeLeft = (int) seconds;
        int minutes = (int) seconds / 60;
        int remainingSeconds = (int) seconds % 60;
        if (seconds == 0) {
            isOver = true;
        }
        return ("Tiempo restante: " + minutes + " minutos y " + remainingSeconds + " segundos");
    }

    public String initilizeSubasta() throws IOException {
        isStarted = true;
        String message = "Se ha iniciado una subasta de " + product.getName() + " con un precio base de " + product.getbasePrice() + countdown();
        String preCommand = "Subasta!!init!!";
        log.add(message);
        return message + preCommand;
//        for (String client : clientsInterested) {
//            client.sendMessage("Se ha iniciado una subasta de " + product.getName() + " con un precio base de " + product.getbasePrice() + countdown());
//        }
    }

    public ArrayList<String> getClientsInterested() {
        return clientsInterested;
    }

    public void sendInfo() throws IOException {
        String message = "Subasta: " + id + " " + product.getName() + " se ha pujado " + formatMoney(maxBid) + " " + countdown();
        log.add(message);
//        for (String client : clientsInterested) {
//        client.sendMessage("Subasta: " + id +" "+ product.getName() + " se ha pujado " + maxBid + " " + countdown());
//        }
    }

    public void terminate() throws IOException {
        String message = "Subasta: " + id + " " + product.getName() + " ha terminado " + formatMoney(maxBid) + " " + countdown();
        log.add(message);
//        for (String client : clientsInterested) {
//        client.sendMessage("Subasta: " + id +" "+ product.getName() + " ha terminado " + maxBid + " " + countdown());
//        }
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


    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        notifyObservers();
    }

    public boolean Bet(int maxBet, String id_client) throws IOException {
        if (maxBet > maxBid) {
            this.maxBid = maxBet;
            this.maxBidClient = id_client;
            //sendInfo();
            return true;
        }

        //notifyObservers();
        return false;
    }

    public int gettimeLeft() {
        return timeLeft;
    }

    public void isOver() {
        isOver = true;
       //notifyObservers();
    }

    @Override
    public String toString() {
        return "ID: " + id + " " + product.getName() + "\n\t Precio Base:" + formatMoney(product.getbasePrice()) + "\n\t Inicio " + horaInicio.format(formatter) + "\n\t Fin  " + horaFin.format(formatter);
    }

    public Product getProduct() {
        return product;
    }

    public int getState() {
        return timeLeft;
    }

    public void start() {
        isStarted = true;
        notifyObservers();
    }

    public void addClient(String client) {
        clientsInterested.add(client);

    }

    public void removeClient(String client) {
        clientsInterested.remove(client);
    }

    public boolean getIsStarted() {
        return isStarted;
    }

    public boolean getIsOver() {
        return isOver;
    }

    public Integer getId() {
        return id;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public ArrayList<String> getClients() {
        return clientsInterested;
    }

    public boolean isUserSuscribed(String id_Client) {
        boolean isSubscribed = false;
        for (String client : clientsInterested) {
            if (client.equals(id_Client)) {
                isSubscribed = true;
            }
        }
        return isSubscribed;
    }

    @Override
    public void addObserver(Observer o) {
        observer = o;
    }

    @Override
    public void removeObserver(Observer o) {
        o = null;
    }

    @Override
    public void notifyObservers() {
        observer.update();
    }

    public String getWinner() {
        String message = "";
        if (!maxBidClient.equals("")) {
            message = maxBidClient + " adquirio " + product.getName() + " por " + formatMoney(maxBid);
        } else {
            message = product.getName() + " no fue comprado";
        }
        return message;
    }
    
    public String getLastMove(){
        String message = "";
        if (!maxBidClient.equals("")) {
            message = "Ultima puja:\n\t"+maxBidClient + " pujó "+ formatMoney(maxBid);
        } else {
            message = "\t\t"+product.getName() + "\nNo hay ninguna puja todavía, precio base: "+formatMoney(product.getbasePrice());
        }
        return message;
    
    }
}
