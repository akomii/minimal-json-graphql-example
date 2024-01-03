/*
 * MIT License
 *
 * Copyright (c) 2024 Alexander Kombeiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.graphql.server;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.example.grahql.server.MyApp;
import org.example.grahql.server.persistence.BasicRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ContextConfiguration;

@GraphQlTest
@AutoConfigureGraphQlTester
@ContextConfiguration(classes = {MyApp.class, BasicRepository.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestGraphQLResolver {

  @Autowired
  private GraphQlTester graphQlTester;

  @Autowired
  private BasicRepository repository;

  @Test
  @Order(1)
  void getFirstAuthor() {
    String query = "query { authorById(id: \"1\") { id firstName lastName } }";
    graphQlTester
        .document(query)
        .execute()
        .path("data.authorById")
        .matchesJson("""
            {"id":"1","firstName":"Joshua","lastName":"Bloch"}
            """);
  }

  @Test
  @Order(2)
  void getAuthors() {
    String query = "query { authors { id firstName lastName } }";
    graphQlTester
        .document(query)
        .execute()
        .path("data.authors")
        .matchesJson("""
            [
            {"id":"1","firstName":"Joshua","lastName":"Bloch"},
            {"id":"2","firstName":"Douglas","lastName":"Adams"},
            {"id":"3","firstName":"Bill","lastName":"Bryson"}
            ]""");
  }

  @Test
  @Order(3)
  void createAuthor() {
    String mutation = "mutation { createAuthor(firstName: \"John\", lastName: \"Doe\") { id firstName lastName } }";
    graphQlTester
        .document(mutation)
        .execute()
        .path("data.createAuthor")
        .matchesJson("""
            {"id":"4","firstName":"John","lastName":"Doe"}
            """);
  }

  @Test
  @Order(4)
  void getFirstBook() {
    String query = "query { bookById(id: \"1\") { id title publishedYear author { id firstName lastName } } }";
    graphQlTester
        .document(query)
        .execute()
        .path("data.bookById")
        .matchesJson("""
            {"id":"1", "title":"Effective Java", "publishedYear":2017,
            "author":{"id":"1","firstName":"Joshua","lastName":"Bloch"}}
            """);
  }

  @Test
  @Order(5)
  void getBooks() {
    String query = "query { books { id title publishedYear author { firstName lastName } } }";
    graphQlTester
        .document(query)
        .execute()
        .path("data.books")
        .matchesJson("""
            [
            {"id":"1","title":"Effective Java","publishedYear":2017,
            "author":{"firstName":"Joshua","lastName":"Bloch"}},
            {"id":"2","title":"Hitchhiker's Guide to the Galaxy","publishedYear":1979,
            "author":{"firstName":"Douglas","lastName":"Adams"}},
            {"id":"3","title":"Down Under","publishedYear":2000,
            "author":{"firstName":"Bill","lastName":"Bryson"}}
            ]""");
  }

  @Test
  @Order(6)
  void createBookWithInvalidAuthor() {
    String mutation = "mutation { createBook(title: \"My Diary\", publishedYear: 2023, authorId: \"99\") { id title publishedYear author { firstName lastName } } }";
    graphQlTester
        .document(mutation)
        .execute()
        .errors()
        .expect(error -> {
          assertThat(error.getMessage()).startsWith("INTERNAL_ERROR");
          return true;
        });
  }
}
