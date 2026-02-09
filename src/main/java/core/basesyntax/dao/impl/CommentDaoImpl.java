package core.basesyntax.dao.impl;

import core.basesyntax.HibernateUtil;
import core.basesyntax.dao.CommentDao;
import core.basesyntax.dao.SmileDao;
import core.basesyntax.model.Comment;

import java.util.ArrayList;
import java.util.List;

import core.basesyntax.model.Smile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CommentDaoImpl extends AbstractDao implements CommentDao {
    public CommentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Comment create(Comment entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<Smile> existingSmiles = new ArrayList<>();

            for (Smile smile : entity.getSmiles()) {
                Smile managedSmile = session.get(Smile.class, smile.getId());

                if (managedSmile == null) {
                    throw new RuntimeException(
                            "Smile with id " + smile.getId() + " not found"
                    );
                }

                existingSmiles.add(managedSmile);
            }

            entity.setSmiles(existingSmiles);
            session.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't create comment", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Comment get(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.get(Comment.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get comment from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Comment> getAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("from Comment", Comment.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all comments from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void remove(Comment entity) {
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
            throw new RuntimeException("Can't remove comment from DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
