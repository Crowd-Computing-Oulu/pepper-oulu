package fi.oulu.danielszabo.pepper_base.log;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fi.oulu.danielszabo.pepper_base.R;

public class LogFragment extends Fragment {

    private final LogFragment thisLogFragment;

    private TextView logTextView;

    private OnFragmentInteractionListener mListener;

    public LogFragment() {
        thisLogFragment = this;
    }

    public static LogFragment newInstance(String param1, String param2) {
        LogFragment fragment = new LogFragment();
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
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        this.logTextView = view.findViewById(R.id.log_output_txt);

        // Add a log listener that will always update the text view within the log fragment on the UI thread
        LOG.addListener("logView", logEntry -> thisLogFragment.getActivity().runOnUiThread(() -> logTextView.setText(
                logTextView.getText() + "\n" + logEntry.toString()
        )) );

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        LOG.removeListener("logView");
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
