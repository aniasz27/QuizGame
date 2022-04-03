package server.api;

import commons.Activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ActivityRepository;

public class TestActivityRepository implements ActivityRepository {

  public ArrayList<Activity> activities;

  @Override
  public Activity getRandomActivity(String idNum, int offset) {
    return null;
  }

  @Override
  public int getRandomActivityCount(String idNum) {
    return 0;
  }

  @Override
  public List<Activity> getSortedActivities() {
    return activities;
  }


  @Override
  public List<Activity> findAll() {
    return activities;
  }

  @Override
  public List<Activity> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Activity> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Activity> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public List<Activity> findAllById(Iterable<String> strings) {
    return null;
  }

  @Override
  public <S extends Activity> long count(Example<S> example) {
    return 0;
  }


  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(String s) {

  }

  @Override
  public void delete(Activity entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends String> strings) {

  }

  @Override
  public void deleteAll(Iterable<? extends Activity> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends Activity> S save(S entity) {
    activities.add(entity);
    return entity;
  }

  @Override
  public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
    activities = (ArrayList<Activity>) entities;
    return (List) activities;
  }

  @Override
  public Optional<Activity> findById(String s) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String s) {
    return false;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Activity> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Activity> entities) {

  }


  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<String> strings) {

  }

  @Override
  public Activity getOne(String s) {
    return null;
  }

  @Override
  public Activity getById(String s) {
    return null;
  }

  @Override
  public <S extends Activity> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }


  @Override
  public <S extends Activity> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Activity, R> R findBy(Example<S> example,
                                          Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  public List<Activity> setSortedActivities(ArrayList<Activity> list) {
    this.activities = list;
    return activities;
  }

}
