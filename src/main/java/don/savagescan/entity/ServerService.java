package don.savagescan.entity;

import don.savagescan.model.ServiceName;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ServerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ServiceName serviceName;
    private int port;
    private String username;
    private String password;
    @ManyToOne
    private Server server;

    @CreationTimestamp
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;


    public ServerService() {
    }

    public ServerService(ServiceName serviceName, int port, String username, String password, Server server) {
        this.serviceName = serviceName;
        this.port = port;
        this.username = username;
        this.password = password;
        this.server = server;
    }
}
