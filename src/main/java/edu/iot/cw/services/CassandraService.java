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
import java.util.*;
import java.util.stream.Collectors;

import static edu.iot.cw.data.Constants.DATETIME_FORMAT;
import static edu.iot.cw.data.Constants.ONLY_HOUR_FORMAT;

@Service
public class CassandraService {
    @Autowired
    MeasurementRepository measurementRepository;

    final Map<String, MeasurementsGettingAction> METHODS_MAP = new HashMap<>() {{
        put("000", (deviceId, startDate, finishDate) -> measurementRepository.findAll());
        put("011", (deviceId, startDate, finishDate) -> measurementRepository.findByMesTimestampBetween(startDate, finishDate));
        put("001", (deviceId, startDate, finishDate) -> measurementRepository.findByMesTimestampBefore(finishDate));
        put("010", (deviceId, startDate, finishDate) -> measurementRepository.findByMesTimestampAfter(startDate));
        put("100", (deviceId, startDate, finishDate) -> measurementRepository.findByDeviceId(deviceId));
        put("111", (deviceId, startDate, finishDate) -> measurementRepository.findByDeviceIdAndMesTimestampBetween(deviceId, startDate, finishDate));
        put("101", (deviceId, startDate, finishDate) -> measurementRepository.findByDeviceIdAndMesTimestampBefore(deviceId, finishDate));
        put("110", (deviceId, startDate, finishDate) -> measurementRepository.findByDeviceIdAndMesTimestampAfter(deviceId, startDate));
    }};
    

    public List<Measurement> getMeasurements(String deviceId, Date startDate, Date finishDate) {
        String condition = isNotNullToString(deviceId) + isNotNullToString(startDate) + isNotNullToString(finishDate);
        return METHODS_MAP.get(condition).get(deviceId, startDate, finishDate);
    }

    public List<Measurement> getMeasurements(String deviceId, Date startDate, Date finishDate, String hour) {
        return getMeasurements(deviceId, startDate, finishDate).stream()
                .filter(measurement -> isMesDateHourEqual(hour, measurement))
                .collect(Collectors.toList());
    }

    public List<Measurement> getMeasurementsForLastDays(String deviceId, int daysForDataset, String hour) {
        Date finishDate = measurementRepository.getMaxDate().orElse(new Date());
        LocalDateTime ldtFinishDate = LocalDateTime.ofInstant(finishDate.toInstant(), ZoneId.systemDefault());
        Date startDate = Date.from(ldtFinishDate.minusDays(daysForDataset).atZone(ZoneId.systemDefault()).toInstant());
        return getMeasurements(deviceId, startDate, finishDate, hour);
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

    private String isNotNullToString(Object obj) {
        return obj == null ? "0" : "1";
    }
}
