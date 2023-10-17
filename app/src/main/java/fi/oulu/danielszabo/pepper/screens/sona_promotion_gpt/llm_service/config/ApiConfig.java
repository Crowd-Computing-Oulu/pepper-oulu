package fi.oulu.danielszabo.pepper.screens.sona_promotion_gpt.llm_service.config;

public class ApiConfig {
  public static final String API_TOKEN = "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";

  static {
    assert !API_TOKEN.equals("add your own token here") : "You must specify your OpenAI API token here!";
  }

}

