package personale.pkgIncaricoMostra;

import museo.Museo;

public interface Strategy {
    /**
     * Crea un IncaricoMostra || oppure distrugge la prima mostra killabile, ovvero la prima mostra che ancora è in
     * realizzazione o in svolgimento.
     * Nel primo caso: In base al bilancio l'amministratore chiama una ConcreteStrategy.
     * Il fatto di passare un Museo porta due vantaggi: poter leggere da li quelle che sono le opere, e in più sapere
     * quali tra quelle appartengono a quel museo, così da risparmiare sui costi di noleggio.
     * Nel secondo caso, l'Amministratore fornisce alla strategy la lista degli IncarichiMostre che lui detiene, e il
     * puntatore {@code this}, che serve per poter avviare la procedura di chiusura della mostra.
     * @param museo Serve per dare le informazioni necessarie alla strategy.
     * @return Un IncaricoMostra che può essere assegnato direttamente a un organizzatore.
     */
    IncaricoMostra strategyMethod(Museo museo);
}
