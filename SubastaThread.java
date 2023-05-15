
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author sebas
 */
public class SubastaThread extends Thread {

    Subasta subasta;
    ServerThread serverThread;

    public SubastaThread(Subasta subasta, ServerThread serverThread) {
        this.subasta = subasta;
        this.serverThread = serverThread;
    }

    public void run() {
        while (!subasta.getIsStarted()) {
            try {
                Thread.sleep(200);
                if (LocalDateTime.now().isAfter(subasta.getStartDateTime())) {
                    String init = subasta.initilizeSubasta();
                    for (ServerThread sT : serverThread.getThreadlist()) {
                        if (sT.getsubastaConnected() != 0 && sT.getsubastaConnected() == subasta.getId()) {
                            //System.out.println(sT.getSocket().getRemoteSocketAddress());
                            sT.output.println(init);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SubastaThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SubastaThread.class.getName()).log(Level.SEVERE, null, ex);
            }

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
