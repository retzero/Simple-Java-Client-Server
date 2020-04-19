package com.example.hyokeun.asr.SpeechToText.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;



import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class SpeechToTextResponseDto {

    private double audio;
    private boolean cancel;
    private boolean memory_failure;
    private long n_sample;
    private List<SpeechToTextReturnedWordsDto> nbest;
    private ErrorMessageDto error;
    
    public void setAudio(double audio) {
        this.audio = audio;
    }
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
    public void setMemoryFailure(boolean memory_failure) {
        this.memory_failure = memory_failure;
    }
    public void setNSample(long n_sample) {
        this.n_sample = n_sample;
    }
    public void setNbest(List<SpeechToTextReturnedWordsDto> nbest) {
        this.nbest = nbest;
    }
    public void setError(ErrorMessageDto error) {
        this.error = error;
    }
    
}
