package fi.oulu.danielszabo.pepper_base.listeners;

import android.util.Log;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ApproachHumanBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.human.Emotion;
import com.aldebaran.qi.sdk.object.human.Gender;
import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.ApproachHuman;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.util.Date;

import fi.oulu.danielszabo.pepper_base.PepperApplication;
import fi.oulu.danielszabo.pepper_base.R;

public class OnRecommendedHumanToApproachChangedListener implements HumanAwareness.OnRecommendedHumanToApproachChangedListener {

    private int timeoutMilliseconds;
    private static long lastTrigger;

    public OnRecommendedHumanToApproachChangedListener(int timeoutMilliseconds) {
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    @Override
    public void onRecommendedHumanToApproachChanged(Human recommendedHumanToApproach) {

        long currTime = new Date().getTime();

        if (currTime - lastTrigger < timeoutMilliseconds) {
            return;
        } else {
            lastTrigger = currTime;
            if (recommendedHumanToApproach != null) {

//                ApproachHuman approachHuman = ApproachHumanBuilder.with(PepperApplication.qiContext)
//                        .withHuman(recommendedHumanToApproach)
//                        .build();
//                approachHuman.run();
            }
        }

        // This is super unreliable...
//        if(recommendedHumanToEngage.getEstimatedGender() == Gender.FEMALE){
//            PepperApplication.simpleSay("Hello beautiful!");
//        } else {
//            PepperApplication.simpleSay("Hello handsome!");
//        }
//

    }
}
