package fi.oulu.danielszabo.pepper.applications.itee_promotion_offline;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Stack;

import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.tools.SimpleController;
import fi.oulu.danielszabo.pepper.tools.SpeechInput;
import fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.offline_service.OfflinePepperService;
import fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.offline_service.ResponseWithOptions;
import fi.oulu.danielszabo.pepper.log.LOG;

public class ITEEPromotionFragment extends Fragment {

    private static final String CONV_ID = "c1";
    private static final OfflinePepperService CONV_SERVICE = new OfflinePepperService();

    private final ITEEPromotionFragment thisITEEPromotionFragment = this;

    private Button[] largeButtons = new Button[5];
    private TextView[] optionNumbers = new TextView[5];
    private Button[] smallButtons = new Button[1];
    private TextView instructionText, captionText;
    private String[] displayedContextualOptions;
    private String[][] contextualOptionPhraseSets;
    private static final String[] hiddenOptions;
    private static final String[][] hiddenOptionPhraseSets;
    private Stack<ResponseWithOptions> responseStack = new Stack<>();
    private ResponseWithOptions currentResponse;

    private Button buttonSkip;

    //    initialise hidden, global options and their phrase sets
    static {
        hiddenOptions = new String[]{
                "GO_BACK",
                "START_OVER",
                "TURN_LEFT",
                "TURN_RIGHT",
                "TURN_AROUND",
                "STEP_FORWARD",
                "REPEAT",
        };

        hiddenOptionPhraseSets = new String[][] {
                new String[] {
                        "Go back", "Back", "Please go back", "Back Please"
                },
                new String[] {
                        "Start Over", "Start Again", "Again", "Restart"
                },
                new String[] {
                        "Turn Left"
                },
                new String[] {
                        "Turn Right"
                },
                new String[] {
                        "Turn Around", "spin"
                },
                new String[] {
                        "Step Forward", "forward", "move forward"
                },
                new String[] {
                        "Repeat", "Please repeat that", "Say again", "Say that again", "Please say again", "Go again", "Please go again"
                }
        };

        assert hiddenOptions.length == hiddenOptionPhraseSets.length;
    }

    public static ITEEPromotionFragment newInstance(String param1, String param2) {
        ITEEPromotionFragment fragment = new ITEEPromotionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itee_promotion_offline, container, false);

        instructionText = view.findViewById(R.id.txt_instruction);
        captionText = view.findViewById(R.id.txt_caption);
        buttonSkip = view.findViewById(R.id.button_skip);

        largeButtons[0] = view.findViewById(R.id.button_lg_1);
        largeButtons[1] = view.findViewById(R.id.button_lg_2);
        largeButtons[2] = view.findViewById(R.id.button_lg_3);
        largeButtons[3] = view.findViewById(R.id.button_lg_4);
        largeButtons[4] = view.findViewById(R.id.button_lg_5);

        optionNumbers[0] = view.findViewById(R.id.txt_option_num1);
        optionNumbers[1] = view.findViewById(R.id.txt_option_num2);
        optionNumbers[2] = view.findViewById(R.id.txt_option_num3);
        optionNumbers[3] = view.findViewById(R.id.txt_option_num4);
        optionNumbers[4] = view.findViewById(R.id.txt_option_num5);

        for (int i = 0; i < largeButtons.length; i++) {
            largeButtons[i].setOnClickListener(v -> onOptionSelected(v));
        }

        smallButtons[0] = view.findViewById(R.id.button_start);
        smallButtons[0].setOnClickListener(v -> onStartButtonPressed(v));

        initConv();

        buttonSkip = view.findViewById(R.id.button_skip);
        buttonSkip.setOnClickListener(v -> onSkipButtonPressed(v));


