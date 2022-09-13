package don.savagescan.scan;

import don.savagescan.entity.Settings;
import don.savagescan.repositories.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class SavageScan {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final ApplicationContext applicationContext;
    private final SettingsRepository settingsRepository;

    @Value("${environment}")
    private String environment;
    private int threads = 0;

    @PostConstruct
    public void init() {
        Settings settings = settingsRepository.findFirstByOrderByIdDesc();
        threads = settings == null ? settingsRepository.save(new Settings(25000)).getThreads() : settings.getThreads();
    }

    public void start() {

        threads = environment.equals("production") ? threads : 1;

        ScanProducer sshProducer = applicationContext.getBean(ScanProducer.class);
        pool.execute(sshProducer);

        for (int i = 0; i < threads; i++) {
            ScanConsumer consumer = applicationContext.getBean(ScanConsumer.class);
            pool.execute(consumer);
        }

        pool.shutdown();
        try {
            boolean terminated = pool.awaitTermination(99, java.util.concurrent.TimeUnit.DAYS);
            System.out.println("terminated: " + terminated);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getMessage());
        }

    }
}
