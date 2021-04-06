package personale;

public class NoMoneyException extends RuntimeException{

    public NoMoneyException(){
        super();
    }
    public NoMoneyException(String message){
        super("Non si può effettuare una transazione senza soldi");
    }
}
