package ca.unb.mobiledev.aceit;

import java.io.Serializable;

public class Hand implements Serializable {
    private String card1 = "back";
    private String card2 = "back";
    private String card3 = "back";
    private String card4 = "back";
    private int pos;
    public Hand(){}


    public void addCard(String card, int pos){
        if(pos == 0){
            card1 = card;
        }

        if(pos == 1){
            card2 = card;
        }

        if(pos == 2){
            card3 = card;
        }

        if(pos == 3){
            card4 = card;
        }
    }

    public String getCard1(){
        return this.card1;
    }

    public String getCard2(){
        return this.card2;
    }

    public String getCard3(){
        return this.card3;
    }

    public String getCard4(){
        return this.card4;
    }

    public String toString(){
        return card1 + " " + card2 + " " + card3 +  " " + card4;
    }


}
