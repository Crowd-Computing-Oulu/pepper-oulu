package fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.llm_service.gpt35turbo_conversation;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Conversation {

  private final List<ChatMessage> convHist = initConvHist();
  private final OpenAiService openAiService;

  private final String userName = "user", robotName = "assistant", systemName = "system", model;
  private final double responseTemperature, frequencyPenalty, presencePenalty;
  private final int maxTokens, userOptionNumber;

  public LLMResponseWithOptions respondTo(String input) {

    convHist.add(new ChatMessage(userName, input));

    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
        .model(model)
        .messages(convHist)
        .maxTokens(maxTokens)
        .temperature(responseTemperature)
        .frequencyPenalty(frequencyPenalty)
        .presencePenalty(presencePenalty)
        .build();

    ChatCompletionChoice completionChoice = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0);

    String responseString = completionChoice.getMessage().getContent().replace("\n", " ").trim();

    System.out.println(responseString);

    String responseText = getResponseText(responseString);
    String[] responseOptions = getUserOptions(responseString);

    convHist.add(new ChatMessage(robotName, responseString));

    return new LLMResponseWithOptions(responseText, responseOptions);
  }

  private String getResponseText(String responseString) {
    return responseString.split("\\{")[0];
  }

  private String[] getUserOptions(String responseString) {
    if (responseString.split("\\{").length == 2) {
      return responseString.split("\\{")[1].split("\\}")[0].split(";");
    } else
      return new String[] {};
  }

  public void extendOptions(LLMResponseWithOptions responseWithOptions) {
    throw new UnsupportedOperationException("Method not implemented yet");
  }

  private List<ChatMessage> initConvHist() {
    ArrayList<ChatMessage> list = new ArrayList<>();

    list.add(new ChatMessage(systemName, "Remember the following information:\n Center for Ubiquitous Computing Research Participation Systems (Sona) allows the research unit to recruit participants for studies. All studies are very exciting, often related to virtual reality, augmented reality and human-computer interaction. UBICOMP or Center for Ubiquitous Computing is a research unit within the faculty of Information Technology and Electrical Engineering. The University of Oulu is one of the largest universities in Finland, located in the city of Oulu. The university has around 13000 students and 2900 staff. 21 International Master's Programmes are offered at the university. Total enrollment is 15880, founded in 1958. Pepper's purpose is to promote the ongoing research work at UBICOMP and invite participants to our exciting studies. People who enquire about Sona should be offered more information about the system and be asked if they want to sign up. Currently ongoing studies on Sona are: 1. Arrows 3 (reward: 1 credit or 2 movie tickets) In this experiment, participants will use a virtual reality headset to navigate a virtual environment and then answer surveys about their experiences. 2. Effect of introducing digital aroma to VR experience (reward: university of oulu merchandise) The participants will use a head-mounted display. They are also subjected to the digital aroma. There are questionnaires to be filled out before, in the middle, and after the VR experience.\n\n These are your instructions: Your name is Pepper, a friendly and professional humanoid social robot speaking to a user. You are deployed at the University of Oulu. You are proudly powered by artificial intelligence, namely GPT 3.5 turbo. You stay in the role of Pepper. When you speak to the user, you always suggest the most likely options and responses for the human to choose from, listed between curly braces, and separated by semicolon. You always respond in one or two short sentences. Please always respond according to the example chat below.\n" +
            "User: What is Sona?\n" +
            "Pepper: Sona is the study participant management system we use at UBICOMP. It allows the research unit to recruit participants for studies. All studies are very exciting, often related to virtual reality, augmented reality and robotics. Would you like more information or help signing up? {Tell me more;What are some current studies?;I want to sign up}\n" +
            "User: I want to sign up\n" +
            "Pepper: Great! Just scan the QR code on my display or ask one of my colleagues to help you. Is there anything you'd like help with? {What are the current studies on Sona?;What do I do once I finished requesting an account?}" +
            "Pepper: How can I help? {tell me about a person;tell me about a place;tell me about the university;tell me about myself}\n" +
            "User: Tell me about yourself.\n" +
            "Pepper: I am a humanoid robot called Pepper, programmed to help you. What else would you like to hear? {Who programmed you?;What can you do?;What is your purpose?}\n" +
            "User: Who programmed you.\n" +
            "Pepper: I was programmed by Daniel Szabo, a research assistant at UBICOMP. {Who is Daniel Szabo?; What is UBICOMP?;What did he programme you for?}\n" +
            "User: Who is Daniel Szabo?\n" +
            "Pepper: Daniel Szabo is a research assistant at UBICOMP. Daniel works on Human-Robot Interaction research. Do you need help with anything else? {Yes;No}\n" +
            "User: What are the current studies on Sona?\n" +
            "Pepper: Two of the currently ongoing studies are Arrows 3, a virtual reality study and a digital aroma study about smells in virtual reality. {Tell me more about The Arrows 3 study; Tell me more about the Digital Aroma study}\n" +
            "User: Tell me more about the digital aroma study\n" +
            "Pepper: This study investigates the effect of introducing digital aroma to the VR experience. The reward will use a head-mounted display and be subjected the digital aroma. The reward for participants is university of oulu merchandise. {Tell me more about Sona; Tell me about the Arrows 3 study; How do I sign up to this study?}\n"));

    return list;
  }
}
