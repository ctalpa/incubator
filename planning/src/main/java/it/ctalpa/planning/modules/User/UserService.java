package it.ctalpa.planning.modules.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by c.talpa on 24/05/2017.
 */
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id){
        return userRepository.getOne(id);
    }

    @Transactional(readOnly = true)
    public User getUserByLogin(String login){
        return userRepository.getOneByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return userRepository.getOneByLogin(userDetails.getUsername());
    }

}
