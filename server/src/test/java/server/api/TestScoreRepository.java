package server.api;

import commons.Score;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ScoreRepository;

public class TestScoreRepository implements ScoreRepository {
  public final List<Score> scores = new ArrayList<>();
  public final List<String> calledMethods = new ArrayList<>();

  private void call(String name) {
    calledMethods.add(name);
  }

  @Override
  public Iterable<Score> getLeaderboard() {
    call("getLeaderboard");
    scores.sort((scoreA, scoreB) -> Integer.compare(scoreB.points, scoreA.points));
    return scores;
  }

  @Override
  public List<Score> findAll() {
    call("findAll");
    return scores;
  }

  @Override
  public List<Score> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Score> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Score> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Score> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Score> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Score> long count(Example<S> example) {
    return 0;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public List<Score> findAllById(Iterable<Long> longs) {
    return null;
  }

  @Override
  public void deleteById(Long andLong) {

  }

  @Override
  public void delete(Score entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {

  }

  @Override
  public void deleteAll(Iterable<? extends Score> entities) {

  }

  @Override
  public void deleteAll() {
    call("deleteAll");
    scores.clear();
  }

  @Override
  public <S extends Score> S save(S entity) {
    call("save");
    entity.id = scores.size();
    scores.add(entity);
    return entity;

  }

  @Override
  public <S extends Score> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<Score> findById(Long andLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long andLong) {
    return false;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Score> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Score> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Score> entities) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {

  }

  @Override
  public Score getOne(Long andLong) {
    return null;
  }

  @Override
  public Score getById(Long andLong) {
    return null;
  }

  @Override
  public <S extends Score> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Score> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Score, R> R findBy(Example<S> example,
                                       Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }
}
