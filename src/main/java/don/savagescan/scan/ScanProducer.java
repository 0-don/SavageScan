package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class ScanProducer implements Runnable {
    private final ScanConfig scanConfig;

    @Override
    public void run() {
        Ipv4 start = scanConfig.getStart();

        do {

            Ipv4 current = start.next();
            for (Ipv4Range reservedIp : scanConfig.getIpv4ReservedIps()) {
                if (reservedIp.contains(current)) {
                    current = reservedIp.end().next();
                    break;
                }
            }
            start = current;

            try {
                scanConfig.getQueue().put(start.toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while (start.hasNext());
    }
}
