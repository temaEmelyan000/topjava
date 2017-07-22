package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private MealDaoImpl mealDao;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealDao = new MealDaoImpl();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Map<String, String[]> parameterMap = req.getParameterMap();
        TemporalAccessor dateTime = dateTimeFormatter.parse(parameterMap.get("dateTime")[0]);
        LocalDateTime localDateTime = LocalDateTime.from(dateTime);


        if (req.getParameter("id").isEmpty()) {
            Meal meal = new Meal(
                    localDateTime,
                    parameterMap.get("description")[0],
                    Integer.parseInt(parameterMap.get("calories")[0])
            );
            mealDao.save(AuthorisedUser.getId(), meal);
        } else {
            Meal meal = new Meal(
                    Integer.parseInt(parameterMap.get("id")[0]),
                    localDateTime,
                    parameterMap.get("description")[0],
                    Integer.parseInt(parameterMap.get("calories")[0])
            );
            mealDao.update(AuthorisedUser.getId(), meal);
        }

        resp.sendRedirect("meals");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (parameterMap.keySet().isEmpty()) {
            List<Meal> all = mealDao.getAll(AuthorisedUser.getId());
            List<MealWithExceed> filteredWithExceeded = MealsUtil.getFilteredWithExceeded(all, LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", filteredWithExceeded);

            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String action = parameterMap.get("action")[0];
            switch (action) {
                case "edit":
                    int id = Integer.parseInt(parameterMap.get("id")[0]);
                    Meal meal = mealDao.get(AuthorisedUser.getId(), id);

                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);

                    break;
                case "add":
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    break;
                case "delete":
                    id = Integer.parseInt(request.getParameter("id"));
                    mealDao.delete(AuthorisedUser.getId(), id);
                    response.sendRedirect("meals");
                    break;
            }
        }
    }
}
