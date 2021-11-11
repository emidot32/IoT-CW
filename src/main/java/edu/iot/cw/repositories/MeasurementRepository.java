package edu.iot.cw.repositories;

import edu.iot.cw.data.model.Measurement;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends CassandraRepository<Measurement, Long> {
}
