package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.utils.Tester;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SavageScanApplication implements CommandLineRunner {

    private final SavageScan savageScan;

    private final Tester tester;

    public SavageScanApplication(SavageScan savageScan, Tester tester) {
        this.savageScan = savageScan;
        this.tester = tester;
    }


    public static void main(String[] args) {
        SpringApplication.run(SavageScanApplication.class, args);
    }


    @Override
    public void run(String... args) {
//        savageScan.start();
        tester.check();
    }

}
