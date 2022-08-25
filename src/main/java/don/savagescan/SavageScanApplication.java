package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.utils.Tester;
import don.savagescan.utils.Tester2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SavageScanApplication implements CommandLineRunner {

    private final SavageScan savageScan;

    private final Tester tester;
    private final Tester2 tester2;

    public SavageScanApplication(SavageScan savageScan, Tester tester, Tester2 tester2) {
        this.savageScan = savageScan;
        this.tester = tester;
        this.tester2 = tester2;
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
