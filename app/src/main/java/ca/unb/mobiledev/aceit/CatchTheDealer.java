package ca.unb.mobiledev.aceit;

import android.util.Log;

import java.util.ArrayList;

enum GameStatus {
    WAITING,
    STARTED,
    COMPLETED,
    CANCELLED
}
public class CatchTheDealer implements Game {
    private String id;
    private ArrayList<User> users;
    private int turn=0;
    private int dealer=0;
    private int streak=0;
    private int ace;
    private int two;
    private int three;
    private int four;
    private int five;
    private int six;
    private int seven;
    private int eight;
    private int nine;
    private int ten;
    private int jack;
    private int queen;
    private int king;
    private int maxPlayers;
    private boolean isStarted;
    public Deck deck;
    private GameStatus status;



    public CatchTheDealer() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }
    public CatchTheDealer(String id, User host){
        this.id=id;
        this.users=new ArrayList<User>();
        this.users.add(host);
        this.ace=0;
        this.two=0;
        this.three=0;
        this.four = 0;
        this.five=0;
        this.six=0;
        this.seven =0;
        this.eight=0;
        this.nine=0;
        this.ten=0;
        this.jack=0;
        this.queen=0;
        this.king=0;
        this.maxPlayers=10;
        this.isStarted=false;
        this.deck = new Deck();
        this.status = GameStatus.WAITING;
    }
    public String getId(){
        return this.id;
    }
    public int getAceCount(){
        return this.ace;
    }
    public int getTwoCount(){return this.two; }
    public int getThreeCount(){
        return this.three;
    }
    public int getFourCount(){
        return this.four;
    }
    public int getFiveCount(){
        return this.five;
    }
    public int getSixCount(){
        return this.six;
    }
    public int getSevenCount(){
        return this.seven;
    }
    public int getEightCount(){
        return this.eight;
    }
    public int getNineCount(){
        return this.nine;
    }
    public int getTenCount(){
        return this.ten;
    }
    public int getJackCount(){
        return this.jack;
    }
    public int getQueenCount(){
        return this.queen;
    }
    public int getKingCount(){
        return this.king;
    }
    public boolean isStarted(){ return this.isStarted; }

    public int getStreak(){
        return this.streak;
    }

    public int getTurn(){
        return this.turn;
    }

    public User getTurnUser(){

        return this.users.get(this.turn);
    }

    public int getDealer(){
        return this.dealer;
    }

    public User getDealerUser(){

        return this.users.get(this.dealer);
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

    public void resetStreak(){
        this.streak=0;
    }

    public void incrementCard(String card){
        switch(card.charAt(0)){
            case 'A': this.ace++;break;
            case '2': this.two++;break;
            case '3': this.three++;break;
            case '4': this.four++;break;
            case '5': this.five++;break;
            case '6': this.six++;break;
            case '7': this.seven++;break;
            case '8': this.eight++;break;
            case '9': this.nine++;break;
            case '1': this.ten++;break;
            case 'J': this.jack++;break;
            case 'Q': this.queen++;break;
            case 'K': this.king++;break;
        }
    }



    public String toString(){
        String result = "";
        result += "Dealer: " + this.getDealerUser().getName() + "\n";
        result += "Turn: " + this.getTurnUser().getName() + "\n";
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
