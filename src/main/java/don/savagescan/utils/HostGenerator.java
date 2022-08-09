package don.savagescan.utils;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HostGenerator {

    public List<Ipv4Range> reservedIpRanges = new ArrayList<>() {{
        add(Ipv4Range.from("0.0.0.0").to("10.255.255.255"));
        add(new Ipv4Range(Ipv4.of("10.0.0.0"), Ipv4.of("10.255.255.255"));
        add(new Ipv4Range(Ipv4.of("100.64.0.0"), Ipv4.of("100.127.255.255"));
        add(new Ipv4Range(Ipv4.of("127.0.0.0"), Ipv4.of("127.255.255.255"));
        add(new Ipv4Range(Ipv4.of("169.254.0.0"), Ipv4.of("169.254.255.255"));
        add(new Ipv4Range(Ipv4.of("172.16.0.0"), Ipv4.of("172.31.255.255"));
        add(new Ipv4Range(Ipv4.of("192.0.0.0"), Ipv4.of("192.0.0.255"));
        add(new Ipv4Range(Ipv4.of("192.0.2.0"), Ipv4.of("192.0.2.255"));
        add(new Ipv4Range(Ipv4.of("192.88.99.0"), Ipv4.of("192.88.99.255"));
        add(new Ipv4Range(Ipv4.of("192.168.0.0"), Ipv4.of("192.168.255.255"));
        add(new Ipv4Range(Ipv4.of("198.18.0.0"), Ipv4.of("198.19.255.255"));
        add(new Ipv4Range(Ipv4.of("198.51.100.0"), Ipv4.of("198.51.100.255"));
        add(new Ipv4Range(Ipv4.of("203.0.113.0"), Ipv4.of("203.0.113.255"));
        add(new Ipv4Range(Ipv4.of("224.0.0.0"), Ipv4.of("239.255.255.255"));
        add(new Ipv4Range(Ipv4.of("233.252.0.0"), Ipv4.of("233.252.0.255"));
        add(new Ipv4Range(Ipv4.of("240.0.0.0"), Ipv4.of("255.255.255.255"));
    }};


    private final ServerRepository serverRepository;

    public HostGenerator( ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void start() {
        Server latestServer = serverRepository.findFirstByOrderByIdDesc();

        long i = 0L;
        if(latestServer != null) {
            i = latestServer.getId();
        }

        System.out.println(Ipv4.of(i));

        while (Ipv4.LAST_IPV4_ADDRESS.asBigInteger().longValue() > i) {

            serverRepository.save(new Server(i));
            i++;
        }

    }

}
