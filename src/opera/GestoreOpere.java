package opera;

import strutturaMuseo.museo.Museo;
import strutturaMuseo.personaleMuseo.amministratore.IncaricoMostra;

import java.util.Set;

/**
 * Contiene il Field static e public con un set di Opere immutabile. Anche le Opere sono oggetti Immutabili:
 * infatti non possono essere costruite fuori da questo package, e l'unica classe che le costruisce è questa.
 */
public final class GestoreOpere {
    public final static Set<Opera> catalogoOpere = Set.of(
            new Opera("La Gioconda", "Leonardo Da Vinci", null, 50),
            new Opera("Notte Stellata", "V. Van Gogh", null, 50),
            new Opera("La Persistenza della Memoria", "Salvador Dalì", null, 50),
            new Opera("L'Urlo", "Edvard Munch", null, 50),
            new Opera("La Ragazza con l'Orecchino di Perla", "Jean Vermeer", null, 50),
            new Opera("L'Urlo", "Edvard Munch", null, 50),
            new Opera("La Nascita di Venere", "Sandro Botticelli", null, 50),
            new Opera("La Venere di Urbino", "Tiziano", null, 50),
            new Opera("American Gothic", "Grant Wood", null, 50),
            new Opera("Olympia", "Edouart Manet", null, 50),
            new Opera("Ophelia", "John Everett Millais", null, 50),
            new Opera("TestA", "Giuseppe", null, 50),
            new Opera("TestB", "Giuseppe", null, 50),
            new Opera("TestC", "Giuseppe", null, 50),
            new Opera("TestD", "Giuseppe", null, 50),
            new Opera("TestE", "Giuseppe", null, 50),
            new Opera("TestF", "Giuseppe", null, 50),
            new Opera("TestG", "Giuseppe", null, 50),
            new Opera("TestH", "Giuseppe", null, 50),
            new Opera("TestI", "Giuseppe", null, 50),
            new Opera("TestL", "Giuseppe", null, 50),
            new Opera("TestM", "Giuseppe", null, 50),
            new Opera("TestN", "Giuseppe", null, 50),
            new Opera("TestO", "Giuseppe", null, 50),
            new Opera("TestP", "Giuseppe", null, 50),
            new Opera("TestR", "Giuseppe", null, 50),
            new Opera("TestS", "Giuseppe", null, 50),
            new Opera("TestT", "Giuseppe", null, 50),
            new Opera("TestU", "Giuseppe", null, 50),
            new Opera("TestV", "Giuseppe", null, 50),
            new Opera("TestW", "Giuseppe", null, 50),
            new Opera("TestX", "Giuseppe", null, 50),
            new Opera("TestY", "Giuseppe", null, 50),
            new Opera("TestZ", "Giuseppe", null, 50),            new Opera("TestA", "Giuseppe", null, 50),
            new Opera("TestAB", "Giuseppe", null, 50),
            new Opera("TestAC", "Giuseppe", null, 50),
            new Opera("TestAD", "Giuseppe", null, 50),
            new Opera("TestAE", "Giuseppe", null, 50),
            new Opera("TestAF", "Giuseppe", null, 50),
            new Opera("TestAG", "Giuseppe", null, 50),
            new Opera("TestAH", "Giuseppe", null, 50),
            new Opera("TestAI", "Giuseppe", null, 50),
            new Opera("TestAL", "Giuseppe", null, 50),
            new Opera("TestAM", "Giuseppe", null, 50),
            new Opera("TestAN", "Giuseppe", null, 50),
            new Opera("TestAO", "Giuseppe", null, 50),
            new Opera("TestAP", "Giuseppe", null, 50),
            new Opera("TestAR", "Giuseppe", null, 50),
            new Opera("TestAS", "Giuseppe", null, 50),
            new Opera("TestAT", "Giuseppe", null, 50),
            new Opera("TestAU", "Giuseppe", null, 50),
            new Opera("TestAV", "Giuseppe", null, 50),
            new Opera("TestAW", "Giuseppe", null, 50),
            new Opera("TestAX", "Giuseppe", null, 50),
            new Opera("TestAY", "Giuseppe", null, 50),
            new Opera("TestAZ", "Giuseppe", null, 50),
            new Opera("TestSuggerimento", "Giuseppe", null, 0)
    );

