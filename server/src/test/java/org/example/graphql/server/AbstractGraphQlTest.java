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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.test.tester.GraphQlTester;

/**
 * Abstract base class for GraphQL integration tests, providing common setup and utility methods for
 * testing GraphQL operations.
 * <p>
 * Usage of this class involves extending it in a concrete test class and implementing the abstract
 * methods with logic for enabling the deserialization of GraphQL responses into specific entity
 * types. Java's type erasure removes generic type information at runtime, making it impossible for
 * Jackson to instantiate objects of generic types or interfaces directly from JSON responses.
 * </p>
 *
 * @param <A> the type parameter extending the {@link Author} interface, representing the author
 *            entity in the GraphQL schema.
 * @param <B> the type parameter extending the {@link Book} interface, representing the book entity
 *            in the GraphQL schema.
 */
public abstract class AbstractGraphQlTest<A extends Author, B extends Book> {

  @Autowired
  protected GraphQlTester graphQlTester;

  private static Long authorId1;
  private static Long authorId2;
  private static Long bookId1;
  private static Long bookId2;
  private static Long bookId3;

  protected abstract List<A> fetchAuthors();

  protected abstract List<B> fetchBooks();

  protected abstract A getAuthorById(Long id);

  protected abstract B getBookById(Long id);

  @Test
  @Order(1)
  void whenPopulate_thenSucceed() {
    populateData();
    verifyAuthorsExist();
    verifyBooksExist();
  }

  private void populateData() {
    authorId1 = createAuthor("Joshua", "Bloch");
    authorId2 = createAuthor("John", "Doe");
    bookId1 = createBook("Effective Java", 2000, authorId1);
    bookId2 = createBook("My Diary, Part 1", 2023, authorId2);
    bookId3 = createBook("My Diary, Part 2", 2023, authorId2);
  }

  private Long createAuthor(String firstName, String lastName) {
    String mutation = String.format(
        "mutation { createAuthor(firstName: \"%s\", lastName: \"%s\") { id } }", firstName,
        lastName);
    return graphQlTester.document(mutation).execute().path("data.createAuthor.id")
        .entity(new ParameterizedTypeReference<Long>() {
        }).get();
  }

  private Long createBook(String title, int publishedYear, Long authorId) {
    String mutation = String.format(
        "mutation { createBook(title: \"%s\", publishedYear: %d, authorId: \"%s\") { id } }", title,
        publishedYear, authorId);
    return graphQlTester.document(mutation).execute().path("data.createBook.id")
        .entity(new ParameterizedTypeReference<Long>() {
        }).get();
  }

  private void verifyAuthorsExist() {
    List<A> authors = fetchAuthors();
    assertThat(authors.size()).isEqualTo(2);
    verifyAuthorInList(authors, "Joshua", "Bloch", 1);
    verifyAuthorInList(authors, "John", "Doe", 2);
  }

  private void verifyAuthorInList(List<A> authors, String firstName, String lastName,
      int publishedBooks) {
    assertTrue(authors.stream().anyMatch(
        author -> firstName.equals(author.getFirstName()) && lastName.equals(author.getLastName())
            && author.getPublishedBookIds().size() == publishedBooks));
  }

  private void verifyBooksExist() {
    List<B> books = fetchBooks();
    assertThat(books.size()).isEqualTo(3);
    verifyBookInList(books, "Effective Java", authorId1);
    verifyBookInList(books, "My Diary, Part 1", authorId2);
    verifyBookInList(books, "My Diary, Part 2", authorId2);
  }

  private void verifyBookInList(List<B> books, String title, Long authorId) {
    assertTrue(books.stream().anyMatch(
        book -> title.equals(book.getTitle()) && authorId.equals(book.getAuthor().getId())));
  }

