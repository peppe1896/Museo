package museo.ticket;

import visitatore.Acquirente;

public class TicketMostraFisicaEVirtuale implements TicketMuseo {
    Acquirente acquirente;
    public TicketMostraFisicaEVirtuale(Acquirente visitatore){
        this.acquirente = visitatore;
    }
}
