package don.savagescan.utils;

import don.savagescan.entity.Server;
import don.savagescan.repositories.ServerRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class HostGenerator {
    public HashMap<String, String> reservedIPs = new HashMap<>() {{
        put("0.0.0.0", "0.255.255.255");
        put("10.0.0.0", "10.255.255.255");
        put("100.64.0.0", "100.127.255.255");
        put("127.0.0.0", "127.255.255.255");
        put("169.254.0.0", "169.254.255.255");
        put("172.16.0.0", "172.31.255.255");
        put("192.0.0.0", "192.0.0.255");
        put("192.0.2.0", "192.0.2.255");
        put("192.88.99.0", "192.88.99.255");
        put("192.168.0.0", "192.168.255.255");
        put("198.18.0.0", "198.19.255.255");
        put("198.51.100.0", "198.51.100.255");
        put("203.0.113.0", "203.0.113.255");
        put("224.0.0.0", "239.255.255.255");
        put("233.252.0.0", "233.252.0.255");
        put("240.0.0.0", "255.255.255.255");
    }};

    private final ServerRepository serverRepository;

    public HostGenerator( ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void start() {
        Server latestServer = serverRepository.findFirstOrderByIdDesc();

        long i = 0L;
        if(latestServer != null) {
            i = ipToLong(latestServer.getHost());
        }

        System.out.println(longToIp(i));

//        while (4294967295L > i) {
//
//        }

    }

    public long ipToLong(String ipAddress) {

        String[] ipAddressInArray = ipAddress.split("\\.");

        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {

            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);

        }

        return result;
    }

    public String longToIp(long ip) {

        Long ipLong = Long.valueOf(ip);
        StringBuilder result = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {
            result.insert(0, ipLong & 0xff);

            if (i < 3) {
                result.insert(0, '.');
            }

            ipLong = ipLong >> 8;
        }

        return result.toString();
    }
}
