package org.example.lab2.controller;

/* Осуществляющий проверку попадания точки в область на
координатной плоскости и формирующий HTML-страницу с
результатами проверки. Должен обрабатывать все запросы,
содержащие сведения о координатах точки и радиусе области.
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.example.lab2.model.Checker;
import org.example.lab2.model.PointRecord;
import org.example.lab2.model.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class AreaCheckServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        Validator validator = new Validator();
        Checker checker = new Checker();

        try {
            Double x = parseDoubleSafe(request.getParameter("x"));
            Double y = parseDoubleSafe(request.getParameter("y"));
            Double r = parseDoubleSafe(request.getParameter("r"));
            boolean byClick = Boolean.parseBoolean(request.getParameter("byClick"));


            if (!validator.isValid(x, y, r)) {
                sendError(response, "Параметры вне допустимого диапазона");
                return;
            }

            long startTime = Long.parseLong((String) request.getAttribute("startTime"));

            boolean hit = checker.isHit(x, y, r);

            long currentTime = System.currentTimeMillis();
            PointRecord record = new PointRecord(
                    x, y, r, hit,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                            .format(java.time.LocalDateTime.now()),
                    currentTime - startTime
            );

            ServletContext context = getServletContext();
            LinkedList<PointRecord> history = (LinkedList<PointRecord>) context.getAttribute("history");
            if (history == null) {
                history = new LinkedList<>();
            }
            history.addFirst(record);
            context.setAttribute("history", history);

            if (byClick) {
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().print(record.toJson());
            } else {
                response.sendRedirect(request.getContextPath()
                        + "/result.jsp?x=" + x + "&y=" + y + "&r=" + r + "&hit=" + hit);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            sendError(response, "Произошла внутренняя ошибка сервера.");
        }
    }


    private Double parseDoubleSafe(String param) {
        if (param == null) return null;
        try {
            return Double.parseDouble(param);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void sendError(HttpServletResponse response, String message) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try (PrintWriter out = response.getWriter()) {
            out.write(message);
        }
    }
}
