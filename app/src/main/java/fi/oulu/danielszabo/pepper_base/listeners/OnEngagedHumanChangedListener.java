package fi.oulu.danielszabo.pepper_base.listeners;

import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import fi.oulu.danielszabo.pepper_base.PepperApplication;
import fi.oulu.danielszabo.pepper_base.R;
import fi.oulu.danielszabo.pepper_base.log.LOG;

public class OnEngagedHumanChangedListener implements HumanAwareness.OnEngagedHumanChangedListener {

    @Override
    public void onEngagedHumanChanged(Human engagedHuman) {

        if(engagedHuman == null) return;

        LOG.debug(this, "onEngagedHumanChanged: " + engagedHuman);
//        PepperApplication.simpleSay("Hello!");

        Topic topic = TopicBuilder.with(PepperApplication.qiContext) // Create the builder using the QiContext.
                .withResource(R.raw.chat) // Set the topic resource.
                .build(); // Build the topic.

        QiChatbot qiChatbot = QiChatbotBuilder.with(PepperApplication.qiContext)
                .withTopic(topic)
                .build();

        Chat chat =  ChatBuilder.with(PepperApplication.qiContext)
                .withChatbot(qiChatbot)
                .build();

        chat.async().run();

        chat.addOnHeardListener(heardPhrase -> System.out.println("heard phrase:" + heardPhrase));

        chat.addOnStartedListener(() -> LOG.debug(this, "Discussion started."));
    }

}
