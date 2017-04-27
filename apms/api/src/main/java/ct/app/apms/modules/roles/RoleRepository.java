package ct.app.apms.modules.roles;


import ct.app.apms.config.db.APMSRepository;

import java.util.Collection;


public interface RoleRepository extends APMSRepository<Role, Integer> {

    Collection<Role> findAllByIdIn(Collection<Integer> ids);

    @Override
    void refresh(Role entity);
}
