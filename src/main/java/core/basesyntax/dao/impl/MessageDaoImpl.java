package core.basesyntax.dao.impl;

import core.basesyntax.HibernateUtil;
import core.basesyntax.dao.MessageDao;
import core.basesyntax.model.Comment;
import core.basesyntax.model.Message;

import java.util.ArrayList;
import java.util.List;

import core.basesyntax.model.Smile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class MessageDaoImpl extends AbstractDao implements MessageDao {
    public MessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Message create(Message entity) {
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
            throw new RuntimeException("Can't create message", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Message get(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(Message.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get message from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Message> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Message", Message.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all messages from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void remove(Message entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Comment managed = session.get(Comment.class, entity.getId());

            if (managed != null) {
                managed.getSmiles().clear();
                session.remove(managed);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't remove message from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
