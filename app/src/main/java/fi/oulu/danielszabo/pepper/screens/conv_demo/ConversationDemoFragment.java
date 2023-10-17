package fi.oulu.danielszabo.pepper.screens.conv_demo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Stack;

import fi.oulu.danielszabo.pepper.PepperApplication;
import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.screens.conv_demo.listeners.ApproachingHumanGreeter;
import fi.oulu.danielszabo.pepper.screens.conv_demo.listeners.TouchResponder;
import fi.oulu.danielszabo.pepper.log.LOG;
import fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service.OfflinePepperService;
import fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service.ResponseWithOptions;
import fi.oulu.danielszabo.pepper.tools.SimpleController;
import fi.oulu.danielszabo.pepper.tools.SpeechInput;

public class ConversationDemoFragment extends Fragment {

//    This application only handles a single dialogue
    private static final String CONV_ID = "c1";

//    Conversation Service
//    private static final GPT35TurboPepperService CONV_SERVICE = new GPT35TurboPepperService(ApiConfig.API_TOKEN);
    private final OfflinePepperService CONV_SERVICE = new OfflinePepperService(this.getActivity());

//    Self-reference for other threads ot be able to call Activity
    private final ConversationDemoFragment thisConversationDemoFragment = this;

//     Always available options and their respective phrase sets
    private static String[] hiddenOptions;
    private static String[][] hiddenOptionPhraseSets;

//     Contextual (based on latest response) available options and their respective phrase sets
    private String[] displayedContextualOptions;
    private String[][] contextualOptionPhraseSets;

//    previous responses in a stack for the "go back" functionality
    private Stack<ResponseWithOptions> responseStack = new Stack<>();

//    UI Element references
    private Button[] largeButtons = new Button[5];
    private Button[] smallButtons = new Button[1];
    private TextView[] optionNumbers = new TextView[5];
    private TextView hmmText, instructionText, captionText;
    private ProgressBar hmmSpinner;
    private Button buttonSkip;

//    latest response from the conversation service
    private ResponseWithOptions currentResponse;

//    Initialisation functions
    private static void initHiddenOptionPhraseSets() {
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

    private static void initHiddenOptions() {
        hiddenOptions = new String[]{
                "GO_BACK",
                "START_OVER",
                "TURN_LEFT",
                "TURN_RIGHT",
                "TURN_AROUND",
                "STEP_FORWARD",
                "REPEAT",
        };
    }

    private static void registerTouchListeners() {
        PepperApplication.qiContext.getTouchAsync().andThenConsume(t -> t.getSensor("Head/Touch").addOnStateChangedListener(new TouchResponder("Head")));
        PepperApplication.qiContext.getTouchAsync().andThenConsume(t -> t.getSensor("LHand/Touch").addOnStateChangedListener(new TouchResponder("Left Hand")));
        PepperApplication.qiContext.getTouchAsync().andThenConsume(t -> t.getSensor("RHand/Touch").addOnStateChangedListener(new TouchResponder("Right Hand")));
    }

    private static void registerHumanAwerenessListeners() {
        // Register logging listeners
//        PepperApplication.qiContext.getHumanAwarenessAsync().andThenConsume(ha -> ha.addOnHumansAroundChangedListener(humans -> LOG.debug("onHumansAroundChanged", humans != null ? humans.toString() : "null")));
//        PepperApplication.qiContext.getHumanAwarenessAsync().andThenConsume(ha -> ha.addOnEngagedHumanChangedListener(engagedHuman -> LOG.debug("onEngagedHumanChanged", engagedHuman != null ? engagedHuman.toString() : "null")));
//        PepperApplication.qiContext.getHumanAwarenessAsync().andThenConsume(ha -> ha.addOnRecommendedHumanToApproachChangedListener(engagedHuman -> LOG.debug("onRecommendedHumanToApproachChanged", engagedHuman != null ? engagedHuman.toString() : "null")));
//        PepperApplication.qiContext.getHumanAwarenessAsync().andThenConsume(ha -> ha.addOnRecommendedHumanToEngageChangedListener(engagedHuman -> LOG.debug("onRecommendedHumanToEngageChanged", engagedHuman != null ? engagedHuman.toString() : "null")));

        // Register behavioural listeners
        PepperApplication.qiContext.getHumanAwarenessAsync().andThenConsume(ha -> ha.addOnHumansAroundChangedListener(new ApproachingHumanGreeter()));
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
            setThinkingVisible(false);
            smallButtons[0].setVisibility(View.INVISIBLE);
        });

        SpeechInput.selectOptionWithPhraseSets(r -> handleSelectedOption(r.getHeardPhrase().getText())
                , displayedContextualOptions, allOptionPhraseSets());


    }

