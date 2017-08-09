package it.ctalpa.planning.config.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface PlanningRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    void refresh(T entity);
}
