package fi.oulu.danielszabo.pepper.screens.conv_demo.listeners;

import android.util.Log;

import com.aldebaran.qi.sdk.object.touch.TouchSensor;
import com.aldebaran.qi.sdk.object.touch.TouchState;

import fi.oulu.danielszabo.pepper.PepperApplication;
import fi.oulu.danielszabo.pepper.tools.SimpleController;


public class TouchResponder extends CustomPepperEventListener implements TouchSensor.OnStateChangedListener {

    private final String sensorName;

    public TouchResponder(String sensorName) {
        this.sensorName = sensorName;
    }

    @Override
    public void onStateChanged(TouchState state) {
        Log.i(PepperApplication.class.toString(), "Sensor " + sensorName + " " + (state.getTouched() ? "touched" : "released") + " at " + state.getTime());
        if(state.getTouched()) {
            runWithTimeout(() -> SimpleController.say("Oh, that's my " + sensorName + "."), 10_000);
        }
    }


}
