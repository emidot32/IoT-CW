package edu.iot.cw.controllers;

import edu.iot.cw.data.dtos.MeanValues;
import edu.iot.cw.data.dtos.Prediction;
import edu.iot.cw.services.CassandraService;
import edu.iot.cw.services.RegressionService;
import edu.iot.cw.services.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/iot/cw/big-data-service/api/statistics")
public class StatisticsController {

    @Autowired
    SparkService sparkService;

    @Autowired
    RegressionService regressionService;

    @Autowired
    CassandraService cassandraService;

    @GetMapping("/prediction/{deviceId}")
    public Prediction getPrediction(@PathVariable String deviceId,
                                    @RequestParam int days,
                                    @RequestParam(name="hour", required = false) String hour) {
        return regressionService.getPrediction(deviceId, days, hour);
    }

    @GetMapping("/mean")
    public MeanValues getMeanValues(@RequestParam(name="start_date", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                    @RequestParam(name="finish_date", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finishDate,
                                    @RequestParam(name="hour", required = false) String hour) {
        return sparkService.getMeanValues(null, startDate, finishDate, hour);
    }

    @GetMapping("/mean/{deviceId}")
    public MeanValues getMeanValues(@PathVariable String deviceId,
                                    @RequestParam(name="start_date", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                    @RequestParam(name="finish_date", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finishDate,
                                    @RequestParam(name="hour", required = false) String hour) {
        return sparkService.getMeanValues(deviceId, startDate, finishDate, hour);
    }
}
