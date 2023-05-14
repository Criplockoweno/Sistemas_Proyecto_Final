
import java.util.ArrayList;

public class User {

    private String name;
    private ArrayList<Integer> subastaSessionInteresados;
    private ArrayList<Integer> subastasInteresados;

    //sychronized methods para puja
    public User(String name) {
        this.name = name;
        this.subastaSessionInteresados = new ArrayList<Integer>();
        this.subastasInteresados = new ArrayList<Integer>();
    }

    public void sendMessage(String message) {
        System.out.println("Sending message: " + message);
    }

    public String getName() {
        return name;
    }

    //to do, control the subscription to subasta sessions
    public void subscribeToSubastaSession(int id_Subasta_Session) {
        subastaSessionInteresados.add(id_Subasta_Session);
    }

    public void unsubscribeToSubastaSession(int id_Subasta_Session) {

        subastaSessionInteresados.remove(id_Subasta_Session);
    }

    public void subscribeToSubasta(int id_Subasta) {
        subastasInteresados.add(id_Subasta);
    }

    public void unsubscribeToSubasta(int id_Subasta) {

        if (subastasInteresados.contains(id_Subasta)) {
            subastasInteresados.remove(Integer.valueOf(id_Subasta));
        } else {
            // handle the case where id_Subasta is not found in the list
        }
    }

    public ArrayList<Integer> getSubastaSessionInteresados() {
        return subastaSessionInteresados;
    }

    public ArrayList<Integer> getSubastasInteresados() {
        return subastasInteresados;
    }

}
