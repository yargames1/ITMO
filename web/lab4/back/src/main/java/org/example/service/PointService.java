package org.example.service;

import org.example.entity.PointResult;
import org.example.entity.User;
import org.example.repo.PointResultRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;

@Stateless
public class PointService {

    @EJB
    private PointResultRepository repository;

    @EJB
    private AuthService authService;

    public PointResult regPoint(double x, double y, double r, HttpServletRequest request) {
        validate(x, y, r);
        User user = authService.getCurrentUser(request);
        long startTime = System.nanoTime();
        boolean hit = checkHit(x, y, r);

        PointResult result = new PointResult();
        result.setUser(user);
        result.setX(x);
        result.setY(y);
        result.setR(r);
        result.setHit(hit);
        result.setServerTime(LocalDateTime.now());
        result.setProcessingTime(
                (System.nanoTime() - startTime) / 1_000_000.0
        );

        repository.save(result);
        return result;
    }

    private void validate(double x, double y, double r) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(r)) {
            throw new BadRequestException("Values must be finite numbers");
        }

        if (r <= 0) {
            throw new BadRequestException("Radius must be positive");
        }

        double limit = 1_000_000;
        if (Math.abs(x) > limit || Math.abs(y) > limit || Math.abs(r) > limit) {
            throw new BadRequestException("Values are out of range");
        }
    }

    private boolean checkHit(double x, double y, double r) {
        if (x <= 0 && y <= 0 && y >= -2 * x - r) return true;
        if (x >= 0 && y >= 0 && (x <= r/2 && y <= r )) return true;
        return x >= 0 && y <= 0 && (x * x + y * y <= r * r);
    }
}