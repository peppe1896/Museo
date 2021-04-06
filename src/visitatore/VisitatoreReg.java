package visitatore;

public class VisitatoreReg extends Visitatore {
    private String username = "username";
    private boolean firstSet = true;

    public VisitatoreReg(){
        super();
    }

    public VisitatoreReg(int money){
        super(money);
    }

    public void setUsername(String username){
        if(firstSet){
            this.username = username;
            firstSet = false;
        }
    }

    public String getUsername(){
        return username;
    }

}
