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

        // GAME SETUP
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String id =  java.util.UUID.randomUUID().toString().split("-")[0];
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        String userName = settings.getString("name", "username");
        String uid = settings.getString("uid", "id");
        Log.d(TAG, "host id:" + uid + ", game id:" + id);
        User host = new User(uid,userName);
        CatchTheDealer catchTheDealer = new CatchTheDealer(id, host);
        myRef.child(id).setValue(catchTheDealer);

        // UI ELEMENTS
        MyAdapter myAdapter = new MyAdapter(catchTheDealer.getUsers());
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

                CatchTheDealer game =  dataSnapshot.getValue(CatchTheDealer.class);
                ArrayList<User> users = game.getUsers();
                setLobbyState(users.size(), game);
                MyAdapter myAdapter = new MyAdapter(users);
                playerListView.setAdapter(myAdapter);
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
                CatchTheDealerLobbyDirections.ActionStartToCatchTheDealer action = CatchTheDealerLobbyDirections.actionStartToCatchTheDealer(id);

                //action.setId("1234");

                NavHostFragment.findNavController(CatchTheDealerLobby.this)
                        .navigate(action);
            }
        });
    }

    private void setLobbyState(int count, CatchTheDealer game){
        if(game.getStatus().equals(GameStatus.WAITING)){
            this.countText.setText(count+"/10");
            if(count>2){
                this.countText.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                this.startBtn.setClickable(true);
            }
            else{
                this.countText.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                this.startBtn.setClickable(false);
            }
        }
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