package fi.oulu.danielszabo.pepper.applications.pepper_study_promotion.offline_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    String role;
    String content;
}

