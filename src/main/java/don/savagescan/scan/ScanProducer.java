package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;

public class ScanProducer implements Runnable {
    private final ScanConfig scanConfig;
    private Ipv4 start;

    public ScanProducer(ScanConfig scanConfig) {
        this.scanConfig = scanConfig;
        this.start = scanConfig.getStart();
    }

    @Override
    public void run() {
        do {

            Ipv4 current = start.next();
//                System.out.println(start);
            for (Ipv4Range reservedIp : scanConfig.getIpv4ReservedIps()) {
                if (reservedIp.contains(current)) {
                    current = reservedIp.end().next();
                    break;
                }
            }
            // send to list
            start = current;

            try {
                scanConfig.getQueue().put(start.toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while (start.hasNext());
    }
}
