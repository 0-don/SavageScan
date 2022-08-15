package don.savagescan.scan;

import don.savagescan.entity.Settings;
import don.savagescan.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SavageScan {

    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final SettingsRepository settingsRepository;
    private int threads = 0;
    @Autowired
    private ScanConfig scanConfig;

    public SavageScan(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @PostConstruct
    public void init() {
        Settings settings = settingsRepository.findFirstByOrderByIdDesc();
        threads = settings == null ? settingsRepository.save(new Settings(2500)).getThreads() : settings.getThreads();
    }

    public void start() throws IOException, InterruptedException {

        ScanProducer sshProducer = new ScanProducer(scanConfig);
        pool.execute(sshProducer);

        for (int i = 0; i < threads; i++) {
            ScanConsumer consumer = new ScanConsumer(scanConfig);
            pool.execute(consumer);
        }

        pool.shutdown();
        pool.awaitTermination(99, java.util.concurrent.TimeUnit.DAYS);
    }
}
