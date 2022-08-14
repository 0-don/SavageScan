package don.savagescan.utils.generator;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;
import don.savagescan.utils.IpRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ServerGenerator {
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private final ServerRepository serverRepository;
    private final BlockingQueue<Server> queue = new LinkedBlockingQueue<>();

    @Value("classpath:reservedIps.json")
    private Resource res;

    public ServerGenerator(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }


    public void start() throws IOException {
        Server latest = serverRepository.findFirstByOrderByIdDesc();

        JsonReader reader = new JsonReader(new InputStreamReader(res.getInputStream()));
        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);

        List<Ipv4Range> ipv4Ranges = new ArrayList<>();
        Arrays.stream(reservedIps).forEach(range ->
                ipv4Ranges.add(Ipv4Range.from(range.getStart()).to(range.getEnd())));

        Ipv4 start = latest == null
                ? Ipv4.FIRST_IPV4_ADDRESS
                : Ipv4.of(latest.getId());

        Producer producer = new Producer(queue, ipv4Ranges, start);
        pool.execute(producer);

        for (int i = 0; i < 200; i++) {
            Consumer consumer = new Consumer(queue, this.serverRepository);
            pool.execute(consumer);
        }

    }


}


