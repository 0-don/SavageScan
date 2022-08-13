package don.savagescan.generator;

import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private final BlockingQueue<Server> queue;
    private final ServerRepository serverRepository;

    private final List<Server> servers = new LinkedList<>();

    public Consumer(BlockingQueue<Server> queue, ServerRepository serverRepository) {
        this.queue = queue;
        this.serverRepository = serverRepository;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Server server = queue.take();

                servers.add(server);


                if (servers.size() == 50) {
                    System.out.println(server);
                    serverRepository.saveAll(servers);
                    servers.clear();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
