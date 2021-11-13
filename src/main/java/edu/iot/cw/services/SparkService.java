package edu.iot.cw.services;


import edu.iot.cw.data.Constants;
import edu.iot.cw.data.Values;
import edu.iot.cw.data.dtos.Prediction;
import edu.iot.cw.data.model.Measurement;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.regression.GeneralizedLinearRegression;
import org.apache.spark.ml.regression.GeneralizedLinearRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SparkService {
    @Autowired
    CassandraService cassandraService;

    @Autowired
    JavaSparkContext sparkContext;

    @Autowired
    SparkSession sparkSession;

    @Value("${daysForDataset}")
    int daysForDataset;

    public Prediction getPrediction(int days, String hour) {
        List<Measurement> measurements = cassandraService.getMeasurementsForLastDays(daysForDataset, hour);
        return Prediction.builder()
                .tmpPrediction(getPrediction(measurements, days, hour, Values.TEMPERATURE))
                .humPrediction(getPrediction(measurements, days, hour, Values.HUMIDITY))
                .build();
    }

    private List<Float> getPrediction(List<Measurement> measurements, int days, String hour, Values value) {
        List<Float> listOfValues = getDatasetForPrediction(measurements, value, hour != null);
        Dataset<Float> dataset = sparkSession.createDataset(listOfValues, Encoders.FLOAT());

        GeneralizedLinearRegression glr = new GeneralizedLinearRegression()
                .setFamily("gaussian")
                .setLink("identity")
                .setMaxIter(10)
                .setRegParam(0.3);
        GeneralizedLinearRegressionModel model = glr.fit(dataset);

        return model.transform(dataset).select("prediction").collectAsList().stream()
                .limit(days)
                .map(row -> (Float) row.get(0))
                .collect(Collectors.toList());
    }

    private List<Float> getDatasetForPrediction(List<Measurement> measurements, Values value, boolean hourExists) {
        return measurements.stream().parallel()
                .collect(Collectors.groupingBy(
                        measurement -> getFormatForGroupingBy(hourExists).format(measurement.getMesTimestamp()),
                        Collectors.averagingDouble(measurement -> getNeededValue(value, measurement))))
                .values().stream()
                .map(Float.class::cast)
                .collect(Collectors.toList());
    }

    private SimpleDateFormat getFormatForGroupingBy(boolean hourExists) {
        return hourExists ? Constants.DATE_FORMAT : Constants.DATE_HOUR_FORMAT;

    }

    private Float getNeededValue(Values value, Measurement measurement) {
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
