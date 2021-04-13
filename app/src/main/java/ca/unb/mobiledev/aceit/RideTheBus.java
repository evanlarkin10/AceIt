package ca.unb.mobiledev.aceit;

import android.util.Log;

import java.util.ArrayList;
enum RideTheBusState{
    DRAW,
    GUESS1,
    GUESS2,
}

public class RideTheBus implements Game {
    private String id;
    private ArrayList<User> users;
    private int turn=1;
    private int dealer=0;
    private int streak=0;
    private int aceCount;
    private int twoCount;
    private int threeCount;
    private int fourCount;
    private int fiveCount;
    private int sixCount;
    private int sevenCount;
    private int eightCount;
    private int nineCount;
    private int tenCount;
    private int jackCount;
    private int queenCount;
    private int kingCount;
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
        this.aceCount=0;
        this.twoCount=0;
        this.threeCount=0;
        this.fourCount = 0;
        this.fiveCount=0;
        this.sixCount=0;
        this.sevenCount =0;
        this.eightCount=0;
        this.nineCount=0;
        this.tenCount=0;
        this.jackCount=0;
        this.queenCount=0;
        this.kingCount=0;
        this.maxPlayers=10;
        this.isStarted=false;
        this.deck = new Deck();
        this.status = GameStatus.WAITING;
        this.state = RideTheBusState.DRAW;
    }
    public String getId(){
        return this.id;
    }
    public int getAceCount(){
        return this.aceCount;
    }
    public int getTwoCount(){return this.twoCount; }
    public int getThreeCount(){
        return this.threeCount;
    }
    public int getFourCount(){
        return this.fourCount;
    }
    public int getFiveCount(){
        return this.fiveCount;
    }
    public int getSixCount(){
        return this.sixCount;
    }
    public int getSevenCount(){
        return this.sevenCount;
    }
    public int getEightCount(){
        return this.eightCount;
    }
    public int getNineCount(){
        return this.nineCount;
    }
    public int getTenCount(){
        return this.tenCount;
    }
    public int getJackCount(){
        return this.jackCount;
    }
    public int getQueenCount(){
        return this.queenCount;
    }
    public int getKingCount(){
        return this.kingCount;
    }
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

    public void incrementCard(String card){
        switch(card.charAt(0)){
            case 'A': this.aceCount++;break;
            case '2': this.twoCount++;break;
            case '3': this.threeCount++;break;
            case '4': this.fourCount++;break;
            case '5': this.fiveCount++;break;
            case '6': this.sixCount++;break;
            case '7': this.sevenCount++;break;
            case '8': this.eightCount++;break;
            case '9': this.nineCount++;break;
            case '1': this.tenCount++;break;
            case 'J': this.jackCount++;break;
            case 'Q': this.queenCount++;break;
            case 'K': this.kingCount++;break;
        }
    }



    public String toString(){
        String result = "";
        result += "Streak: " + this.getStreak()+"\n";
        result += "A:" + this.getAceCount() + " ";
        result += "2:" + this.getTwoCount() + " ";
        result += "3:" + this.getThreeCount() + " ";
        result += "4:" + this.getFourCount() + " ";
        result += "5:" + this.getFiveCount() + " ";
        result += "6:" + this.getSixCount() + " ";
        result += "7:" + this.getSevenCount() + " ";
        result += "8:" + this.getEightCount() + " ";
        result += "9:" + this.getNineCount() + " ";
        result += "10:" + this.getTenCount() + " ";
        result += "J:" + this.getJackCount() + " ";
        result += "Q:" + this.getQueenCount() + " ";
        result += "K:" + this.getKingCount() + " ";
        result+="Users" + this.users.size();
        return result;
    }


}
