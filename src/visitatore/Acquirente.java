package visitatore;

import strutturaMuseo.museoAndAdmin.Ticket;
import strutturaMuseo.NoMoneyException;

/**
 * E' l'interfaccia che preve i metodi paga, addTicket, ottieniRimborso, getBilancio e accomuna tutte quelle
 * classi che possono acquistare Ticket o biglietti
 */
public interface Acquirente {
    void paga(int somma) throws NoMoneyException;
    void addTicket(Ticket ticket);
    void ottieniRimborso(int rimborso);
    int getBilancio();
}
