package don.savagescan.generator;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;

import java.util.List;

public class Producer extends Thread {
    private final List<Long> queue;
    private final List<Ipv4Range> ipv4Ranges;
    private Ipv4 start;

    public Producer(List<Long> queue, List<Ipv4Range> ipv4Ranges, Ipv4 start) {
        this.queue = queue;
        this.ipv4Ranges = ipv4Ranges;
        this.start = start;
    }


    @Override
    public void run() {
        do {
            synchronized (queue) {
                while (queue.size() == HostGenerator.MAX_SIZE) {
                    try {
                        System.out.println("Queue is full, Producer thread waiting for consumer to take something from queue");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                Ipv4 current = start.next();
                System.out.println(start);
                for (Ipv4Range ipRange : ipv4Ranges) {
                    if (ipRange.contains(current)) {
                        current = ipRange.end();
                        break;
                    }
                }
                // send to list
                start = current;
                queue.add(start.asBigInteger().longValue());

                if (queue.size() == HostGenerator.MAX_SIZE) {
                    queue.notifyAll();
                }
            }
        } while (start.hasNext());
    }
}
