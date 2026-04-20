package org.example.rest;

import org.example.entity.User;
import org.example.repo.PointResultRepository;
import org.example.rest.dto.PointRequest;
import org.example.rest.dto.PointResultDto;
import org.example.service.AuthService;
import org.example.service.PointService;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/points")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PointController {

    @Context
    private HttpServletRequest request;

    @EJB
    private PointService service;

    @EJB
    private PointResultRepository repository;

    @EJB
    private AuthService authService;

    @POST
    public PointResultDto reg(PointRequest pointRequest) {

        return new PointResultDto(service.regPoint(
                pointRequest.getX(),
                pointRequest.getY(),
                pointRequest.getR(),
                request
        ));
    }

    @GET
    public List<PointResultDto> getAll() {
        User user = authService.getCurrentUser(request);
        System.out.println(user.getLogin());
        return repository.findAllByUser(user)
                .stream()
                .map(PointResultDto::new)
                .toList();
    }

    @DELETE
    public void clear() {
        User user = authService.getCurrentUser(request);
        repository.deleteAll(user);
    }

    @POST
    public void foo( ) {
        // DO PROTECTED
    }
}
