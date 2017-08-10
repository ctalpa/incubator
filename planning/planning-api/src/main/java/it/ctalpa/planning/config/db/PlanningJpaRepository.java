package it.ctalpa.planning.config.db;


import it.ctalpa.planning.Application;
import it.ctalpa.planning.config.db.validators.UniqueKey;
import it.ctalpa.planning.config.error.CustomParametrizedException;
import it.ctalpa.planning.config.error.ErrorConstants;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
public class PlanningJpaRepository<T, ID extends Serializable>
    extends SimpleJpaRepository<T, ID> implements PlanningRepository<T, ID> {

    private final EntityManager entityManager;

    public PlanningJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public void refresh(T entity) {
        entityManager.refresh(entity);
    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        checkTheUniqueness(entity);
        return super.save(entity);
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        checkTheUniqueness(entity);
        return super.saveAndFlush(entity);
    }


    private <S extends T> void checkTheUniqueness (S entity) {
        final Class entityOriginalClass = retrieveOriginalEntityClass(entity);
        if (entityOriginalClass.isAnnotationPresent(UniqueKey.class)) {
            final UniqueKey uniqueKey = (UniqueKey) entityOriginalClass.getAnnotation(UniqueKey.class);
            final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
            final Root<?> root = criteriaQuery.from(entityOriginalClass);
            final List<Predicate> predicates = new ArrayList<>(uniqueKey.columnNames().length);
            try {
                for (int i = 0; i < uniqueKey.columnNames().length; i++) {
                    final String propertyName = uniqueKey.columnNames()[i];
                    final PropertyDescriptor desc = new PropertyDescriptor(propertyName, entityOriginalClass);
                    final Method readMethod = desc.getReadMethod();
                    final Object propertyValue = readMethod.invoke(entity);
                    final Predicate predicate = criteriaBuilder.equal(root.get(propertyName), propertyValue);
                    predicates.add(predicate);
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ie) {
                throw new RuntimeException("invalid use of the @UniqueKey annotation", ie);
            }
            criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
            final TypedQuery<Object> typedQuery = entityManager.createQuery(criteriaQuery);
            final FlushModeType currentFlushMode = entityManager.getFlushMode();
            entityManager.setFlushMode(FlushModeType.COMMIT);
            final List<Object> resultSet = typedQuery.getResultList();
            final Object currentId = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            entityManager.setFlushMode(currentFlushMode);
            if (currentId != null) {
            /* Updating an existing entity */
                final List<S> entities = (List<S>) resultSet;
                for (S existingEntity : entities) {
                    final Object existingId = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(existingEntity);
                    if (!currentId.equals(existingId)) {
                        throw new CustomParametrizedException(
                            ErrorConstants.ERR_UNIQUENESS_VIOLATION, ErrorConstants.ERR_UNIQUENESS_VIOLATION_DESC,
                            entityOriginalClass, uniqueKey.columnNames());
                    }
                }
            } else {
            /* Creating a new entity */
                if (resultSet.size() > 0) {
                    throw new CustomParametrizedException(
                        ErrorConstants.ERR_UNIQUENESS_VIOLATION, ErrorConstants.ERR_UNIQUENESS_VIOLATION_DESC,
                        entityOriginalClass, uniqueKey.columnNames());
                }
            }
        }
    }

    private <S extends T> Class<?> retrieveOriginalEntityClass (S entity) {
        String className = entity.getClass().getName();
        if(className.contains("_$$_"))
        {
            className = className.substring(0,className.indexOf("_$$_"));
            try {
                return Application.class.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("invalid application configuration", cnfe);
            }
        } else {
            return entity.getClass();
        }
    }
}
