package edu.iot.cw.controller;

import edu.iot.cw.model.Measurement;
import edu.iot.cw.service.CassandraService;
import edu.iot.cw.service.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iot/cw/big-data/api")
public class MainController {

    @Autowired
    SparkService sparkService;

    @Autowired
    CassandraService cassandraService;

    @PostMapping("/measurement")
    public void createMeasurement(@RequestBody Measurement measurement) {
        cassandraService.createMeasurement(measurement);
    }

    @GetMapping("/measurements")
    public List<Measurement> getAllMeasurements() {
        return cassandraService.getAllMeasurements();
    }

}
