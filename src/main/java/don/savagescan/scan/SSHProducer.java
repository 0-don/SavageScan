package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SSHProducer implements Runnable {
    private final BlockingQueue<String> queue;
    private final List<Ipv4Range> ipv4Ranges;
    private Ipv4 start;

    public SSHProducer(BlockingQueue<String> queue, List<Ipv4Range> ipv4Ranges, Ipv4 start) {
        this.queue = queue;
        this.ipv4Ranges = ipv4Ranges;
        this.start = start;
    }


    @Override
    public void run() {
        do {
            if (queue.size() < 1_000_000) {
                Ipv4 current = start.next();
//                System.out.println(start);
                for (Ipv4Range ipRange : ipv4Ranges) {
                    if (ipRange.contains(current)) {
                        current = ipRange.end().next();
                        break;
                    }
                }
                // send to list
                start = current;

                queue.add(start.toString());
            }

        } while (start.hasNext());
    }
}
