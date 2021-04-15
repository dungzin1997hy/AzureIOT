package signup.signup.dao.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import signup.signup.dao.DeviceDAO;
import signup.signup.entity.Device;
import signup.signup.entity.Weather;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Queue;

@Repository
@Transactional
public class DeviceDAOImpl implements DeviceDAO {
    @Autowired
    EntityManager entityManager;

    @Override
    public void addDevice(Device device) {
        entityManager.persist(device);
    }

    @Override
    public List<Device> getAllDevice() {
        String sql ="Select u from Device u";
        Query query = entityManager.createQuery(sql);
        List<Device> list = query.getResultList();
        return list;
    }
}
