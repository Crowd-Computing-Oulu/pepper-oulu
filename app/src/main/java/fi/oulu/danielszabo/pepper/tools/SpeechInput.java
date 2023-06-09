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

//    these two allow us to pause the listen and restart it
    private static Consumer<ListenResult> callback;
    private static String[] options;

    public static void selectOption(Consumer<ListenResult> callback, String... options) {

        SpeechInput.callback = callback;
        SpeechInput.options = options;

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
                StringBuilder log = new StringBuilder("Listening for phraseSetList (selectOption):");
                for (int i = 0; i < phraseSets.size(); i++) {
                    log.append("\n").append(i).append(" - ").append(phraseSets.get(i).getPhrases());
                }
                LOG.debug(SpeechInput.class, log.toString());
                try {
                    (currentListen = ListenBuilder
                            .with((PepperApplication.qiContext))
                            .withPhraseSets(phraseSets)
                            .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                            .build()
                            .async()
                            .run())
                            .andThenConsume(callback).andThenConsume(__ -> {
                        currentListen.cancel(false);
                        currentListen = null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void selectOptionWithPhraseSets(Consumer<ListenResult> callback, String[] options, String[][] phraseSets) {

        SpeechInput.callback = callback;
        SpeechInput.options = options;

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

                StringBuilder log = new StringBuilder("Listening for phraseSetList (selectOptionWithPhraseSets):");
                for (int i = 0; i < phraseSets.length; i++) {
                    log.append("\n").append(i).append(" - ").append(Arrays.toString(phraseSets[i]));
                }
                LOG.debug(SpeechInput.class, log.toString());
                try {
                    (currentListen = ListenBuilder
                            .with((PepperApplication.qiContext))
                            .withPhraseSets(phraseSetList)
                            .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                            .build()
                            .async()
                            .run())
                            .andThenConsume(callback).andThenConsume(__ -> {
                                currentListen.cancel(false);
                                currentListen = null;
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static boolean isListening() {
        return currentListen != null;
    }

    public static void pauseWhile(Runnable runnable) {
        LOG.debug(SimpleController.class.getSimpleName(), "Pausing Listen");
//        cancel currently ongoing speech input (if there is one)
        cancelListen();
//        run whatever the caller wanted
        runnable.run();
        LOG.debug(SimpleController.class.getSimpleName(), "Continuing Listen");
//        restart the speech input if needed
        if(callback != null && options != null) {
            selectOption(callback, options);
        }
    }

    public static void cancelListen() {
        if(isListening()) {
            currentListen.cancel(true);
            LOG.warning(SpeechInput.class, "Listen Cancelled.");
            currentListen = null;
        }
    }
}
