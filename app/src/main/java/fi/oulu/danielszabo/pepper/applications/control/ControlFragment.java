package fi.oulu.danielszabo.pepper.applications.control;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.tools.SimpleController;


public class ControlFragment extends Fragment {

    private final ControlFragment thisControlFragment = this;

    private OnFragmentInteractionListener mListener;

    private Button turnLeftBtn, turnRightBtn, turnAroundBtn, stepForwardBtn, sayBtn;
    private EditText sayField;

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
