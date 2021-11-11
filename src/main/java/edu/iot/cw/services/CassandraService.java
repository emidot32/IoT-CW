package edu.iot.cw.services;

import edu.iot.cw.Constants;
import edu.iot.cw.data.dtos.MeasurementValues;
import edu.iot.cw.data.model.Measurement;
import edu.iot.cw.exceptions.BigDataRuntimeException;
import edu.iot.cw.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CassandraService {
    @Autowired
    MeasurementRepository measurementRepository;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public void saveMeasurement(Measurement measurement) {
        measurement.setId(UUID.randomUUID());
        if (measurement.getMesTimestamp() == null) {
            measurement.setMesTimestamp(new Date());
        }
        measurementRepository.insert(measurement);
    }

    public ResponseEntity<String> saveMeasurements(MeasurementValues measurementValues) {
        measurementValues.getValues().stream().parallel()
                .map(this::getMeasurement)
                .forEach(this::saveMeasurement);
        return ResponseEntity.ok("Measurements is saved");
    }

    private Measurement getMeasurement(List<String> measurementValue) {
        try {
            return Measurement.builder()
                    .deviceId(measurementValue.get(0))
                    .temperature(Float.valueOf(measurementValue.get(1)))
                    .humidity(Float.valueOf(measurementValue.get(2)))
                    .mesTimestamp(simpleDateFormat.parse(measurementValue.get(3)))
                    .build();
        } catch (ParseException e) {
            throw new BigDataRuntimeException(e.getMessage());
        }
    }

}
