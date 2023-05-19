package fi.oulu.danielszabo.pepper_base.gpt_prototype;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.aldebaran.qi.Consumer;

import java.util.Arrays;

import fi.oulu.danielszabo.pepper_base.R;
import fi.oulu.danielszabo.pepper_base.control.SimpleController;
import fi.oulu.danielszabo.pepper_base.control.SpeechInput;
import fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service.config.ApiConfig;
import fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service.gpt35turbo_conversation.LLMResponseWithOptions;
import fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service.service.GPT35TurboPepperService;
import fi.oulu.danielszabo.pepper_base.log.LOG;

public class GPTFragment extends Fragment {

    private final GPTFragment thisGPTFragment = this;

    private GPTFragment.OnFragmentInteractionListener mListener;

    private Button[] largeButtons = new Button[5];
    private TextView[] optionNumbers = new TextView[5];
    private Button[] smallButtons = new Button[3];
    private TextView hmmText;

    private final String CONV_ID = "c1";
    private final GPT35TurboPepperService llmService = new GPT35TurboPepperService(ApiConfig.API_TOKEN);
    private String[] contextualOptions;

    private LLMResponseWithOptions lastResponse;

    public static GPTFragment newInstance(String param1, String param2) {
        GPTFragment fragment = new GPTFragment();
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
        View view = inflater.inflate(R.layout.fragment_gpt_prototype, container, false);

        hmmText = view.findViewById(R.id.txt_hmm);

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
        smallButtons[1] = view.findViewById(R.id.button_other);
        smallButtons[1].setOnClickListener(v -> onOtherButtonPressed(v));
        smallButtons[2] = view.findViewById(R.id.button_bye);
        smallButtons[2].setOnClickListener(v -> onByeButtonPressed(v));

        init();

        return view;
    }

    private void init() {
        llmService.startConversation(CONV_ID);

        final String[] staticTopicOptions = { "tell me about a person",
                "tell me about a place",
                "tell me about the university",
                "A university facility",
                "Tell me about yourself" };

        contextualOptions = staticTopicOptions;

        thisGPTFragment.getActivity().runOnUiThread(() -> {
            for (int i = 0; i < largeButtons.length; i++) {
                if (contextualOptions.length > i) {
                    largeButtons[i].setText(contextualOptions[i]);
                    largeButtons[i].setVisibility(View.VISIBLE);
                    optionNumbers[i].setVisibility(View.VISIBLE);
                } else {
                    largeButtons[i].setVisibility(View.INVISIBLE);
                    optionNumbers[i].setVisibility(View.INVISIBLE);
                }
            }
            setThinking(false);
        });

        SpeechInput.selectOption(r -> onOptionSelected(r.getHeardPhrase().getText())
                , contextualOptions);

    }

    public void onStartButtonPressed(View view) {
        init();
    }

    public void onOtherButtonPressed(View view) {
        setThinking(true);

        AsyncTask.execute(() -> {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            llmService.extendOptions(CONV_ID, lastResponse);

            if(lastResponse.getOptions().length == 0){
                contextualOptions = new String[]{"Tell me more", "Help me with something else", "Thank you"};
            } else {
                contextualOptions = lastResponse.getOptions();
            }

            SimpleController.say(__ -> {
                thisGPTFragment.getActivity().runOnUiThread(() -> {
                    setThinking(false);
                    for (int i = 0; i < largeButtons.length; i++) {
                        if(contextualOptions.length > i) {
                            largeButtons[i].setText(contextualOptions[i]);
                            largeButtons[i].setVisibility(View.VISIBLE);
                            optionNumbers[i].setVisibility(View.VISIBLE);
                        } else {
                            largeButtons[i].setVisibility(View.INVISIBLE);
                            optionNumbers[i].setVisibility(View.INVISIBLE);
                        }
                    }
                });
            },lastResponse.getResponseText());
        });
    }

    public void onByeButtonPressed(View view) {
        onOptionSelected(((Button) view).getText().toString());
    }

    public void onOptionSelected(View view) {
        SpeechInput.cancelListen();
        onOptionSelected(((Button) view).getText().toString());
    }

    private void onOptionSelected(String input) {
        thisGPTFragment.getActivity().runOnUiThread(() -> {
            setThinking(true);
        });

        AsyncTask.execute(() -> {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            lastResponse = llmService.respondTo(CONV_ID, input);

            if(lastResponse.getOptions().length == 0){
                contextualOptions = new String[]{"Tell me more", "Help me with something else", "Thank you"};
            } else {
                contextualOptions = lastResponse.getOptions();
            }

            SimpleController.say(__ -> {
                thisGPTFragment.getActivity().runOnUiThread(() -> {
                    setThinking(false);
                    for (int i = 0; i < largeButtons.length; i++) {
                        if(contextualOptions.length > i) {
                            largeButtons[i].setText(contextualOptions[i]);
                            largeButtons[i].setVisibility(View.VISIBLE);
                            optionNumbers[i].setVisibility(View.VISIBLE);
                        } else {
                            largeButtons[i].setVisibility(View.INVISIBLE);
                            optionNumbers[i].setVisibility(View.INVISIBLE);
                        }
                    }
                });
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void setThinking(boolean thinking) {

        for (int i = 0; i < largeButtons.length; i++) {
            largeButtons[i].setVisibility(thinking ? View.INVISIBLE : View.VISIBLE);
        }
        for (int i = 0; i < optionNumbers.length; i++) {
            optionNumbers[i].setVisibility(thinking ? View.INVISIBLE : View.VISIBLE);
        }
        for (int i = 0; i < smallButtons.length; i++) {
            smallButtons[i].setVisibility(thinking ? View.INVISIBLE : View.VISIBLE);
        }

        hmmText.setVisibility(thinking?View.VISIBLE:View.INVISIBLE);

        if(thinking){
            hmmText.setText("Hmm");
            SimpleController.say( __ -> AsyncTask.execute(() -> {
                try {
                    for (int i = 0; i < 8; i++) {
                        thisGPTFragment.getActivity().runOnUiThread(() -> {
                            hmmText.setText(hmmText.getText() + ".");
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }),"Hmm...");

        }


    }
}
