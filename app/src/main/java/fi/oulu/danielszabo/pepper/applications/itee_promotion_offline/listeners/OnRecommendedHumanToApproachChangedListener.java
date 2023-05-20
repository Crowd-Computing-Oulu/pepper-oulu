package fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.listeners;

import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.util.Date;

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
