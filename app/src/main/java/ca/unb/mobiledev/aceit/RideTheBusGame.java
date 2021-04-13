package ca.unb.mobiledev.aceit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class RideTheBusGame extends Fragment {
    private String TAG = "RIDE_THE_BUS_GAME";
    private String id;
    String drawString;
    String butLeft;
    String butRight;
    private Button tlBut;
    private Button trBut;
    private Button brBut;
    private Button blBut;
    private Button drawBut;
    private String user_id = "";
    private RideTheBus game;
    private ImageView hand1;
    private ImageView hand2;
    private ImageView hand3;
    private ImageView hand4;
    private int cardPos = 0;
    private int butClick= 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
   // private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public RideTheBusGame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RideTheBusGame.
     */
    // TODO: Rename and change types and number of parameters
  /**
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_the_bus_game, container, false);
    }

    public void setGame(RideTheBus game){
        this.game = game;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tlBut = view.findViewById(R.id.TLbut);
        trBut = view.findViewById(R.id.TRbut);
        blBut = view.findViewById(R.id.BLbut);
        brBut = view.findViewById(R.id.BRbut);

        blBut.setVisibility(view.INVISIBLE);
        brBut.setVisibility(view.INVISIBLE);
        drawBut = view.findViewById(R.id.drawBut);

        tlBut.setVisibility(View.INVISIBLE);
        trBut.setVisibility(View.INVISIBLE);
        drawBut.setVisibility(View.INVISIBLE);

        tlBut.setClickable(false);
        trBut.setClickable(false);
        blBut.setClickable(false);
        brBut.setClickable(false);
        drawBut.setClickable(false);
        drawBut.setEnabled(false);




        //tlBut.setText("Left Button");
        RideTheBusScreenArgs args = RideTheBusScreenArgs.fromBundle(getArguments());
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


                RideTheBus game =  dataSnapshot.getValue(RideTheBus.class);

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

        hand1 = view.findViewById(R.id.imageView4);
        hand2 = view.findViewById(R.id.imageView7);
        hand3 = view.findViewById(R.id.imageView8);
        hand4 = view.findViewById(R.id.imageView9);

        trBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butClick = rightButtonClick();
            }
        });

        tlBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butClick = leftButtonClick();
            }
        });

        drawBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawButtonClick();
            }
        });

        brBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butClick = bottomRightButton();
            }
        });

        blBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butClick = bottomLeftButton();
            }
        });

        }





        ////////////////Update the image view change deck respectively


    private void handleGameStateUpdate() {

        //User turn = this.game.getUsers().get(this.game.getTurn());
        //User dealer =this.game.getUsers().get(this.game.getDealer());
        ///////////////Check if hand
        butClick = 0 ;
        drawBut.setEnabled(false);


        if(this.game.getState().equals(RideTheBusState.RB)){
            ///////////////if turn show buttons and make clickable
            tlBut.setVisibility(View.VISIBLE);
            trBut.setVisibility(View.VISIBLE);
            drawBut.setVisibility(View.VISIBLE);

            tlBut.setText("RED");
            trBut.setText("BLACK");
            //tlBut.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
            //trBut.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
            tlBut.setClickable(true);
            trBut.setClickable(true);
            //drawBut.setClickable(true);
        }

        if(this.game.getState().equals(RideTheBusState.HL)){
            ///////////////if turn show buttons and make clickable
            tlBut.setVisibility(View.VISIBLE);
            trBut.setVisibility(View.VISIBLE);
            drawBut.setVisibility(View.VISIBLE);

            tlBut.setText("Higher");
            trBut.setText("Lower");

            tlBut.setClickable(true);
            trBut.setClickable(true);
            //drawBut.setClickable(true);
        }

        if(this.game.getState().equals(RideTheBusState.IO)){
            ///////////////if turn show buttons and make clickable
            tlBut.setVisibility(View.VISIBLE);
            trBut.setVisibility(View.VISIBLE);
            drawBut.setVisibility(View.VISIBLE);

            tlBut.setText("In Between");
            trBut.setText("Outside");

            tlBut.setClickable(true);
            trBut.setClickable(true);
            //drawBut.setClickable(true);
        }

        if(this.game.getState().equals(RideTheBusState.SUIT)){
            ///////////////if turn show buttons and make clickable
            tlBut.setVisibility(View.VISIBLE);
            trBut.setVisibility(View.VISIBLE);
            brBut.setVisibility(View.VISIBLE);
            blBut.setVisibility(View.VISIBLE);
            drawBut.setVisibility(View.VISIBLE);


            tlBut.setText("Hearts");
            trBut.setText("Clubs");
            brBut.setText("Diamonds");
            blBut.setText("Spaids");


            tlBut.setClickable(true);
            trBut.setClickable(true);
            blBut.setClickable(true);
            brBut.setClickable(true);
            //drawBut.setClickable(true);
        }
/*
        else{
            tlBut.setVisibility(View.INVISIBLE);
            trBut.setVisibility(View.INVISIBLE);
            blBut.setVisibility(View.INVISIBLE);
            brBut.setVisibility(View.INVISIBLE);
            drawBut.setVisibility(View.INVISIBLE);

            brBut.setClickable(false);
            blBut.setClickable(false);
            drawBut.setClickable(false);
        }

*/







        ///////////////once right or left is chosen let the draw button be clickable

    }


    private void updateDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Log.d(TAG,"UPDATED DB:\n"+ this.game.toString());
        myRef.child(id).setValue(this.game);
    }

    private int rightButtonClick(){
        Log.d(TAG, "right button click");
        drawBut.setClickable(true);
        drawBut.setEnabled(true);

        return 2;
    }

    private int leftButtonClick(){
        drawBut.setClickable(true);
        drawBut.setEnabled(true);
        return 1;

    }

    private void drawButtonClick(){
        Log.d(TAG, "draw button click");
        //this.game.setState(RideTheBusState.SUIT);
        handleDraw();
        handleGameStateUpdate();
        updateDB();


    }

    private int bottomRightButton(){
        drawBut.setClickable(true);
        drawBut.setEnabled(true);
        return 3;
    }

    private int bottomLeftButton(){
        drawBut.setClickable(true);
        drawBut.setEnabled(true);
        return 4;
    }

    private void handleDraw(){
        String card = this.game.getDeck().drawCard();
        this.game.setCardDrawn(card);
        if(cardPos == 0){
            cardPos+=1;
            this.game.setState(RideTheBusState.HL);
            hand1.setImageResource(R.drawable.d2);
            //hand1.setImageResource(R.drawable.);
        }

        else if(cardPos == 1){
            cardPos+=1;
            this.game.setState(RideTheBusState.IO);
            hand2.setImageResource(R.drawable.d2);
        }

        else if(cardPos == 2){
            cardPos+=1;
            this.game.setState(RideTheBusState.SUIT);
            hand3.setImageResource(R.drawable.d2);
        }

        else if(cardPos == 3){
            cardPos+=1;
            //this.game.setState(RideTheBusState.SUIT);
            hand4.setImageResource(R.drawable.d2);
        }
        int cardID = getResources().getIdentifier( card + ".png", "drawable", "assets");
        Log.d(TAG, "handleDraw: " + cardID);
        Log.d(TAG, "handleDraw: " + cardPos);
        Log.d(TAG, "handleDraw: " + this.game.getState());

        //eventText.setText("Await the guesses");
        //setDrawClickable(false);
        updateDB();
    }


}