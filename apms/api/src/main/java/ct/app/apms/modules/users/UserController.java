package ct.app.apms.modules.users;

import ct.app.apms.modules.roles.RoleViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public Page<UserViewModel> getAllUsers(
            @SortDefault(sort = {"login"}, direction = Sort.Direction.ASC) Pageable pageable) {
        final Page<User> page = userService.getAllUsers(pageable);
        final Page<UserViewModel> resultPage = new PageImpl<>(
            userMapper.toViewModel(page), pageable, page.getTotalElements()
        );
        return resultPage;
    }

    @GetMapping("/{id}")
    public UserViewModel getUserById(@PathVariable Integer id) {
        return userMapper.toViewModel(userService.getUserById(id));
    }

    /**
     * Returns the currently logged in user
     * @return
     */
    @GetMapping("/current")
    public UserViewModel getCurrentUser() {
        return userMapper.toViewModel(userService.getAuthenticatedUser());
    }

    @PostMapping
    public UserViewModel createUser(@Valid @RequestBody User user) {
        return userMapper.toViewModel(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public UserViewModel updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userMapper.toViewModel(userService.updateUser(id, user));
    }

    @PutMapping("/{id}/roles")
    public UserViewModel updateUserRoles(@PathVariable Integer id, @RequestBody Collection<RoleViewModel> roleIds) {
        return userMapper.toViewModel(userService.updateUserRoles(id, roleIds));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
