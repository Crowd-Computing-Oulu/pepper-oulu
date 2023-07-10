package fi.oulu.danielszabo.pepper.log;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import fi.oulu.danielszabo.pepper.R;

public class LogFragment extends Fragment {

    private TextView logTextView;
    private Switch debugSwitch;
    private Switch infoSwitch;
    private Switch warningSwitch;
    private Switch errorSwitch;
    private boolean showDebugLogs = true;
    private boolean showInfoLogs = true;
    private boolean showWarningLogs = true;
    private boolean showErrorLogs = true;
    private List<String> logEntries;

    private OnFragmentInteractionListener mListener;

    public LogFragment() {
        logEntries = new ArrayList<>();
    }

    public static LogFragment newInstance(String param1, String param2) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        // Add a log listener to update the logTextView
        LOG.addListener("logView", logEntry -> {
            logEntries.add(logEntry.toString());
            if (shouldDisplayLog(extractLogLevelFromString(logEntry.toString()))) {
                getActivity().runOnUiThread(() -> logTextView.append(logEntry.toString() + "\n"));
            }
        });

        // Find the clear button and set its OnClickListener
        Button clearButton = view.findViewById(R.id.btn_clear);
        clearButton.setOnClickListener(v -> logTextView.setText(""));

        // Find the debug switcher and set its OnCheckedChangeListener
        debugSwitch = view.findViewById(R.id.switch_debug);
        debugSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showDebugLogs = isChecked;
            refreshLogTextView();
        });

        // Find the info switcher and set its OnCheckedChangeListener
        infoSwitch = view.findViewById(R.id.switch_info2);
        infoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showInfoLogs = isChecked;
            refreshLogTextView();
        });

        // Find the warning switcher and set its OnCheckedChangeListener
        warningSwitch = view.findViewById(R.id.switch_warning2);
        warningSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showWarningLogs = isChecked;
            refreshLogTextView();
        });

        // Find the error switcher and set its OnCheckedChangeListener
        errorSwitch = view.findViewById(R.id.switch_error);
        errorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showErrorLogs = isChecked;
            refreshLogTextView();
        });

        return view;
    }

    // Method to refresh the logTextView
    private void refreshLogTextView() {
        logTextView.setText("");
        for (String logEntry : logEntries) {
            LogEntry.LogLevel logLevel = extractLogLevelFromString(logEntry);
            if (shouldDisplayLog(logLevel)) {
                logTextView.append(logEntry + "\n");
            }
        }
    }

    private LogEntry.LogLevel extractLogLevelFromString(String logEntry) {
        if (logEntry.contains("INFO")) {
            return LogEntry.LogLevel.INFO;
        } else if (logEntry.contains("DEBUG")) {
            return LogEntry.LogLevel.DEBUG;
        } else if (logEntry.contains("WARNING")) {
            return LogEntry.LogLevel.WARNING;
        } else if (logEntry.contains("ERROR")) {
            return LogEntry.LogLevel.ERROR;
        }
        return LogEntry.LogLevel.DEBUG;
    }

    private boolean shouldDisplayLog(LogEntry.LogLevel logLevel) {
        return (showDebugLogs && logLevel == LogEntry.LogLevel.DEBUG) ||
                (showInfoLogs && logLevel == LogEntry.LogLevel.INFO) ||
                (showWarningLogs && logLevel == LogEntry.LogLevel.WARNING) ||
                (showErrorLogs && logLevel == LogEntry.LogLevel.ERROR);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
