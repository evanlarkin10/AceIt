package ca.unb.mobiledev.aceit;

import android.util.Log;

import java.util.ArrayList;
enum RideTheBusState{
    RB,
    HL,
    IO,
    SUIT,
    WAIT
}

public class RideTheBus implements Game {
    private String id;
    private ArrayList<User> users;
    private ArrayList<Hand> hands;
    private int turn=0;
    private int dealer=0;
    private int streak=0;
//    private String card1 = "back";
 //   private String card2 = "back";
  //  private String card3 = "back";
   // private String card4 = "back";
    private int maxPlayers;
    private boolean isStarted;
    public Deck deck;
    private GameStatus status;
    private RideTheBusState state;
    private String cardDrawn;
    private GameType type=GameType.CROSS_THE_BRIDGE;



    public RideTheBus() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }
    public RideTheBus(String id, User host){
        this.id=id;
        this.users=new ArrayList<User>();
        this.users.add(host);
        this.hands=new ArrayList<Hand>();
        this.hands.add(new Hand());


        this.maxPlayers=10;
        this.isStarted=false;
        this.deck = new Deck();
        this.status = GameStatus.WAITING;
        this.state = RideTheBusState.RB;
    }
    public String getId(){
        return this.id;
    }

    public String getCard1(){ return this.hands.get(this.turn).getCard1();}
    public String getCard2(){ return this.hands.get(this.turn).getCard2();}
    public String getCard3(){ return this.hands.get(this.turn).getCard3();}
    public String getCard4(){ return this.hands.get(this.turn).getCard4();}
    public boolean isStarted(){ return this.isStarted; }

    public int getStreak(){
        return this.streak;
    }

    public int getTurn(){
        return this.turn;
    }



    public int getDealer(){
        return this.dealer;
    }



    public ArrayList<User> getUsers(){
        return this.users;
    }

    public Deck getDeck(){return this.deck;}

    public void addUser(User user){
        this.users.add(user);
    }

    public void start(){
        this.isStarted=true;
    }

    public void nextDealer(){

        int dealerIndex = (this.dealer+1) % this.users.size();
        this.dealer=dealerIndex;
    }

    public void nextTurn(){

        int turnIndex = (this.turn+1) % this.users.size();
        this.turn=turnIndex;
    }

    public void incrementStreak(){
        int streak = this.streak+1;
        this.streak=streak;
    }

    public GameType getGameType() {
        return this.type;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status=status;
    }

    public RideTheBusState getState() {
        return this.state;
    }

    public void setState(RideTheBusState state) {
        this.state = state;
    }

    public void setCardDrawn(String card){
        cardDrawn=card;
    }

    public String getCardDrawn(){
        return this.cardDrawn;
    }

    public void resetStreak(){
        this.streak=0;
    }

    public void setCard1(String card){hands.get(getTurn()).addCard(card,0);}
    public void setCard2(String card){hands.get(getTurn()).addCard(card,1);}
    public void setCard3(String card){hands.get(getTurn()).addCard(card,2);}
    public void setCard4(String card){hands.get(getTurn()).addCard(card,3);}







    public String toString(){
        String result = "";
        result+="Card1" + this.hands.get(getTurn()).getCard1();
        result+="Card1" + this.hands.get(getTurn()).getCard1();
        result+="Card1" + this.hands.get(getTurn()).getCard1();
        result+="Card1" + this.hands.get(getTurn()).getCard1();
        result+="Users" + this.users.size();
        return result;
    }


}
