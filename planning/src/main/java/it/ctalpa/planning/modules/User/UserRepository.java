package it.ctalpa.planning.modules.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by c.talpa on 24/05/2017.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User getOneByLogin(String login);
}
