package don.savagescan.scan;

import don.savagescan.entity.Settings;
import don.savagescan.repositories.SettingsRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SavageScan {

    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final SettingsRepository settingsRepository;
    private final ScanConfig scanConfig;
    private int threads = 0;

    public SavageScan(SettingsRepository settingsRepository, ScanConfig scanConfig) {
        this.settingsRepository = settingsRepository;
        this.scanConfig = scanConfig;
    }

    @PostConstruct
    public void init() {
        Settings settings = settingsRepository.findFirstByOrderByIdDesc();
        threads = settings == null ? settingsRepository.save(new Settings(2000)).getThreads() : settings.getThreads();
    }

    public void start() throws IOException, InterruptedException {

        ScanProducer sshProducer = new ScanProducer(scanConfig);
        pool.execute(sshProducer);

        for (int i = 0; i < threads; i++) {
            ScanConsumer consumer = new ScanConsumer(scanConfig);
            pool.execute(consumer);
        }

        pool.shutdown();
        boolean terminated = pool.awaitTermination(99, java.util.concurrent.TimeUnit.DAYS);
        System.out.println("terminated: " + terminated);
    }
}
