package it.ctalpa.planning.security;


import it.ctalpa.planning.modules.User.User;
import it.ctalpa.planning.modules.User.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log;
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        log = LoggerFactory.getLogger(UserDetailsService.class);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        login = login.toLowerCase();

        log.debug("Attempting to fetch user details for '{}'", login);

        User user = userRepository.getOneByLogin(login);

        if (user == null) {
            log.error("User '{}' not found!", login);
            throw new UsernameNotFoundException(String.format("User '%s' not found!", login));
        }

        log.debug("Attempting to fetch user permissions for '{}'", login);
        
        List<GrantedAuthority> authorities = user.getPermissions().stream()
            .map(p -> (GrantedAuthority) () -> p)
            .collect(Collectors.toList());

        log.debug("Loaded {} authorities for user", authorities.size());

        return new org.springframework.security.core.userdetails.User(login, user.getPassword(), authorities);
    }
}
