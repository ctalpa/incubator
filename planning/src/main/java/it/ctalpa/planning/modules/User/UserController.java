package it.ctalpa.planning.modules.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('user_view')")
    public Page<UserDTO> getAllUser(@SortDefault(sort ={"login"}, direction = Sort.Direction.ASC) Pageable pageable) {
        final Page<User> userPage= userService.getAllUsers(pageable);
        final Page<UserDTO> userDTOPage = new PageImpl<UserDTO>(userMapper.toViewModel(userPage), pageable,userPage.getTotalElements());

        return userDTOPage;
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user_view')")
    public UserDTO getUserById(Integer userId){

        User user =userService.getUserById(userId);
        return userMapper.toViewModel(user);
    }

    @GetMapping("/current")
    public UserDTO getCurrentUser(){
        return userMapper.toViewModel(userService.getAuthenticatedUser());
    }

}
