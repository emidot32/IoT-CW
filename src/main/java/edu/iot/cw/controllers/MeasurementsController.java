package edu.iot.cw.controllers;

import edu.iot.cw.data.dtos.MeasurementValues;
import edu.iot.cw.data.model.Measurement;
import edu.iot.cw.services.CassandraService;
import edu.iot.cw.services.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public List<Measurement> getMeasurements(@RequestParam(name="start_date", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                             @RequestParam(name="finish_date", required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finishDate,
                                             @RequestParam(name="hour", required = false) String hour) {
        return cassandraService.getMeasurements(null, startDate, finishDate, hour);
    }

    @GetMapping("/{deviceId}")
    public List<Measurement> getDeviceMeasurements(@PathVariable String deviceId,
                                                   @RequestParam(name="start_date", required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                   @RequestParam(name="finish_date", required = false)
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finishDate,
                                                   @RequestParam(name="hour", required = false) String hour) {
        return cassandraService.getMeasurements(deviceId, startDate, finishDate, hour);
    }
}
