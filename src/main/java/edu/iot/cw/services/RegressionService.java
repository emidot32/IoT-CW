package edu.iot.cw.services;

import edu.iot.cw.data.Constants;
import edu.iot.cw.data.Values;
import edu.iot.cw.data.dtos.Prediction;
import edu.iot.cw.data.model.Measurement;
import edu.iot.cw.exceptions.BigDataRuntimeException;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegressionService extends SparkService {

    @Value("${daysForDataset}")
    int daysForDataset;

    public Prediction getPrediction(int days, String hour) {
        List<Measurement> measurements = cassandraService.getMeasurementsForLastDays(daysForDataset, hour).stream()
                .sorted(Comparator.comparing(Measurement::getMesTimestamp))
                .collect(Collectors.toList());
        return Prediction.builder()
                .tmpPrediction(getPrediction(measurements, days, hour, Values.TEMPERATURE))
                .humPrediction(getPrediction(measurements, days, hour, Values.HUMIDITY))
                .build();
    }

    private List<Float> getPrediction(List<Measurement> measurements, int days, String hour, Values value) {

        Dataset<Row> dataset = getDataset(measurements, hour, value);
        return Optional.ofNullable(dataset)
                .map(this::trainModel)
                .map(models -> models.transform(dataset).select("prediction"))
                .map(model -> model.collectAsList().stream()
                    .limit(days)
                    .map(row -> ((Double) row.get(0)).floatValue())
                    .collect(Collectors.toList()))
                .orElseThrow(() -> new BigDataRuntimeException("Prediction is impossible"));
    }

    private Dataset<Row> getDataset(List<Measurement> measurements, String hour, Values value) {
        List<Float> list = getDatasetForPrediction(measurements, value, hour != null);
        List<Row> listOfValues = list.stream()
                .map(RowFactory::create)
                .collect(Collectors.toList());
        StructType schema = new StructType(
            new StructField[] {
                new StructField("value", DataTypes.FloatType, false, Metadata.empty())
        });
        Dataset<Row> dataset = sparkSession.createDataFrame(listOfValues, schema);

        return Optional.ofNullable(dataset)
            //.map(this::filterData)
            //.map(this::castColumns)
            .map(this::assembleColumns)
            //.map(this::normalizeColumns)
            .orElseThrow(() -> new BigDataRuntimeException("Dataset in null"));
    }

    private Dataset<Row> assembleColumns(Dataset<Row> dataset) {
        return new VectorAssembler()
                .setInputCols(new String[]{"value"})
                .setOutputCol("features")
                .setHandleInvalid("skip")
                .transform(dataset);
    }

    private LinearRegressionModel trainModel(Dataset<Row> dataset) {
        return new LinearRegression()
                .setFeaturesCol("features")
                .setLabelCol("value")
                .setMaxIter(15)
                .setRegParam(0.5)
                .fit(dataset);
    }

    private List<Float> getDatasetForPrediction(List<Measurement> measurements, Values value, boolean hourExists) {
        return measurements.stream().parallel()
                .collect(Collectors.groupingBy(
                        measurement -> getFormatForGroupingBy(hourExists).format(measurement.getMesTimestamp()),
                        Collectors.averagingDouble(measurement -> getNeededValue(value, measurement))))
                .values().stream()
                .map(Double::floatValue)
                .collect(Collectors.toList());
    }

    private SimpleDateFormat getFormatForGroupingBy(boolean hourExists) {
        return hourExists ? Constants.DATE_FORMAT : Constants.DATE_HOUR_FORMAT;

    }

}
