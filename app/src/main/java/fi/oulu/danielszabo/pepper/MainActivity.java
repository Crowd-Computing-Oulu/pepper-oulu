package fi.oulu.danielszabo.pepper;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
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

import fi.oulu.danielszabo.pepper.screens.Help;
import fi.oulu.danielszabo.pepper.screens.control.ControlFragment;
import fi.oulu.danielszabo.pepper.screens.conv_demo.ConversationDemoFragment;
import fi.oulu.danielszabo.pepper.screens.home.HomeFragment;
import fi.oulu.danielszabo.pepper.log.LOG;
import fi.oulu.danielszabo.pepper.log.LogFragment;
import fi.oulu.danielszabo.pepper.screens.actions.Action;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks, LogFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, ControlFragment.OnFragmentInteractionListener, Action.OnFragmentInteractionListener, Help.OnFragmentInteractionListener {
    private Fragment fragment;
    private ImageButton btn_help, btnvolume;
    private TextView statusText;
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
        statusText = (TextView) findViewById(R.id.txt_status);

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

    }

    public void onAppSelectorPressed(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        Fragment newFragment = null;
        int id = view.getId();
        if (id == R.id.btn_home) {
            newFragment = new HomeFragment();
        }  else if (id == R.id.btn_control) {
            newFragment = new ControlFragment();
        }  else if (id == R.id.btn_help) {
            newFragment = new Help();
        } else if (id == R.id.btn_conv_demo) {
            newFragment = new ConversationDemoFragment();
        } else if (id == R.id.btn_action) {
            newFragment = new Action();
        } else {
            newFragment = new HomeFragment();
        }
        fragmentTransaction.replace(R.id.fragment, newFragment, this.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public void qiContextInitialised(boolean success) {
        runOnUiThread(() -> {
            if (success) {
                this.statusText.setText(R.string.status_ok);
            } else {
                this.statusText.setText(R.string.status_error);
            }
        });
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
        PepperApplication.initialize(qiContext, this);

        LOG.debug(this, "Qi Context Created");

        // Set default fragment
        onAppSelectorPressed(findViewById(R.id.btn_conv_demo));
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
