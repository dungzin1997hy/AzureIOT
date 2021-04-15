package signup.signup.entity;


import javax.persistence.*;

@Entity
@Table (name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String deviceId;
    public String connectionString;
}
