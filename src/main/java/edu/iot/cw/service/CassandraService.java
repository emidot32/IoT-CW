package edu.iot.cw.service;

import edu.iot.cw.model.Measurement;
import edu.iot.cw.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CassandraService {
    @Autowired
    MeasurementRepository measurementRepository;

    public void createMeasurement(Measurement measurement) {
        measurement.setId(UUID.randomUUID());
        if (measurement.getMesTimestamp() == null) {
            measurement.setMesTimestamp(new Date());
        }
        measurementRepository.insert(measurement);
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }
}
