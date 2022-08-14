package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import don.savagescan.connector.SSH;
import don.savagescan.entity.Server;

public class ScanConsumer implements Runnable {

    private final SSH ssh;

    private final ScanConfig scanConfig;

    public ScanConsumer(ScanConfig scanConfig) {
        this.scanConfig = scanConfig;
        this.ssh = new SSH(scanConfig.getSshPasswords());
    }

    @Override
    public void run() {
        while (scanConfig.getCurrent().asBigInteger().longValue() < Ipv4.LAST_IPV4_ADDRESS.asBigInteger().longValue()) {
            try {
                String ip = scanConfig.getQueue().take();
                ssh.setHost(ip);
                boolean state = ssh.tryConnections();

                if (state) {
                    scanConfig.getServerRepository().save(new Server(ip));
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
