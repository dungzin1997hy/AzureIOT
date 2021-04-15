package signup.signup.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "temperature")
    public double temperature;

    @Column(name = "humidity")
    public double humidity;

    @Column(name = "wind_speed")
    public double windSpeed;
    public LocalDateTime time;
}
