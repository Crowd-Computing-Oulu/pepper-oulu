package fi.oulu.danielszabo.pepper.screens.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.tools.MimicTts;
import fi.oulu.danielszabo.pepper.tools.SimpleController;


public class ControlFragment extends Fragment {

    private final ControlFragment thisControlFragment = this;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;


    private Button turnLeftBtn, turnRightBtn, turnAroundBtn, stepForwardBtn, sayBtn;
    private EditText sayField;
    private Switch switchTts;

    public ControlFragment() {
        // Required empty public constructor
    }

    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        turnLeftBtn = view.findViewById(R.id.btn_left);
        turnLeftBtn.setOnClickListener(this::onTurnLeftButtonPressed);
        turnRightBtn = view.findViewById(R.id.btn_right);
        turnRightBtn.setOnClickListener(this::onTurnRightButtonPressed);
        turnAroundBtn = view.findViewById(R.id.btn_turnaround);
        turnAroundBtn.setOnClickListener(this::onTurnAroundButtonPressed);
        stepForwardBtn = view.findViewById(R.id.btn_forward);
        stepForwardBtn.setOnClickListener(this::onForwardButtonPressed);
        sayBtn = view.findViewById(R.id.btn_say);
        sayBtn.setOnClickListener(this::onSayButtonPressed);

        this.sayField = view.findViewById(R.id.txtfield_say);

        // Mimic3 engine On and Off switch
        switchTts = view.findViewById(R.id.switch_tts);
        switchTts.setChecked(sharedPreferences.getBoolean("switch_state", true));
        switchTts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.d("ControlFragment", "Mimic3 Engine is ON");
                MimicTts.setServerAvailable(null);
            } else {
                Log.d("ControlFragment", "Mimic3 Engine is OFF");
                MimicTts.setServerAvailable(false);
            }

            // Save the switch state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("switch_state", isChecked);
            editor.apply();
        });

        return view;
    }


    public void onForwardButtonPressed(View view) {
        SimpleController.moveForward();
    }

    public void onTurnLeftButtonPressed(View view) {
        SimpleController.turnLeft();
    }

    public void onTurnRightButtonPressed(View view) {
        SimpleController.turnRight();
    }

    public void onTurnAroundButtonPressed(View view) {
        SimpleController.turnAround();
    }

    public void onSayButtonPressed(View view) {
        SimpleController.say(sayField.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
