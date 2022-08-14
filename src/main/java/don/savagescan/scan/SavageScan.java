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
    private SSHConfig sshConfig;

    public void start() throws IOException {


        SSHProducer sshProducer = new SSHProducer(sshConfig);
        pool.execute(sshProducer);

        for (int i = 0; i < 10000; i++) {
            SSHConsumer consumer = new SSHConsumer(sshConfig);
            pool.execute(consumer);
        }

    }
}
