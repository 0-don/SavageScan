package don.savagescan.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "host", length = 15, nullable = false, unique = true)
    private String host;


    @CreationTimestamp
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @UpdateTimestamp
    @Column(name = "modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "server")
    private Set<ServerService> serverServices = new HashSet<>();

    public Server(String host) {
        this.host = host;
    }

    public Server addServerService(ServerService serverService) {
        serverService.setServer(this);
        this.serverServices.add(serverService);
        return this;
    }

}
