package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.naming.OperationNotSupportedException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class DataJpaUserRepositoryImpl implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = new Sort("name", "email");

    private final CrudUserRepository crudRepository;

    @Autowired
    public DataJpaUserRepositoryImpl(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findOne(id);
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public User getWithMeals(int id) throws OperationNotSupportedException {
        User user = crudRepository.findByIdAndFetchMealsEagerly(id);

        if (user == null) {
            user = get(id);
            if (user != null) {
                user.setMeals(Collections.emptyList());
            }
        } else {
            user.getMeals().sort(Comparator.comparing(Meal::getDateTime).reversed());
        }
        return user;
    }
}
