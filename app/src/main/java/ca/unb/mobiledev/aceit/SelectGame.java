package ca.unb.mobiledev.aceit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectGame extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.select_game, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.rules_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(R.id.action_select_to_home);
            }
        });

        view.findViewById(R.id.button_select_horserace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GAME SETUP
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                String id =  java.util.UUID.randomUUID().toString().split("-")[0];
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
                String userName = settings.getString("name", "username");
                String uid = settings.getString("uid", "id");
                User host = new User(uid,userName);
                HorseRace race = new HorseRace(id, host);
                Log.d("CATCH", "Game created id:" + id);
                myRef.child(id).setValue(race);
                Log.d("CATCH", "Game created id:" + id);
                SelectGameDirections.ActionSelectToCatchTheDealerLobby action = SelectGameDirections.actionSelectToCatchTheDealerLobby(id);
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(action);
            }
        });

        view.findViewById(R.id.button_select_catchthedealer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GAME SETUP
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                String id =  java.util.UUID.randomUUID().toString().split("-")[0];
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
                String userName = settings.getString("name", "username");
                String uid = settings.getString("uid", "id");
                User host = new User(uid,userName);
                CatchTheDealer catchTheDealer = new CatchTheDealer(id, host);
                Log.d("CATCH", "Game created id:" + id);
                myRef.child(id).setValue(catchTheDealer);
                SelectGameDirections.ActionSelectToCatchTheDealerLobby action = SelectGameDirections.actionSelectToCatchTheDealerLobby(id);
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(action);
            }
        });

        view.findViewById(R.id.button_select_crossthebridge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                String id =  java.util.UUID.randomUUID().toString().split("-")[0];
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
                String userName = settings.getString("name", "username");
                String uid = settings.getString("uid", "id");
                User host = new User(uid,userName);
                //CatchTheDealer catchTheDealer = new CatchTheDealer(id, host);
                RideTheBus rideTheBus = new RideTheBus(id, host);
                Log.d("CATCH", "Game created id:" + id);
                //myRef.child(id).setValue(catchTheDealer);
                myRef.child(id).setValue(rideTheBus);

               // SelectGameDirections.action_SelectGame_to_rideTheBusLobby action = SelectGameDirections.action_SelectGame_to_rideTheBusLobby(id);
                //SelectGameDirections.action_SelectGame_to_rideTheBusLobby action
                //SelectGameDirections.action_SelectGame_to_rideTheBusLobby action = SelectGameDirections.actionSelectToCatchTheDealerLobby()
                //SelectGameDirections.action_SelectGame_to_rideTheBusScreen action = SelectGameDirections.action_SelectGame_to_rideTheBusScreen(id);
                SelectGameDirections.ActionSelectGameToRideTheBusScreen action = SelectGameDirections.actionSelectGameToRideTheBusScreen(id);
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(action);
            }
        });
    }
}