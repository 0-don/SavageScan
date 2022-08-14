package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.utils.generator.ServerGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SavagescanApplication implements CommandLineRunner {

    @Autowired
    private ServerGenerator hostGenerator;

    @Autowired
    private SavageScan savageScan;

    public static void main(String[] args) {
        SpringApplication.run(SavagescanApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        hostGenerator.start();
        savageScan.start();
    }

}
