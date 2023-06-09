package fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.listeners;

import android.util.Log;

import com.aldebaran.qi.sdk.object.touch.TouchSensor;
import com.aldebaran.qi.sdk.object.touch.TouchState;

import fi.oulu.danielszabo.pepper.PepperApplication;
import fi.oulu.danielszabo.pepper.tools.SimpleController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TouchResponder extends CustomPepperEventListener implements TouchSensor.OnStateChangedListener {

    private final String sensorName;

    @Override
    public void onStateChanged(TouchState state) {
        Log.i(PepperApplication.class.toString(), "Sensor " + sensorName + " " + (state.getTouched() ? "touched" : "released") + " at " + state.getTime());
        if(state.getTouched()) {
            runWithTimeout(() -> SimpleController.say("Oh, that's my " + sensorName + "."), 10_000);
        }
    }


}
