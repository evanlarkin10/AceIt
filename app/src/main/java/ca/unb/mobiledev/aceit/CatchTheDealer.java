package ca.unb.mobiledev.aceit;

import java.util.ArrayList;

public class CatchTheDealer {
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

    public void nextDealer(){
        int dealerIndex = dealer+1 % users.size();
        this.dealer=dealerIndex;
    }

    public void nextTurn(){
        int turnIndex = turn+1 % users.size();
        this.turn=turnIndex;
    }

    public void incrementStreak(){
        int streak = this.streak+1;
        this.streak=streak;
    }

    public void resetStreak(){
        this.streak=0;
    }
}
