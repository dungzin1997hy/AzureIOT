package signup.signup.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import signup.signup.dao.WeatherDAO;
import signup.signup.entity.Weather;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;


@Repository
@Transactional
public class WeatherDAOImpl implements WeatherDAO {
    @Autowired
    EntityManager entityManager;
    @Override
    public void addWeather(Weather weather) {
        entityManager.persist(weather);
    }
}
