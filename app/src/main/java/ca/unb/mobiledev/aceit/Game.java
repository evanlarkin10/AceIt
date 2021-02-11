package ca.unb.mobiledev.aceit;

public class Game {
    private String id;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }
    public Game(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
}
