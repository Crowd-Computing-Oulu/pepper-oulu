package fi.oulu.danielszabo.pepper.applications.gpt_prototype.llm_service.prompts;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import fi.oulu.danielszabo.pepper.applications.gpt_prototype.llm_service.gpt3_conversation.ConversationHistory;

public class PromptTemplateManager {

  private static ClassLoaderTemplateResolver resolver;

  public PromptTemplateManager() {
    resolver = new ClassLoaderTemplateResolver();
    resolver.setTemplateMode(TemplateMode.TEXT);
    resolver.setCharacterEncoding("UTF-8");
    resolver.setPrefix("/prompts/");
    resolver.setSuffix(".prompt");
  }

  public String getResponsePrompt(
      String robotName,
      String userName,
      ConversationHistory conversationHistory) {

    Context context = new Context();

    context.setVariable("robot_name", robotName);
    context.setVariable("user_name", userName);
    context.setVariable("conversation_history", conversationHistory);

    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);

    return templateEngine.process("response_base", context).trim();

  }

  public String getOptionsPrompt(
      String robotName,
      String userName,
      ConversationHistory conversationHistory) {

    Context context = new Context();

    context.setVariable("robot_name", robotName);
    context.setVariable("user_name", userName);
    context.setVariable("conversation_history", conversationHistory);

    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);

    return templateEngine.process("options_base", context).trim();

  }

}
