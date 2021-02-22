package ca.unb.mobiledev.aceit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(R.id.action_select_to_home);
            }
        });

        view.findViewById(R.id.button_select_catchthedealer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(R.id.action_select_to_catch_the_dealer_lobby);
            }
        });

        view.findViewById(R.id.button_select_crossthebridge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SelectGame.this)
                        .navigate(R.id.action_select_to_home);
            }
        });
    }
}