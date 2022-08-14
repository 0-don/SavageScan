package don.savagescan.scan;

import don.savagescan.connector.SSH;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SSHConsumer implements Runnable {
    private final BlockingQueue<String> queue;
    private final SSH ssh = new SSH();

    private final List<String> sshPasswords;

    public SSHConsumer(BlockingQueue<String> queue, List<String> sshPasswords) {
        this.queue = queue;
        this.sshPasswords = sshPasswords;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String ip = queue.take();
                ssh.setHost(ip, sshPasswords);
                ssh.tryConnections();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
