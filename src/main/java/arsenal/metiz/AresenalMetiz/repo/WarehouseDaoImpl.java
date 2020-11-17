package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    @Autowired
    public WarehouseDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean exists(String mark, String diameter, String part, String plav) {
        String queryString = "SELECT dt FROM WarehousePosition dt WHERE dt.part = :part";
        TypedQuery<WarehousePosition> query = em.createQuery(queryString, WarehousePosition.class);
        query.setParameter("part", part);
        try {
            WarehousePosition wp = query.getResultList().get(0);
            return wp.getMark().equalsIgnoreCase(mark) && wp.getDiameter().equals(diameter) && wp.getPlav().equals(plav);
        } catch (NoResultException | IndexOutOfBoundsException e) {
            return true;
        }
    }

    private CriteriaQuery<WarehousePosition> builder(String mark, String diameter, String plav) {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<WarehousePosition> cq = qb.createQuery(WarehousePosition.class);
        Root<WarehousePosition> wp = cq.from(WarehousePosition.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(qb.equal(wp.get("mark"), mark));
        predicates.add(qb.equal(wp.get("diameter"), diameter));
        predicates.add(qb.equal(wp.get("plav"), plav));
        cq.select(wp)
                .where(predicates.toArray(new Predicate[]{}));
        return cq;
    }
}
