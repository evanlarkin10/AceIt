package ca.unb.mobiledev.aceit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTVITY" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //basicReadWrite();

    }

    public void basicReadWrite() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();



        // [END write_message]

        // [START read_message]
        // Read from the database

        ValueEventListener game1Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, dataSnapshot.getValue().toString());


                CatchTheDealer game =  dataSnapshot.getValue(CatchTheDealer.class);

                Log.d(TAG, "Game ID:"+ game.getId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };


        User host = new User("1","Evan");
        myRef.child("game1").addValueEventListener(game1Listener);

        // [END read_message]
        CatchTheDealer catchTheDealer1 = new CatchTheDealer("123", host);

        myRef.child("game1").setValue(catchTheDealer1);

    }



}