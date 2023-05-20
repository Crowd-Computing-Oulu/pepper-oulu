package fi.oulu.danielszabo.pepper.applications.sona_promotion_gpt.llm_service.gpt3_conversation;

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
  private final long timestamp;

  public LLMResponseWithOptions(String responseText, String... options) {

    this.responseText = responseText;
    this.options = options;

    timestamp = new Date().getTime();
  }

}
