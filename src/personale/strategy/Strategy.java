package personale.strategy;

import museo.Museo;
import personale.strategy.IncaricoMostra;

public interface Strategy {
    /**
     * Crea un IncaricoMostra. In base al bilancio l'amministratore chiama una ConcreteStrategy.
     * Il fatto di passare un Museo porta due vantaggi: poter leggere da li quelle che sono le opere, e in più sapere
     * quali tra quelle appartengono a quel museo, così da risparmiare sui costi di noleggio.
     * Sto splittando le responsabilità?
     * @param museo Serve per dare le informazioni necessarie alla strategy.
     * @return Un IncaricoMostra che può essere assegnato direttamente a un organizzatore.
     */
    IncaricoMostra creaIncaricoMostra(Museo museo);
}
