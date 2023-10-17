package fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service;


import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class OfflinePepperService {

  private Map<String, Conversation> conversations = new HashMap<>();

  private Context context;

  public OfflinePepperService(Context context) {
    this.context = context;
  }

  public Conversation startConversation(String conversationId) {
    return conversations.put(
        conversationId,
        new Conversation(context));
  }

  public void resetConversation(String conversationId) {
    if (conversations.containsKey(conversationId)) {
      conversations.put(conversationId, new Conversation(context));
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
