package fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.llm_service.service;

import com.theokanning.openai.service.OpenAiService;

import java.util.HashMap;
import java.util.Map;

import fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.llm_service.gpt35turbo_conversation.Conversation;
import fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.llm_service.gpt35turbo_conversation.LLMResponseWithOptions;

public class GPT35TurboPepperService {

  private static final String MODEL = "gpt-3.5-turbo";
  private static final int MAX_TOKENS = 1500;
  private static final double RESPONSE_TEMPERATURE = 1.0; // 0.2 -> relatively consistent, 0.7 -> a bit more creative, 1.0 -> madness
  private static final double FREQUENCY_PENALTY = 0.0;
  private static final double PRESENCE_PENALTY = 0.6;
  private static final int USER_OPTION_NUMBER = 5;

  private final OpenAiService openAiService;

  private Map<String, Conversation> conversations = new HashMap<>();

  public GPT35TurboPepperService(String apiToken) {
    this.openAiService = new OpenAiService(apiToken);
  }

  public Conversation startConversation(String conversationId) {
    return conversations.put(
        conversationId,
        new Conversation(openAiService, MODEL, RESPONSE_TEMPERATURE,
            FREQUENCY_PENALTY,
            PRESENCE_PENALTY, MAX_TOKENS, USER_OPTION_NUMBER));
  }

  public LLMResponseWithOptions respondTo(String conversationId, String input) {
    if (conversations.containsKey(conversationId)) {
      return conversations.get(conversationId).respondTo(input);
    } else
      return null;
  }

  public void extendOptions(String conversationId, LLMResponseWithOptions responseWithOptions) {
    if (conversations.containsKey(conversationId)) {
      conversations.get(conversationId).extendOptions(responseWithOptions);
    } else {
      throw new RuntimeException("No conversation with id " + conversationId);
    }

  }

  public LLMResponseWithOptions getStartingState(String convId) {
    return new LLMResponseWithOptions(null, "Tell me about yourself", "Tell me about ITEE", "Tell me about UBICOMP", "Give me a fitness tip", "Tell me a joke");
  }

  public LLMResponseWithOptions resetConversation(String convId) {
    conversations.remove(convId);
    startConversation(convId);
    return getStartingState(convId);
  }


}
