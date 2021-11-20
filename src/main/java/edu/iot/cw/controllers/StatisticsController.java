package edu.iot.cw.controllers;

import edu.iot.cw.data.dtos.Prediction;
import edu.iot.cw.services.CassandraService;
import edu.iot.cw.services.RegressionService;
import edu.iot.cw.services.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iot/cw/big-data-service/api/statistics")
public class StatisticsController {

    @Autowired
    SparkService sparkService;
    @Autowired
    RegressionService regressionService;

    @Autowired
    CassandraService cassandraService;

    @GetMapping("/prediction")
    public Prediction getPrediction(@RequestParam int days, String hour) {
        return regressionService.getPrediction(days, hour);
    }
}
