package fi.oulu.danielszabo.pepper_base.itee_promotion_offline.offline_service;

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

