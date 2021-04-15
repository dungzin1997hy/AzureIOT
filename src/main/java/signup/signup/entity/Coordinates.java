package signup.signup.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String deviceSend;

    public double latitude;

    public double longtitude;

    public double height;

    public String idSend;

    public LocalDateTime time;

    public Coordinates(double latitude, double longtitude, double height) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.height = height;
    }

    public Coordinates(int id, double latitude, double longtitude, double height, LocalDateTime time) {
        this.id = id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.height = height;
        this.time = time;
    }

    public Coordinates(String deviceSend, double latitude, double longtitude, double height, String idSend) {
        this.deviceSend = deviceSend;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.height = height;
        this.idSend = idSend;
    }

    public Coordinates() {
    }

    public Coordinates(String deviceSend) {
        this.deviceSend = deviceSend;
    }

    public Coordinates(String deviceSend, String idSend) {
        this.deviceSend = deviceSend;
        this.idSend = idSend;
    }
}
