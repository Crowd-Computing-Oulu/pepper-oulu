package fi.oulu.danielszabo.pepper.applications.gpt_prototype.llm_service.gpt3_conversation;

import java.util.Date;
import java.util.LinkedList;

import lombok.Getter;

@Getter
public class ConversationHistory {

  LinkedList<ConversationHistoryMessage> messages = new LinkedList<>();

  public ConversationHistoryMessage add(String source, String content) {
    ConversationHistoryMessage message = new ConversationHistoryMessage(source, content, new Date().getTime());
    messages.add(message);
    return message;
  }

}
