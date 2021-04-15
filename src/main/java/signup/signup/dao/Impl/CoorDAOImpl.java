package signup.signup.dao.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import signup.signup.dao.CoordinatesDAO;
import signup.signup.entity.Coordinates;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@Repository
@Transactional
public class CoorDAOImpl implements CoordinatesDAO {
    @Autowired
    EntityManager entityManager;

    @Override
    public void addCoor(Coordinates coordinates) {
        System.out.println("add to database");
        entityManager.persist(coordinates);
    }
}
