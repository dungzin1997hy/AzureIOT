package signup.signup.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import signup.signup.dao.MessageSendDAO;
import signup.signup.entity.MessageSend;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class MessageSendDAOImpl implements MessageSendDAO {
    @Autowired
    EntityManager entityManager;
    @Override
    public void addMessageSend(MessageSend messageSend) {
        entityManager.persist(messageSend);
    }
}