//    Fragment lifecycle methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conv_demo, container, false);

        hmmText = view.findViewById(R.id.txt_hmm);
        instructionText = view.findViewById(R.id.txt_instruction);
        captionText = view.findViewById(R.id.txt_caption);
        hmmSpinner = view.findViewById(R.id.spinner_hmm);

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
            largeButtons[i].setOnClickListener(v -> onOptionButtonPressed(v));
        }

        smallButtons[0] = view.findViewById(R.id.button_start);
        smallButtons[0].setOnClickListener(v -> onStartButtonPressed(v));

        //    Initialisation
        {
            initHiddenOptions();
            initHiddenOptionPhraseSets();
            registerHumanAwerenessListeners();
            registerTouchListeners();
            initConv();
        }

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

            SpeechInput.selectOptionWithPhraseSets(r -> handleSelectedOption(r.getHeardPhrase().getText())
                    , displayedContextualOptions, allOptionPhraseSets());
        });

        SpeechInput.selectOptionWithPhraseSets(r -> handleSelectedOption(r.getHeardPhrase().getText()), displayedContextualOptions, allOptionPhraseSets());
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

//    Button press event listeners

    private void onStartButtonPressed(View view) {
        initConv();

        AsyncTask.execute(() -> {
            SimpleController.say(__ -> {}, "Hello!");
        });
    }

    private void onOptionButtonPressed(View view) {
        SpeechInput.cancelListen();
        handleSelectedOption(((Button) view).getText().toString());
    }

//    Conversation logic methods

    /**
     *
     *  This method checks the input (regardless of where it comes from) for any matches against hidden options
     * and then executes them.
     *
     * @return whether a hidden input was matched (and executed)
     *
     */
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
                                setThinkingVisible(false);
                                buttonSkip.setVisibility(View.VISIBLE);
                            });

                            SimpleController.say(__ -> {
//                                hide captions again when done talking
                                 runOnUiThread(() -> {
                                    captionText.setVisibility(View.INVISIBLE);
                                    setCaptionsVisible(false);
                                    setOptionsVisible(true);
                                    setThinkingVisible(false);
                                     buttonSkip.setVisibility(View.INVISIBLE);
                                });

                                SpeechInput.selectOptionWithPhraseSets(r -> handleSelectedOption(r.getHeardPhrase().getText())
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
                        setThinkingVisible(false);

                    });


                    SpeechInput.selectOptionWithPhraseSets(r -> handleSelectedOption(r.getHeardPhrase().getText())
                            , displayedContextualOptions, allOptionPhraseSets());

                    return true;
                }
            }
        }
        LOG.debug(this, "NO HIDDEN INPUT in " + input);
        return false;
    }

    /**
     *
     *  This method receives the input from the user and asynchronously processes it. It will check
     *  the input for hidden commands, ask the service for a response and call handleNewCurrentResponse
     *  to handle the service's response, while changing
     *
     * @return whether a hidden input was matched (and executed)
     *
     * @param input the input from the user
     *
     */
    private void handleSelectedOption(String input) {

        LOG.debug(this, "Heard Phrase: " + input);

        runOnUiThread(() -> {
            setCaptionsVisible(false);
            setOptionsVisible(false);
            setThinkingVisible(true);
            smallButtons[0].setText("Start over");
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

    /**yes
     *
     *  This method is called by handleSelectedOption() after obtaining a response from the
     *  conversation service. Here, we update the options displayed to the user in the background
     *  while reading out the response to the user and showing the captions. Finally, it calls
     *  selectOptionWithPhraseSets() to prompt the user for the next input.
     *
     */
    private void handleNewCurrentResponse() {
//        Restart conversation if service did not provide new options to choose from (reached leaf)
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
            setThinkingVisible(false);
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
                        buttonSkip.setVisibility(currentResponse.getOptions() == null || currentResponse.getOptions().length == 0 ? View.INVISIBLE : View.VISIBLE);
                    }
                }
            });

            runOnUiThread(() -> {
                captionText.setVisibility(View.INVISIBLE);
                setCaptionsVisible(false);
                setOptionsVisible(true);
                setThinkingVisible(false);
                buttonSkip.setVisibility(View.INVISIBLE);
            });

            SpeechInput.selectOptionWithPhraseSets(r -> handleSelectedOption(r.getHeardPhrase().getText())
                    , displayedContextualOptions, allOptionPhraseSets());

        }, currentResponse.getResponseText());

    }


    /**
     *
     * This method returns the phrase sets as arrays for all options, including hidden actions.
     *
     *  @return phrase sets as arrays for all options, including hidden actions
     *
     */
    private String[][] allOptionPhraseSets() {
        String[][] result = Arrays.copyOf(hiddenOptionPhraseSets, hiddenOptionPhraseSets.length + contextualOptionPhraseSets.length);
        System.arraycopy(contextualOptionPhraseSets, 0, result, hiddenOptionPhraseSets.length, contextualOptionPhraseSets.length);
        return result;
    }

//    Screen state updater methods
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

    private void setThinkingVisible(boolean visible) {
        hmmSpinner.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        hmmText.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

//    Other helper methods

    private void runOnUiThread(Runnable runnable){
        thisConversationDemoFragment.getActivity().runOnUiThread(runnable);
    }


}
