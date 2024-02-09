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

package org.example.graphql.redis;

import java.util.List;
import org.example.graphql.redis.models.RedisAuthor;
import org.example.graphql.redis.models.RedisBook;
import org.example.graphql.server.AbstractGraphQlTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * A concrete implementation of {@link AbstractGraphQlTest} tailored for testing GraphQL operations
 * within a Redis-based environment. This class leverages a Redis container to provide a realistic
 * testing context for author and book entities stored in Redis. It overrides abstract methods from
 * {@link AbstractGraphQlTest} to fetch and manipulate {@link RedisAuthor} and {@link RedisBook}
 * entities through GraphQL queries and mutations, ensuring that the GraphQL schema and resolvers
 * function as expected when interacting with Redis.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MyApp.class)
@AutoConfigureGraphQlTester
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class RedisGraphQlTest extends AbstractGraphQlTest<RedisAuthor, RedisBook> {

  @Container
  private static final RedisContainer CONTAINER = new RedisContainer();

  @Override
  protected List<RedisAuthor> fetchAuthors() {
    String query = "query { authors { firstName lastName publishedBookIds } }";
    return graphQlTester.document(query).execute().path("data.authors")
        .entity(new ParameterizedTypeReference<List<RedisAuthor>>() {
        }).get();
  }

  @Override
  protected List<RedisBook> fetchBooks() {
    String query = "query { books { title author { id } } }";
    return graphQlTester.document(query).execute().path("data.books")
        .entity(new ParameterizedTypeReference<List<RedisBook>>() {
        }).get();
  }

  @Override
  protected RedisAuthor getAuthorById(Long id) {
    String query = String.format(
        "query { authorById(id: \"%s\") { firstName lastName publishedBookIds } }", id);
    return graphQlTester.document(query).execute().path("data.authorById")
        .entity(new ParameterizedTypeReference<RedisAuthor>() {
        }).get();
  }

  @Override
  protected RedisBook getBookById(Long id) {
    String query = String.format(
        "query { bookById(id: \"%s\") { title publishedYear author { firstName lastName } } }", id);
    return graphQlTester.document(query).execute().path("data.bookById")
        .entity(new ParameterizedTypeReference<RedisBook>() {
        }).get();
  }
}
