package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ActivityRepository extends JpaRepository<Activity, String> {
  @Query(value = "SELECT * from activity where id LIKE :idNum limit 1 offset :offset", nativeQuery = true)
  Activity getRandomActivity(String idNum, int offset);

  @Query(value = "SELECT count(id) from activity where id LIKE :idNum", nativeQuery = true)
  int getRandomActivityCount(@Param("idNum") String idNum);
}
