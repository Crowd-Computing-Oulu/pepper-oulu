package fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.tools.SimpleController;
import fi.oulu.danielszabo.pepper.tools.SpeechInput;
import fi.oulu.danielszabo.pepper.log.LOG;
import fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.llm_service.config.ApiConfig;
import fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.llm_service.gpt35turbo_conversation.LLMResponseWithOptions;
import fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.llm_service.service.GPT35TurboPepperService;

public class SonaPromotionGPTFragment extends Fragment {

    private static final long TIMEOUT_MILLIS = 10 * 1000;
    private static final String CONV_ID = "c1";
    private static final GPT35TurboPepperService LLM_SERVICE = new GPT35TurboPepperService(ApiConfig.API_TOKEN);
    private static final String[] STATIC_TOPIC_OPTIONS = { "what is Sona?",
            "How do I sign up to Sona?",
            "tell me about ITEE",
            "tell me about UBICOMP",
            "who are you?" };

    private final SonaPromotionGPTFragment thisSonaPromotionGPTFragment = this;

    private Button[] largeButtons = new Button[5];
    private TextView[] optionNumbers = new TextView[5];
    private Button[] smallButtons = new Button[1];
    private TextView hmmText, instructionText, captionText;
    private ProgressBar hmmSpinner;
    private ConstraintLayout qrCard;
    private Timer timeoutTimer = new Timer();;
    private String[] contextualOptions;
    private LLMResponseWithOptions lastResponse;

    public static SonaPromotionGPTFragment newInstance(String param1, String param2) {
        SonaPromotionGPTFragment fragment = new SonaPromotionGPTFragment();
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
        View view = inflater.inflate(R.layout.fragment_sona_promotion, container, false);

        hmmText = view.findViewById(R.id.txt_hmm);
        instructionText = view.findViewById(R.id.txt_instruction);
        captionText = view.findViewById(R.id.txt_caption);
        hmmSpinner = view.findViewById(R.id.spinner_hmm);
        qrCard = view.findViewById(R.id.card_qr);

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

        init();


        return view;
    }

    private void init() {
        cancelTimer();

        LLM_SERVICE.startConversation(CONV_ID);

        contextualOptions = STATIC_TOPIC_OPTIONS;

        thisSonaPromotionGPTFragment.getActivity().runOnUiThread(() -> {
            for (int i = 0; i < largeButtons.length; i++) {
                if (contextualOptions.length > i) {
                    largeButtons[i].setText(contextualOptions[i]);
                    largeButtons[i].setVisibility(View.VISIBLE);
//                    optionNumbers[i].setVisibility(View.VISIBLE);
                } else {
                    largeButtons[i].setVisibility(View.INVISIBLE);
//                    optionNumbers[i].setVisibility(View.INVISIBLE);
                }
            }
        });

        thisSonaPromotionGPTFragment.getActivity().runOnUiThread(() -> {
            setCaptionsVisible(false);
            setThinkingVisible(false);
            setOptionsVisible(true);
            smallButtons[0].setVisibility(View.INVISIBLE);
        });

        SpeechInput.selectOption(r -> onOptionSelected(r.getHeardPhrase().getText())
                , contextualOptions);


    }

    public void onStartButtonPressed(View view) {
        init();

        AsyncTask.execute(() -> {
            SimpleController.say(__ -> {}, "Hello!");
        });
    }

    public void onOptionSelected(View view) {
        SpeechInput.cancelListen();
        onOptionSelected(((Button) view).getText().toString());
    }

    private void onOptionSelected(String input) {
        thisSonaPromotionGPTFragment.getActivity().runOnUiThread(() -> {
            setCaptionsVisible(false);
            setThinkingVisible(true);
            setOptionsVisible(false);
            smallButtons[0].setText("Start over");
        });



        AsyncTask.execute(() -> {

            lastResponse = LLM_SERVICE.respondTo(CONV_ID, input);

            if(lastResponse.getOptions().length == 0){
                contextualOptions = STATIC_TOPIC_OPTIONS;
            } else {
                contextualOptions = lastResponse.getOptions();
            }

            thisSonaPromotionGPTFragment.getActivity().runOnUiThread(() -> {
                captionText.setText(lastResponse.getResponseText());
                setCaptionsVisible(true);
                setThinkingVisible(false);
                setOptionsVisible(false);
            });

            SimpleController.say(__ -> {
                thisSonaPromotionGPTFragment.getActivity().runOnUiThread(() -> {
                    for (int i = 0; i < largeButtons.length; i++) {
                        if(contextualOptions.length > i) {
                            largeButtons[i].setText(contextualOptions[i]);
                            largeButtons[i].setVisibility(View.VISIBLE);
//                            optionNumbers[i].setVisibility(View.VISIBLE);
                        } else {
                            largeButtons[i].setVisibility(View.GONE);
//                            optionNumbers[i].setVisibility(View.GONE);
                        }
                    }
                });

                thisSonaPromotionGPTFragment.getActivity().runOnUiThread(() -> {

                captionText.setVisibility(View.INVISIBLE);
                    setCaptionsVisible(false);
                    setThinkingVisible(false);
                    setOptionsVisible(true);
                });

                startTimer();
                SpeechInput.selectOption(r -> onOptionSelected(r.getHeardPhrase().getText())
                        , contextualOptions);

            }, lastResponse.getResponseText());
        });


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

    private void setOptionsVisible(boolean visible) {
        qrCard.setVisibility(visible ? View.VISIBLE : View.GONE);
        for (int i = 0; i < smallButtons.length; i++) {
            smallButtons[i].setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        instructionText.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

        if(visible && largeButtons[0].getVisibility() == View.VISIBLE) return;
        for (int i = 0; i < largeButtons.length; i++) {
            largeButtons[i].setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        for (int i = 0; i < optionNumbers.length; i++) {
//            optionNumbers[i].setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void setThinkingVisible(boolean visible) {
        hmmSpinner.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        hmmText.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    private void setCaptionsVisible(boolean visible) {
        captionText.setVisibility(visible?View.VISIBLE:View.INVISIBLE);
    }

    private void startTimer() {
        if(timeoutTimer != null) {
            timeoutTimer.cancel();

            timeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    init();
                }
            }, TIMEOUT_MILLIS);
        }
    }

    private void cancelTimer() {
        if(timeoutTimer != null ) {
            timeoutTimer.cancel();
            timeoutTimer = null;
        }
    }

}
