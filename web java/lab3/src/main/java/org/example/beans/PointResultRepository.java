package org.example.beans;


import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class PointResultRepository {

    @PersistenceContext(unitName = "HitPU")
    private EntityManager em;

    public PointResult save(PointResult result) {
        em.persist(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<PointResult> findAll() {
        Query q = em.createQuery("SELECT p FROM PointResult p ORDER BY p.serverTime DESC");
        return q.getResultList();
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM PointResult").executeUpdate();
    }
}
