package com.netcracker.edu.kiar.bank.api.module.user;

import com.netcracker.edu.kiar.bank.api.module.exceptions.BusinessException;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserDTO;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserCreationForm;

import java.util.List;

public interface UserService {
    /**
     * Returns a list of all users.
     * @return List of UserDTO objects.
     */
    List<UserDTO> getAllUsers();

    /**
     * Finds a user by their username.
     * @param username Username of the user to find.
     * @return UserDTO object representing the user, or null if no user is found.
     * @throws BusinessException with the error code "ERR-0003" if no user found.
     */
    UserDTO findByUsername(String username);

    /**
     * Creates a new user.
     * @param form Form containing information for creating a new user.
     * @return UserDTO object representing the new user.
     */
    UserDTO createNewUser(UserCreationForm form);

    /**
     * Updates an existing user. In case if no user found throws BusinessException.
     * @param form Form containing information for updating a user.
     * @return UserDTO object representing the updated user.
     * @throws BusinessException with the error code "ERR-0003" if no user found.
     */
    UserDTO updateUser(UserCreationForm form);

    /**
     * Deletes a user by their username.
     * @param username Username of the user to delete.
     * @return UserDTO object representing the deleted user.
     * @throws BusinessException with the error code "ERR-0003" if no user found.
     */
    UserDTO deleteUser(String username);

    /**
     * Deletes currently logged-in user.
     * @return UserDTO object representing the deleted user.
     */
    UserDTO deleteCurrentUser();

    /**
     * Returns a UserDTO representing the currently logged-in user.
     * @return a UserDTO representing the currently logged-in user
     */
    UserDTO getCurrentUserInfo();
}
