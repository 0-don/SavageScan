package don.savagescan.generator;

import don.savagescan.repositories.ServerRepository;

import java.util.List;

public class Consumer extends Thread {
    private final List<Long> queue;
    private final ServerRepository serverRepository;

    public Consumer(List<Long> queue, ServerRepository serverRepository) {
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

                serverRepository.save(queue);

                queue.clear();
                queue.notifyAll();
            }
        }
    }
}
