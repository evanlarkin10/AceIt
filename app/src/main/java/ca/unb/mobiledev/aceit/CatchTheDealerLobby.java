package ca.unb.mobiledev.aceit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CatchTheDealerLobby extends Fragment {
    private String TAG ="CATCH_THE_DEALER_LOBBY";

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // TODO SET ID AND NAME
        User host = new User("1","Evan");
        CatchTheDealer catchTheDealer = new CatchTheDealer("123", host);
        myRef.child("game1").setValue(catchTheDealer);

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
                NavHostFragment.findNavController(CatchTheDealerLobby.this)
                        .navigate(R.id.action_start_to_catch_the_dealer);
            }
        });
    }
}