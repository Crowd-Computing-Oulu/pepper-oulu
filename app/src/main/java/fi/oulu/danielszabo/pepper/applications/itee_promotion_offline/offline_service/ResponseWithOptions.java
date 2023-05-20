package fi.oulu.danielszabo.pepper.applications.itee_promotion_offline.offline_service;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ResponseWithOptions {

  private final String responseText;

  @Setter
  private String[] options;

  private String[][] phraseSets;

  private final long timestamp;

  public ResponseWithOptions(String responseText, String[][] phraseSets, String... options) {

    this.responseText = responseText;
    this.phraseSets = phraseSets;
    this.options = options;

    timestamp = new Date().getTime();
  }

}
