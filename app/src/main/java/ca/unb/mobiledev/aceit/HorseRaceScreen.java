package ca.unb.mobiledev.aceit;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HorseRaceScreen extends Fragment {
    private String TAG = "HORSE_RACE_SCREEN";
    private HorseRace game;
    private String id;
    private TextView horseTitle;
    private TextView yourBet;
    private TextView betValue;
    private TextView on;
    private TextView betSuit;
    private String user_id = "";
    private Button increaseBet;
    private Button decreaseBet;
    private Button confirmBet;
    private Button spadeButton;
    private Button heartButton;
    private Button clubButton;
    private Button diamondButton;
    private ImageView side5;
    private ImageView side4;
    private ImageView side3;
    private ImageView side2;
    private ImageView side1;
    private ImageView spadeMover;
    private ImageView spade1;
    private ImageView spade2;
    private ImageView spade3;
    private ImageView spade4;
    private ImageView spade5;
    private ImageView heartMover;
    private ImageView heart1;
    private ImageView heart2;
    private ImageView heart3;
    private ImageView heart4;
    private ImageView heart5;
    private ImageView clubMover;
    private ImageView club1;
    private ImageView club2;
    private ImageView club3;
    private ImageView club4;
    private ImageView club5;
    private ImageView diamondMover;
    private ImageView diamond1;
    private ImageView diamond2;
    private ImageView diamond3;
    private ImageView diamond4;
    private ImageView diamond5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate view
        View view = inflater.inflate(R.layout.horse_race, container, false);
        return view;
    }

    public void setGame(HorseRace game) { this.game = game; }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO Still need to find out how Ev did this part.
        //I have checked the build.gradle and it is present there, maybe I have the wrong ID?
        HorseRaceScreenArgs args = HorseRaceScreenArgs.fromBundle(getArguments());
        id = args.getId();

        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        user_id = settings.getString("uid", "id");

        //Datebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, snapshot.getValue().toString());
                Log.d(TAG,"Listened");

                HorseRace game = snapshot.getValue(HorseRace.class);
                setGame(game);
                handleGameStateUpdate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());

            }
        };
        myRef.child(id).addValueEventListener(gameListener);

        //Setting up buttons, imageviews
        horseTitle = view.findViewById(R.id.horse_race_title);
        yourBet = view.findViewById(R.id.yourBet_textview);
        betValue = view.findViewById(R.id.betValue_textview);
        on = view.findViewById(R.id.on_textview);
        betSuit = view.findViewById(R.id.betsuit_textview);

        increaseBet = view.findViewById(R.id.higher_bet);
        increaseBet.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View v) { game.userList.get(0).increaseBet();
        }

    });

        decreaseBet = view.findViewById(R.id.lower_bet);
        decreaseBet.setOnClickListener(new View.OnClickListener()
        {
        @Override
                public void onClick(View v) {
            game.userList.get(0).decreaseBet();
        }
        });

        spadeButton = view.findViewById(R.id.spade_button);
        spadeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                game.userList.get(0).setSuit("Spade");
            }
        });

        heartButton = view.findViewById(R.id.heart_button);
        heartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                game.userList.get(0).setSuit("Heart");
            }
        });

        clubButton = view.findViewById(R.id.club_button);
        clubButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                game.userList.get(0).setSuit("Club");
            }
        });

        diamondButton = view.findViewById(R.id.diamond_button);
        diamondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.userList.get(0).setSuit("Diamond");
            }
        });

        }

        private void updateDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Log.d(TAG, "Updated database");
        myRef.child(id).setValue(this.game);
        }





    private void handleGameStateUpdate() {

        //update DB
        updateDB();

        //Display what suit the player bet on and how much they bet
        ((TextView)getActivity().findViewById(R.id.yourBet_textview)).setText(game.userList.get(0).getBetValue());
        ((TextView)getActivity().findViewById(R.id.betValue_textview)).setText(game.userList.get(0).getSuit());

        if(!game.isStarted()) { //If game is still in betting phase enable all betting buttons
            increaseBet.setEnabled(true);
            decreaseBet.setEnabled(true);
            confirmBet.setEnabled(true);
            spadeButton.setEnabled(true);
            heartButton.setEnabled(true);
            clubButton.setEnabled(true);
            diamondButton.setEnabled(true);
        }
        else {//If game has started, disable betting buttons
            increaseBet.setEnabled(false);
            decreaseBet.setEnabled(false);
            confirmBet.setEnabled(false);
            spadeButton.setEnabled(false);
            heartButton.setEnabled(false);
            clubButton.setEnabled(false);
            diamondButton.setEnabled(false);

        }

        //on game over state
        if(this.game.getStatus().equals(GameStatus.COMPLETED)) {
            NavHostFragment.findNavController(HorseRaceScreen.this)
                    .navigate(R.id.action_horseRaceScreen_to_HomeScreen);
            Log.d(TAG, "Game over, returning to home screen.");
        }

        //on racing state
        if(this.game.getStatus().equals("RACING"))
        {
            String card = game.deck.drawCard();
            game.cardsDrawn.add(card);
            String suit = String.valueOf(card.charAt(1)); //Suit is second char in string
            switch(suit) {
                //Spade
                case "S":
                    game.incrementSpade();
                    break;
                case "H" :
                    game.incrementHeart();
                    break;
                case "C" :
                    game.incrementClub();
                    break;
                case "D" :
                    game.incrementDiamond();
                    break;
                default:
                    Log.i(TAG, "Card drawn resulted in error.");
                    break;

            }
        }

        //Updating side where turned cards appear
        switch(game.cardsDrawn.size()) {
            case 1:
                ((ImageView)getActivity().findViewById(R.id.side1)).setImageResource(checkImageResource(game.cardsDrawn.get(0)));
                ((ImageView)getActivity().findViewById(R.id.side2)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side3)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side4)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side5)).setImageResource(R.drawable.back);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.side1)).setImageResource(checkImageResource(game.cardsDrawn.get(0)));
                ((ImageView)getActivity().findViewById(R.id.side2)).setImageResource(checkImageResource(game.cardsDrawn.get(1)));
                ((ImageView)getActivity().findViewById(R.id.side3)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side4)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side5)).setImageResource(R.drawable.back);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.side1)).setImageResource(checkImageResource(game.cardsDrawn.get(0)));
                ((ImageView)getActivity().findViewById(R.id.side2)).setImageResource(checkImageResource(game.cardsDrawn.get(1)));
                ((ImageView)getActivity().findViewById(R.id.side3)).setImageResource(checkImageResource(game.cardsDrawn.get(2)));
                ((ImageView)getActivity().findViewById(R.id.side4)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side5)).setImageResource(R.drawable.back);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.side1)).setImageResource(checkImageResource(game.cardsDrawn.get(0)));
                ((ImageView)getActivity().findViewById(R.id.side2)).setImageResource(checkImageResource(game.cardsDrawn.get(1)));
                ((ImageView)getActivity().findViewById(R.id.side3)).setImageResource(checkImageResource(game.cardsDrawn.get(2)));
                ((ImageView)getActivity().findViewById(R.id.side4)).setImageResource(checkImageResource(game.cardsDrawn.get(3)));
                ((ImageView)getActivity().findViewById(R.id.side5)).setImageResource(R.drawable.back);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.side1)).setImageResource(checkImageResource(game.cardsDrawn.get(0)));
                ((ImageView)getActivity().findViewById(R.id.side2)).setImageResource(checkImageResource(game.cardsDrawn.get(1)));
                ((ImageView)getActivity().findViewById(R.id.side3)).setImageResource(checkImageResource(game.cardsDrawn.get(2)));
                ((ImageView)getActivity().findViewById(R.id.side4)).setImageResource(checkImageResource(game.cardsDrawn.get(3)));
                ((ImageView)getActivity().findViewById(R.id.side5)).setImageResource(checkImageResource(game.cardsDrawn.get(4)));
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.side1)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side2)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side3)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side4)).setImageResource(R.drawable.back);
                ((ImageView)getActivity().findViewById(R.id.side5)).setImageResource(R.drawable.back);
                break;
        }


        //Depending on how many spades have been drawn, display the ace of spades
        //Temporarily has to be shown as a king because the ace of clubs is misbehaving
        //Do the same for all four suits
        switch(game.getSpadeCount()) {
            case 1:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(R.drawable.sk);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(R.drawable.sk);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(R.drawable.sk);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(R.drawable.sk);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(R.drawable.sk);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(R.drawable.sk);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
        }

        switch(game.getHeartCount()) {
            case 1:
                ((ImageView)getActivity().findViewById(R.id.heart_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position1)).setImageResource(R.drawable.ah);
                ((ImageView)getActivity().findViewById(R.id.heart_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.heart_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position2)).setImageResource(R.drawable.ah);
                ((ImageView)getActivity().findViewById(R.id.heart_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.heart_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position3)).setImageResource(R.drawable.ah);
                ((ImageView)getActivity().findViewById(R.id.heart_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.heart_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position4)).setImageResource(R.drawable.ah);
                ((ImageView)getActivity().findViewById(R.id.heart_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.heart_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position5)).setImageResource(R.drawable.ah);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.heart_mover)).setImageResource(R.drawable.ah);
                ((ImageView)getActivity().findViewById(R.id.heart_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.heart_position5)).setImageResource(android.R.color.transparent);
                break;
        }

        switch(game.getClubCount()) {
            case 1:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(R.drawable.ck);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(R.drawable.ck);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(R.drawable.ck);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(R.drawable.ck);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(R.drawable.ck);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(R.drawable.sk);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
        }

        switch(game.getDiamondCount()) {
            case 1:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(R.drawable.dk);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(R.drawable.dk);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(R.drawable.dk);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(R.drawable.dk);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(R.drawable.dk);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(R.drawable.dk);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
        }
    }

    private int checkImageResource(String s) {
        //This method will take a card value and returns its image path to use to update UI
        String valueIn = String.valueOf(s.charAt(0)); //value is first part of string
        String suitIn = String.valueOf(s.charAt(1)); //value is first part of string
        int returnValue = 0;

        if(suitIn.equals("S"))
        {
            switch(valueIn) {
                //ace of spades missing, ignoring and it will go to default
                case "2" :
                    returnValue = R.drawable.s2;
                    break;
                case "3" :
                    returnValue = R.drawable.s3;
                    break;
                case "4" :
                    returnValue = R.drawable.s4;
                    break;
                case "5" :
                    returnValue = R.drawable.s5;
                    break;
                case "6" :
                    returnValue = R.drawable.s6;
                    break;
                case "7" :
                    returnValue = R.drawable.s7;
                    break;
                case "8" :
                    returnValue = R.drawable.s8;
                    break;
                case "9" :
                    returnValue = R.drawable.s9;
                    break;
                case "10" :
                    returnValue = R.drawable.s10;
                    break;
                case "J" :
                    returnValue = R.drawable.sj;
                    break;
                case "Q" :
                    returnValue = R.drawable.sq;
                    break;
                case "K" :
                    returnValue = R.drawable.sk;
                    break;
                default:
                    returnValue = R.drawable.back;
                    break;
            }
        }

        else if(suitIn.equals("H"))
        {
            switch(valueIn) {
                case "A" :
                    returnValue = R.drawable.ah;
                    break;
                case "2" :
                    returnValue = R.drawable.h2;
                    break;
                case "3" :
                    returnValue = R.drawable.h3;
                    break;
                case "4" :
                    returnValue = R.drawable.h4;
                    break;
                case "5" :
                    returnValue = R.drawable.h5;
                    break;
                case "6" :
                    returnValue = R.drawable.h6;
                    break;
                case "7" :
                    returnValue = R.drawable.h7;
                    break;
                case "8" :
                    returnValue = R.drawable.h8;
                    break;
                case "9" :
                    returnValue = R.drawable.h9;
                    break;
                case "10" :
                    returnValue = R.drawable.h10;
                    break;
                case "J" :
                    returnValue = R.drawable.hj;
                    break;
                case "Q" :
                    returnValue = R.drawable.hq;
                    break;
                case "K" :
                    returnValue = R.drawable.hk;
                    break;
                default:
                    returnValue = R.drawable.back;
                    break;
            }
        }
        else if(suitIn.equals("C"))
        {
            switch(valueIn) {
                case "A" :
                    returnValue = R.drawable.ac;
                    break;
                case "2" :
                    returnValue = R.drawable.c2;
                    break;
                case "3" :
                    returnValue = R.drawable.c3;
                    break;
                case "4" :
                    returnValue = R.drawable.c4;
                    break;
                case "5" :
                    returnValue = R.drawable.c5;
                    break;
                case "6" :
                    returnValue = R.drawable.c6;
                    break;
                case "7" :
                    returnValue = R.drawable.c7;
                    break;
                case "8" :
                    returnValue = R.drawable.c8;
                    break;
                case "9" :
                    returnValue = R.drawable.c9;
                    break;
                case "10" :
                    returnValue = R.drawable.c10;
                    break;
                case "J" :
                    returnValue = R.drawable.cj;
                    break;
                case "Q" :
                    returnValue = R.drawable.cq;
                    break;
                case "K" :
                    returnValue = R.drawable.ck;
                    break;
                default:
                    returnValue = R.drawable.back;
                    break;
            }
        }

        else if(suitIn.equals("D"))
        {
            switch(valueIn) {
                case "A" :
                    returnValue = R.drawable.ad;
                    break;
                case "2" :
                    returnValue = R.drawable.d2;
                    break;
                case "3" :
                    returnValue = R.drawable.d3;
                    break;
                case "4" :
                    returnValue = R.drawable.d4;
                    break;
                case "5" :
                    returnValue = R.drawable.d5;
                    break;
                case "6" :
                    returnValue = R.drawable.d6;
                    break;
                case "7" :
                    returnValue = R.drawable.d7;
                    break;
                case "8" :
                    returnValue = R.drawable.d8;
                    break;
                case "9" :
                    returnValue = R.drawable.d9;
                    break;
                case "10" :
                    returnValue = R.drawable.d10;
                    break;
                case "J" :
                    returnValue = R.drawable.dj;
                    break;
                case "Q" :
                    returnValue = R.drawable.dq;
                    break;
                case "K" :
                    returnValue = R.drawable.dk;
                    break;
                default:
                    returnValue = R.drawable.back;
                    break;
            }
        }
        else
        {
            Log.d(TAG, "Error with card drawn.");
        }

        return returnValue;
    }
}
