package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(int userId, Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.save(userId, meal);
    }

    public void update(int userId, Meal meal, int id) {
        log.info("update {} with id={} and user id={}", meal, id, userId);
        checkIdConsistent(meal, id);
        service.update(userId, meal);
    }

    public void delete(int userId, int id) {
        log.info("delete {}", id);
        service.delete(userId, id);
    }

    public Meal get(int userId, int id) {
        log.info("get {}", id);
        return service.get(userId, id);
    }

    public List<MealWithExceed> getAll(int userId) {
        log.info("getAll");
        return MealsUtil
                .getWithExceeded(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> getAllBetweenDateTime(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Meal> allBetweenDates = service.getAllBetweenDates(userId, startDateTime.toLocalDate(), endDateTime.toLocalDate());

        return MealsUtil.getFilteredWithExceeded(allBetweenDates, startDateTime.toLocalTime(), endDateTime.toLocalTime(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

}