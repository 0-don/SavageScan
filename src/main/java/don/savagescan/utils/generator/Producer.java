package don.savagescan.utils.generator;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import don.savagescan.entity.Server;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private final BlockingQueue<Server> queue;
    private final List<Ipv4Range> ipv4Ranges;
    private Ipv4 start;

    public Producer(BlockingQueue<Server> queue, List<Ipv4Range> ipv4Ranges, Ipv4 start) {
        this.queue = queue;
        this.ipv4Ranges = ipv4Ranges;
        this.start = start;
    }


    @Override
    public void run() {
        do {

            if (queue.size() < 1000000) {
                Ipv4 current = start.next();
//                System.out.println(start);
                for (Ipv4Range ipRange : ipv4Ranges) {
                    if (ipRange.contains(current)) {
                        current = ipRange.end();
                        break;
                    }
                }
                // send to list
                start = current;

//                queue.add(new Server(start.asBigInteger().longValue()));
            }


        } while (start.hasNext());
    }
}
