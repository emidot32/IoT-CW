package edu.iot.cw.services;


import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SparkService {
    @Autowired
    CassandraService cassandraService;

    @Autowired
    JavaSparkContext sc;

}
