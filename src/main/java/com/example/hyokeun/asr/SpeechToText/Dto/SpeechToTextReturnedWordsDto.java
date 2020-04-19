package com.example.hyokeun.asr.SpeechToText.Dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class SpeechToTextReturnedWordsDto {
    private int confidence;
    private String extHypothesis;
    private String hypothesis;
    private List<String> word;
    @SerializedName("final")
    private boolean final_;
    
    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
    public void setExtHypothesis(String extHypothesis) {
        this.extHypothesis = extHypothesis;
    }
    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }
    public void setWord(List<String> word) {
        this.word = word;
    }
    public void setFinal(boolean final_) {
        this.final_ = final_;
    }
}
