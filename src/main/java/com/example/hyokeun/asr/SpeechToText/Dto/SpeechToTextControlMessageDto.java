package com.example.hyokeun.asr.SpeechToText.Dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpeechToTextControlMessageDto {

    private boolean wakeup;
    @SerializedName("wakeup-plm-path")
    private String wakeupPlmPath;
    @SerializedName("plm-path")
    private String plmPath;
    @SerializedName("plm-weight")
    private double plmWeight;
    @SerializedName("enroll-path")
    private String enrollPath;
    @SerializedName("norm-para-path")
    private String normParaPath;
    private boolean ns;
    @SerializedName("data-path")
    private String dataPath;
    private String GPSXY;
}
