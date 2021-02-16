package ca.unb.mobiledev.aceit;

public class HorseRace {
    private int spadeCount;
    private int heartCount;
    private int clubCount;
    private int diamondCount;
    private int raceLength; //The amount of side cards
    private boolean betFinished = false; //default to not finished betting
    private String turnedCardSuit;
    private String sideCardSuit;
    ArrayList<Bet> userList = new ArrayList<Bet>;


    public HorseRace()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    //First stage of the game:
    //Set up the three users that will be playing
    User dav = new User("69", "Dav");
    User ev = new User("420", "Ev");
    User barb = new User("18", "Barb");

    //Set up the bets the users are making
    Bet davsBet = new Bet(dav, 3, "Diamond");
    Bet evsBet = new Bet(ev, 2, "Heart");
    Bet barbsBet = new Bet(barb, 4, "Spade");

    userList.add(davsBet);
    userList.add(evsBet);
    userList.add(barbsBet);

    //Checking the bet values of all users to see if betting is finished
    betFinished = true;
    for(int i=0; i<userList.size(); i++)
    {
        //If any users bet equals the default 0 then betFinished is false and break loop
        if(userList.get(i).getBetValue() == 0)
        {
            betFinished = false;
            break;
        }

        //If all users have a non-zero bet then betFinished will be true and we will move to stage two
    }


    //Second stage of the game
    if(betFinished)
    {
        //Checking to make sure that no suit has made it past the finish line
        if(!(spadeCount > raceLength || heartCount > raceLength ||
                clubCount > raceLength || diamondCount > raceLength))
        {
            //Checking the card that is turned from the deck
            //Card turned moves its suit up one spot
            if (turnedCardSuitcardSuit == "Spade") { spadeCount++; }
            else if (turnedCardSuit == "Heart") { heartCount++; }
            else if (turnedCardSuit == "Club") { clubCount++; }
            else if (turnedCardSuit == "Diamond") { diamondCount++; }
            else { System.out.println("Turned card suit error."); }

            //Checking the card that is turned on the side
            //Side card moves its suit back one spot
            if (sideCardSuit == "Spade") { spadeCount--; }
            else if (sideCardSuit == "Heart") { heartCount--; }
            else if (sideCardSuit == "Club") { clubCount--; }
            else if (sideCardSuit == "Diamond") { diamondCount--; }
            else { System.out.println("Side card suit error."); }
        }

        else if(spadeCount > raceLength)
        {
            for(int i=0; i<userList.size();i++)
            {
                if(userList.get(i).getSuit() == "Spade")
                {
                    System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getName(),
                            userList.get(i).getBetValue());
                }
            }
        }

        else if(heartCount > raceLength)
        {
            for(int i=0; i<userList.size();i++)
            {
                if(userList.get(i).getSuit() == "Heart")
                {
                    System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getName(),
                            userList.get(i).getBetValue());
                }
            }
        }

        else if(clubCount > raceLength)
        {
            for(int i=0; i<userList.size();i++)
            {
                if(userList.get(i).getSuit() == "Club")
                {
                    System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getName(),
                            userList.get(i).getBetValue());
                }
            }
        }

        else if(diamondCount > raceLength)
        {
            for(int i=0; i<userList.size();i++)
            {
                if(userList.get(i).getSuit() == "Diamond")
                {
                    System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getName(),
                            userList.get(i).getBetValue());
                }
            }
        }


    }

}