/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package server.database;

import commons.Quote;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
  /**
   * Query the first quote of the person whose firstName coincides with the given firstName
   *
   * @param firstName firstName of person
   * @return quote associated to person with firstName
   */
  @Query("select q.quote from Quote q where q.person.firstName = ?1")
  String findByPerson(String firstName);

  /**
   * Update the quote with the given id and quote
   *
   * @param id    id of the quote
   * @param quote new quote
   * @return Number of updated entities
   */
  @Transactional
  @Modifying
  @Query(value = "UPDATE Quote q SET q.quote = :quote WHERE q.id = :id", nativeQuery = true)
  int updateById(@Param("id") long id, @Param("quote") String quote);
}