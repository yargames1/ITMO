package org.example.rest;


import org.example.entity.User;
import org.example.repo.UserRepository;
import org.example.rest.dto.LoginRequest;
import org.example.rest.dto.UserDto;
import org.example.service.AuthService;
import org.example.service.PasswordService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/main")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Context
    private HttpServletRequest request;

    @EJB
    private UserRepository userRepository;

    @EJB
    private PasswordService passwordService;

    @EJB
    private AuthService authService;

    @POST
    @Path("/register")
    public Response register(LoginRequest loginRequest) {

        if (loginRequest.getLogin() == null || loginRequest.getPasswd() == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Необходимы логин и пароль")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        boolean exists = userRepository
                .findByLogin(loginRequest.getLogin())
                .isPresent();

        if (exists) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("Пользователь уже существует")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        String salt = passwordService.generateSalt();
        String hash = passwordService.hashPassword(
                loginRequest.getPasswd(), salt
        );

        User user = new User();
        user.setLogin(loginRequest.getLogin());
        user.setSalt(salt);
        user.setPasswdHash(hash);

        userRepository.save(user);

        authService.login(user, request);

        return Response.status(Response.Status.CREATED).build();
    }


    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest){

        User user = userRepository
                .findByLogin(loginRequest.getLogin())
                .orElse(null);

        if (user == null){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Неверный логин или пароль") // Тут разумеется логин, но вроде не принято говорить, почему нельзя пройти
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        boolean valid = passwordService.checkPassword(
                loginRequest.getPasswd(), user.getSalt(), user.getPasswdHash()
        );

        if (!valid) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Неверный логин или пароль") // Ну а тут соответственно пароль
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        authService.login(user, request);

        return Response.ok().build();

    }

    @POST
    @Path("/logout")
    public Response logout(){
        authService.logout(request);
        return Response.ok().build();
    }

    @GET
    @Path("/me")
    public UserDto me(){
        return new UserDto(authService.getCurrentUser(request));
    }
}
