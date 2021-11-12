package edu.iot.cw.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Prediction {
    @JsonProperty("tmp_prediction")
    List<Float> tmpPrediction;

    @JsonProperty("hum_prediction")
    List<Float> humPrediction;

}
