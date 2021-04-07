package visitatore;

import museo.Suggeritore;
import museo.ticket.TicketMuseo;
import museo.personaleMuseo.NoMoneyException;

import java.util.ArrayList;

public class Visitatore implements Suggeritore, Acquirente {
    private int bilancio = 0;
    private ArrayList<TicketMuseo> ticketsAcquistati = new ArrayList<>();

    public Visitatore(int bilancio){
        this.bilancio = bilancio;
    }

    public Visitatore(){
        this(0);
    }

    /**
     * Toglie dei soldiRichiesti dal bilancio di un visitatore
     * @param soldiRichiesti
     * @return True se li toglie.
     */
    public void paga(int soldiRichiesti){
        if(bilancio < soldiRichiesti) {
            throw new NoMoneyException(soldiRichiesti, bilancio);
        } else {
            bilancio -= soldiRichiesti;
        }
    }

    public void setBilancio(int bilancio){
        this.bilancio = bilancio;
    }

    public void addTicket(TicketMuseo ticketMuseo){
        this.ticketsAcquistati.add(ticketMuseo);
    }

    public void ottieniRimborso(int rimborso){
        this.bilancio += rimborso;
    }

    public int getBilancio(){
        return bilancio;
    }
}
