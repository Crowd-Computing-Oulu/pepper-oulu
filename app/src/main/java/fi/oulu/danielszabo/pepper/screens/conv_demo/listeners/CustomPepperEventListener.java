package fi.oulu.danielszabo.pepper.screens.conv_demo.listeners;

import java.util.Date;

public abstract class CustomPepperEventListener {

    private long lastTriggered = 0;

    protected void resetTimeout() {
        lastTriggered = new Date().getTime();
    }

    private boolean timeoutOver(long timeoutInMillis){
        long currTime = new Date().getTime();
        return currTime - lastTriggered > timeoutInMillis;
    }

    void runWithTimeout(Runnable runnable, long timeoutInMillis) {

        if(timeoutOver(timeoutInMillis)) {
            runnable.run();
            resetTimeout();
        }
    }

}
