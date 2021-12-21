package edu.iot.cw.services;

import edu.iot.cw.data.Constants;
import edu.iot.cw.data.model.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TestDataService {

    @Autowired
    CassandraService cassandraService;

    private static Map<Integer, Double[]> timeTempHumMap = new HashMap<>() {{
        put(0, new Double[]{15.0, 63.0});
        put(1, new Double[]{15.0, 62.0});
        put(2, new Double[]{14.5, 61.0});
        put(3, new Double[]{14.1, 60.0});
        put(4, new Double[]{13.7, 59.0});
        put(5, new Double[]{14.0, 58.0});
        put(6, new Double[]{14.3, 60.0});
        put(7, new Double[]{14.8, 63.0});
        put(8, new Double[]{15.4, 66.0});
        put(9, new Double[]{16.1, 69.0});
        put(10, new Double[]{16.5, 72.0});
        put(11, new Double[]{17.2, 75.0});
        put(12, new Double[]{17.4, 77.0});
        put(13, new Double[]{17.9, 79.0});
        put(14, new Double[]{18.4, 81.0});
        put(15, new Double[]{19.0, 81.0});
        put(16, new Double[]{19.7, 84.0});
        put(17, new Double[]{20.1, 86.0});
        put(18, new Double[]{19.0, 83.0});
        put(19, new Double[]{18.5, 77.0});
        put(20, new Double[]{17.9, 73.0});
        put(21, new Double[]{16.8, 69.0});
        put(22, new Double[]{16.3, 67.0});
        put(23, new Double[]{15.6, 64.0});
    }};

    public void createTestData(int deviceNum) throws ParseException {
    	int day = 0;
		Random random = new Random();
        for (LocalDate ld: getDateRange(LocalDate.now().minusDays(2), 2)) {
			for (int i = 0; i < 24; i++) {
				double oldTemp = (timeTempHumMap.get(i)[0]-4)/5;
				double oldHum = (timeTempHumMap.get(i)[1]-13)/5;
				float temp = (float) (timeTempHumMap.get(i)[0] - (1 + (oldTemp - 1)) * random.nextDouble() - day*0.15);
				float hum = (float) (timeTempHumMap.get(i)[1] - ((1 + (oldHum - 1)*random.nextDouble())));
				Date mesDate = Constants.DATETIME_FORMAT.parse(
						String.format("%s %02d:01:01", ld.toString(), i));
				Measurement measurement = Measurement.builder()
						.deviceId(""+random.nextInt(deviceNum))
						.temperature(temp)
						.humidity(hum)
						.mesTimestamp(mesDate)
						.build();
//				System.out.println(measurement);
				cassandraService.saveMeasurement(measurement);
				if (random.nextDouble() < 0.4) {
					mesDate = Constants.DATETIME_FORMAT.parse(
							String.format("%s %02d:31:01", ld.toString(), i));
					cassandraService.saveMeasurement(
							Measurement.builder()
							.deviceId(""+random.nextInt(deviceNum))
							.temperature((float) (temp+0.5))
							.humidity(hum+1)
							.mesTimestamp(mesDate)
							.build());
				}
			}
			day++;
		}

    }

	public static List<LocalDate> getDateRange(LocalDate startDate, int days) {
		return IntStream.iterate(0, i -> i + 1)
		  .limit(days)
		  .mapToObj(startDate::plusDays)
		  .collect(Collectors.toList());
	}
}
