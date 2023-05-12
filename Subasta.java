// Purpose: Class that represents a subasta
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;

public class Subasta implements Subject{
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

    public Subasta(Product product, Integer id, int duration) {
        this.id = id;
        this.product = product;
        this.maxBid = product.getbasePrice();
        this.maxBidClient = null;
        this.isOver = false;
        this.isStarted = false;
        this.clientsInterested = new ArrayList<String>();
        this.horaInicio = LocalDateTime.now();
        this.horaFin = horaInicio.plusMinutes(duration);
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
    public void initilizeSubasta() {
        isStarted=true;
        for (String client : clientsInterested) {
            client.sendMessage("Se ha iniciado una subasta de " + product.getName() + " con un precio base de " + product.getbasePrice() + countdown());
        }
    }

    public void sendInfo(){
        for (String client : clientsInterested) {
        client.sendMessage("Subasta: " + id +" "+ product.getName() + " se ha pujado " + maxBid + " " + countdown());
        }
    }
    
    public void terminate(){
        for (String client : clientsInterested) {
        client.sendMessage("Subasta: " + id +" "+ product.getName() + " ha terminado " + maxBid + " " + countdown());
        }
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        notifyObservers();
    }
    public boolean Bet(int maxBet, String id_client) {
        if (maxBet > maxBid) {
            this.maxBid = maxBet;
            this.maxBidClient = id_client;
            sendInfo();
            return true;
        }
        
        notifyObservers();
        return false;
    }
    public int gettimeLeft() {
        return timeLeft;
    }
    public void isOver() {
        isOver = true;
        notifyObservers();
    }
    public String toString() {
        return "Subasta: " + id +" "+ product.getName() + " " + product.getbasePrice() + " " + horaInicio + " " + horaFin;
    }
    public int  getState() {
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
    public Product getProduct() {
        return product;
    }
    public int getTimeLeft() {
        return timeLeft;
    }
    public ArrayList<String> getClients() {
        return clientsInterested;
    }
    public boolean isUserSuscribed(String id_Client){
        boolean isSubscribed = false;
        for (String client : clientsInterested) {
            if(client.equals(id_Client))
                isSubscribed= true;
        }
            return isSubscribed;         
    }

    @Override
    public void addObserver(Observer o){
        observer=o;
    }

    @Override
    public void removeObserver(Observer o){
        o=null;
    }

    @Override
    public void notifyObservers(){
        observer.update();
    }
}


