package signup.signup.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class MessageSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String deviceId;
    public LocalDateTime time;
    public String idSend;
}
