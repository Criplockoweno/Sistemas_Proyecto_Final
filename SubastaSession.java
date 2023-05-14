
import java.util.ArrayList;

public class SubastaSession {

    private String tema;
    private ArrayList<Subasta> subastas;
    private ArrayList<User> clientsInterested;
    private boolean isFinished;
    private Integer id_SubastaSession;

    public SubastaSession(String tema, Integer Id) {
        this.tema = tema;
        this.id_SubastaSession = Id;
        this.subastas = new ArrayList<Subasta>();
        this.clientsInterested = new ArrayList<User>();
        this.isFinished = false;
    }

    public void addSubasta(Subasta subasta) {
        subastas.add(subasta);
    }

    public void addClient(User client) {
        clientsInterested.add(client);
    }

    public void removeClient(User client) {
        clientsInterested.remove(client);
    }

    public ArrayList<User> getClients() {
        return clientsInterested;
    }

    public ArrayList<Subasta> getSubastas() {
        return subastas;
    }

    public String getTema() {
        return tema;
    }

    public void initilizeSubasta(Integer id_Subasta) {
        Subasta subasta = subastas.get(id_Subasta);
        for (User client : clientsInterested) {
            client.sendMessage("Se ha iniciado una subasta de " + subasta.getProduct().getName() + " con un precio base de " + subasta.getProduct().getbasePrice() + " y un tiempo de " + subasta.getTimeLeft() + " segundos");
        }
    }

    public void isFinished() {
        isFinished = true;

    }

    public Integer getId() {
        return id_SubastaSession;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    @Override
    public String toString() {
        String header = "ID: " + getId() + " Tema: " + tema;
        String elements = "";
        for (Subasta subasta : subastas) {
            if (!subasta.getIsOver()) {
                elements = elements + "\n\t-" + subasta.getProduct().getName();
            }
        }
        if(elements.equals("")){
            elements="\n\tNo hay subastas activas en la sesion";
        }
        return header + elements;
    }

}
