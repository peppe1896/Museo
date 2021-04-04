package opera;

import museo.Museo;
import java.util.Set;

public final class GestoreOpere {
    private static Set<Opera> catalogoOpere;

    /**
     * Inizializza un gestore opere, che serve per lo sharing delle opere tra vari musei.
     * Il costruttore del museo crea l'unico GestoreOpere che dovrebbe esistere, e ogni volta
     * che qualcuno deve usare qualche opera, usa il metodo del Museo getGestoreOpere() e poi
     * usa lo stesso GestoreOpere per chiedere Opere, restituirle, affittarle e cambiare le parti
     * dello stato dell'opera che si possono cambiare, quale l'utilizzatore e la disponibilità.
     * @param museo Serve solo per avere due diversi proprietari per diverse opere.
     */
    public GestoreOpere(Museo museo){
        catalogoOpere = Set.of(
                new Opera("La Gioconda", "Leonardo Da Vinci", null, 50),
                new Opera("Notte Stellata", "V. Van Gogh", null, 50),
                new Opera("La Persistenza della Memoria", "Salvador Dalì", null, 50),
                new Opera("L'Urlo", "Edvard Munch", null, 50),
                new Opera("La Ragazza con l'Orecchino di Perla", "Jean Vermeer", museo, 50),
                new Opera("L'Urlo", "Edvard Munch", museo, 50),
                new Opera("La Nascita di Venere", "Sandro Botticelli", museo, 50),
                new Opera("La Venere di Urbino", "Tiziano", museo, 50),
                new Opera("American Gothic", "Grant Wood", museo, 50),
                new Opera("Olympia", "Edouart Manet", museo, 50),
                new Opera("Ophelia", "John Everett Millais", museo, 50),
                new Opera("TestA", "Giuseppe", null, 50),
                new Opera("TestB", "Giuseppe", null, 50),
                new Opera("TestSuggerimento", "Giuseppe", museo, 0)
        );
    }

    /**
     * Previene che le opere possano venire copiate
     * @return
     */
    public Set<Opera> getCatalogoOpere(){
        return catalogoOpere;
    }

    /**
     * Affitta un opera a un Museo richiedente, verificando che l'opera non sia di questo museo e facendo aumentare il
     * bilancio del museo proprietario del costo di noleggio dell'opera.
     * <p> Potrebbe essere un null pointer il proprietario. In quel caso stampo a schermo l'avviso, ma comunque l'opera
     * la affitta, anche se ad affittarla è un proprietario null.
     * @param operaDaAffittare Opera che è richiesta per l'affitto
     * @param richiedente Il museo che richiede l'opera
     */

    public void affittaOperaAMuseo(Opera operaDaAffittare, Museo richiedente){
        if(operaDaAffittare.getProprietario() != richiedente)
            if(!operaDaAffittare.isBusy()) {
                operaDaAffittare.affitta(richiedente);
                try {
                    // aggiungo il bilancio al proprietario dell'opera, se non è null
                    operaDaAffittare.getProprietario().addBilancio(this, operaDaAffittare.getCostoNoleggio());
                }
                catch (NullPointerException e) {
                    System.err.println("Trovato proprietario null");
                }
            }
    }

    /**
     * Viene chiamato questo metodo alla fine di una mostra per permettere la restituzione al leggittimo proprietario
     * dell'opera.
     * @param operaDaRestituire L'opera che deve essere restituita
     */
    public void restituisciOperaAffittata(Opera operaDaRestituire){
        operaDaRestituire.rilasciaOpera();
    }

    /**
     * @param nomeOpera E' la String contenente il nome dell'opera ricercata
     * @return Opera se trova l'Opera con quell'esatto nome || null se non ne trova
     */
    public Opera getOperaNome(String nomeOpera){
        for(Opera o: catalogoOpere)
            if(o.getNome().equals(nomeOpera))
                return o;
        return null;
    }
}
