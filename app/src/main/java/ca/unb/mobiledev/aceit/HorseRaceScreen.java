package ca.unb.mobiledev.aceit;

import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private int betCount=0;
    private String suit="S";
    private TextView horseTitle;
    private TextView yourBet;
    private TextView betValue;
    private TextView on;
    private TextView betSuit;
    private Button increaseBet;
    private Button decreaseBet;
    private Button confirmBet;
    private Button spadeButton;
    private Button heartButton;
    private Button clubButton;
    private Button diamondButton;
    private Button nextCard;
    private Button confirmButton;
    private ImageView side5;
    private ImageView side4;
    private ImageView side3;
    private ImageView side2;
    private ImageView side1;
    private ImageView deck;
    private String user_name="";
    private String user_id="";

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

        HorseRaceScreenArgs args = HorseRaceScreenArgs.fromBundle(getArguments());
        id = args.getId();

        deck = view.findViewById(R.id.deck);
        side1 = view.findViewById(R.id.side1);
        side2 = view.findViewById(R.id.side2);
        side3 = view.findViewById(R.id.side3);
        side4 = view.findViewById(R.id.side4);
        side5 = view.findViewById(R.id.side5);


        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        user_id = settings.getString("uid", "id");
        user_name = settings.getString("name", "Enter Name");

        //Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Set game to started
         myRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        DataSnapshot dataSnapshot = task.getResult();
                        HorseRace game =  dataSnapshot.getValue(HorseRace.class);
                        game.setStatus(GameStatus.STARTED);
                        myRef.child(id).setValue(game);
                    }
                }
            });



        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Log.d(TAG, snapshot.getValue().toString());
                    Log.d(TAG, "Listened");

                    HorseRace game = snapshot.getValue(HorseRace.class);
                    //game.setStatus(GameStatus.STARTED);
                    setGame(game);
                    handleGameStateUpdate();
                }catch(Exception e){
                    Context context = getActivity().getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "ERROR", duration);
                    toast.show();
                    NavHostFragment.findNavController(HorseRaceScreen.this)
                            .navigate(R.id.action_horserace_to_homescreen);
                }
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
        yourBet.setText("You Bet");
        betValue = view.findViewById(R.id.betValue_textview);
        betValue.setText("0");
        on = view.findViewById(R.id.on_textview);
        betSuit = view.findViewById(R.id.betsuit_textview);
        betSuit.setText("spades");
        nextCard = view.findViewById(R.id.next_card);
        nextCard.setEnabled(false);
        increaseBet = view.findViewById(R.id.higher_bet);
        increaseBet.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View v) {
                betCount++;
                betValue.setText(""+betCount);
        }

    });

        decreaseBet = view.findViewById(R.id.lower_bet);
        decreaseBet.setOnClickListener(new View.OnClickListener()
        {
        @Override
                public void onClick(View v) {
            betCount--;
            betValue.setText(""+betCount);
        }
        });

        spadeButton = view.findViewById(R.id.spade_button);
        spadeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                suit="S";
                betSuit.setText("spades");
            }
        });

        heartButton = view.findViewById(R.id.heart_button);
        heartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                suit="H";
                betSuit.setText("hearts");
            }
        });

        clubButton = view.findViewById(R.id.club_button);
        clubButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                suit="C";
                betSuit.setText("clubs");
            }
        });

        diamondButton = view.findViewById(R.id.diamond_button);
        diamondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suit="D";
                betSuit.setText("diamonds");
            }
        });
        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                game.addBet(new User(user_id,user_name), betCount, suit);
                updateDB();
                Log.d(TAG, "SET CONFIRM DISABLED");
                setBettingUI(false);
            }
        });
        nextCard = view.findViewById(R.id.next_card);
        nextCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                round();
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
        //Log.d("CARDS", )
        //Set Side cards
        side1.setImageResource(checkImageResource(this.game.getSideCard1()));
        side2.setImageResource(checkImageResource(this.game.getSideCard2()));
        side3.setImageResource(checkImageResource(this.game.getSideCard3()));
        side4.setImageResource(checkImageResource(this.game.getSideCard4()));
        side5.setImageResource(checkImageResource(this.game.getSideCard5()));
        deck.setImageResource(checkImageResource(this.game.getTopDeck()));

        Log.d(TAG, "STATUS" + this.game.getStatus());
        //on game over state
        if(this.game.getStatus().equals(GameStatus.COMPLETED)) {
            NavHostFragment.findNavController(HorseRaceScreen.this)
                    .navigate(R.id.action_horserace_to_homescreen);
            Log.d(TAG, "Game over, returning to home screen.");
        }
        //Log.d(TAG, "BETS" + this.game.getBets().size());
        Log.d(TAG, "Bet count"+ this.game.getBets().size()+"");
        if(this.game.getUsers().size()==this.game.getBets().size()){
            this.game.setState(HorseRaceState.RACING);
            updateDB();
        }

        Log.d(TAG, "DRAWOR:"+ game.getUsers().get(0).getId()+" "+ user_id + " " + game.isStarted());
        if(game.isStarted() && game.getUsers().get(0).getId().equals(user_id)) { //If game is still in betting phase enable all betting buttons
            Log.d(TAG, "DRAWOR TRUE:"+ game.getUsers().get(0).getId()+" "+ user_id);
            nextCard.setEnabled(true);
        }

        /*
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
        }*/


        //Depending on how many spades have been drawn, display the ace of spades
        //Temporarily has to be shown as a king because the ace of clubs is misbehaving
        //Do the same for all four suits
        switch(game.getSpadeCount()) {
            case 1:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(R.drawable.as);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(R.drawable.as);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(R.drawable.as);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(R.drawable.as);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.spade_position5)).setImageResource(R.drawable.as);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.spade_mover)).setImageResource(R.drawable.as);
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
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(R.drawable.ac);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(R.drawable.ac);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(R.drawable.ac);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(R.drawable.ac);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.club_position5)).setImageResource(R.drawable.ac);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.club_mover)).setImageResource(R.drawable.ac);
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
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(R.drawable.ad);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 2:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(R.drawable.ad);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 3:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(R.drawable.ad);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 4:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(R.drawable.ad);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
            case 5:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(R.drawable.ad);
                break;
            default:
                ((ImageView)getActivity().findViewById(R.id.diamond_mover)).setImageResource(R.drawable.ad);
                ((ImageView)getActivity().findViewById(R.id.diamond_position1)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position2)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position3)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position4)).setImageResource(android.R.color.transparent);
                ((ImageView)getActivity().findViewById(R.id.diamond_position5)).setImageResource(android.R.color.transparent);
                break;
        }
    }

    private int checkImageResource(String s) {
        if(s.equals("back")){
            return R.drawable.back;
        }
        //This method will take a card value and returns its image path to use to update UI
        String valueIn = String.valueOf(s.charAt(0)); //value is first part of string
        String suitIn = String.valueOf(s.charAt(1)); //value is first part of string
        if(String.valueOf(s.charAt(0)).equals("1")){
            // Its a ten, suit is third char
            suitIn = String.valueOf(s.charAt(2));
        }
        int returnValue = 0;

        if(suitIn.equals("S"))
        {
            switch(valueIn) {
                case "A" :
                    returnValue = R.drawable.as;
                    break;
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
                case "1" :
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
                case "1" :
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
                case "1" :
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
                case "1" :
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
    public void round(){
        String card = game.deck.drawCard();
        Log.d(TAG, "SELECTED CARD" + card);
        game.cardsDrawn.add(card);
        String suit = String.valueOf(card.charAt(1)); //Suit is second char in string
        if(String.valueOf(card.charAt(0)).equals("1")){
            // Its a ten, suit is third char
            suit = String.valueOf(card.charAt(2));
        }

        deck.setImageResource(checkImageResource(card));
        this.game.setTopDeck(card);
        switch(suit) {
            //Spade
            case "S":
                game.incrementSpade();
                int newSpadeCount = game.getSpadeCount();
                if(newSpadeCount>=6){
                    done("Spades");
                }
                if(newSpadeCount>game.getLeftFlipped()){
                    // Flip left
                    flipLeft();
                }
                break;
            case "H" :
                game.incrementHeart();
                int newHeartCount = game.getHeartCount();
                if(newHeartCount>=6){
                    done("Hearts");
                }
                if(newHeartCount>game.getLeftFlipped()){
                    // Flip left
                    flipLeft();
                }
                break;
            case "C" :
                game.incrementClub();
                int newClubCount = game.getClubCount();
                if(newClubCount>=6){
                    done("Clubs");
                }
                if(newClubCount>game.getLeftFlipped()){
                    // Flip left
                    flipLeft();
                }
                break;
            case "D" :
                game.incrementDiamond();
                int newDiamondCount = game.getDiamondCount();
                if(newDiamondCount>=6){
                    done("Diamonds");
                }
                if(newDiamondCount>game.getLeftFlipped()){
                    // Flip left
                    flipLeft();
                }
                break;
            default:
                Log.i(TAG, "Card drawn resulted in error.");
                break;

        }
        updateDB();
    }
    public void flipLeft(){
        Log.d(TAG, "FLIP LEFT");

        String card = game.deck.drawCard();
        game.cardsDrawn.add(card);
        game.incrementLeftFlipped();
        String suit = String.valueOf(card.charAt(1)); //Suit is second char in string
        if(String.valueOf(card.charAt(0)).equals("1")){

            // Its a ten, suit is third char
            suit = String.valueOf(card.charAt(2));
            Log.d(TAG, "ITS A 10:" + suit);
        }
        Log.d(TAG, "MOVE RESULT BACK ONE:" + suit);
        int left = game.getLeftFlipped();
        Log.d(TAG, "LFLIPPED" + left);

        switch(left){
            case 1:
                side1.setImageResource(checkImageResource(card));
                this.game.setSideCard1(card);
                break;
            case 2:
                side2.setImageResource(checkImageResource(card));
                this.game.setSideCard2(card);
                break;
            case 3:
                side3.setImageResource(checkImageResource(card));
                this.game.setSideCard3(card);
                break;
            case 4:
                side4.setImageResource(checkImageResource(card));
                this.game.setSideCard4(card);
                break;
            case 5:
                side5.setImageResource(checkImageResource(card));
                this.game.setSideCard5(card);
                break;
        }
        // Decrement if on left side
        switch(suit) {
            case "S":
                if(game.getSpadeCount()>0)
                    game.decrementSpade();
                break;
            case "H" :
                if(game.getHeartCount()>0)
                    game.decrementHeart();
                break;
            case "C" :
                if(game.getClubCount()>0)
                    game.decrementClub();
                break;
            case "D" :
                if(game.getDiamondCount()>0)
                    game.decrementDiamond();
                break;
            default:
                Log.i(TAG, "Card drawn resulted in error.");
                break;

        }
        updateDB();

    }
    public void done(String suit){
        Context context = getActivity().getApplicationContext();
        CharSequence text = suit + " wins!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        game.setStatus(GameStatus.COMPLETED);
        //updateDB();
    }
    public void setBettingUI(boolean status){
        increaseBet.setEnabled(status);
        decreaseBet.setEnabled(status);
        confirmButton.setEnabled(status);
        spadeButton.setEnabled(status);
        heartButton.setEnabled(status);
        clubButton.setEnabled(status);
        diamondButton.setEnabled(status);
    }
}
