package ca.unb.mobiledev.aceit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class CatchTheDealerScreen extends Fragment {
    private String TAG ="CATCH_THE_DEALER_SCREEN";
    private CatchTheDealer game;
    private String id;
    private TextView eventText;
    private String user_id = "";
    private ImageView draw;
    private ImageView ace;
    private ImageView two;
    private ImageView three;
    private ImageView four;
    private ImageView five;
    private ImageView six;
    private ImageView seven;
    private ImageView eight;
    private ImageView nine;
    private ImageView ten;
    private ImageView jack;
    private ImageView queen;
    private ImageView king;
    private Button higher;
    private Button lower;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.catch_the_dealer, container, false);
        return view;
    }
    public void setGame(CatchTheDealer game){
            this.game = game;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CatchTheDealerScreenArgs args = CatchTheDealerScreenArgs.fromBundle(getArguments());
        id = args.getId();
        // Set user id
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        user_id = settings.getString("uid", "id");
        //-------------Set up DB Listener
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, dataSnapshot.getValue().toString());
                Log.d(TAG, "LISTENED");


                CatchTheDealer game =  dataSnapshot.getValue(CatchTheDealer.class);
                //game.setStatus(GameStatus.STARTED);
                setGame(game);
                handleGameStateUpdate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        myRef.child(id).addValueEventListener(gameListener);
        //-------------End set up DB Listener

        //---------------- Card Click Handlers
        eventText = view.findViewById(R.id.eventText);
        higher = view.findViewById(R.id.higher_button);
        lower = view.findViewById(R.id.lower_button);
        higher.setVisibility(View.GONE);
        lower.setVisibility(View.GONE);
        draw =  view.findViewById(R.id.drawCard);
        draw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleDraw();
            }
        });
        ace =  view.findViewById(R.id.cardImgAD);
        ace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('A');
            }
        });
        two =  view.findViewById(R.id.cardImg2D);
        two.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('2');
            }
        });
        three =  view.findViewById(R.id.cardImg3D);
        three.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('3');
            }
        });
        four =  view.findViewById(R.id.cardImg4D);
        four.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('4');
            }
        });
        five =  view.findViewById(R.id.cardImg5D);
        five.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('5');
            }
        });
        six = view.findViewById(R.id.cardImg6D);
        six.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('6');
            }
        });
        seven = view.findViewById(R.id.cardImg7D);
        seven.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('7');
            }
        });
        eight =  view.findViewById(R.id.cardImg8D);
        eight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('8');
            }
        });
        nine =  view.findViewById(R.id.cardImg9D);
        nine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('9');
            }
        });
        ten =  view.findViewById(R.id.cardImg10D);
        ten.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('1');
            }
        });
        jack =  view.findViewById(R.id.cardImgJD);
        jack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('J');
            }
        });
        queen =  view.findViewById(R.id.cardImgQD);
        queen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('Q');
            }
        });
        king =  view.findViewById(R.id.cardImgKD);
        king.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick('K');
            }
        });

        //---------------- End Card Click Handlers


    }

    private void updateDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Log.d(TAG,"UPDATED DB:\n"+ this.game.toString());
        myRef.child(id).setValue(this.game);
    }

    private void handleDraw(){
        String card = this.game.getDeck().drawCard();
        this.game.setCardDrawn(card);
        this.game.setState(CatchTheDealerState.GUESS1);
        eventText.setText("Await the guesses");
        setDrawClickable(false);
        updateDB();
    }

    private void handleCardClick(char card){
        boolean clickable = true;
        // Cant select if all four of suit has been drawn
        switch(card){
            case 'A': if(this.game.getAceCount()>=4)clickable=false;break;
            case '2': if(this.game.getTwoCount()>=4)clickable=false;break;
            case '3': if(this.game.getThreeCount()>=4)clickable=false;break;
            case '4': if(this.game.getFourCount()>=4)clickable=false;break;
            case '5': if(this.game.getFiveCount()>=4)clickable=false;break;
            case '6': if(this.game.getSixCount()>=4)clickable=false;break;
            case '7': if(this.game.getSevenCount()>=4)clickable=false;break;
            case '8': if(this.game.getEightCount()>=4)clickable=false;break;
            case '9': if(this.game.getNineCount()>=4)clickable=false;break;
            case '1': if(this.game.getTenCount()>=4)clickable=false;break;
            case 'J': if(this.game.getJackCount()>=4)clickable=false;break;
            case 'Q': if(this.game.getQueenCount()>=4)clickable=false;break;
            case 'K': if(this.game.getKingCount()>=4)clickable=false;break;
        }
        if(clickable) {
            if (this.game.getState() == CatchTheDealerState.GUESS1) {
                Log.d(TAG, "CARD PRESS" + card + " Guess1" + " drawn=" + this.game.getCardDrawn());
                round(this.game.getCardDrawn(), card, 1);
                this.game.setState(CatchTheDealerState.GUESS2);
                updateDB();
            } else if (this.game.getState() == CatchTheDealerState.GUESS2) {
                Log.d(TAG, "CARD PRESS" + card + " Guess2");
                round(this.game.getCardDrawn(), card, 2);
                this.game.setState(CatchTheDealerState.DRAW);
                updateDB();
            }
        }

    }

    private void handleGameStateUpdate(){
        ((TextView)getActivity().findViewById(R.id.aceCount)).setText(this.game.getAceCount()+"");
        ((TextView)getActivity().findViewById(R.id.twoCount)).setText(this.game.getTwoCount()+"");
        ((TextView)getActivity().findViewById(R.id.threeCount)).setText(this.game.getThreeCount()+"");
        ((TextView)getActivity().findViewById(R.id.fourCount)).setText(this.game.getFourCount()+"");
        ((TextView)getActivity().findViewById(R.id.fiveCount)).setText(this.game.getFiveCount()+"");
        ((TextView)getActivity().findViewById(R.id.sixCount)).setText(this.game.getSixCount()+"");
        ((TextView)getActivity().findViewById(R.id.sevenCount)).setText(this.game.getSevenCount()+"");
        ((TextView)getActivity().findViewById(R.id.eightCount)).setText(this.game.getEightCount()+"");
        ((TextView)getActivity().findViewById(R.id.nineCount)).setText(this.game.getNineCount()+"");
        ((TextView)getActivity().findViewById(R.id.tenCount)).setText(this.game.getTenCount()+"");
        ((TextView)getActivity().findViewById(R.id.jackCount)).setText(this.game.getJackCount()+"");
        ((TextView)getActivity().findViewById(R.id.queenCount)).setText(this.game.getQueenCount()+"");
        ((TextView)getActivity().findViewById(R.id.kingCount)).setText(this.game.getKingCount()+"");
        User turn = this.game.getUsers().get(this.game.getTurn());
        User dealer =this.game.getUsers().get(this.game.getDealer());
        ((TextView)getActivity().findViewById(R.id.dealerText)).setText("Dealer:" + dealer.getName());
        ((TextView)getActivity().findViewById(R.id.turnText)).setText("Turn:" + turn.getName());
        ((TextView)getActivity().findViewById(R.id.streakText)).setText("Streak:" +this.game.getStreak()+"");

        Log.d(TAG, "Game State"+ this.game.getState());
        Log.d(TAG, "Turn ID"+turn.getId() + " user id : " + user_id);
        Log.d(TAG, "Equal"+this.game.getState().equals(CatchTheDealerState.DRAW));
        Log.d(TAG, "Equal"+dealer.getId().equals(user_id));
        Log.d(TAG, "Equal"+this.game.getState().equals(CatchTheDealerState.GUESS1));
        Log.d(TAG, "TurnID"+turn.getId() + " " +user_id);
        Log.d(TAG, "Equal"+turn.getId().equals(user_id));

        // Game over
        if(this.game.getStatus().equals(GameStatus.COMPLETED)){
            NavHostFragment.findNavController(CatchTheDealerScreen.this)
                    .navigate(R.id.action_catch_the_dealer_home);
        }

        // DRAW STATE
        if(this.game.getState().equals(CatchTheDealerState.DRAW)){
            Log.d("DRAWN:", this.game.getDeck().getDrawn()+"");
            // YOUR DEAL
            if(dealer.getId().equals(user_id)){
                //IS GAME OVER?
                if(this.game.getDeck().getDrawn()==52){
                    this.game.setStatus(GameStatus.COMPLETED);
                    updateDB();
                }
                Log.d(TAG, "YOUR DEAL");
                eventText.setText("You are dealer! Draw a card.");
                setDrawClickable(true);
            }
        }
        // YOUR GUESS
        if(this.game.getState().equals(CatchTheDealerState.GUESS1)){
            if(turn.getId().equals(user_id)){
                Log.d(TAG, "YOUR TURN");
                eventText.setText("Your Turn! Guess a card");
                setCardsClickable(true);
            }
        }
        if(!dealer.getId().equals(user_id) && !turn.getId().equals(user_id)){
            eventText.setText("Await your turn!");
        }
    }

    public void setCardsClickable(boolean state){
        ace.setClickable(state);
        two.setClickable(state);
        three.setClickable(state);
        four.setClickable(state);
        five.setClickable(state);
        six.setClickable(state);
        seven.setClickable(state);
        eight.setClickable(state);
        nine.setClickable(state);
        ten.setClickable(state);
        jack.setClickable(state);
        queen.setClickable(state);
        king.setClickable(state);
    }

    public void setDrawClickable(boolean state){
        draw.setClickable(state);
    }

    public void enableHigherLower(){
        higher.setVisibility(View.VISIBLE);
        lower.setVisibility(View.VISIBLE);
    }

    public void round(String card, char guess, int guessNum){
        User turn = this.game.getUsers().get(this.game.getTurn());
        User dealer =this.game.getUsers().get(this.game.getDealer());
        Log.d(TAG, "Next Round");
        Log.d(TAG, "Drawn Card" + card);
        Log.d(TAG, turn.getName() + " guesses " + guess + "(#" + guessNum + ")");
        int compare = this.game.getDeck().compare(guess, card.charAt(0));
        if(compare>0&& guessNum==1){
            Log.d(TAG, "Card is higher than " + guess);
            eventText.setText("Card is higher! Guess again");
            this.game.setState(CatchTheDealerState.GUESS2);
        }
        else if(compare<0&& guessNum==1) {
            Log.d(TAG, "Card is lower than " + guess);
            eventText.setText("Card is lower! Guess again");
            this.game.setState(CatchTheDealerState.GUESS2);
        }
        else if(compare==0 && guessNum==1) {
            Log.d(TAG, " Got it! 3 to " + dealer.getName());
            //eventText.setText(" Got it! 3 to " + dealer.getName());
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Got it! 3 to " + dealer.getName();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            this.game.incrementCard(card);
            this.game.resetStreak();
            this.game.nextTurn();
            setCardsClickable(false);
        }
        else if(compare==0 && guessNum==2){
            Log.d(TAG, " Got it! 1 to" + dealer.getName());
            //eventText.setText( " Got it! 1 to" + dealer.getName());
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Got it! 1 to " + dealer.getName();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            this.game.incrementCard(card);
            this.game.nextTurn();
            setCardsClickable(false);
        }
        else if(compare!=0 && guessNum ==2){
           this.game.incrementCard(card);
            this.game.nextTurn();
            this.game.incrementStreak();
            Log.d(TAG, "Wrong! Diff is " + this.game.getDeck().difference(card.charAt(0), guess));
            //eventText.setText("Wrong! Diff is " + this.game.getDeck().difference(card.charAt(0), guess));
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Wrong! Difference is " + this.game.getDeck().difference(card.charAt(0), guess);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            setCardsClickable(false);
        }

        if(this.game.getStreak()==3){
            this.game.resetStreak();
            this.game.nextDealer();
        }

        Log.d(TAG, "Game State Update\n" + this.game.toString());
    }

}