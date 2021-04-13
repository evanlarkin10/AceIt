package ca.unb.mobiledev.aceit;

import java.util.UUID;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class HomeScreen extends Fragment {
    private String TAG ="HOME";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_screen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        TextView editName = view.findViewById(R.id.edit_name);
        // Get from the SharedPreferences
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        String userName = settings.getString("name", "Enter Name");
        String id = settings.getString("uid", "id");
        SharedPreferences.Editor editor = settings.edit();
        // ID
        if(id.equals("id")) {
            String uid = UUID.randomUUID().toString();
            Log.d(TAG, "SETTING ID" +uid);
            editor.putString("uid", uid);
            editor.apply();
        }
        //Name
        if(userName.equals("Enter Name")) {
            Log.d(TAG, "USERNAME WAS EMPTY");
            editName.setText("Enter Name");
        }
        else{
            //Initialize name text from storage if its been set already
            editName.setText(userName);
        }

        //On unfocus, update the name in the storage
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Log.d(TAG, "CHANGED NAME TO: " + editName.getText());
                    editor.putString("name", editName.getText().toString());
                    // Apply the edits!
                    editor.apply();
                }

            }
        });

        view.findViewById(R.id.start_select_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_home_to_select);
            }
        });


        view.findViewById(R.id.start_select_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_home_to_select);
            }
        });

        view.findViewById(R.id.join_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_home_to_join);
            }
        });

        view.findViewById(R.id.rules_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeScreen.this)
                        .navigate(R.id.action_home_to_rules);
            }
        });
    }
}