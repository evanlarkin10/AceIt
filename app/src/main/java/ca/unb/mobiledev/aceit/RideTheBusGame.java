package ca.unb.mobiledev.aceit;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class RideTheBusGame extends Fragment {
    private String TAG = "RIDE_THE_BUS_GAME";
    private String id;
    String drawString;
    String butLeft;
    String butRight;
    private Button tlBut;
    private Button trBut;
    private Button drawBut;
    private String user_id = "";
    private RideTheBus game;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
   // private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public RideTheBusGame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RideTheBusGame.
     */
    // TODO: Rename and change types and number of parameters
  /**
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_the_bus_game, container, false);
    }

    public void setGame(RideTheBus game){
        this.game = game;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tlBut = view.findViewById(R.id.TLbut);
        trBut = view.findViewById(R.id.TRbut);
        drawBut = view.findViewById(R.id.drawBut);
        //tlBut.setText("Left Button");
        RideTheBusScreenArgs args = RideTheBusScreenArgs.fromBundle(getArguments());
        id = args.getId();

        // Set user id
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences("NAME", 0);
        user_id = settings.getString("uid", "id");
        //-------------Set up DB Listener
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, dataSnapshot.getValue().toString());
                Log.d(TAG, "LISTENED");


                RideTheBus game =  dataSnapshot.getValue(RideTheBus.class);
                setGame(game);
                handleGameStateUpdate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }






    };
        myRef.child(id).addValueEventListener(gameListener);
        //-------------End set up DB Listener
}

    private void handleGameStateUpdate() {

    }


    private void updateDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Log.d(TAG,"UPDATED DB:\n"+ this.game.toString());
        myRef.child(id).setValue(this.game);
    }


}