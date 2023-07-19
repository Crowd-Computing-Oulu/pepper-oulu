package fi.oulu.danielszabo.pepper;

import com.aldebaran.qi.sdk.QiContext;

public class PepperApplication {

    public static QiContext qiContext;

    static void initialize(QiContext qiContext) {
        PepperApplication.qiContext = qiContext;
    }
}
