package ca.unb.mobiledev.aceit;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.catch_the_dealer, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //-------------Set up DB Listener
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, dataSnapshot.getValue().toString());
                Log.d(TAG, "LISTENED");


                CatchTheDealer game =  dataSnapshot.getValue(CatchTheDealer.class);
                Log.d(TAG, "ACE COUNT LISTENER:" + game.getAceCount() +  " "+ game.getId());
                handleGameStateUpdate(game);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        myRef.child("game1").addValueEventListener(gameListener);
        //-------------End set up DB Listener

        //---------------- Card Click Handlers
        ImageView ace =  view.findViewById(R.id.cardImgAD);
        ace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("A");
            }
        });
        ImageView two =  view.findViewById(R.id.cardImg2D);
        two.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("2");
            }
        });
        ImageView three =  view.findViewById(R.id.cardImg3D);
        three.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("3");
            }
        });
        ImageView four =  view.findViewById(R.id.cardImg4D);
        four.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("4");
            }
        });
        ImageView five =  view.findViewById(R.id.cardImg5D);
        five.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("5");
            }
        });
        ImageView six = view.findViewById(R.id.cardImg6D);
        six.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("6");
            }
        });
        ImageView seven = view.findViewById(R.id.cardImg7D);
        seven.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("7");
            }
        });
        ImageView eight =  view.findViewById(R.id.cardImg8D);
        eight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("8");
            }
        });
        ImageView nine =  view.findViewById(R.id.cardImg9D);
        nine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("9");
            }
        });
        ImageView ten =  view.findViewById(R.id.cardImg10D);
        ten.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("10");
            }
        });
        ImageView jack =  view.findViewById(R.id.cardImgJD);
        jack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("J");
            }
        });
        ImageView queen =  view.findViewById(R.id.cardImgQD);
        queen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("Q");
            }
        });
        ImageView king =  view.findViewById(R.id.cardImgKD);
        king.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleCardClick("K");
            }
        });

        //---------------- End Card Click Handlers




        Log.d(TAG,"VIEW CREATED");
        // Simulate game
        User user1 = new User("1", "Evan");
        User user2 = new User("2", "Dav");
        User user3 = new User("3", "Barb");
        CatchTheDealer game = new CatchTheDealer("1234", user1);
        game.addUser(user2);
        game.addUser(user3);
        game.toString();
        SimCatchTheDealer sim=new SimCatchTheDealer(game);
        sim.run();


    }

    private void handleCardClick(String card){
        Log.d(TAG, "CARD PRESS" + card);
    }

    private void handleGameStateUpdate(CatchTheDealer game){
        Log.d(TAG, "ACE COUNT:" + game.getAceCount() +  " "+ game.getId());
        ((TextView)getActivity().findViewById(R.id.aceCount)).setText(game.getAceCount()+"");
        ((TextView)getActivity().findViewById(R.id.twoCount)).setText(game.getTwoCount()+"");
        ((TextView)getActivity().findViewById(R.id.threeCount)).setText(game.getThreeCount()+"");
        ((TextView)getActivity().findViewById(R.id.fourCount)).setText(game.getFourCount()+"");
        ((TextView)getActivity().findViewById(R.id.fiveCount)).setText(game.getFiveCount()+"");
        ((TextView)getActivity().findViewById(R.id.sixCount)).setText(game.getSixCount()+"");
        ((TextView)getActivity().findViewById(R.id.sevenCount)).setText(game.getSevenCount()+"");
        ((TextView)getActivity().findViewById(R.id.eightCount)).setText(game.getEightCount()+"");
        ((TextView)getActivity().findViewById(R.id.nineCount)).setText(game.getNineCount()+"");
        ((TextView)getActivity().findViewById(R.id.tenCount)).setText(game.getTenCount()+"");
        ((TextView)getActivity().findViewById(R.id.jackCount)).setText(game.getJackCount()+"");
        ((TextView)getActivity().findViewById(R.id.queenCount)).setText(game.getQueenCount()+"");
        ((TextView)getActivity().findViewById(R.id.kingCount)).setText(game.getKingCount()+"");
        ((TextView)getActivity().findViewById(R.id.dealerText)).setText("Dealer:" + game.getDealerUser().getName());
        ((TextView)getActivity().findViewById(R.id.turnText)).setText("Turn:" +game.getTurnUser().getName());
        ((TextView)getActivity().findViewById(R.id.streakText)).setText("Streak:" +game.getStreak()+"");


    }


}