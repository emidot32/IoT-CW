package edu.iot.cw.services;


import edu.iot.cw.data.Values;
import edu.iot.cw.data.model.Measurement;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SparkService implements Serializable {
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


//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);
//
//    public ResponseEntity<String> saveMeasurements(MeasurementValues measurementValues) {
//        JavaRDD<List<String>> measurementsRDD = sc.parallelize(measurementValues.getValues());
//        measurementsRDD
//                  .map(this::getMeasurement)
//                  .foreach(measurement -> cassandraService.saveMeasurement(measurement));
//        return ResponseEntity.ok("Measurements are saved");
//    }
//
//    private Measurement getMeasurement(List<String> measurementValue) {
//        try {
//            return Measurement.builder()
//                    .deviceId(measurementValue.get(0))
//                    .temperature(Float.valueOf(measurementValue.get(1)))
//                    .humidity(Float.valueOf(measurementValue.get(2)))
//                    .mesTimestamp(simpleDateFormat.parse(measurementValue.get(3)))
//                    .build();
//        } catch (ParseException e) {
//            throw new BigDataRuntimeException(e.getMessage());
//        }
//    }

}
