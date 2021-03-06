package ca.unb.mobiledev.aceit;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CatchTheDealerLobby extends Fragment {
    private String TAG ="CATCH_THE_DEALER_LOBBY";
    private Button startBtn;
    private TextView countText;
    private TextView gameCodeText;
    private GameType gameType = GameType.CATCH_THE_DEALER;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lobby, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CatchTheDealerScreenArgs args = CatchTheDealerScreenArgs.fromBundle(getArguments());
        String id = args.getId();

        //DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // UI ELEMENTS
        MyAdapter myAdapter = new MyAdapter(new ArrayList<>());
        RecyclerView playerListView = view.findViewById(R.id.players_list);
        playerListView.setLayoutManager(new LinearLayoutManager(getContext()));
        playerListView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        startBtn = view.findViewById(R.id.start_game_button);
        startBtn.setClickable(false);

        countText = getActivity().findViewById(R.id.playersCountText);
        gameCodeText = getActivity().findViewById(R.id.game_code_text);
        gameCodeText.setText("Game Code: " + id);

        // LOBBY LISTENER

        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Game game =  dataSnapshot.getValue(CatchTheDealer.class);
                Log.d("GAMETYPE", ""+game.getGameType());

                ArrayList<User> users = game.getUsers();
                setLobbyState(users.size(), game);
                MyAdapter myAdapter = new MyAdapter(users);
                playerListView.setAdapter(myAdapter);
                if(game.getStatus()==GameStatus.STARTED){
                    CatchTheDealerLobbyDirections.ActionStartToCatchTheDealer actionCatch = CatchTheDealerLobbyDirections.actionStartToCatchTheDealer(id);
                    CatchTheDealerLobbyDirections.ActionStartToHorserace actionHorse = CatchTheDealerLobbyDirections.actionStartToHorserace(id);
                    CatchTheDealerLobbyDirections.ActionStartToCrossthebridge actionCross = CatchTheDealerLobbyDirections.actionStartToCrossthebridge(id);
                    //action.setId("1234");
                    Log.d(TAG, "GT"+gameType);
                    myRef.child(id).removeEventListener(this);
                    if(gameType==GameType.CATCH_THE_DEALER) {
                        NavHostFragment.findNavController(CatchTheDealerLobby.this)
                                .navigate(actionCatch);
                    }
                    if(gameType==GameType.HORSE_RACE) {
                        NavHostFragment.findNavController(CatchTheDealerLobby.this)
                                .navigate(actionHorse);
                    }
                    if(gameType==GameType.CROSS_THE_BRIDGE) {
                        NavHostFragment.findNavController(CatchTheDealerLobby.this)
                                .navigate(actionCross);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        myRef.child(id).addValueEventListener(gameListener);



        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CatchTheDealerLobby.this)
                        .navigate(R.id.action_start_to_home);
            }
        });
        view.findViewById(R.id.start_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatchTheDealerLobbyDirections.ActionStartToCatchTheDealer actionCatch = CatchTheDealerLobbyDirections.actionStartToCatchTheDealer(id);
                CatchTheDealerLobbyDirections.ActionStartToHorserace actionHorse = CatchTheDealerLobbyDirections.actionStartToHorserace(id);
                CatchTheDealerLobbyDirections.ActionStartToCrossthebridge actionCross = CatchTheDealerLobbyDirections.actionStartToCrossthebridge(id);
                //action.setId("1234");
                Log.d(TAG, "GT"+gameType);
                myRef.child(id).removeEventListener(gameListener);
                if(gameType==GameType.CATCH_THE_DEALER) {
                    NavHostFragment.findNavController(CatchTheDealerLobby.this)
                            .navigate(actionCatch);
                }
                if(gameType==GameType.HORSE_RACE) {
                    NavHostFragment.findNavController(CatchTheDealerLobby.this)
                            .navigate(actionHorse);
                }
                if(gameType==GameType.CROSS_THE_BRIDGE) {
                    NavHostFragment.findNavController(CatchTheDealerLobby.this)
                            .navigate(actionCross);
                }

                // TODO: Repeat conditional for other game types.
            }
        });
    }

    private void setLobbyState(int count, Game game){
        gameType = game.getGameType();
        try {
            if (game.getStatus().equals(GameStatus.WAITING)) {
                this.countText.setText(count + "/10");
                if (count > 2) {
                    this.countText.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                    this.startBtn.setClickable(true);
                } else {
                    this.countText.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                    this.startBtn.setClickable(false);
                }
            }
        }
        catch(Exception e){ }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<User> users;

        public MyAdapter(ArrayList<User> userList) {
            users = userList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.player_item_layout, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final User user = users.get(position);
            holder.mTextView.setText(user.getName());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

}