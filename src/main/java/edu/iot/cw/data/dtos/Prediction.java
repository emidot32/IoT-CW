package edu.iot.cw.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {
    @JsonProperty("tmp_prediction")
    List<Float> tmpPrediction;

    @JsonProperty("hum_prediction")
    List<Float> humPrediction;

}
