package fi.oulu.danielszabo.pepper;

import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;

public class PepperApplication {

    public static QiContext qiContext;

    public static int state = 0;

    public static void initialize(QiContext qiContext) {
        PepperApplication.qiContext = qiContext;
        state = 1;
        PepperApplication.registerHumanAwerenessListeners();
        state = 2;
        PepperApplication.registerTouchListeners();
        state = 3;
        // Let Pepper announce herself after startup
//        SimpleController.say("All systems online. P3PR ready for combat.");
    }

    public static void registerTouchListeners() {
        qiContext.getTouch().getSensor("Head/Touch").addOnStateChangedListener(touchState -> {
            Log.i(PepperApplication.class.toString(), "Sensor " + (touchState.getTouched() ? "touched" : "released") + " at " + touchState.getTime());

//            SimpleController.say("Oh, that's my head!");
        });

        qiContext.getTouch().getSensor("LHand/Touch").addOnStateChangedListener(touchState -> {
            Log.i(PepperApplication.class.toString(), "Sensor " + (touchState.getTouched() ? "touched" : "released") + " at " + touchState.getTime());

//            SimpleController.say("Oh, that's my left hand!");
        });

        qiContext.getTouch().getSensor("RHand/Touch").addOnStateChangedListener(touchState -> {
            Log.i(PepperApplication.class.toString(), "Sensor " + (touchState.getTouched() ? "touched" : "released") + " at " + touchState.getTime());

//            SimpleController.say("Oh, that's my left hand!");
        });
    }

    public static void registerHumanAwerenessListeners() {
        // Register logging listeners
//        qiContext.getHumanAwareness().addOnHumansAroundChangedListener(humans -> LOG.debug("onHumansAroundChanged", humans.toString()));
//        qiContext.getHumanAwareness().addOnEngagedHumanChangedListener(engagedHuman -> LOG.debug("onEngagedHumanChanged", engagedHuman.toString()));
//        qiContext.getHumanAwareness().addOnRecommendedHumanToApproachChangedListener(engagedHuman -> LOG.debug("onRecommendedHumanToApproachChanged", engagedHuman.toString()));
//        qiContext.getHumanAwareness().addOnRecommendedHumanToEngageChangedListener(engagedHuman -> LOG.debug("onRecommendedHumanToEngageChanged", engagedHuman.toString()));

        // Register behavioural listeners
//        qiContext.getHumanAwareness().addOnHumansAroundChangedListener(new OnHumansAroundChangedListener());
//          qiContext.getHumanAwareness().addOnEngagedHumanChangedListener(new OnEngagedHumanChangedListener());
//        qiContext.getHumanAwareness().addOnRecommendedHumanToApproachChangedListener(new OnRecommendedHumanToApproachChangedListener(10_000));
//        qiContext.getHumanAwareness().addOnRecommendedHumanToEngageChangedListener(new OnRecommendedHumanToEngageChangedListener(10_000));

    }




}
