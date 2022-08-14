package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import don.savagescan.connector.SSH;
import don.savagescan.entity.Server;

public class SSHConsumer implements Runnable {

    private final SSH ssh;

    private final SSHConfig sshConfig;

    public SSHConsumer(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
        this.ssh = new SSH(sshConfig.getSshPasswords());
    }

    @Override
    public void run() {
        while (sshConfig.getCurrent().asBigInteger().longValue() < Ipv4.LAST_IPV4_ADDRESS.asBigInteger().longValue()) {
            try {
                String ip = sshConfig.getQueue().take();
                ssh.setHost(ip);
                boolean state = ssh.tryConnections();

                if (state) {
                    sshConfig.getServerRepository().save(new Server(ip));
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
