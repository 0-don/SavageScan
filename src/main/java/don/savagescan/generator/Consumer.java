package don.savagescan.generator;

import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;

import java.util.List;

public class Consumer extends Thread {
    private final List<Server> queue;
    private final ServerRepository serverRepository;

    public Consumer(List<Server> queue, ServerRepository serverRepository) {
        this.queue = queue;
        this.serverRepository = serverRepository;
    }


    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.size() != HostGenerator.MAX_SIZE) {
                    System.out.println("Queue is empty, Consumer thread is waiting for producer thread to put something in queue");
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                serverRepository.saveAll(queue);

                queue.clear();
                queue.notifyAll();
            }
        }
    }
}
