package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealService {
    Meal save(int userId, Meal meal);

    void update(int userId, Meal meal);

    // false if not found
    void delete(int userId, int id);

    // null if not found
    Meal get(int userId, int id);

    List<Meal> getAll(int userId);

    List<Meal> getAllBetweenDates(int userId, LocalDate startDate, LocalDate endDate);
}