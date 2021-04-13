package ca.unb.mobiledev.aceit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrossTheBridgeRules#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrossTheBridgeRules extends Fragment {


    public CrossTheBridgeRules() {
        // Required empty public constructor
    }

    public static CrossTheBridgeRules newInstance() {
        CrossTheBridgeRules fragment = new CrossTheBridgeRules();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cross_the_bridge_rules, container, false);
    }
}