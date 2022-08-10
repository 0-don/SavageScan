package don.savagescan.utils;

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

@Component
public class HostGenerator {

    private final ServerRepository serverRepository;

    @Value("classpath:reservedIps.json")
    private Resource res;

    public HostGenerator(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }


    public void start() throws IOException {
        Server latestServer = serverRepository.findFirstByOrderByIdDesc();

        JsonReader reader = new JsonReader(new InputStreamReader(res.getInputStream()));
        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);
        
        long i = 0L;
        if (latestServer != null) {
            i = latestServer.getId() + 1L;
        }

        while (Ipv4.LAST_IPV4_ADDRESS.asBigInteger().longValue() > i) {
            System.out.println(Ipv4.of(i));
            for (IpRange ipRange : reservedIps) {
                Ipv4Range range = Ipv4Range.from(ipRange.getStart()).to(ipRange.getEnd());
                if (range.contains(Ipv4.of(i))) {
                    i = Ipv4.of(ipRange.getEnd()).asBigInteger().longValue() + 1L;
                    break;
                }
            }

            Server server = new Server(i);
            serverRepository.save(server);
            i++;
        }

    }

}


