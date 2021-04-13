package ca.unb.mobiledev.aceit;

import java.util.ArrayList;

enum HorseRaceState{
    BETTING,
    RACING,
}

public class HorseRace implements Game {
    private String id;
    private ArrayList<User> users;
    private int spadeCount;
    private int heartCount;
    private int clubCount;
    private int diamondCount;
    private int raceLength = 5; //The amount of side cards
    private GameStatus status;
    private HorseRaceState state;
    private String sideCard1Suit;
    private String sideCard2Suit;
    private String sideCard3Suit;
    private String sideCard4Suit;
    private String sideCard5Suit;
    private int leftFlipped=0;
    public Deck deck;
    private GameType gameType = GameType.HORSE_RACE;
    ArrayList<Bet> bets = new ArrayList<>(); // this will have to be changed
    ArrayList<String> cardsDrawn = new ArrayList<>();


    public HorseRace() {  } // Default constructor required for calls to DataSnapshot.getValue(Game.class)

    public HorseRace(String id, User host)
    {
        this.id = id;
        this.users = new ArrayList<>();
        this.users.add(host);
        this.spadeCount = 0;
        this.heartCount = 0;
        this.clubCount = 0;
        this.diamondCount = 0;
        this.raceLength = 5; //play five cards on the side
        this.status = GameStatus.WAITING;
        this.state = HorseRaceState.BETTING;
        this.sideCard1Suit = "";
        this.sideCard2Suit = "";
        this.sideCard3Suit = "";
        this.sideCard4Suit = "";
        this.sideCard5Suit = "";
        this.deck = new Deck();
        this.bets=new ArrayList<Bet>();
        this.leftFlipped=0;
    }

    public int getSpadeCount() { return this.spadeCount; }
    public void incrementSpade() { this.spadeCount++; }
    public void decrementSpade() { this.spadeCount--; }
    public int getHeartCount() { return this.heartCount; }
    public void incrementHeart() { this.heartCount++ ; }
    public void decrementHeart() { this.heartCount-- ; }
    public int getClubCount() { return this.clubCount; }
    public void incrementClub() { this.clubCount++; }
    public void decrementClub() { this.clubCount--; }
    public int getDiamondCount() { return this.diamondCount; }
    public void incrementDiamond() { this.diamondCount++; }
    public void decrementDiamond() { this.diamondCount--; }
    public GameType getGameType() {
        return this.gameType;
    }
    public ArrayList<Bet> getBets(){
        return this.bets;
    }
    public void setBets(ArrayList<Bet> bets){
        this.bets = bets;
    }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status=status; }
    public void setState(HorseRaceState state){
        this.state=state;
    }
    public HorseRaceState getState() { return state; }


    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    public void addBet(User user,int count, String suit){
        Bet bet = new Bet(user, count, suit);
        bets.add(bet);
    }

    public int getLeftFlipped(){
        return this.leftFlipped;
    }

    public void incrementLeftFlipped(){
        this.leftFlipped++;
    }

    @Override
    public boolean isStarted() {
        return this.state==HorseRaceState.RACING;
    }

    @Override
    public void start() {

    }

//        userList.add(davsBet);
//        userList.add(evsBet);
//        userList.add(barbsBet);

        //Checking the bet values of all users to see if betting is finished
//        betFinished = true;
//        for (int i = 0; i < userList.size(); i++) {
//            //If any users bet equals the default 0 then betFinished is false and break loop
//            if (userList.get(i).getBetValue() == 0) {
//                betFinished = false;
//                break;
//            }
//
//            //If all users have a non-zero bet then betFinished will be true and we will move to stage two
//        }


        //Second stage of the game

    /*
        if (betFinished) {
            //Checking to make sure that no suit has made it past the finish line
            if (!(spadeCount > raceLength || heartCount > raceLength ||
                    clubCount > raceLength || diamondCount > raceLength)) {
                //Checking the card that is turned from the deck
                //Card turned moves its suit up one spot
                if (turnedCardSuit == "Spade") {
                    spadeCount++;
                } else if (turnedCardSuit == "Heart") {
                    heartCount++;
                } else if (turnedCardSuit == "Club") {
                    clubCount++;
                } else if (turnedCardSuit == "Diamond") {
                    diamondCount++;
                } else {
                    System.out.println("Turned card suit error.");
                }

                //Checking the card that is turned on the side
                //Side card moves its suit back one spot
                if (sideCardSuit == "Spade") {
                    spadeCount--;
                } else if (sideCardSuit == "Heart") {
                    heartCount--;
                } else if (sideCardSuit == "Club") {
                    clubCount--;
                } else if (sideCardSuit == "Diamond") {
                    diamondCount--;
                } else {
                    System.out.println("Side card suit error.");
                }
            } else if (spadeCount > raceLength) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getSuit() == "Spade") {
                        System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getUserName(),
                                userList.get(i).getBetValue());
                    }
                }
            } else if (heartCount > raceLength) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getSuit() == "Heart") {
                        System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getUserName(),
                                userList.get(i).getBetValue());
                    }
                }
            } else if (clubCount > raceLength) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getSuit() == "Club") {
                        System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getUserName(),
                                userList.get(i).getBetValue());
                    }
                }
            } else if (diamondCount > raceLength) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getSuit() == "Diamond") {
                        System.out.printf("Congrats %s, give out %d drinks!", userList.get(i).getUserName(),
                                userList.get(i).getBetValue());
                    }
                }
            }


        } */

    }