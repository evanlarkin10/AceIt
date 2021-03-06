
package ca.unb.mobiledev.aceit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinGame extends Fragment {
    TextView codeInput;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.join_game, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        codeInput=view.findViewById(R.id.join_code_field);
        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(JoinGame.this)
                        .navigate(R.id.action_join_to_home);
            }
        });


        view.findViewById(R.id.join_lobby_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = codeInput.getText().toString();
                Log.d("JOIN", "ID join:"+id);
                // Add yourself to the game users
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            DataSnapshot dataSnapshot = task.getResult();
                            Game game =  dataSnapshot.getValue(CatchTheDealer.class);
                            Log.d("GAMET", ""+game.getGameType());
                            if(game.getGameType()==GameType.HORSE_RACE){
                                game =  dataSnapshot.getValue(HorseRace.class);
                            }
                            else if(game.getGameType()==GameType.CROSS_THE_BRIDGE){
                                game =  dataSnapshot.getValue(RideTheBus.class);
                            }
                            SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
                            String userName = settings.getString("name", "username");
                            String uid = settings.getString("uid", "id");
                            User newUser = new User(uid,userName);
                            if(game.getUsers().size()>=10){
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Lobby Full";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else if(game.getStatus()==GameStatus.STARTED){
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Game has already started";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else if(game.getStatus()==GameStatus.COMPLETED){
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Game has already finished";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else{
                                game.addUser(newUser);
                                if(game.getGameType()==GameType.CROSS_THE_BRIDGE){
                                    Hand hand = new Hand();
                                    game.addHand(hand);
                                }
                                myRef.child(id).setValue(game);
                                JoinGameDirections.ActionJoinToCatchTheDealerLobby action = JoinGameDirections.actionJoinToCatchTheDealerLobby(id);
                                NavHostFragment.findNavController(JoinGame.this)
                                        .navigate(action);
                            }

                        }
                    }
                });

            }
        });
    }
}