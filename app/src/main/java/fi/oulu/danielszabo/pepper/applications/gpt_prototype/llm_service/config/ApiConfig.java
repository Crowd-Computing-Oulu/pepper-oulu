package fi.oulu.danielszabo.pepper.applications.gpt_prototype.llm_service.config;

public class ApiConfig {

  public static final String API_TOKEN = "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";

  static {
    assert !API_TOKEN.equals("add your own token here") : "You must specify your OpenAI API token here!";
  }

}
