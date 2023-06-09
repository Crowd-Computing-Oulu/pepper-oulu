package fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.offline_service;


import java.util.HashMap;
import java.util.Map;

public class OfflinePepperService {

  private Map<String, Conversation> conversations = new HashMap<>();

  public OfflinePepperService() {
  }

  public Conversation startConversation(String conversationId) {
    return conversations.put(
        conversationId,
        new Conversation());
  }

  public void resetConversation(String conversationId) {
    if (conversations.containsKey(conversationId)) {
      conversations.put(conversationId, new Conversation());
    }
  }

  public ResponseWithOptions getStartingState(String conversationId) {
    if (conversations.containsKey(conversationId)) {
      return conversations.get(conversationId).getStartingState();
    } else
      return null;
  }

  public ResponseWithOptions respondTo(String conversationId, String input) {
    if (conversations.containsKey(conversationId)) {
      return conversations.get(conversationId).respondTo(input);
    } else
      return null;
  }

}
