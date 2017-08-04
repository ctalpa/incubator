package it.ctalpa.planning.modules.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by c.talpa on 24/05/2017.
 */
@Mapper
public interface UserMapper  {

    UserDTO toViewModel(User user);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    User toModel(UserDTO userDto);
}
