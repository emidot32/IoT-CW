package edu.iot.cw.services;

import edu.iot.cw.data.model.Measurement;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@FunctionalInterface
public interface MeasurementsGettingAction extends Serializable {
    List<Measurement> get(String deviceId, Date startDate, Date finishDate);
}
