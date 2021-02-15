package ca.unb.mobiledev.aceit;

public class RideTheBus{
    private String id;

    public RideTheBus() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }
    public RideTheBus(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
}
