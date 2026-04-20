package org.example.repo;

import org.example.entity.PointResult;
import org.example.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class PointResultRepository {

    @PersistenceContext(unitName = "HitPU")
    private EntityManager em;

    public void save(PointResult result) {
        em.persist(result);
    }

    @SuppressWarnings("unchecked")
    public List<PointResult> findAllByUser(User user) {
        Query q = em.createQuery("SELECT p FROM PointResult p WHERE p.user = :user ORDER BY p.serverTime DESC").setParameter("user", user);
        return q.getResultList();
    }

    public void deleteAll(User user) {
        em.createQuery("DELETE FROM PointResult p WHERE p.user = :user").setParameter("user", user).executeUpdate();
    }
}
