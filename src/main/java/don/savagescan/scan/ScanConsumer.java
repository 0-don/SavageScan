package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import don.savagescan.connector.SSH;
import don.savagescan.entity.CurrentServer;
import don.savagescan.entity.Server;
import don.savagescan.entity.ServerService;
import don.savagescan.model.ServiceName;

public class ScanConsumer implements Runnable {

    private final SSH ssh;

    private final ScanConfig scanConfig;

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

                if (sshState) {
                    Server server = new Server(ssh.getHost());
                    server.addServerService(new ServerService(ServiceName.SSH, ssh.getUsername(), ssh.getPassword(), ssh.getPort()));
                    scanConfig.getServerRepository().save(server);
                }

                if (scanConfig.getCurrent() < Ipv4.of(ip).asBigInteger().longValue()) {
                    System.out.println("update");
                    scanConfig.setCurrent(Ipv4.of(ip).asBigInteger().longValue());
                    CurrentServer currentServer = scanConfig.getCurrentServerRepository().findFirstByOrderByIdDesc();
                    currentServer.setHost(ip);
                    scanConfig.getCurrentServerRepository().save(currentServer);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
