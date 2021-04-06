package visitatore;

import museo.Suggeritore;
import museo.TicketMuseo;

import java.util.ArrayList;

public class Visitatore implements Suggeritore, Acquirente {
    protected int bilancio = 0;
    protected ArrayList<TicketMuseo> ticketsAcquistati = new ArrayList<>();
    public Visitatore(){}
    /**
     Chiamata da Museo per pagare
     **/
    public boolean paga(int soldi){
        if(bilancio>=soldi){
            bilancio -= soldi;
            return true;
        }
        return false;
    }

    public void addTicket(TicketMuseo ticketMuseo){
        this.ticketsAcquistati.add(ticketMuseo);
    }

    public Visitatore(int bilancio){
        this.bilancio = bilancio;
    }

    public void ottieniRimborso(int rimborso){
        this.bilancio += rimborso;
    }

    public int getBilancio(){
        return bilancio;
    }
}
