package edu.iot.cw.services;


import edu.iot.cw.data.dtos.Prediction;
import edu.iot.cw.data.model.Measurement;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.regression.GeneralizedLinearRegression;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        List<Float> tmpList = measurements.stream().parallel()
                .map(Measurement::getTemperature)
                .collect(Collectors.toList());
        List<Float> humList = measurements.stream().parallel()
                .map(Measurement::getHumidity)
                .collect(Collectors.toList());
        Dataset<Float> tmpDataset = sparkSession.createDataset(tmpList, Encoders.FLOAT());
        Dataset<Float> humDataset = sparkSession.createDataset(humList, Encoders.FLOAT());

        GeneralizedLinearRegression glr = new GeneralizedLinearRegression()
                .setFamily("gaussian")
                .setLink("identity")
                .setMaxIter(10)
                .setRegParam(0.3);

        return new Prediction();
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
