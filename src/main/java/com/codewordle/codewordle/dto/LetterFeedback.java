package com.codewordle.codewordle.dto;
import com.codewordle.codewordle.model.FeedbackStatus;
import lombok.*;
@Getter @AllArgsConstructor
public class LetterFeedback {
    private char letter;
    private FeedbackStatus status;
}