  @Test
  @Order(2)
  void createInvalidAuthor() {
    String mutation = "mutation { createAuthor(firstName: 123, lastName: 456) { id } }";
    GraphQlTester.Response response = graphQlTester.document(mutation).execute();
    response.errors().satisfy(errors -> {
      Assertions.assertThat(errors).isNotEmpty();
      Assertions.assertThat(errors.get(0).getMessage()).contains("Validation error");
    });
  }

  @Test
  @Order(3)
  void createInvalidBook() {
    String mutation = "mutation { createBook(title: 123, publishedYear: \"ABC\", authorId: \"invalid-author-id\") { id } }";
    GraphQlTester.Response response = graphQlTester.document(mutation).execute();
    response.errors().satisfy(errors -> {
      Assertions.assertThat(errors).isNotEmpty();
      Assertions.assertThat(errors.get(0).getMessage()).contains("Validation error");
    });
  }

  @Test
  @Order(4)
  void createBookWithNonexistentAuthor() {
    String mutation = "mutation { createBook(title: \"A Good Book\", publishedYear: 2023, authorId: \"nonexistent\") { id } }";
    GraphQlTester.Response response = graphQlTester.document(mutation).execute();
    response.errors().satisfy(errors -> {
      Assertions.assertThat(errors).isNotEmpty();
      Assertions.assertThat(errors.get(0).getMessage()).contains("INTERNAL_ERROR");
    });
  }

  @Test
  @Order(5)
  void checkSingleAuthor() {
    A author = getAuthorById(authorId2);
    assertThat(author.getFirstName()).isEqualTo("John");
    assertThat(author.getLastName()).isEqualTo("Doe");
    assertThat(author.getPublishedBookIds().size()).isEqualTo(2);
  }

  @Test
  @Order(6)
  void getNonexistentAuthorById() {
    String query = "query { authorById(id: 99) { id firstName lastName publishedBookIds } }";
    graphQlTester.document(query).execute().path("data.authorById").valueIsNull();
  }

  @Test
  @Order(7)
  void checkSingleBook() {
    B book = getBookById(bookId1);
    assertThat(book.getTitle()).isEqualTo("Effective Java");
    assertThat(book.getPublishedYear()).isEqualTo(2000);
    Author author = book.getAuthor();
    assertThat(author.getFirstName()).isEqualTo("Joshua");
    assertThat(author.getLastName()).isEqualTo("Bloch");
  }

  @Test
  @Order(8)
  void getNonExistingBookById() {
    String query = "query { bookById(id: 99) { id title publishedYear author { id } } }";
    graphQlTester.document(query).execute().path("data.bookById").valueIsNull();
  }

  @Test
  @Order(9)
  void deleteAuthor() {
    String mutation = String.format("mutation { deleteAuthor(id: \"%s\") }", authorId1);
    graphQlTester.document(mutation).execute().path("data.deleteAuthor").entity(Boolean.class)
        .isEqualTo(true);
    assertThat(fetchBooks().size()).isEqualTo(2);
    assertThat(fetchAuthors().size()).isEqualTo(1);
  }

  @Test
  @Order(10)
  void deleteNonexistentAuthor() {
    String mutation = "mutation { deleteAuthor(id: 99) }";
    graphQlTester.document(mutation).execute().path("data.deleteAuthor").entity(Boolean.class)
        .isEqualTo(false);
  }

  @Test
  @Order(10)
  void deleteBook() {
    String mutation = String.format("mutation { deleteBook(id: \"%s\") }", bookId3);
    graphQlTester.document(mutation).execute().path("data.deleteBook").entity(Boolean.class)
        .isEqualTo(true);
    assertThat(fetchBooks().size()).isEqualTo(1);
    A author = getAuthorById(authorId2);
    assertThat(author.getPublishedBookIds().size()).isEqualTo(1);
  }

  @Test
  @Order(11)
  void deleteNonexistentBook() {
    String mutation = "mutation { deleteBook(id: 99) }";
    graphQlTester.document(mutation).execute().path("data.deleteBook").entity(Boolean.class)
        .isEqualTo(false);
  }
}
