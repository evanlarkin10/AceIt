package ca.unb.mobiledev.aceit;

public class CatchTheDealer {
    private String id;

    public CatchTheDealer() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }
    public CatchTheDealer(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
}
