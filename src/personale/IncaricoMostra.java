package personale;

import opera.Opera;

import java.util.ArrayList;
import java.util.List;

public class IncaricoMostra implements Incarico{
    private ArrayList<Opera> opereMostra;
    private int bilancio;
    private Personale organizzatore;
    private ArrayList<Personale> impiegati;

    IncaricoMostra(int bilancioMostra){
        opereMostra = new ArrayList<>();
        bilancio = bilancioMostra;
        impiegati = new ArrayList<>();
    }

    public void setOrganizzatore(Organizzatore organizzatore){
        this.organizzatore = organizzatore;
    }
    public void setOpereMostra(List<Opera> opere){
        opereMostra = (ArrayList<Opera>) opere;
    }
    public void addImpiegato(Impiegato impiegato){
        impiegati.add(impiegato);
        impiegato.setOccupato();
    }
    public int getBilancio(){
        return bilancio;
    }
    public List<Opera> getOpereMostra(){
        return opereMostra;
    }
}
