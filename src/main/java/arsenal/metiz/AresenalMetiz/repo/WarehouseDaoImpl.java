package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.Position;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseDaoImpl implements WarehouseDao {

    private final EntityManager em;

    final PositionRepo repo;

    @Autowired
    public WarehouseDaoImpl(EntityManager em, PositionRepo repo) {
        this.em = em;
        this.repo = repo;
    }

    @Override
    @Transactional
    public void update(Position position) {
        Session session = em.unwrap(Session.class);
        session.update(position);
    }

    @Override
    public boolean exists(String mark, String diameter, String part, String plav) {
        return (existInWarehouse(mark, diameter, part, plav));
    }

    @Override
    @Transactional
    public void saveWithId(Position position) {
        em.persist(em.merge(position));
    }

    public boolean existInWarehouse(String mark, String diameter, String part, String plav) {
        part = part.trim();
        String queryString = "SELECT dt FROM Position dt WHERE dt.part = :part";
        TypedQuery<Position> query = em.createQuery(queryString, Position.class);
        query.setParameter("part", part);
        try {
            Position wp = query.getResultList().get(0);
            return wp.getMark().equalsIgnoreCase(mark.trim()) && wp.getDiameter().equals(diameter.trim()) && wp.getPlav().equals(plav.trim());
        } catch (NoResultException | IndexOutOfBoundsException e) {
            return true;
        }
    }

    private CriteriaQuery<Position> builder(String mark, String diameter, String plav) {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Position> cq = qb.createQuery(Position.class);
        Root<Position> wp = cq.from(Position.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(qb.equal(wp.get("mark"), mark));
        predicates.add(qb.equal(wp.get("diameter"), diameter));
        predicates.add(qb.equal(wp.get("plav"), plav));
        cq.select(wp)
                .where(predicates.toArray(new Predicate[]{}));
        return cq;
    }
}
