package fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

  private final List<ChatMessage> convHist = initConvHist();

  private final String userName = "user", robotName = "assistant";

  private final OfflineConversationTree oft;

  public Conversation(Context context) {
    oft = new OfflineConversationTree();

  }

  public ResponseWithOptions respondTo(String input) {

    convHist.add(new ChatMessage(userName, input));

    try {
      oft.input(input);
    } catch (Exception e) {
      e.printStackTrace();
      reset();
      return new ResponseWithOptions("Sorry, I encountered an error. Let's try again.", oft.getCurrent().getOptionPhraseSets(), oft.getCurrent().getOptions());
    }

    convHist.add(new ChatMessage(robotName, oft.getCurrent().getOutput()));

    return new ResponseWithOptions(oft.getCurrent().getOutput(),  oft.getCurrent().getOptionPhraseSets(),oft.getCurrent().getOptions());
  }

  private List<ChatMessage> initConvHist() {
    if(convHist != null) {
      convHist.clear();
      return null;
    }
    return new ArrayList<>();
  }

  ResponseWithOptions getStartingState() {
     return new ResponseWithOptions(oft.getRoot().getOutput(),  oft.getRoot().getOptionPhraseSets(), oft.getRoot().getOptions());
  }

  private void reset() {
    initConvHist();
  }
}
