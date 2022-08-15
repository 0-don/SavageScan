package don.savagescan.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SavageScan {

    private static final ExecutorService pool = Executors.newCachedThreadPool();
    @Autowired
    private ScanConfig scanConfig;

    public void start() throws IOException, InterruptedException {

        ScanProducer sshProducer = new ScanProducer(scanConfig);
        pool.execute(sshProducer);

        for (int i = 0; i < 2000; i++) {
            ScanConsumer consumer = new ScanConsumer(scanConfig);
            pool.execute(consumer);
        }

        pool.shutdown();
        pool.awaitTermination(99, java.util.concurrent.TimeUnit.DAYS);
    }
}
