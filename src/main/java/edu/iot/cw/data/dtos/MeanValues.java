package edu.iot.cw.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeanValues {
    @JsonProperty("mean_temp")
    private Double meanTemp;

    @JsonProperty("mean_hum")
    private Double meanHum;
}
