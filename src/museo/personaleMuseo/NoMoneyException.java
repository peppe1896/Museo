package museo.personaleMuseo;

public class NoMoneyException extends RuntimeException{

    public NoMoneyException(){
        super("Non si può effettuare una transazione senza soldi");
    }

    public NoMoneyException(int soldiRichiesti, int soldiPresenti){
        super("Presenti: " + soldiPresenti + " - Richiesti: "+ soldiRichiesti + "." +
                " Non è possibile completare il tasferimento.");
    }

}
