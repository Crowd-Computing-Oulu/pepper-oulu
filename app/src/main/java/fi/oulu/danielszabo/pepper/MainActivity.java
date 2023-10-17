package fi.oulu.danielszabo.pepper;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.SpeechEngine;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.SpeechEngine;

import fi.oulu.danielszabo.pepper.applications.Help;
import fi.oulu.danielszabo.pepper.applications.control.ControlFragment;
import fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.PepperStudyPromotionFragment;
import fi.oulu.danielszabo.pepper.tools.SimpleController;
import fi.oulu.danielszabo.pepper.tools.SpeechInput;
import fi.oulu.danielszabo.pepper.applications.gpt_prototype.GPTFragment;
import fi.oulu.danielszabo.pepper.applications.home.HomeFragment;
import fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.ITEEPromotionFragment;
import fi.oulu.danielszabo.pepper.log.LOG;
import fi.oulu.danielszabo.pepper.log.LogFragment;
import fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.SonaPromotionGPTFragment;
import fi.oulu.danielszabo.pepper.applications.action.Action;

import fi.oulu.danielszabo.pepper.applications.action.Action;
import fi.oulu.danielszabo.pepper.tools.MimicTts;


public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks, LogFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, ControlFragment.OnFragmentInteractionListener, Action.OnFragmentInteractionListener, Help.OnFragmentInteractionListener {


    private Fragment fragment;
    private ImageButton btn_help, btnvolume;
    private boolean isMuted = false;
    private boolean isEnglish = true;
    private SpeechEngine speechEngine;

    private AudioManager audioManager;

//    private TTSRequestTask ttsRequestTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOG.debug(this, "onCreate");


        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);

        // Set activity layout
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

//        ttsRequestTask = new TTSRequestTask();

        this.fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

//        TextView btn_lang = findViewById(R.id.btn_lang);
//        btn_lang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isEnglish = !isEnglish;
//                if (isEnglish) {
//                    btn_lang.setText("FI");
//                    MimicTts.setServerEndpoint("http://100.79.68.64:59125/api/tts?voice=fi_FI/harri-tapani-ylilammi_low");
//                } else {
//                    btn_lang.setText("EN");
//                    MimicTts.setServerEndpoint("http://100.79.68.64:59125/api/tts?voice=en_US/vctk_low#p240");
//                }
//            }
//        });

        // Add mute button and display skip button
        btnvolume = findViewById(R.id.btn_volume);
        btnvolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuted = !isMuted;
                if (isMuted) {
                    btnvolume.setImageResource(R.drawable.ic_volume_off);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                } else {
                    btnvolume.setImageResource(R.drawable.ic_volume_on);
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }
            }

            private void setSpeechOutputVolume(float volume) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int targetVolume = (int) (volume * maxVolume);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0);
            }
        });

        btn_help = findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAppSelectorPressed(v);
            }
        });
    }

    public void onAppSelectorPressed(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        Fragment newFragment = null;
        int id = view.getId();
        if (id == R.id.btn_home) {
            newFragment = new HomeFragment();
        } else if (id == R.id.btn_gpt_prototype) {
            newFragment = new GPTFragment();
        } else if (id == R.id.btn_sona) {
            newFragment = new SonaPromotionGPTFragment();
        } else if (id == R.id.btn_control) {
            newFragment = new ControlFragment();
        } else if (id == R.id.btn_offline) {
            newFragment = new ITEEPromotionFragment();
        } else if (id == R.id.btn_help) {
            newFragment = new Help();
        } else if (id == R.id.btn_study) {
            newFragment = new PepperStudyPromotionFragment();
        } else if (id == R.id.btn_action) {
            newFragment = new Action();
        } else {
            newFragment = new HomeFragment();
        }
        fragmentTransaction.replace(R.id.fragment, newFragment, this.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        LOG.debug(this, "onRobotFocusGained");

        // Store context object in global application state, initialize the whole app
        PepperApplication.initialize(qiContext);

        LOG.debug(this, "Qi Context Created");

        // Set default fragment
        onAppSelectorPressed(findViewById(R.id.btn_study));
    }

    @Override
    public void onRobotFocusLost() {
        LOG.debug(this, "onRobotFocusLost");
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        LOG.debug(this, "onRobotFocusRefused: " + reason);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        LOG.debug(this, "onFragmentInteraction: " + uri);
    }
}
