package ca.unb.mobiledev.aceit;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Deck implements Serializable {
    private int drawn;
    private int remaining;
    private ArrayList<String> cards;
    //public Deck(){}
    public Deck(){
        this.drawn=0;
        this.remaining = 52;
        this.cards= new ArrayList<String>();
        for(int i=2;i<11;i++){
            this.cards.add(i+"C");
            this.cards.add(i+"D");
            this.cards.add(i+"H");
            this.cards.add(i+"S");
        }
        this.cards.add("AC");
        this.cards.add("AD");
        this.cards.add("AH");
        this.cards.add("AS");

        this.cards.add("JC");
        this.cards.add("JD");
        this.cards.add("JH");
        this.cards.add("JS");

        this.cards.add("QC");
        this.cards.add("QD");
        this.cards.add("QH");
        this.cards.add("QS");

        this.cards.add("KC");
        this.cards.add("KD");
        this.cards.add("KH");
        this.cards.add("KS");
    }

    public ArrayList<String> getCards(){return this.cards;}

    public int getRemaining() {
        return this.remaining;
    }
    public int getDrawn(){
        return this.drawn;
    }

    public String drawCard(){
        Random ran = new Random();
        int position = ran.nextInt(this.cards.size());
        String card = this.cards.get(position);
        this.cards.remove(position);
        this.drawn=this.drawn+1;
        this.remaining=this.remaining-1;
        return card;
    }

    //Is c1 higher than c2?
    public int compare(char c1, char c2){
        String[] defined = {"2","3","4","5","6","7","8","9","1","J","Q","K"};
        if(Arrays.asList(defined).indexOf(String.valueOf(c1)) < Arrays.asList(defined).indexOf(String.valueOf(c2))){
            return 1;
        }
        if(Arrays.asList(defined).indexOf(String.valueOf(c1)) > Arrays.asList(defined).indexOf(String.valueOf(c2))){
            return -1;
        }
        else{ return 0;}
    }

    public int difference(char c1, char c2){
        String[] defined = {"2","3","4","5","6","7","8","9","1","J","Q","K"};
        int v1 = Arrays.asList(defined).indexOf(String.valueOf(c1));
        int v2 = Arrays.asList(defined).indexOf(String.valueOf(c2));
        return Math.abs(v1-v2);
    }
}
