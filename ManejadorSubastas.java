
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public enum ManejadorSubastas{

    INSTANCE;
    private HashMap<Integer, Subasta> subastas;
    private HashMap<String, User> suscribers;
    private HashMap<Integer, SubastaSession> subastaSessions;

    ManejadorSubastas() {
        //System.out.println("costructor called");
        this.subastas = new HashMap<>();
        this.suscribers = new HashMap<>();
        this.subastaSessions = new HashMap<>();
    }
    
    public ManejadorSubastas getInstance() {
        return INSTANCE;
    }
    
    public HashMap<String, User> getSubscribers(){
        return suscribers;
    }
    
    public HashMap<Integer, Subasta> getSubastas(){
        return subastas;
    }
    public HashMap<Integer, SubastaSession> getSubastaSessions(){
        return subastaSessions;
    }
            
    public synchronized void addSubasta(Subasta subasta) {
        subastas.put(subasta.getId(), subasta);
    }

    public synchronized void removeSubasta(Subasta subasta) {
        subastas.remove(subasta.getId());
    }

    public synchronized void addSuscriber(User client) {
        suscribers.put(client.getName(), client);
    }

    public synchronized void removeSuscriber(User client) {
        suscribers.remove(client.getName());
    }

    public synchronized void update() {
        System.out.println("update");
    }

    public synchronized void notifySuscribers(String message) {
        for (User users : suscribers.values()) {
            users.sendMessage(message);
        }
    }
    public void addSubastaSession(SubastaSession session){
        
       // System.out.println(session.getId());
        
        subastaSessions.put(session.getId(), session);
        
        for(Subasta subasta: session.getSubastas()){
            subastas.put(subasta.getId(), subasta);
        }
    }
    public synchronized void removeSubastaSession(SubastaSession subastaSession) {
        subastaSessions.remove(subastaSession);
    }

    public synchronized void addSubastaToSession(Subasta subasta, SubastaSession subastaSession) {
        subastaSession.addSubasta(subasta);
    }

    public synchronized void initializeSubastaSession(int id_Subasta_Session, int id_Subasta) {
        subastaSessions.get(id_Subasta_Session).initilizeSubasta(id_Subasta);
    }

    public synchronized int subscribeToSubastaSession(String id_Client, Integer id_Subasta_Session) {
        // if the subasta session doesnt exist, return 0
        if (subastaSessions.get(id_Subasta_Session) == null) {
            return 0;
        }

        //if a client isnt subscribed to a subasta, he cant subscribe to a subasta session
        for (Subasta subasta : subastaSessions.get(id_Subasta_Session).getSubastas()) {
            if ((subasta.getClients().contains(suscribers.get(id_Client)))) {
                return 1;
            }
        }
        //if the subasta session is finished, return 2
        if (subastaSessions.get(id_Subasta_Session).getIsFinished() == true) {
            return 2;
        } else {
            //if everything is ok, subscribe the client to the subasta session,return 3
            subastaSessions.get(id_Subasta_Session).addClient(suscribers.get(id_Client));
            suscribers.get(id_Client).subscribeToSubastaSession(id_Subasta_Session);
            
            for(Subasta subasta: subastaSessions.get(id_Subasta_Session).getSubastas()){
                subasta.addClient(id_Client);
                suscribers.get(id_Client).subscribeToSubasta(subasta.getId());
            }
            return 3;
        }
        
        

    }

    // to do, control how to unsuscribe with the the number of subastas interesadas 
    public synchronized int unsubscribeToSubastaSession(Integer id_Client, Integer id_Subasta_Session) {
        subastaSessions.get(id_Subasta_Session).removeClient(suscribers.get(id_Client));
        return 1;
    }

    public synchronized int findSubastaSessionOfaSubasta(int id_Subasta) {
        int id_SubastaSession = 0;

        return id_SubastaSession;
    }


    public synchronized int subcribeToSubasta(String id_Client, Integer id_Subasta) {
        // if the subasta doesnt exist, return 0
        if (subastas.get(id_Subasta) == null) {
            return 0;
        }
        // if the subasta is started, return 1
        if (subastas.get(id_Subasta).getIsStarted() == true) {
            return 1;
        }
        // if the subasta is over, return 2
        if (subastas.get(id_Subasta).getIsOver() == true) {
            return 2;
        } else {
            // if everything is ok, subscribe the client to the subasta, return 3
            subastas.get(id_Subasta).addClient(id_Client);
            suscribers.get(id_Client).subscribeToSubasta(id_Subasta);
            return 3;
        }
        
        
    }

    public synchronized int unsubscribeToSubasta(String id_Client, Integer id_Subasta) {
        // if the subasta doesnt exist, return 0
        if (subastas.get(id_Subasta) == null) {
            return 0;
        }
        
        // if the subasta is started, return 1
        if (subastas.get(id_Subasta).getIsStarted() == true) {
            return 1;
        }
        // if the subasta is over, return 2
        if (subastas.get(id_Subasta).getIsOver() == true) {
            return 2;
        } else {

            // if everything is ok, unsubscribe the client to the subasta, return 3
            subastas.get(id_Subasta).removeClient(id_Client);
            suscribers.get(id_Client).unsubscribeToSubasta(id_Subasta);
            //System.out.println("flag 4");
            return 3;
        }
        
       
    }

}
