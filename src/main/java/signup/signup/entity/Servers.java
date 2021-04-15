package signup.signup.entity;

import javax.persistence.*;

@Entity
@Table(name="ServerInfo")
public class Servers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String Country;
    public String City;
    public String Provider;
    public String Host;
    public String IDServer;

    public Servers(String country, String city, String provider, String host, String ID) {
        Country = country;
        City = city;
        Provider = provider;
        Host = host;
        this.IDServer = ID;
    }
}
