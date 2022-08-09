package don.savagescan.entity;

import com.github.jgonian.ipmath.Ipv4;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Server {

    @Id
    @Column(name="id", updatable= false,nullable = false)
    private long id;

    @Column(name="host", length = 15, nullable = false, unique = true)
    private String host;

    @Column(name="checked_times", columnDefinition = "int default 0")
    private int checkedTimes;

    @CreationTimestamp
    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @UpdateTimestamp
    @Column(name="modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    @PrePersist
    private void prePersist(){
        Ipv4 stringIp = Ipv4.of(this.id);
        this.host = stringIp.toString();
    }
}
