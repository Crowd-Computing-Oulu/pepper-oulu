package fi.oulu.danielszabo.pepper_base.listeners;


import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ApproachHumanBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.util.Date;

import fi.oulu.danielszabo.pepper_base.PepperApplication;
import fi.oulu.danielszabo.pepper_base.R;
import fi.oulu.danielszabo.pepper_base.control.SimpleController;
import fi.oulu.danielszabo.pepper_base.log.LOG;

public class OnRecommendedHumanToEngageChangedListener implements HumanAwareness.OnRecommendedHumanToEngageChangedListener {

    private int timeoutMilliseconds;
    private static long lastTrigger;

    private static boolean listening = false;

    public OnRecommendedHumanToEngageChangedListener(int timeoutMilliseconds) {
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    @Override
    public void onRecommendedHumanToEngageChanged(Human recommendedHumanToEngage) {
        long currTime = new Date().getTime();

        if (currTime - lastTrigger < timeoutMilliseconds) {
            return;
        } else {
            if(listening) {
               return;
            } else {

                lastTrigger = currTime;
                if (recommendedHumanToEngage != null) {
                    SimpleController.say("Welcome to the UBICOMP lab!");
                }

                listening = true;
            }
            LOG.debug(this, "Waiting for voice command!");

            PhraseSet phraseSet = PhraseSetBuilder.with(PepperApplication.qiContext)
                    .withTexts("Turn around", "Turn left", "Turn right", "Spin", "Hello")
                    .build();


            Listen listen = ListenBuilder.with(PepperApplication.qiContext)
                    .withPhraseSet(phraseSet)

                    .build();

            ListenResult listenResult = listen.run();

            PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet(); // Equals to forwards.

            System.out.println(matchedPhraseSet.getPhrases());

            for(Phrase phrase : matchedPhraseSet.getPhrases()) {
                LOG.debug(this, "Heard phrase: " + phrase.getText()); // Prints "Heard phrase: forwards".
                switch (phrase.getText()){
                    case "Turn around" : {
                        SimpleController.say("Okay!").spin();
                        break;
                    }
                    case "Turn left" : {
                        SimpleController.say("Okay!").turnAround();
                        break;
                    }
                    case "Turn right" : {
                        SimpleController.say("Okay!").turnRight();
                        break;
                    }
                    case "Spin" : {
                        SimpleController.say("Okay!").turnAround();
                        break;
                    }
                    case "Step forward" : {
                        SimpleController.say("Okay!").moveForward();
                        break;
                    }
                    case "Hello" : {
                        SimpleController.say("Hello friend!");
                    }
                }

                listening = false;
            }
        }

    }
}
