package org.example.lab2.controller;

/*
Определяющий тип запроса, и, в зависимости от того, содержит
ли запрос информацию о координатах точки и радиусе, делегирующий
его обработку одному из перечисленных ниже компонентов. Все запросы
внутри приложения должны передаваться этому сервлету (по методу GET
или POST в зависимости от варианта задания), остальные сервлеты с
веб-страниц напрямую вызываться не должны.
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ControllerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("startTime", Long.toString(System.currentTimeMillis()));

        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String r = request.getParameter("r");

        if (x != null && y != null && r != null) {
            // есть координаты на проверку
            RequestDispatcher dispatcher = request.getRequestDispatcher("/areaCheck");
            dispatcher.forward(request, response);
        }
        else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String historyNeed = request.getParameter("history");
        if ("true".equals(historyNeed)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/history");
            dispatcher.forward(request, response);
        }
        else{
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
    }
}

