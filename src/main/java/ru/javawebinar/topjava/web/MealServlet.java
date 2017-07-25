package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;

    private ConfigurableApplicationContext appCtx;

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        String action = request.getParameter("action");
        if (action == null) {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                controller.create(AuthorizedUser.id(), meal);
            } else {
                controller.update(AuthorizedUser.id(), meal, meal.getId());
            }
            response.sendRedirect("meals");

        } else if (action.equals("filter")) {
            log.info("get filtered");

            LocalDate startDate = request.getParameter("startDate").isEmpty() ?
                    LocalDate.MIN :
                    LocalDate.from(dateFormatter.parse(request.getParameter("startDate")));
            request.setAttribute("startDate", startDate);

            LocalDate endDate = request.getParameter("endDate").isEmpty() ?
                    LocalDate.MAX :
                    LocalDate.from(dateFormatter.parse(request.getParameter("endDate")));
            request.setAttribute("endDate", endDate);

            LocalTime startTime = request.getParameter("startTime").isEmpty() ?
                    LocalTime.MIN :
                    LocalTime.from(timeFormatter.parse(request.getParameter("startTime")));
            request.setAttribute("startTime", startTime);

            LocalTime endTime = request.getParameter("endTime").isEmpty() ?
                    LocalTime.MAX :
                    LocalTime.from(timeFormatter.parse(request.getParameter("endTime")));
            request.setAttribute("endTime", endTime);

            request.setAttribute("meals",
                    controller.getAllBetweenDateTime(
                            AuthorizedUser.id(), LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime)));

            request.getRequestDispatcher("/meals.jsp").forward(request, response);

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(AuthorizedUser.id(), id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(AuthorizedUser.id(), getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.getAll(AuthorizedUser.id()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
