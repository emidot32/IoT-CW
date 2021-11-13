package edu.iot.cw.services;

import edu.iot.cw.data.dtos.MeasurementValues;
import edu.iot.cw.data.model.Measurement;
import edu.iot.cw.exceptions.BigDataRuntimeException;
import edu.iot.cw.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static edu.iot.cw.data.Constants.DATETIME_FORMAT;
import static edu.iot.cw.data.Constants.ONLY_HOUR_FORMAT;

@Service
public class CassandraService {
    @Autowired
    MeasurementRepository measurementRepository;

    public List<Measurement> getMeasurements(Date startDate, Date finishDate, String hour) {
        return measurementRepository.findAll().stream().parallel()
                .filter(measurement -> isMesDateInRange(startDate, finishDate, measurement))
                .filter(measurement -> isMesDateHourEqual(hour, measurement))
                .collect(Collectors.toList());
    }

    public List<Measurement> getMeasurementsForLastDays(int daysForDataset, String hour) {
        Date finishDate = measurementRepository.getMaxDate().orElse(new Date());
        LocalDateTime ldtFinishDate = LocalDateTime.ofInstant(finishDate.toInstant(), ZoneId.systemDefault());
        Date startDate = Date.from(ldtFinishDate.minusDays(daysForDataset).atZone(ZoneId.systemDefault()).toInstant());
        return getMeasurements(startDate, finishDate, hour);
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
        return ResponseEntity.ok("Measurements are saved");
    }

    private boolean isMesDateInRange(Date startDate, Date finishDate, Measurement measurement) {
        if (startDate != null && finishDate != null) {
            return measurement.getMesTimestamp().after(startDate)
                    && measurement.getMesTimestamp().before(finishDate);
        } else if (startDate == null && finishDate != null) {
            return measurement.getMesTimestamp().before(finishDate);
        } else if (startDate != null && finishDate == null) {
            return measurement.getMesTimestamp().after(startDate);
        }
        return true;
    }

    private boolean isMesDateHourEqual(String hour, Measurement measurement) {
        return hour == null || ONLY_HOUR_FORMAT.format(measurement.getMesTimestamp()).equals(hour);
    }

    private Measurement getMeasurement(List<String> measurementValue) {
        try {
            return Measurement.builder()
                    .deviceId(measurementValue.get(0))
                    .temperature(Float.valueOf(measurementValue.get(1)))
                    .humidity(Float.valueOf(measurementValue.get(2)))
                    .mesTimestamp(DATETIME_FORMAT.parse(measurementValue.get(3)))
                    .build();
        } catch (ParseException e) {
            throw new BigDataRuntimeException(e.getMessage());
        }
    }

}
