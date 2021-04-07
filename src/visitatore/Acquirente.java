package visitatore;

import museo.TicketMuseo;
import personale.NoMoneyException;

public interface Acquirente {
    void paga(int somma) throws NoMoneyException;
    void addTicket(TicketMuseo ticketMuseo);
    void ottieniRimborso(int rimborso);
    int getBilancio();
}
