package visitatore;

import museo.TicketMuseo;

import java.util.ArrayList;

public class VisitatoreReg extends Visitatore implements Acquirente {
    private String username = "username";
    private boolean firstSet = true;
    private int bilancio = 0;
    private ArrayList<TicketMuseo> ticketsAcquistati = new ArrayList<>();

    public VisitatoreReg(int bilancio) {
        super(bilancio);
    }
/*
    public boolean paga(int soldi){
        if(bilancio>=soldi){
            bilancio -= soldi;
            return true;
        }
        return false;
    }
*/
    public void addTicket(TicketMuseo ticketMuseo){
        this.ticketsAcquistati.add(ticketMuseo);
    }


    public void ottieniRimborso(int rimborso){
        this.bilancio += rimborso;
    }

    public int getBilancio(){
        return bilancio;
    }

    public void setUsername(String username){
        if(firstSet){
            this.username = username;
            firstSet = false;
        }
    }

    public String getUsername(){
        return username;
    }
}
