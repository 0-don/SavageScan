package don.savagescan;

import don.savagescan.scan.SavageScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SavageScanApplication implements CommandLineRunner {

    private final SavageScan savageScan;

    public SavageScanApplication(SavageScan savageScan ) {
        this.savageScan = savageScan;
    }


    public static void main(String[] args) {
        SpringApplication.run(SavageScanApplication.class, args);
    }


    @Override
    public void run(String... args) {
        savageScan.start();
//        tester.check();
//        tester2.check();
    }

}
