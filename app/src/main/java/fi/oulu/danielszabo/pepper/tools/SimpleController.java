package fi.oulu.danielszabo.pepper.tools;

import com.aldebaran.qi.Consumer;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;

import fi.oulu.danielszabo.pepper.PepperApplication;
import fi.oulu.danielszabo.pepper.R;
import fi.oulu.danielszabo.pepper.log.LOG;

public class SimpleController {

    private final static SimpleController INSTANCE = new SimpleController();

    private static Future currentSay = null;

    public static SimpleController say(Consumer<Void> then, final String text) {
        if(SpeechInput.isListening()) {

            SpeechInput.pauseWhile(() -> {
                String[] options = text.split(";");
                String randomlySelectedOption = options[(int)(Math.random()*options.length)];
                SayBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                        .withText("\\rspd=90\\ \\vct=100\\" + randomlySelectedOption) // Set the text to say.
                        .build().run();
                currentSay = null;
                try {
                    then.consume(null);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } else {
            SayBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                    .withText("\\rspd=90\\ \\vct=100\\" + text) // Set the text to say.
                    .buildAsync().andThenConsume(say -> currentSay = say.async().run().andThenConsume(__ -> currentSay = null).andThenConsume(then));
        }

        return INSTANCE;
    }

    public static SimpleController say(String text) {
        return say(__ -> {}, text);
    }

    public static SimpleController cancelSay() {
        currentSay.cancel(true);
        currentSay = null;
        return INSTANCE;
    }

    public static boolean isSaying() {
        return currentSay != null;
    }


    public static SimpleController moveForward() {
        AnimationBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                .withResources(R.raw.step_forward) // Set the animation resource.
                .buildAsync().andThenConsume(a -> {
            AnimateBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                    .withAnimation(a) // Set the animation.
                    .buildAsync().thenConsume(a2 -> a2.andThenConsume(a3 -> a3.run()));
        });

        return INSTANCE;
    }

    public static SimpleController turnLeft() {
        AnimationBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                .withResources(R.raw.turn_left) // Set the animation resource.
                .buildAsync().andThenConsume(a -> {
                     AnimateBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                            .withAnimation(a) // Set the animation.
                            .buildAsync().thenConsume(a2 -> a2.andThenConsume(a3 -> a3.run()));
                });

        return INSTANCE;
    }

    public static SimpleController turnRight() {
        AnimationBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                .withResources(R.raw.turn_right) // Set the animation resource.
                .buildAsync().andThenConsume(a -> {
            AnimateBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                    .withAnimation(a) // Set the animation.
                    .buildAsync().thenConsume(a2 -> a2.andThenConsume(a3 -> a3.run()));
        });

        return INSTANCE;
    }

    public static SimpleController turnAround() {
        AnimationBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                .withResources(R.raw.turn_around) // Set the animation resource.
                .buildAsync().andThenConsume(a -> {
            AnimateBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                    .withAnimation(a) // Set the animation.
                    .buildAsync().thenConsume(a2 -> a2.andThenConsume(a3 -> a3.run()));
        });

        return INSTANCE;
    }


    public static SimpleController spin() {
        AnimationBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                .withResources(R.raw.spin) // Set the animation resource.
                .buildAsync().andThenConsume(a -> {
            AnimateBuilder.with(PepperApplication.qiContext) // Create the builder with the context.
                    .withAnimation(a) // Set the animation.
                    .buildAsync().thenConsume(a2 -> a2.andThenConsume(a3 -> a3.run()));
        });

        return INSTANCE;
    }

    public static SimpleController setWheelLock(boolean lock) {
        // TODO: implement somehow?
        return INSTANCE;
    }
}