        return view;
    }

    private void onSkipButtonPressed(View view) {
        // Stop any ongoing speech output
        SimpleController.stopSpeaking();

        updateUIWithoutResponse();
    }

    private void updateUIWithoutResponse() {
        runOnUiThread(() -> {
            // Update UI elements without Pepper speaking a response
            for (int i = 0; i < largeButtons.length; i++) {
                if (displayedContextualOptions.length > i) {
                    largeButtons[i].setText(displayedContextualOptions[i]);
                    largeButtons[i].setVisibility(View.VISIBLE);
                } else {
                    largeButtons[i].setVisibility(View.GONE);
                }
            }

            captionText.setVisibility(View.INVISIBLE);
            setCaptionsVisible(false);
            setOptionsVisible(true);
            buttonSkip.setVisibility(View.INVISIBLE);


            SpeechInput.selectOptionWithPhraseSets(r -> onOptionSelected(r.getHeardPhrase().getText())
                    , displayedContextualOptions, allOptionPhraseSets());
        });

        SpeechInput.selectOptionWithPhraseSets(r -> onOptionSelected(r.getHeardPhrase().getText()), displayedContextualOptions, allOptionPhraseSets());
    }

    private void initConv() {

        responseStack = new Stack<>();
        CONV_SERVICE.startConversation(CONV_ID);
        displayedContextualOptions = CONV_SERVICE.getStartingState(CONV_ID).getOptions();
        contextualOptionPhraseSets = CONV_SERVICE.getStartingState(CONV_ID).getPhraseSets();

        runOnUiThread(() -> {
            for (int i = 0; i < largeButtons.length; i++) {
                if (displayedContextualOptions.length > i) {
                    largeButtons[i].setText(displayedContextualOptions[i]);
                    largeButtons[i].setVisibility(View.VISIBLE);
                } else {
                    largeButtons[i].setVisibility(View.INVISIBLE);
                }
            }
        });


        runOnUiThread(() -> {
            setCaptionsVisible(false);
            setOptionsVisible(true);
            smallButtons[0].setVisibility(View.INVISIBLE);
            buttonSkip.setVisibility(View.INVISIBLE);
        });

        SpeechInput.selectOptionWithPhraseSets(r -> onOptionSelected(r.getHeardPhrase().getText())
                , displayedContextualOptions, allOptionPhraseSets());


    }

    private String[][] allOptionPhraseSets() {
        String[][] result = Arrays.copyOf(hiddenOptionPhraseSets, hiddenOptionPhraseSets.length + contextualOptionPhraseSets.length);
        System.arraycopy(contextualOptionPhraseSets, 0, result, hiddenOptionPhraseSets.length, contextualOptionPhraseSets.length);
        return result;
    }

    private boolean executeHiddenOptionSelected(String input) {
        for (int i = 0; i < hiddenOptionPhraseSets.length; i++) {
            for (int j = 0; j < hiddenOptionPhraseSets[i].length; j++) {
                if(hiddenOptionPhraseSets[i][j].equalsIgnoreCase(input)) {
                    LOG.debug(this, "HIDDEN INPUT: " + hiddenOptions[i]);
                    switch (hiddenOptions[i]) {
                        case "GO_BACK": {
                            if(!responseStack.empty()) {
                                currentResponse = responseStack.pop();
                                handleNewCurrentResponse();
                            }
                            break;
                        }
                        case "START_OVER": {
                            initConv();
                            return true;
                        }
                        case "TURN_LEFT": {
                            SimpleController.turnLeft();
                            break;
                        }
                        case "TURN_RIGHT": {
                            SimpleController.turnRight();
                            break;
                        }
                        case "TURN_AROUND": {
                            SimpleController.turnAround();
                            break;
                        }
                        case "STEP_FORWARD":  {
                            SimpleController.moveForward();
                            break;
                        }
                        case "REPEAT":  {
//                          update captions text, make them visible
                            runOnUiThread(() -> {
                                captionText.setText(currentResponse.getResponseText());
                                setCaptionsVisible(true);
                                setOptionsVisible(false);
                            });

                            SimpleController.say(__ -> {
//                                hide captions again when done talking
                                runOnUiThread(() -> {
                                    captionText.setVisibility(View.INVISIBLE);
                                    setCaptionsVisible(false);
                                    setOptionsVisible(true);
                                    buttonSkip.setVisibility(View.INVISIBLE);
                                });

                                SpeechInput.selectOptionWithPhraseSets(r -> onOptionSelected(r.getHeardPhrase().getText())
                                        , displayedContextualOptions, allOptionPhraseSets());

                            }, currentResponse.getResponseText());
//                            We shouldn't start listening until we finished yapping, it'll have its own return path
                            return true;
                        }
                    }


                    runOnUiThread(() -> {
                        for (int k = 0; k < largeButtons.length; k++) {
                            if(displayedContextualOptions.length > k) {
                                largeButtons[k].setText(displayedContextualOptions[k]);
                                largeButtons[k].setVisibility(View.VISIBLE);
                            } else {
                                largeButtons[k].setVisibility(View.GONE);
                            }
                        }

                        setCaptionsVisible(false);
                        setOptionsVisible(true);
                        buttonSkip.setVisibility(View.INVISIBLE);
                    });


                    SpeechInput.selectOptionWithPhraseSets(r -> onOptionSelected(r.getHeardPhrase().getText())
                            , displayedContextualOptions, allOptionPhraseSets());

                    return true;
                }
            }
        }
        LOG.debug(this, "NO HIDDEN INPUT in " + input);
        return false;
    }

    public void onStartButtonPressed(View view) {
        initConv();

        AsyncTask.execute(() -> {
            SimpleController.say(__ -> {}, getActivity().getString(R.string.hello));
        });
    }

    public void onOptionSelected(View view) {
        SpeechInput.cancelListen();
        onOptionSelected(((Button) view).getText().toString());
    }

    private void onOptionSelected(String input) {

        LOG.debug(this, "Heard Phrase: " + input);

        runOnUiThread(() -> {
            setCaptionsVisible(false);
            setOptionsVisible(false);
            smallButtons[0].setText(R.string.start_over);
        });

        AsyncTask.execute(() -> {
//            Check for special, hidden commands
            if (!executeHiddenOptionSelected(input)) {
                currentResponse = CONV_SERVICE.respondTo(CONV_ID, input);
                responseStack.push(currentResponse);
                handleNewCurrentResponse();
            }
        });

    }

    private void handleNewCurrentResponse() {
        if(currentResponse.getOptions() == null || currentResponse.getOptions().length == 0){
            LOG.debug(this, "Resetting conversation");
            CONV_SERVICE.resetConversation(CONV_ID);
            displayedContextualOptions = CONV_SERVICE.getStartingState(CONV_ID).getOptions();
            contextualOptionPhraseSets = CONV_SERVICE.getStartingState(CONV_ID).getPhraseSets();
        } else {
            displayedContextualOptions = currentResponse.getOptions();
            contextualOptionPhraseSets = currentResponse.getPhraseSets();
        }

        runOnUiThread(() -> {
            captionText.setText(currentResponse.getResponseText());
            setCaptionsVisible(true);
            setOptionsVisible(false);
            buttonSkip.setVisibility(View.VISIBLE);
        });

        SimpleController.say(__ -> {
            runOnUiThread(() -> {
                for (int i = 0; i < largeButtons.length; i++) {
                    if(displayedContextualOptions.length > i) {
                        largeButtons[i].setText(displayedContextualOptions[i]);
                        largeButtons[i].setVisibility(View.VISIBLE);
                        buttonSkip.setVisibility(currentResponse.getOptions() == null || currentResponse.getOptions().length == 0 ? View.INVISIBLE : View.VISIBLE);


                    } else {
                        largeButtons[i].setVisibility(View.GONE);
                        buttonSkip.setVisibility(View.INVISIBLE);
                        buttonSkip.setVisibility(currentResponse.getOptions() == null || currentResponse.getOptions().length == 0 ? View.INVISIBLE : View.VISIBLE);
                    }
                }
            });

            runOnUiThread(() -> {
                captionText.setVisibility(View.INVISIBLE);
                setCaptionsVisible(false);
                setOptionsVisible(true);
                buttonSkip.setVisibility(View.INVISIBLE);

            });

            SpeechInput.selectOptionWithPhraseSets(r -> onOptionSelected(r.getHeardPhrase().getText())
                    , displayedContextualOptions, allOptionPhraseSets());

        }, currentResponse.getResponseText());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LOG.removeListener("logView");
    }

    private void runOnUiThread(Runnable runnable){
        thisITEEPromotionFragment.getActivity().runOnUiThread(runnable);
    }

    private void setOptionsVisible(boolean visible) {
        for (int i = 0; i < smallButtons.length; i++) {
            smallButtons[i].setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        instructionText.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

        if(visible && largeButtons[0].getVisibility() == View.VISIBLE) return;
        for (int i = 0; i < largeButtons.length; i++) {
            largeButtons[i].setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        for (int i = 0; i < optionNumbers.length; i++) {
        }
    }

    private void setCaptionsVisible(boolean visible) {
        captionText.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }



}
