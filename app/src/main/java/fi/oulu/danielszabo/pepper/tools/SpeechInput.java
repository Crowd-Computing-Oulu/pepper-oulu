package fi.oulu.danielszabo.pepper.tools;

import android.os.AsyncTask;

import com.aldebaran.qi.Consumer;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.object.conversation.BodyLanguageOption;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.oulu.danielszabo.pepper.PepperApplication;
import fi.oulu.danielszabo.pepper.log.LOG;

public class SpeechInput {

    private static Future<ListenResult> currentListen = null;

    public static void selectOption(Consumer<ListenResult> callback, String... options) {
        if(isListening()){
            LOG.error(SpeechInput.class, "Pepper is already listening!");
            cancelListen();
        }

        if(PepperApplication.qiContext == null) {
            LOG.error(SpeechInput.class, "Tried to call SpeechInput.selectOption() before qiContext was initialised.");
        } else {
            AsyncTask.execute(() -> {
                List<PhraseSet> phraseSets = new ArrayList<>();
                for (int i = 0; i < options.length; i++) {
                    phraseSets.add(PhraseSetBuilder.with(PepperApplication.qiContext)
                            .withTexts(options[i])
                            .build());
                }
                currentListen = ListenBuilder.with((PepperApplication.qiContext)).withPhraseSets(phraseSets).build().async().run();
                try {
                    currentListen.andThenConsume(callback).andThenConsume(__ -> {
                        currentListen.cancel(false);
                        currentListen = null;
                    });

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    public static void selectOptionWithPhraseSets(Consumer<ListenResult> callback, String[] options, String[][] phraseSets) {

        if(isListening()){
            LOG.warning(SpeechInput.class, "Pepper is already listening!");
            cancelListen();
            try {
                // pepper might need some time to catch up
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(PepperApplication.qiContext == null) {
            LOG.error(SpeechInput.class, "Tried to call SpeechInput.selectOption() before qiContext was initialised.");
        } else {
            AsyncTask.execute(() -> {
                List<PhraseSet> phraseSetList = new ArrayList<>();
//                add non-hidden contextual options
                for (int i = 0; i < options.length; i++) {
                    phraseSetList.add(PhraseSetBuilder.with(PepperApplication.qiContext)
                            .withTexts(phraseSets[i])
                            .build());
                }

                // add hidden global options
                for (int i = options.length; i < phraseSets.length; i++) {
                    phraseSetList.add(PhraseSetBuilder.with(PepperApplication.qiContext)
                            .withTexts(phraseSets[i])
                            .build());
                }

                StringBuilder log = new StringBuilder("Listening for phraseSetList:");
                for (int i = 0; i < phraseSets.length; i++) {
                    log.append("\n").append(i).append(" - ").append(Arrays.toString(phraseSets[i]));
                }
                LOG.debug(SpeechInput.class, log.toString());

                currentListen = ListenBuilder
                        .with((PepperApplication.qiContext))
                        .withPhraseSets(phraseSetList)
                        .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                        .build()
                        .async()
                        .run();


                try {
                    currentListen.andThenConsume(callback).andThenConsume(__ -> {
                        currentListen.cancel(false);
                        currentListen = null;
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }

    public static boolean isListening() {
        return currentListen != null;
    }


    public static void pauseWhile() {
        cancelListen();
    }

    public static void cancelListen() {
        LOG.warning(SpeechInput.class, "Listen Cancelled.");
        if(currentListen != null) currentListen.cancel(true);
        currentListen = null;
    }
}
