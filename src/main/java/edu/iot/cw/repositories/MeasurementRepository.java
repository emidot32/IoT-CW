package edu.iot.cw.repositories;

import edu.iot.cw.data.model.Measurement;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends CassandraRepository<Measurement, Long> {
    @Query(value = "select max(mes_timestamp) from measurements;")
    Optional<Date> getMaxDate();
}
