package fi.oulu.danielszabo.pepper.log;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.oulu.danielszabo.pepper.R;

public class LogFragment extends Fragment {

    private StringBuilder logBuilder;

    private final LogFragment thisLogFragment;
    private TextView logTextView;
    private OnFragmentInteractionListener mListener;
    private List<String> logEntries;

    public LogFragment() {
        thisLogFragment = this;
        logEntries = new ArrayList<>();
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
        logEntries = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        logTextView = view.findViewById(R.id.log_output_txt);

        // Retrieve and display the stored log entries
        for (LogEntry logEntry : LOG.logEntryList) {
            logEntries.add(logEntry.toString());
            logTextView.append(logEntry.toString() + "\n");
        }

        // Add a log listener to update the logTextView with new log entries
        LOG.addListener("logView", logEntry -> {
            logEntries.add(logEntry.toString());
            getActivity().runOnUiThread(() -> logTextView.append(logEntry.toString() + "\n"));
        });

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
        logBuilder = null;
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
