package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    Meal save(Meal meal);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id =:id AND m.user.id =:user_id")
    int delete(@Param("id") int id, @Param("user_id") int userId);

    Meal findFirstByIdAndUserId(Integer id, Integer userId);

    List<Meal> findAllByUserId(int userId, Sort sort);

    List<Meal> findAllByUserIdAndDateTimeBetween(int userId, LocalDateTime start, LocalDateTime end, Sort sort);

    //http://stackoverflow.com/questions/15359306/how-to-load-lazy-fetched-items-from-hibernate-jpa-in-my-controller
    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id = (:id)")
    Meal findByIdAndFetchUser(@Param("id") int id);

}
