package arsenal.metiz.AresenalMetiz.repo;

import arsenal.metiz.AresenalMetiz.models.IdContainer;
import arsenal.metiz.AresenalMetiz.models.ManufacturePosition;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
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
import java.util.Optional;

@Repository
public class WarehouseDaoImpl implements WarehouseDao {

    private final EntityManager em;

    final WarehouseRepo repo;

    @Autowired
    public WarehouseDaoImpl(EntityManager em, WarehouseRepo repo) {
        this.em = em;
        this.repo = repo;
    }

    @Transactional
    @Override
    public WarehousePosition save(WarehousePosition position) {
        Long id = position.getId();
        WarehousePosition position1 = em.merge(position);
        if (id == null) {
            em.persist(new IdContainer(position1.getId(), position1));
            return position1;
        } else {
            em.persist(new IdContainer(position.getId(), position1));
            return position;
        }
    }

    @Override
    public Optional<WarehousePosition> getById(Long id) {
        IdContainer idContainer = em.find(IdContainer.class, id);
        WarehousePosition position = null;
        if (idContainer != null) {
            position = idContainer.getPosition();
        }
        if (position == null) {
            position = repo.findById(id).orElse(null);
        }
        return Optional.ofNullable(position);
    }

    @Override
    @Transactional
    public void update(WarehousePosition position) {
        Session session = em.unwrap(Session.class);
        session.update(position);
    }

    @Override
    public boolean exists(String mark, String diameter, String part, String plav) {
        return (existInWarehouse(mark, diameter, part, plav) || existsInManufacture(mark, diameter, part, plav));
    }

    @Override
    @Transactional
    public void saveWithId(WarehousePosition position) {
        em.persist(em.merge(position));
    }

    public boolean existInWarehouse(String mark, String diameter, String part, String plav) {
        part = part.trim();
        String queryString = "SELECT dt FROM WarehousePosition dt WHERE dt.part = :part";
        TypedQuery<WarehousePosition> query = em.createQuery(queryString, WarehousePosition.class);
        query.setParameter("part", part);
        try {
            WarehousePosition wp = query.getResultList().get(0);
            return wp.getMark().equalsIgnoreCase(mark.trim()) && wp.getDiameter().equals(diameter.trim()) && wp.getPlav().equals(plav.trim());
        } catch (NoResultException | IndexOutOfBoundsException e) {
            return true;
        }
    }

    public boolean existsInManufacture(String mark, String diameter, String part, String plav) {
        part = part.trim();
        String queryString = "SELECT dt FROM ManufacturePosition dt WHERE dt.part = :part";
        TypedQuery<ManufacturePosition> query = em.createQuery(queryString, ManufacturePosition.class);
        query.setParameter("part", part);
        try {
            ManufacturePosition wp = query.getResultList().get(0);
            return wp.getMark().equalsIgnoreCase(mark.trim()) && wp.getDiameter().equals(diameter.trim()) && wp.getPlav().equals(plav.trim());
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
