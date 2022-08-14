package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;

public class SSHProducer implements Runnable {
    SSHConfig sshConfig;
    private Ipv4 start;

    public SSHProducer(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
        this.start = sshConfig.getStart();
    }

    @Override
    public void run() {
        do {

            Ipv4 current = start.next();
//                System.out.println(start);
            for (Ipv4Range ipRange : sshConfig.getIpv4ReservedIps()) {
                if (ipRange.contains(current)) {
                    current = ipRange.end().next();
                    break;
                }
            }
            // send to list
            start = current;

            try {
                sshConfig.getQueue().put(start.toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while (start.hasNext());
    }
}