    /**
     * Ogni GestoreOpere ha lo stesso catalogo di opere. Ogni museo ha un GestoreOpere e ogni museo può quindi avere
     * noleggiare opere. Per i test ho questo costruttore. Per gli altri, ho il metodo setProprietario
     * che è di questa classe, e che può permettere di cambiare proprietario a un'opera se quell'opera
     * gli appartiene. Originariamente tutti i proprietari sono null.
     * @param museo Serve solo per avere due diversi proprietari per diverse opere.
     */
    public GestoreOpere(Museo museo){
        boolean pari = true;
        for(Opera o:catalogoOpere){
            if(pari){
                o.setProprietario(museo);
                pari = !pari;
            }else{
                pari = !pari;
            }
        }
        Opera a = this.getOperaNome("TestA");
        a.setProprietario(null);
        a = this.getOperaNome("TestB");
        a.setProprietario(null);
        a = this.getOperaNome("TestY");
        a.setProprietario(null);
        a = this.getOperaNome("TestZ");
        a.setProprietario(null);
    }

    public GestoreOpere(){}
    /**
     * Creo un GestoreOpere che prevede due Musei, e che assegna due proprietari.
     * @param a
     * @param b
     */
    public GestoreOpere(Museo a, Museo b){
        this(a);
        for(Opera o:catalogoOpere)
            if(o.getProprietario() != a)
                o.setProprietario(b);
    }
    /**
     * Grazie al Set Immutabile e al fatto che anche le opere sono immutabili, sono sicuro di poter passare
     * il set a chiunque lo richieda. Le uniche modifiche alle opere possono essere fatte dall'oggetto GestoreOpere,
     * essendo l'unico oggetto presente nel package di Opera.
     * @return
     */
    public Set<Opera> getCatalogoOpere(){
        return catalogoOpere;
    }

    /**
     * Affitta un opera a un Museo richiedente, verificando che l'opera non sia di questo museo e facendo aumentare il
     * bilancio del museo proprietario del costo di noleggio dell'opera.
     * Attenzione: non si preoccupa di fare la transazione sui soldi, ma solo di aggiungere al proprietario la somma
     * del prestito. Sta a chi ha chiamato questa funzione di
     * <p> Potrebbe essere un null pointer il proprietario. In quel caso stampo a schermo l'avviso, ma comunque l'opera
     * la affitta, anche se ad affittarla è un proprietario null.
     * @param operaDaAffittare Opera che è richiesta per l'affitto
     * @param museoRichiedente Il museo che richiede l'opera
     */

    public void affittaOperaAMuseo(Opera operaDaAffittare, IncaricoMostra incaricoRichiedente, Museo museoRichiedente){
        try {
            if (!operaDaAffittare.isBusy()) {
                if (operaDaAffittare.getProprietario() != museoRichiedente) {
                    int costoOperaDaAffittare = operaDaAffittare.getCostoNoleggio();
                    try {
                        incaricoRichiedente.prelevaDenaro(this, costoOperaDaAffittare);
                        operaDaAffittare.affitta(museoRichiedente);
                        operaDaAffittare.getProprietario().addBilancio(this, operaDaAffittare.getCostoNoleggio());
                    } catch (NullPointerException e) {
                        System.err.println("Trovato proprietario null. I soldi per la transazione " +
                                "dell'Opera sono andati perduti.");
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                else {
                    operaDaAffittare.affitta(museoRichiedente);
                }
            }else
                throw new Exception("L'opera è già noleggiata.");
        }catch (Exception e) {System.err.println(e.getMessage());
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

    /**
     * Usato per ripristinare i test. Reimposta il catalogo opere.
     */
    public void resetAllOpere(){
        for(Opera o:catalogoOpere){
            o.setProprietario(null);
            restituisciOperaAffittata(o);
        }
        Set<Opera> setCheck = catalogoOpere;
    }
}
