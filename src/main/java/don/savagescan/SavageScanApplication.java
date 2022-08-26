package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.scan.ScanCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SavageScanApplication implements CommandLineRunner {

    private final SavageScan savageScan;

    private final ScanCheck scanCheck;


    public static void main(String[] args) {
        SpringApplication.run(SavageScanApplication.class, args);
    }

    @Override
    public void run(String... args) {
        savageScan.start();
//        scanCheck.check();
    }

}
