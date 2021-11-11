package edu.iot.cw.controllers;

import edu.iot.cw.data.dtos.MeasurementValues;
import edu.iot.cw.data.model.Measurement;
import edu.iot.cw.services.CassandraService;
import edu.iot.cw.services.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iot/cw/big-data-service/api/measurements")
public class MeasurementsController {

    @Autowired
    SparkService sparkService;

    @Autowired
    CassandraService cassandraService;

    @PostMapping("/measurement")
    public void saveMeasurement(@RequestBody Measurement measurement) {
        cassandraService.saveMeasurement(measurement);
    }

    @PostMapping
    public ResponseEntity<String> saveMeasurements(@RequestBody MeasurementValues measurements) {
        return cassandraService.saveMeasurements(measurements);
    }

    @GetMapping
    public List<Measurement> getAllMeasurements() {
        return cassandraService.getAllMeasurements();
    }

}
