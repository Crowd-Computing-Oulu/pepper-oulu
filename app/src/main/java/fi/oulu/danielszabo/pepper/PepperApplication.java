package fi.oulu.danielszabo.pepper;

import android.content.Context;

import com.aldebaran.qi.sdk.QiContext;

public class PepperApplication {

    public static QiContext qiContext;

    public static MainActivity mainActivity;

    static void initialize(QiContext qiContext, MainActivity mainActivity) {
        PepperApplication.qiContext = qiContext;
        PepperApplication.mainActivity = mainActivity;

        mainActivity.qiContextInitialised(true);
    }
}
