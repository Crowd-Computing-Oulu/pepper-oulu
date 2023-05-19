package fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service.config;

public class ApiConfig {

  public static final String API_TOKEN = "add your own token here";

  static {
    assert !API_TOKEN.equals("add your own token here") : "You must specify your OpenAI API token here!";
  }

}
