package ca.unb.mobiledev.aceit;

import android.util.Log;

import java.util.ArrayList;

public class SimCatchTheDealer {
    private String TAG ="SIM_CATCH_THE_DEALER_SCREEN";
    private CatchTheDealer game;



    public SimCatchTheDealer() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }
    public SimCatchTheDealer(CatchTheDealer game){
        this.game=game;
        this.run();

    }

    public void round(String card, char guess, char secondGuess){
        Log.d(TAG, "Next Round");
        Log.d(TAG, "Drawn Card" + card);
        Log.d(TAG, this.game.getTurnUser().getName() + " guesses " + guess);
        int compare = this.game.getDeck().compare(guess, card.charAt(0));
        if(compare>0){
            Log.d(TAG, " Card is higher than " + guess);
            Log.d(TAG, this.game.getTurnUser().getName() + " guesses second" + secondGuess);
            compare = this.game.getDeck().compare(secondGuess, card.charAt(0));
            if(compare!=0){
                Log.d(TAG, "Wrong! Drink diff " + this.game.getDeck().difference(card.charAt(0), secondGuess));
                this.game.incrementStreak();
            }
            else {
                Log.d(TAG, " Got it! 1 Drinks to" + this.game.getDealerUser().getName());
                this.game.resetStreak();
            }
        }
        else if(compare<0) {
            Log.d(TAG, " Card is lower than " + guess);
            Log.d(TAG, this.game.getTurnUser().getName() + " guesses second" + secondGuess);
            compare = this.game.getDeck().compare(secondGuess, card.charAt(0));
            if(compare!=0){
                Log.d(TAG, "Wrong! Drink diff " + this.game.getDeck().difference(card.charAt(0), secondGuess));
                this.game.incrementStreak();
            }
            else {
                Log.d(TAG, " Got it! 1 Drinks to" + this.game.getDealerUser().getName());
                this.game.resetStreak();
            }
        }
        else {
            Log.d(TAG, " Got it! 3 Drinks to" + this.game.getDealerUser().getName());
            this.game.resetStreak();
        }
        this.game.incrementCard(card);
        this.game.nextTurn();
        if(this.game.getStreak()==3){
            this.game.resetStreak();
            this.game.nextDealer();
        }
        Log.d(TAG, "Game State\n" + this.game.toString());
    }
    public void run(){
        Log.d(TAG, "Starting simulation");
        Log.d(TAG, this.game.toString());

        String card = this.game.getDeck().drawCard();
        char guess = '8';
        char secondGuess = 'J';
        round(card, guess, secondGuess);

        card = this.game.getDeck().drawCard();
        guess = '5';
        secondGuess = 'K';
        round(card, guess, secondGuess);

        card = this.game.getDeck().drawCard();
        guess = '7';
        secondGuess = '2';
        round(card, guess, secondGuess);

        card = this.game.getDeck().drawCard();
        guess = '9';
        secondGuess = '4';
        round(card, guess, secondGuess);






    }



}
