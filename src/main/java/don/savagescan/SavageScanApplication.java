package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.scan.ScanCheck;
import don.savagescan.utils.Nmap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static java.net.http.HttpResponse.BodyHandlers;

@SpringBootApplication
@RequiredArgsConstructor
public class SavageScanApplication implements CommandLineRunner {

    private final SavageScan savageScan;

    private final ScanCheck scanCheck;
    private final Nmap nmap;

    @Value("${environment}")
    private String environment;

    public static void main(String[] args) {
        SpringApplication.run(SavageScanApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://api.ipify.org/?format=text")).build();
            var response = HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

//        nmap.scan();
        if (environment.equals("production")) {
            savageScan.start();
        } else {
            scanCheck.check();
        }

    }

}
