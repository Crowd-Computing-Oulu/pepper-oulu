package fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.llm_service.config;

public class ApiConfig {

  public static final String API_TOKEN = "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";

  static {
    assert !API_TOKEN.equals("add your own token here") : "You must specify your OpenAI API token here!";
  }

}