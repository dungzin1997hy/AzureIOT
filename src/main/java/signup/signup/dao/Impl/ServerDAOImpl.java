package signup.signup.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import signup.signup.dao.ServerDAO;
import signup.signup.entity.Servers;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ServerDAOImpl implements ServerDAO {
    @Autowired
    EntityManager entityManager;
    @Override
    public void addServer(Servers servers) {
        entityManager.persist(servers);
    }
}
