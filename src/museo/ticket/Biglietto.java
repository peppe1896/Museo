package museo.ticket;

import visitatore.Visitatore;

public class Biglietto implements TicketMuseo {
    Visitatore acquirente;
    public Biglietto(Visitatore visitatore){
        this.acquirente = visitatore;
    }
}
