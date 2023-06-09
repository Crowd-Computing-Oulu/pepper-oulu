package fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.llm_service.gpt35turbo_conversation;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class LLMResponseWithOptions {

  private final String responseText;

  @Setter
  private String[] options;

  private String[][] phraseSets;
  private final long timestamp;

  public LLMResponseWithOptions(String responseText, String... options) {

    this.responseText = responseText;
    this.options = options;

    this.phraseSets = new String[options.length][1];

    for (int i = 0; i < options.length; i++) {
      this.phraseSets[i][0] = this.options[i];
    }

    timestamp = new Date().getTime();
  }


}
