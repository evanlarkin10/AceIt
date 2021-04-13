package ca.unb.mobiledev.aceit;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class HorseRaceLobby extends Fragment {

    private String TAG = "HORSE_RACE_LOBBY";
    private Button startButton;
    private TextView gameCodeText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.lobby, container, false); //lobby has to be within my xml file
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Setup mimicking evans database setup


        String id = java.util.UUID.randomUUID().toString().split("_")[0];
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        String username = settings.getString("name", "username");
        String uid = settings.getString("uid", "id");
        Log.d(TAG, "host id:" + uid + ", game id:" + id);
        User host = new User(uid, username);
        HorseRace  horseRace = new HorseRace();






    }
}
