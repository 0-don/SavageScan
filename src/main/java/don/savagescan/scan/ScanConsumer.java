package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import don.savagescan.connector.SSH;

import java.util.Random;

public class ScanConsumer implements Runnable {

    private final SSH ssh;

    private final ScanConfig scanConfig;

    Random random = new Random();

    public ScanConsumer(ScanConfig scanConfig) {
        this.scanConfig = scanConfig;
        this.ssh = new SSH(scanConfig.getSshPasswords());
    }

    @Override
    public void run() {
        while (scanConfig.getCurrent() < Ipv4.LAST_IPV4_ADDRESS.asBigInteger().longValue()) {
            try {
                String ip = scanConfig.getQueue().take();
                ssh.setHost(ip);
                boolean sshState = ssh.tryConnections();


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
