package core.basesyntax.dao.impl;

import core.basesyntax.HibernateUtil;
import core.basesyntax.dao.MessageDetailsDao;
import core.basesyntax.model.Message;
import core.basesyntax.model.MessageDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class MessageDetailsDaoImpl extends AbstractDao implements MessageDetailsDao {
    public MessageDetailsDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public MessageDetails create(MessageDetails entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't create message details", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public MessageDetails get(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(MessageDetails.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get message details from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
