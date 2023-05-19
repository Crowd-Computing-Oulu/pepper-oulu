package fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service.gpt3_conversation;

import java.util.ArrayList;
import java.util.List;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service.prompts.PromptTemplateManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Conversation {

  private final ConversationHistory convHist = new ConversationHistory();
  private final PromptTemplateManager ptm;
  private final OpenAiService openAiService;

  private final String userName, robotName, model;
  private final double responseTemperature, optionsTemperature, frequencyPenalty, presencePenalty;
  private final int maxTokens, userOptionNumber;

  public LLMResponseWithOptions respondTo(String input) {

    convHist.add(userName, input);

    ArrayList<String> stopList = new ArrayList<>();
    stopList.add(userName + ":");
    stopList.add(robotName + ":");

    CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt(ptm.getResponsePrompt(robotName, userName, convHist))
        .model(model)
        .echo(false)
        .maxTokens(maxTokens)
        .temperature(responseTemperature)
        .stop(stopList)
        .frequencyPenalty(frequencyPenalty)
        .presencePenalty(presencePenalty)
        .build();

    CompletionChoice completionChoice = openAiService.createCompletion(completionRequest).getChoices().get(0);

    String responseString = completionChoice.getText().replace("\n", " ").trim();

    convHist.add(robotName, responseString);

    String[] responseOptions = getUserOptions(userOptionNumber);

    return new LLMResponseWithOptions(responseString, responseOptions);
  }

  public void extendOptions(LLMResponseWithOptions responseWithOptions) {

    String[] currentOptions = responseWithOptions.getOptions();

    String[] extendedOptions = new String[currentOptions.length + 5];
    for (int i = 0; i < currentOptions.length; i++) {
      extendedOptions[i] = currentOptions[i];
    }
    String[] newOptions = getUserOptions(5);
    for (int i = currentOptions.length; i < extendedOptions.length; i++) {
      extendedOptions[i] = newOptions[i - currentOptions.length];
    }

    responseWithOptions.setOptions(extendedOptions);

  }

  private String[] getUserOptions(int n) {

    ArrayList<String> stopList = new ArrayList<>();
    stopList.add(userName + ":");
    stopList.add(robotName + ":");
    stopList.add(".");
    stopList.add("?");

    CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt(ptm.getOptionsPrompt(robotName, userName, convHist))
        .model(model)
        .n(n)
        .echo(false)
        .maxTokens(maxTokens)
        // the temperature for options should always be high, we want very
        // different options
        .temperature(optionsTemperature)
        .stop(stopList)
        .frequencyPenalty(frequencyPenalty)
        .presencePenalty(presencePenalty)
        .build();

    String[] options = new String[n];

    List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();

    for (int i = 0; i < n; i++) {
      options[i] = choices.get(i).getText().trim();
    }

    return options;
  }

}
