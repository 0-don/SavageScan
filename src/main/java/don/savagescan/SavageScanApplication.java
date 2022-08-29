package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.scan.ScanCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@SpringBootApplication
@RequiredArgsConstructor
public class SavageScanApplication implements CommandLineRunner {

    private final SavageScan savageScan;

    private final ScanCheck scanCheck;

    @Value("${environment}")
    private String environment;

    public static void main(String[] args) {
        SpringApplication.run(SavageScanApplication.class, args);
    }

    @Override
    public void run(String... args) {


        try {
            URL yahoo = new URL("https://api.ipify.org/?format=text");
            URLConnection yc = yahoo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        if (environment.equals("production")) {
//            savageScan.start();
//        } else {
//            scanCheck.check();
//        }
    }

}
