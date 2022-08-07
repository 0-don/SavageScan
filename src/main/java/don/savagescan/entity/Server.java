package don.savagescan.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
