package fi.oulu.danielszabo.pepper_base.sona_promotion_gpt.llm_service.gpt3_conversation;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConversationHistoryMessage {

  private String source, content;
  private long timestamp;

}
