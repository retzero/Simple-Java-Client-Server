package com.example.hyokeun.asr.SpeechToText.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessageDto {
    private int type;
    private String retText;
}
