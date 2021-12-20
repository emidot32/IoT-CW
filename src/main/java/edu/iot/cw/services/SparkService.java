package edu.iot.cw.services;


import edu.iot.cw.data.Values;
import edu.iot.cw.data.dtos.MeanValues;
import edu.iot.cw.data.model.Measurement;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SparkService {
    @Autowired
    CassandraService cassandraService;

    @Autowired
    JavaSparkContext sparkContext;

    @Autowired
    SparkSession sparkSession;


    protected Float getNeededValue(Values value, Measurement measurement) {
        return Values.TEMPERATURE.equals(value)
                ? measurement.getTemperature()
                : measurement.getHumidity();
    }

    public MeanValues getMeanValues(String deviceId, Date startDate, Date finishDate, String hour) {
        List<Measurement> measurements = cassandraService.getMeasurements(deviceId, startDate, finishDate, hour);
        return MeanValues.builder()
                .meanTemp(getMeanValue(measurements, Values.TEMPERATURE))
                .meanHum(getMeanValue(measurements, Values.HUMIDITY))
                .build();
    }

    private Double getMeanValue(List<Measurement> measurements, Values neededValue) {
        return measurements.stream().parallel()
                .mapToDouble(measurement -> getNeededValue(neededValue, measurement))
                .average().orElse(0.0);
    }

}
