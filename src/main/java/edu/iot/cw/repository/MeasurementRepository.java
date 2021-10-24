package edu.iot.cw.repository;

import edu.iot.cw.model.Measurement;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends CassandraRepository<Measurement, Long> {
}
