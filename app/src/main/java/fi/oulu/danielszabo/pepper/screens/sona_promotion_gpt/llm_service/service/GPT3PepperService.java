package fi.oulu.danielszabo.pepper.screens.sona_promotion_gpt.llm_service.service;

import com.theokanning.openai.service.OpenAiService;

import java.util.HashMap;
import java.util.Map;

import fi.oulu.danielszabo.pepper.screens.sona_promotion_gpt.llm_service.gpt3_conversation.Conversation;
import fi.oulu.danielszabo.pepper.screens.sona_promotion_gpt.llm_service.gpt3_conversation.LLMResponseWithOptions;
import fi.oulu.danielszabo.pepper.screens.sona_promotion_gpt.llm_service.prompts.PromptTemplateManager;

public class GPT3PepperService {

  // use ada model for development where model accuracy/output doesn't matter
  // (it also makes testing faster)
  // private static final String MODEL = "ada";

  private static final String MODEL = "davinci";
  private static final int MAX_TOKENS = 1500;
  private static final double RESPONSE_TEMPERATURE = 0.2;
  private static final double OPTIONS_TEMPERATURE = 0.7;
  private static final double FREQUENCY_PENALTY = 0.0;
  private static final double PRESENCE_PENALTY = 0.6;
  private static final int USER_OPTION_NUMBER = 5;

  private static final String ROBOT_NAME = "Robot";
  private static final String USER_NAME = "User";

  private final OpenAiService openAiService;
  private final PromptTemplateManager ptm;

  private Map<String, Conversation> conversations = new HashMap<>();

  public GPT3PepperService(String apiToken) {
    this.openAiService = new OpenAiService(apiToken);
    this.ptm = new PromptTemplateManager();
  }

  public Conversation startConversation(String conversationId) {
    return conversations.put(
        conversationId,
        new Conversation(ptm, openAiService, USER_NAME, ROBOT_NAME, MODEL, RESPONSE_TEMPERATURE, OPTIONS_TEMPERATURE,
            FREQUENCY_PENALTY,
            PRESENCE_PENALTY, MAX_TOKENS, USER_OPTION_NUMBER));
  }

  public LLMResponseWithOptions respondTo(String conversationId, String input) {
    if (conversations.containsKey(conversationId)) {
      return conversations.get(conversationId).respondTo(input);
    } else
      return null;
  }

  public void extendOptions(String conversationId, String input, LLMResponseWithOptions responseWithOptions) {
    if (conversations.containsKey(conversationId)) {
      conversations.get(conversationId).extendOptions(responseWithOptions);
    } else {
      throw new RuntimeException("No conversation with id " + conversationId);
    }

  }

}
