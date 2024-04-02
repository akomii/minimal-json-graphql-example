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

package org.example.graphql.elasticsearch;

import java.util.List;
import org.example.graphql.elasticsearch.models.ElasticAuthor;
import org.example.graphql.elasticsearch.models.ElasticBook;
import org.example.graphql.server.AbstractGraphQlTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * A specialized test suite for validating GraphQL interactions with an Elasticsearch-backed graph database. This class extends
 * {@link AbstractGraphQlTest} and integrates with an Elasticsearch container to simulate a production-like Elasticsearch environment. It implements
 * abstract methods from the parent class, providing Elasticsearch-specific logic for executing GraphQL queries and mutations against
 * {@link ElasticAuthor} and {@link ElasticBook} entities. This ensures comprehensive testing of your GraphQL resolvers and their behavior with an
 * Elasticsearch datastore.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MyApp.class)
@AutoConfigureGraphQlTester
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class ElasticGraphQlTest extends AbstractGraphQlTest<ElasticAuthor, ElasticBook> {

  @Container
  private static final ElasticContainer CONTAINER = new ElasticContainer();

  @Override
  protected List<ElasticAuthor> fetchAuthors() {
    String query = "query { authors { firstName lastName publishedBookIds } }";
    return graphQlTester.document(query).execute().path("data.authors")
        .entity(new ParameterizedTypeReference<List<ElasticAuthor>>() {
        }).get();
  }

  @Override
  protected List<ElasticBook> fetchBooks() {
    String query = "query { books { title author { id } } }";
    return graphQlTester.document(query).execute().path("data.books")
        .entity(new ParameterizedTypeReference<List<ElasticBook>>() {
        }).get();
  }

  @Override
  protected ElasticAuthor getAuthorById(Long id) {
    String query = String.format(
        "query { authorById(id: \"%s\") { firstName lastName publishedBookIds } }", id);
    return graphQlTester.document(query).execute().path("data.authorById")
        .entity(new ParameterizedTypeReference<ElasticAuthor>() {
        }).get();
  }

  @Override
  protected ElasticBook getBookById(Long id) {
    String query = String.format(
        "query { bookById(id: \"%s\") { title publishedYear author { firstName lastName } } }", id);
    return graphQlTester.document(query).execute().path("data.bookById")
        .entity(new ParameterizedTypeReference<ElasticBook>() {
        }).get();
  }
}
