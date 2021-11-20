package edu.iot.cw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CwApplication 
//	implements CommandLineRunner
	{

    public static void main(String[] args) {
        SpringApplication.run(CwApplication.class, args);
    }

//    private static Map<Integer, Double[]> timeTempHumMap = new HashMap<>();
//    static {
//    	timeTempHumMap.put(0, new Double[]{15.0, 63.0});
//    	timeTempHumMap.put(1, new Double[]{15.0, 62.0});
//    	timeTempHumMap.put(2, new Double[]{14.5, 61.0});
//    	timeTempHumMap.put(3, new Double[]{14.1, 60.0});
//    	timeTempHumMap.put(4, new Double[]{13.7, 59.0});
//    	timeTempHumMap.put(5, new Double[]{14.0, 58.0});
//    	timeTempHumMap.put(6, new Double[]{14.3, 60.0});
//    	timeTempHumMap.put(7, new Double[]{14.8, 63.0});
//    	timeTempHumMap.put(8, new Double[]{15.4, 66.0});
//    	timeTempHumMap.put(9, new Double[]{16.1, 69.0});
//    	timeTempHumMap.put(10, new Double[]{16.5, 72.0});
//    	timeTempHumMap.put(11, new Double[]{17.2, 75.0});
//    	timeTempHumMap.put(12, new Double[]{17.4, 77.0});
//    	timeTempHumMap.put(13, new Double[]{17.9, 79.0});
//    	timeTempHumMap.put(14, new Double[]{18.4, 81.0});
//    	timeTempHumMap.put(15, new Double[]{19.0, 81.0});
//    	timeTempHumMap.put(16, new Double[]{19.7, 84.0});
//    	timeTempHumMap.put(17, new Double[]{20.1, 86.0});
//    	timeTempHumMap.put(18, new Double[]{19.0, 83.0});
//    	timeTempHumMap.put(19, new Double[]{18.5, 77.0});
//    	timeTempHumMap.put(20, new Double[]{17.9, 73.0});
//    	timeTempHumMap.put(21, new Double[]{16.8, 69.0});
//    	timeTempHumMap.put(22, new Double[]{16.3, 67.0});
//    	timeTempHumMap.put(23, new Double[]{15.6, 64.0});
//	}
//
//	@Autowired
//	CassandraService cassandraService;
//
//	@Override
//    public void run(String... args) throws ParseException {
//    	int day = 0;
//		Random random = new Random();
//        for (LocalDate ld: getDateRange(LocalDate.now().minusDays(60), 60)) {
//			for (int i = 0; i < 24; i++) {
//				double oldTemp = timeTempHumMap.get(i)[0]/5;
//				double oldHum = timeTempHumMap.get(i)[1]/5;
//				float temp = (float) (timeTempHumMap.get(i)[0] - (1 + (oldTemp - 1)) * random.nextDouble() - day*0.15);
//				float hum = (float) (timeTempHumMap.get(i)[1] - ((1 + (oldHum - 1)*random.nextDouble())));
//				Date mesDate = Constants.DATETIME_FORMAT.parse(
//						String.format("%s %02d:01:01", ld.toString(), i));
//				Measurement measurement = Measurement.builder()
//						.deviceId(""+random.nextInt(100))
//						.temperature(temp)
//						.humidity(hum)
//						.mesTimestamp(mesDate)
//						.build();
//				System.out.println(measurement);
//				cassandraService.saveMeasurement(measurement);
//				if (random.nextDouble() < 0.4) {
//					mesDate = Constants.DATETIME_FORMAT.parse(
//							String.format("%s %02d:31:01", ld.toString(), i));
//					cassandraService.saveMeasurement(
//							Measurement.builder()
//							.deviceId(""+random.nextInt(100))
//							.temperature((float) (temp+0.5))
//							.humidity(hum+1)
//							.mesTimestamp(mesDate)
//							.build());
//				}
//			}
//			day++;
//		}
//
//    }
//
//	public static List<LocalDate> getDateRange(LocalDate startDate, int days) {
//		return IntStream.iterate(0, i -> i + 1)
//		  .limit(days)
//		  .mapToObj(startDate::plusDays)
//		  .collect(Collectors.toList());
//	}

}
