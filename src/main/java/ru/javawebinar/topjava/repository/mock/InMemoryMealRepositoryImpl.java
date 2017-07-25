package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(1);

    private static final int DEFAULT_USER_ID = 0;
    private static final int ADMIN_ID = 1;

    {
        MealsUtil.MEALS.forEach(meal -> this.save(DEFAULT_USER_ID, meal));

        MealsUtil.ADMIN_MEALS.forEach(meal -> this.save(ADMIN_ID, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }

        return repository.computeIfAbsent(userId, integer -> new ConcurrentHashMap<>())
                .put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> integerMealMap = repository.get(userId);
        return integerMealMap != null && integerMealMap.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> integerMealMap = repository.get(userId);

        return integerMealMap == null ? null : integerMealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> integerMealMap = repository.get(userId);
        return new ArrayList<>(integerMealMap
                .values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList()));
    }

    @Override
    public List<Meal> getAllBetweenDate(int userId, LocalDate startDate, LocalDate endDate) {
        Map<Integer, Meal> integerMealMap = repository.get(userId);
        return new ArrayList<>(integerMealMap
                .values()
                .stream()
                .filter(meal -> meal.getDateTime().toLocalDate().isAfter(startDate)
                        && meal.getDateTime().toLocalDate().isBefore(endDate))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList()));
    }
}

