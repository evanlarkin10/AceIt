package ca.unb.mobiledev.aceit;

public class Bet{
    private User user;
    private int betPlaced;
    private String suit;

    public Bet()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    //Constructor that must be fed a user
    public Bet(User user)
    {
        this.user = user;
        betPlaced = 0;
        suit = "";
    }

    //Constructor that takes user and there bet info
    public Bet(User user, int betPlaced, String suit)
    {
        this.user = user;
        this.betPlaced = betPlaced;
        this.suit = suit;
    }

    //Adding suit and bet value to a Bet
    public void placeBet(int bet, String suit)
    {
        betPlaced = bet;
        this.suit = suit;
    }

    public void increaseBet() { betPlaced++; }
    public void decreaseBet() { betPlaced--; }
    public void setSuit(String suit) { this.suit = suit; }


    public int getBetValue() { return betPlaced; }

    public String getSuit() { return suit; }

    public String getUserName() { return user.getName(); }
}