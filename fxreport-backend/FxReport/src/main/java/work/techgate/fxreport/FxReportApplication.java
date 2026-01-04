package work.techgate.fxreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FxReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxReportApplication.class, args);
	}

}
