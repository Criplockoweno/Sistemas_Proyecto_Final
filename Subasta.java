// Purpose: Class that represents a subasta
import java.util.ArrayList;
public class Subasta implements Subject{
    private Integer id;
    private Observer observer;
    private Product product;
    private int timeLeft;
    private int horaInicio;
    private int horaFin;
    private int maxBid;
    private User maxBidClient;
    private boolean isOver;
    private boolean isStarted;
    private ArrayList<User> clientsInterested;

    public Subasta(Product product,int horaFin, int horaInicio, Integer id) {
        this.id = id;
        this.product = product;
        this.timeLeft = horaFin - horaInicio;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.maxBid = product.getbasePrice();
        this.maxBidClient = null;
        this.isOver = false;
        this.isStarted = false;
        this.clientsInterested = new ArrayList<User>();
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
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        notifyObservers();
    }
    public void setMaxBet(int maxBet, User client) {
        this.maxBidClient = client;
        this.maxBid = maxBet;
        notifyObservers();
    }
    public int gettimeLeft() {
        return timeLeft;
    }
    public void isOver() {
        isOver = true;
        notifyObservers();
    }
    public String toString() {
        return "Subasta: " + product.getName() + " " + product.getbasePrice() + " " + horaInicio + " " + horaFin;
    }
    public int  getState() {
        return timeLeft;
    }
    public void start() {
        isStarted = true;
        notifyObservers();
    }
    public void addClient(User client) {
        clientsInterested.add(client);
        
    }
    public void removeClient(User client) {
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
    public ArrayList<User> getClients() {
        return clientsInterested;
    }
}


