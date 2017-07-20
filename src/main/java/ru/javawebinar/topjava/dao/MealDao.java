package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    public Meal get(int userId, int id);

    public List<Meal> getAll(int id);

    public void save(int userId, Meal meal);

    public void update(int userId, Meal meal);

    public void delete(int userId, int id);
}
