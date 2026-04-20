package org.example.service;

import org.example.entity.User;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;

@Stateless
public class AuthService {

    public User getCurrentUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new NotAuthorizedException("User not authorized");
        }

        User user = (User) request
                .getSession(false)
                .getAttribute("user");

        if (user == null) {
            throw new NotAuthorizedException("User not authorized");
        }

        return user;
    }

    public void login(User user, HttpServletRequest request) {
        request.getSession(true).setAttribute("user", user);
    }

    public void logout(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
    }
}
