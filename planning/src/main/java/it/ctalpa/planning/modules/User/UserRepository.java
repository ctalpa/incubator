package it.ctalpa.planning.modules.User;

import it.ctalpa.planning.config.db.PlanningRepository;

/**
 * Created by c.talpa on 24/05/2017.
 */
public interface UserRepository extends PlanningRepository<User, Integer> {

    User getOneByLogin(String login);
}
