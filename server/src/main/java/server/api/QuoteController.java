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

package server.api;

import commons.Quote;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import server.database.QuoteRepository;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

  private final Random random;
  private final QuoteRepository repo;

  public QuoteController(Random random, QuoteRepository repo) {
    this.random = random;
    this.repo = repo;
  }

  @GetMapping(path = {"", "/"})
  public List<Quote> getAll() {
    return repo.findAll();
  }

  /**
   * Finds the first quote of the person whose firstName coincides with the given firstName
   *
   * @param firstName firstName of person
   * @return ResponseEntity of the quote String associated to person with firstName
   */
  @GetMapping("/byname/{name}")
  public ResponseEntity<String> getByName(@PathVariable("name") String firstName) {
    return ResponseEntity.ok(repo.findByPerson(firstName));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Quote> getById(@PathVariable("id") long id) {
    if (id < 0 || !repo.existsById(id)) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(repo.getById(id));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> deleteById(@PathVariable("id") long id) {
    if (id < 0 || !repo.existsById(id)) {
      return new ResponseEntity<>("Invalid Id", HttpStatus.BAD_REQUEST);
    }
    Quote deleted = repo.getById(id);
    repo.deleteById(id);
    return new ResponseEntity<>("Deletion Successful",
      HttpStatus.OK);
  }

  @PostMapping(path = {"", "/"})
  public ResponseEntity<Quote> add(@RequestBody Quote quote) {

    if (quote.person == null || isNullOrEmpty(quote.person.firstName) || isNullOrEmpty(quote.person.lastName)
      || isNullOrEmpty(quote.quote)) {
      return ResponseEntity.badRequest().build();
    }

    Quote saved = repo.save(quote);
    return ResponseEntity.ok(saved);
  }

  private static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }

  @GetMapping("rnd")
  public ResponseEntity<Quote> getRandom() {
    var idx = random.nextInt((int) repo.count());
    return ResponseEntity.ok(repo.getById((long) idx));
  }

  /**
   * Returns the name of the author for a certain quote
   *
   * @param quote to be searched for
   * @return first name + last name of the author / "Quote not found!" if it doesn't exist
   */
  @GetMapping("/author/{quote}")
  public ResponseEntity<String> getAuthor(@PathVariable("quote") String quote) {
    Iterator<Quote> allq = repo.findAll().iterator();
    while (allq.hasNext()) {
      Quote cq = allq.next();
      if (cq.quote.equals(quote)) {
        return ResponseEntity.ok(cq.person.firstName + " " + cq.person.lastName);
      }
    }
    return ResponseEntity.ok("Quote not found!");
  }

  /**
   * Updates a quote based on id
   *
   * @param id    id of the quote
   *              provided in the request url
   * @param quote New quote to replace the old quote
   *              provided in the request body
   * @return Number of updated entities
   */
  @PutMapping("/updateQuote/{id}/")
  public ResponseEntity<Integer> updateQuoteById(@PathVariable("id") long id, @RequestBody String quote) {
    if (id < 0 || !repo.existsById(id)) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(repo.updateById(id, quote));
  }
}