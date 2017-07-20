package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AuthorisedUser;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {
    {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        meals.forEach(meal -> save(AuthorisedUser.getId(), meal));
    }

    private static Map<Integer, Map<Integer, Meal>> map = new ConcurrentHashMap<>();

    private static AtomicInteger id = new AtomicInteger(0);

    private int generateId() {
        return id.incrementAndGet();
    }

    @Override
    public Meal get(int userId, int id) {
        return map.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> integerMealMap = map.get(userId);
        return new ArrayList<>(integerMealMap.values());
    }

    @Override
    public void save(int userId, Meal meal) {
        meal.setId(generateId());
        map
                .computeIfAbsent(userId, integer -> new ConcurrentHashMap<>())
                .put(meal.getId(), meal);
    }

    @Override
    public void update(int userId, Meal meal) {
    }

    @Override
    public void delete(int userId, int id) {
        map.get(userId).remove(id);
    }
}
