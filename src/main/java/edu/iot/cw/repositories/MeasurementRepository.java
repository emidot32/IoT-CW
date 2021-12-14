package edu.iot.cw.repositories;

import edu.iot.cw.data.model.Measurement;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends CassandraRepository<Measurement, Long> {
    @Query(value = "select max(mes_timestamp) from measurements;")
    Optional<Date> getMaxDate();

    @AllowFiltering
    List<Measurement> findByDeviceId(String deviceId);

    @AllowFiltering
    List<Measurement> findByDeviceIdAndMesTimestampBetween(String deviceId, Date startDate, Date finishDate);

    @AllowFiltering
    List<Measurement> findByDeviceIdAndMesTimestampBefore(String deviceId, Date finishDate);

    @AllowFiltering
    List<Measurement> findByDeviceIdAndMesTimestampAfter(String deviceId, Date startDate);
}
