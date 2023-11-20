package com.rrg.springbootjpah2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import com.rrg.springbootjpah2.dto.ResponseDTO;
import com.rrg.springbootjpah2.dto.UserDTO;
import com.rrg.springbootjpah2.model.User;
import com.rrg.springbootjpah2.repository.UserRepository;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Schema;
import org.modelmapper.ModelMapper;
import javax.validation.Valid;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Operation(summary = "Get all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @ApiResponse(responseCode = "200", description = "Found the Users", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class)) })
    @GetMapping(path ="/users" , produces = {"application/json"})
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(required = false) String query) {
        try {

            List<UserDTO> users = userRepository.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @ApiResponse(responseCode = "200", description = "Found the User", content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserDTO.class)) })
    @GetMapping(path = "/users/{id}", produces = {"application/json"})
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") UUID id) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>( toDto(userData.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Creates a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the User"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PostMapping(path ="/users" , produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {

            if (existEmail(userDTO.getEmail())) {

                responseDTO.setMensaje("El Correo ya Esta Registrado");
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST) ;
            }

            User user = modelMapper.map(userDTO, User.class);
            System.out.println("User:" + user.toString());
            System.out.println("userDTO:" + userDTO);

            User _user = userRepository.save(user);
            responseDTO.setMensaje(toDto(_user).toString());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException d) {
            responseDTO.setMensaje(d.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            responseDTO.setMensaje(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Update  User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Updated"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PutMapping(path="/users/{id}" , produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserDTO userDTO) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User _user =  userData.get();

            return new ResponseEntity<>(toDto(userRepository.save(_user)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Removes a requested User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") UUID id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Removes all  User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private UserDTO toDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

private boolean existEmail (String email ) {
    return !userRepository.findByEmail(email).isEmpty();
}

}
