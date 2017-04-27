package ct.app.apms.config.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface APMSRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    void refresh(T entity);
}
