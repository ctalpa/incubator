package ct.app.apms.modules.users;


import ct.app.apms.config.db.APMSRepository;

public interface UserRepository extends APMSRepository<User, Integer> {

    User getOneByLogin(String login);
}
