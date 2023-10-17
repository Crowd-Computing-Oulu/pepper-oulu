package fi.oulu.danielszabo.pepper.screens.conv_demo.tree_conv_service;

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

  public String[] getOptions() {
    return options;
  }

  public String[][] getPhraseSets() {
      return phraseSets;
  }

  public String getResponseText() {
    return responseText;
  }
}
