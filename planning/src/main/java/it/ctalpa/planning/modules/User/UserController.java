package it.ctalpa.planning.modules.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by c.talpa on 24/05/2017.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper){
        this.userService=userService;
        this.userMapper=userMapper;
    }

    @GetMapping
    public String getAllUser(){
        return "Ciao";
    }


}
