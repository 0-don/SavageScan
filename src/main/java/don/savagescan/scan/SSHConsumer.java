package don.savagescan.scan;

import don.savagescan.connector.SSH;

public class SSHConsumer implements Runnable {

    private final SSH ssh;

    private final SSHConfig sshConfig;

    public SSHConsumer(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
        this.ssh = new SSH(sshConfig.getSshPasswords());
    }

    @Override
    public void run() {
        while (true) {
            try {
                String ip = sshConfig.getQueue().take();
                ssh.setHost(ip);
                ssh.tryConnections();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
