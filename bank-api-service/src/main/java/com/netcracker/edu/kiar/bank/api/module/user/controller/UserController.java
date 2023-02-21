package com.netcracker.edu.kiar.bank.api.module.user.controller;

import com.netcracker.edu.kiar.bank.api.module.user.UserService;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserCreationForm;
import com.netcracker.edu.kiar.bank.api.module.user.model.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * The UserController class is a REST controller that handles HTTP requests related to operations with user's profiles.
 * It maps the incoming requests to the corresponding methods of the UserService class.
 * The controller provides methods for getting all users, finding a user by username,
 * creating a new user, deleting and updating an existing user by username and deleting currently logged-in user.
 */
@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("http://localhost:5173/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles HTTP GET requests to "/api/v1/user/get_all" and returns a list of all users in the system.
     *
     * @return a ResponseEntity containing a list of UserDTO objects and an HTTP status code.
     */
    @GetMapping("/get_all")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Handles HTTP GET requests to "/api/v1/user/find_by_username/{username}" and returns the user with the specified username.
     *
     * @param username the username of the user to find.
     * @return a ResponseEntity containing a UserDTO object and an HTTP status code.
     */
    @GetMapping("/find_by_username/{username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        UserDTO user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/user/create_user" and creates a new user based on the information
     * in the UserCreationForm object passed in the request body.
     * @param form a UserCreationForm object containing the information needed to create a new user.
     * @return a ResponseEntity containing a UserDTO object and an HTTP status code.
     */
    @PostMapping("/create_user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreationForm form) {
        UserDTO user = userService.createNewUser(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Handles HTTP DELETE requests to "/api/v1/user/delete_by_username/{username}" and deletes the user with the
     * specified username.
     * @param username the username of the user to delete.
     * @return a ResponseEntity containing a UserDTO object and an HTTP status code.
     */
    @DeleteMapping("/delete_by_username/{username}")
    public ResponseEntity<UserDTO> deleteByUsername(@PathVariable String username) {
        UserDTO user = userService.deleteUser(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Handles HTTP DELETE requests to "/api/v1/user/delete_current_user" and deletes the currently logged-in user.
     * @return a ResponseEntity containing a UserDTO object and an HTTP status code.
     */
    @DeleteMapping("/delete_current_user")
    public ResponseEntity<UserDTO> deleteCurrentUser() {
        UserDTO user = userService.deleteCurrentUser();
        return ResponseEntity.ok(user);
    }

    /**
     * Handles HTTP POST requests to "/api/v1/user/update_user" and updates the information of the currently logged-in user.
     * @param form a UserCreationForm object containing the information needed to update the user.
     * @return a ResponseEntity containing a UserDTO object and an HTTP status code.
     */
    @PutMapping("/update_user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserCreationForm form) {
        UserDTO user = userService.updateUser(form);
        return ResponseEntity.ok(user);
    }
}