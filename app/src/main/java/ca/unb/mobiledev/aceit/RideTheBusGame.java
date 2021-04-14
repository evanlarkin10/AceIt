package ca.unb.mobiledev.aceit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private TextView eventText;
    private int cardPos = 0;
    private int butClick= 0;


    public RideTheBusGame() {
        // Required empty public constructor
    }

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
        eventText = view.findViewById(R.id.event_rtb);


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
        RideTheBusGameArgs args = RideTheBusGameArgs.fromBundle(getArguments());
        id = args.getId();

        // Set user id
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        user_id = settings.getString("uid", "id");
        //-------------Set up DB Listener
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());

                }

                else{
                    try{
                    DataSnapshot dataSnapshot = task.getResult();
                    RideTheBus game =  dataSnapshot.getValue(RideTheBus.class);
                    game.setStatus(GameStatus.STARTED);
                    myRef.child(id).setValue(game);
                }catch(Exception e){
                    Context context = getActivity().getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "ERROR", duration);
                    toast.show();
                    NavHostFragment.findNavController(RideTheBusGame.this)
                            .navigate(R.id.action_rideTheBusGame2_to_HomeScreen);
                }

                }

            }
        });
        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, dataSnapshot.getValue().toString());
                Log.d(TAG, "LISTENED");


                RideTheBus game =  dataSnapshot.getValue(RideTheBus.class);
                Log.d("Ride the bus game ", game.toString());

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

        Log.d(TAG, "TEEST"+ this.game.getTurn() + " " + this.game.getCard1());
        //hand1.setImageResource(checkImageResource(this.game.getCard1()));
        //hand2.setImageResource(checkImageResource(this.game.getCard2()));
        //hand3.setImageResource(checkImageResource(this.game.getCard3()));
        //hand4.setImageResource(checkImageResource(this.game.getCard4()));



        //User turn = this.game.getUsers().get(this.game.getTurn());
        //User dealer =this.game.getUsers().get(this.game.getDealer());
        ///////////////Check if hand
        butClick = 0 ;
        drawBut.setEnabled(false);
        User turn = this.game.getUsers().get(this.game.getTurn());
        if(turn.getId().equals(user_id)) {
            eventText.setText("Your Turn");
            tlBut.setEnabled(true);
            trBut.setEnabled(true);
            blBut.setEnabled(true);
            brBut.setEnabled(true);
            //drawBut.setEnabled(true);


            if (this.game.getState().equals(RideTheBusState.RB)) {
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

            if (this.game.getState().equals(RideTheBusState.HL)) {
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

            if (this.game.getState().equals(RideTheBusState.IO)) {
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

            if (this.game.getState().equals(RideTheBusState.SUIT)) {
                ///////////////if turn show buttons and make clickable
                tlBut.setVisibility(View.VISIBLE);
                trBut.setVisibility(View.VISIBLE);
                brBut.setVisibility(View.VISIBLE);
                blBut.setVisibility(View.VISIBLE);
                drawBut.setVisibility(View.VISIBLE);


                tlBut.setText("Hearts");
                trBut.setText("Clubs");
                brBut.setText("Diamonds");
                blBut.setText("Spades");


                tlBut.setClickable(true);
                trBut.setClickable(true);
                blBut.setClickable(true);
                brBut.setClickable(true);
                //drawBut.setClickable(true);
            }
        }

        else{
            eventText.setText("Wait Your Turn.");
            tlBut.setEnabled(false);
            trBut.setEnabled(false);
            blBut.setEnabled(false);
            brBut.setEnabled(false);
            //drawBut.setEnabled(false);
        }
        if(this.game.getStatus().equals(GameStatus.COMPLETED)){
            try {
                NavHostFragment.findNavController(RideTheBusGame.this)
                        .navigate(R.id.action_rideTheBusGame2_to_HomeScreen);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Multiple navigation attempts handled.");
            }

        }


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
        return 4;
    }

    private int bottomLeftButton(){
        drawBut.setClickable(true);
        drawBut.setEnabled(true);
        return 3;
    }

    private void handleDraw(){
        String card = this.game.getDeck().drawCard();
        String suit = String.valueOf(card.charAt(1));

        this.game.setCardDrawn(card);
        int out = checkImageResource(card);

        Log.d("TAG", "CALLING SET CARD" + card);
        if(cardPos == 0){
            this.game.setCard1(card);
            cardPos+=1;
            if(this.game.getTurn()==this.game.getUsers().size()) {
                this.game.setState(RideTheBusState.HL);
            }
            hand1.setImageResource(out);

            if(suit.equals("H") || suit.equals("D")){
                if(butClick == 1){
                    //true
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();

                }
                else{
                    //false
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            if(suit.equals("S") || suit.equals("C")){
                if(butClick == 2){
                    //true
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();


                }

                else{
                    //false
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            //hand1.setImageResource(R.drawable.);
        }

        else if(cardPos == 1){
            cardPos+=1;
            this.game.setCard2(card);
            if(this.game.getTurn()==this.game.getUsers().size()) {
                this.game.setState(RideTheBusState.IO);
            }

            if(this.game.getDeck().compare(this.game.getCard1().charAt(0), this.game.getCard2().charAt(0)) == 1){
                if(butClick == 1){
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }

                if(butClick == 2){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            if(this.game.getDeck().compare(this.game.getCard1().charAt(0), this.game.getCard2().charAt(0)) == -1){
                if(butClick == 2){
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }

                if(butClick == 1){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            if(this.game.getDeck().compare(this.game.getCard1().charAt(0), this.game.getCard2().charAt(0)) == 0){

                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();



            }
            hand2.setImageResource(out);
        }

        else if(cardPos == 2){
             this.game.setCard3(card);
            cardPos+=1;

            char c1 = this.game.getCard1().charAt(0);
            char c2 = this.game.getCard2().charAt(0);
            char c3 = this.game.getCard3().charAt(0);
            //B1 FR 3 TRUE THEN{
            //3 > 1 AND 3 < 2
            //}

            if(butClick == 1){
                if(this.game.getDeck().compare(c1, c3) == 1 && this.game.getDeck().compare(c3, c2) == 1){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            //B2 FR 3 TRUE THEN{
            //3<1 AND 3>2
            //}

            if(butClick == 2){
                if(this.game.getDeck().compare(c3, c1) == 1 && this.game.getDeck().compare(c2, c3) == 1){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }




            if(this.game.getTurn()==this.game.getUsers().size()) {
                this.game.setState(RideTheBusState.SUIT);
            }
            hand3.setImageResource(out);
        }

        else if(cardPos == 3){
            this.game.setCard4(card);
            cardPos+=1;
            String c4 = String.valueOf(this.game.getCard4().charAt(1));
            if(c4.equals("H")){
                if(butClick == 1){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();

                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            if(c4.equals("D")){
                if(butClick == 3){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

            if(c4.equals("S")){
                if(butClick == 4){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

            if(c4.equals("C")){
                if(butClick == 2){
                    Toast toast = Toast.makeText(getContext(), "Correct Give out a drink", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getContext(), "Incorrect take a drink", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
            //this.game.setState(RideTheBusState.SUIT);
            hand4.setImageResource(out);
            if(this.game.getTurn()==this.game.getUsers().size()) {
                this.game.setStatus(GameStatus.COMPLETED);
            }
            updateDB();
        }
        int cardID = getResources().getIdentifier( card + ".png", "drawable", "assets");
        Log.d(TAG, "handleDraw: " + cardID);
        Log.d(TAG, "handleDraw: " + cardPos);
        Log.d(TAG, "handleDraw: " + this.game.getState());

        //eventText.setText("Await the guesses");
        //setDrawClickable(false);
        this.game.nextTurn();
        updateDB();
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
            Log.d(TAG, "ITS A 10:" + s);
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


}