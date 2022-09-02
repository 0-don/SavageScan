package don.savagescan;

import don.savagescan.scan.SavageScan;
import don.savagescan.scan.ScanCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://api.ipify.org/?format=text")).build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (environment.equals("production")) {
            savageScan.start();
        } else {
            scanCheck.check();
        }
    }

}
