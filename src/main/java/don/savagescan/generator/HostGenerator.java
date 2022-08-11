package don.savagescan.generator;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HostGenerator {

    public static final int MAX_SIZE = 1000;
    private final ServerRepository serverRepository;
    private final List<Server> queue = new ArrayList<>();

    @Value("classpath:reservedIps.json")
    private Resource res;

    public HostGenerator(ServerRepository serverRepository) {
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

        Thread producer = new Producer(queue, ipv4Ranges, start);
        Thread consumer = new Consumer(queue, this.serverRepository);

        producer.start();
        consumer.start();
    }

//    public void start() throws IOException {
//        Server latest = serverRepository.findFirstByOrderByIdDesc();
//
//        JsonReader reader = new JsonReader(new InputStreamReader(res.getInputStream()));
//        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);
//
//        List<Ipv4Range> ipv4Ranges = new ArrayList<>();
//        Arrays.stream(reservedIps).forEach(range ->
//                ipv4Ranges.add(Ipv4Range.from(range.getStart()).to(range.getEnd())));
//
//
//        Ipv4 start = latest == null
//                ? Ipv4.FIRST_IPV4_ADDRESS
//                : Ipv4.of(latest.getId());
//
//        do {
//            Ipv4 current = start.next();
//            System.out.println(start);
//            for (Ipv4Range ipRange : ipv4Ranges) {
//                if (ipRange.contains(current)) {
//                    current = ipRange.end();
//                    break;
//                }
//            }
//            // send to list
//            start = current;
//
//            Server server = new Server(start.asBigInteger().longValue());
//            serverRepository.save(server);
//
//        } while (start.hasNext());
//
//    }

}


