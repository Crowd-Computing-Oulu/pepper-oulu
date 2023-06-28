package fi.oulu.danielszabo.pepper;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;

import fi.oulu.danielszabo.pepper.applications.control.ControlFragment;
import fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.PepperStudyPromotionFragment;
import fi.oulu.danielszabo.pepper.tools.SpeechInput;
import fi.oulu.danielszabo.pepper.applications.gpt_prototype.GPTFragment;
import fi.oulu.danielszabo.pepper.applications.home.HomeFragment;
import fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.ITEEPromotionFragment;
import fi.oulu.danielszabo.pepper.log.LOG;
import fi.oulu.danielszabo.pepper.log.LogFragment;
import fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.SonaPromotionGPTFragment;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks, LogFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, ControlFragment.OnFragmentInteractionListener {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOG.debug(this, "onCreate");


        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);

        // Set activity layout
        setContentView(R.layout.activity_main);

        this.fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    public void onAppSelectorPressed(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        Fragment newFragment = null;
        switch (view.getId()) {
            case R.id.btn_home: {
                newFragment = new HomeFragment();
                break;
            }
            case R.id.btn_log: {
                newFragment = new LogFragment();
                break;
            }
            case R.id.btn_gpt_prototype: {
                newFragment = new GPTFragment();
                break;
            }
            case R.id.btn_sona: {
                newFragment = new SonaPromotionGPTFragment();
                break;
            }
            case R.id.btn_control: {
                newFragment = new ControlFragment();
                break;
            }
            case R.id.btn_offline: {
                newFragment = new ITEEPromotionFragment();
                break;
            }
            case R.id.btn_study: {
                newFragment = new PepperStudyPromotionFragment();
                break;
            }
            default: {
                newFragment = new HomeFragment();
                break;
            }
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
