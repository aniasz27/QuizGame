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
   * Update the name of the person associated with the quote with the given id
   *
   * @param id        id of the quote
   * @param firstName new first name
   * @param lastName  new last name
   * @return Number of updated entities
   */
  @Transactional
  @Modifying
  @Query(
    value = "UPDATE Person p SET p.first_name = :firstName, p.last_name = :lastName WHERE p.id = :id",
    nativeQuery = true
  )
  @SuppressWarnings("unused")
  void updateNameById(
    @Param("id") long id,
    @Param("firstName") String firstName,
    @Param("lastName") String lastName
  );

  @Query(value = "select p.first_Name, p.last_name from Person p join quote q on q.person_id = p.id where q.quote = ?1",
    nativeQuery = true)
  String getAuthor(String quote);

  /**
   * Query the first quote of the person whose firstName coincides with the given firstName
   *
   * @param firstName firstName of person
   * @return quote associated to person with firstName
   */
  @Query("select q.quote from Quote q where q.person.firstName = ?1")
  String findByPerson(String firstName);
}