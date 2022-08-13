package don.savagescan.generator;

import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    private final BlockingQueue<Server> queue;
    private final ServerRepository serverRepository;

    public Consumer(BlockingQueue<Server> queue, ServerRepository serverRepository) {
        this.queue = queue;
        this.serverRepository = serverRepository;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Server server = queue.take();

                System.out.println(server);

                serverRepository.save(server);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
