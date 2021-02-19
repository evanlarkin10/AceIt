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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.io.InputStream;

public class CatchTheDealerScreen extends Fragment {
    private String TAG ="CATCH_THE_DEALER_SCREEN";

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


}