package org.example.repo;

import org.example.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless
public class UserRepository {

    @PersistenceContext(unitName = "HitPU")
    private EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public Optional<User> findByLogin(String login) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.login = :login",
                User.class
        );
        query.setParameter("login", login);

        return query.getResultStream().findFirst();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }
}
