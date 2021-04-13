package ca.unb.mobiledev.aceit;

import java.util.ArrayList;

public class BetList
{
    ArrayList<Bet> betList = new ArrayList<Bet>();

    public BetList()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    //public BetList()

    public void addBet(Bet bet)
    {
        boolean flag = false;
        for(int i=0; i<betList.size(); i++)
        {
            //if(betList.)
        }
        betList.add(bet);
    }

    //public int getBetValue()
    {
        //return betList.bet.getBetValue();
    }



}