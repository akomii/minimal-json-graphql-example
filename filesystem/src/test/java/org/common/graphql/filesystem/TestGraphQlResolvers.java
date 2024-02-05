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

package org.common.graphql.filesystem;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.example.graphql.filesystem.MyApp;
import org.example.graphql.filesystem.models.FileSystemAuthor;
import org.example.graphql.filesystem.models.FileSystemBook;
import org.example.graphql.server.models.Author;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Path;

/**
 * This class contains unit tests for GraphQL resolvers. It tests the creation, retrieval, and
 * deletion of authors and books. It uses the Spring Boot Test environment with a GraphQL tester and
 * a Redis container. The tests are ordered using the @Order annotation.
 */
// TODO merge Tests and put abstract test into server package
// TODO add clean up of created dirs
// TODO fix bugs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MyApp.class)
@AutoConfigureGraphQlTester
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestGraphQlResolvers {

  @Autowired
  private GraphQlTester graphQlTester;

  private static Long AUTHOR_ID_1;
  private static Long AUTHOR_ID_2;
  private static Long BOOK_ID_1;
  private static Long BOOK_ID_2;
  private static Long BOOK_ID_3;


  @Test
  @Order(1)
  void populate() {
    AUTHOR_ID_1 = createAuthor("Joshua", "Bloch");
    AUTHOR_ID_2 = createAuthor("John", "Doe");
    BOOK_ID_1 = createBook("Effective Java", 2000, AUTHOR_ID_1);
    BOOK_ID_2 = createBook("My Diary, Part 1", 2023, AUTHOR_ID_2);
    BOOK_ID_3 = createBook("My Diary, Part 2", 2023, AUTHOR_ID_2);
    checkAuthors();
    checkBooks();
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

  private void checkAuthors() {
    List<FileSystemAuthor> authors = getAuthors();
    assertThat(authors.size()).isEqualTo(2);
    assertTrue(authors.stream().anyMatch(
        author -> "Joshua".equals(author.getFirstName()) && "Bloch".equals(author.getLastName())
            && author.getPublishedBookIds().size() == 1));
    assertTrue(authors.stream().anyMatch(
        author -> "John".equals(author.getFirstName()) && "Doe".equals(author.getLastName())
            && author.getPublishedBookIds().size() == 2));
  }

  private List<FileSystemAuthor> getAuthors() {
    String query = "query { authors { firstName lastName publishedBookIds } }";
    return graphQlTester.document(query).execute().path("data.authors")
        .entity(new ParameterizedTypeReference<List<FileSystemAuthor>>() {
        }).get();
  }

  private void checkBooks() {
    List<FileSystemBook> books = getBooks();
    assertThat(books.size()).isEqualTo(3);
    assertTrue(books.stream().anyMatch(
        book -> "Effective Java".equals(book.getTitle())
            && AUTHOR_ID_1.equals(book.getAuthor().getId())));
    assertTrue(books.stream().anyMatch(
        book -> "My Diary, Part 1".equals(book.getTitle())
            && AUTHOR_ID_2.equals(book.getAuthor().getId())));
    assertTrue(books.stream().anyMatch(
        book -> "My Diary, Part 2".equals(book.getTitle())
            && AUTHOR_ID_2.equals(book.getAuthor().getId())));
  }

  private List<FileSystemBook> getBooks() {
    String query = "query { books { title author { id } } }";
    return graphQlTester.document(query).execute().path("data.books")
        .entity(new ParameterizedTypeReference<List<FileSystemBook>>() {
        }).get();
  }

  @Test
  @Order(2)
  void createInvalidAuthor() {
    String mutation = "mutation { createAuthor(firstName: 99, lastName: 99) { id } }";
    graphQlTester.document(mutation).execute().errors().expect(error -> {
      assertThat(error.getMessage()).startsWith("Validation error");
      return true;
    });
  }

  @Test
  @Order(3)
  void createInvalidBook() {
    String mutation = "mutation { createBook(title: 99, publishedYear: 99, authorId: \"some-valid-author\") { id } }";
    graphQlTester.document(mutation).execute().errors().expect(error -> {
      assertThat(error.getMessage()).startsWith("Validation error");
      return true;
    });
  }

  @Test
  @Order(4)
  void createBookWithNonexistingAuthor() {
    String mutation = "mutation { createBook(title: \"A Good Book\", publishedYear: 2023, authorId: \"nonexistent\") { id } }";
    graphQlTester.document(mutation).execute().errors().expect(error -> {
      assertThat(error.getMessage()).startsWith("INTERNAL_ERROR");
      return true;
    });
  }

  @Test
  @Order(5)
  void checkSingleAuthor() {
    FileSystemAuthor author = getAuthorById(AUTHOR_ID_2);
    assertThat(author.getFirstName()).isEqualTo("John");
    assertThat(author.getLastName()).isEqualTo("Doe");
    assertThat(author.getPublishedBookIds().size()).isEqualTo(2);
  }

  private FileSystemAuthor getAuthorById(Long id) {
    String query = String.format(
        "query { authorById(id: \"%s\") { firstName lastName publishedBookIds } }", id);
    return graphQlTester.document(query).execute().path("data.authorById")
        .entity(new ParameterizedTypeReference<FileSystemAuthor>() {
        }).get();
  }

  @Test
  @Order(6)
  void getNonExistingAuthorById() {
    String query = "query { authorById(id: \"99\") { id } }";
    Path path = graphQlTester.document(query).execute().path("data.authorById").valueIsNull();
    assertThat(path).isNotNull();
  }

  @Test
  @Order(7)
  void checkSingleBook() {
    FileSystemBook book = getBookById(BOOK_ID_1);
    assertThat(book.getTitle()).isEqualTo("Effective Java");
    assertThat(book.getPublishedYear()).isEqualTo(2000);
    Author author = book.getAuthor();
    assertThat(author.getFirstName()).isEqualTo("Joshua");
    assertThat(author.getLastName()).isEqualTo("Bloch");
  }

  private FileSystemBook getBookById(Long id) {
    String query = String.format(
        "query { bookById(id: \"%s\") { title publishedYear author { firstName lastName } } }", id);
    return graphQlTester.document(query).execute().path("data.bookById")
        .entity(new ParameterizedTypeReference<FileSystemBook>() {
        }).get();
  }

  @Test
  @Order(8)
  void getNonExistingBookById() {
    String query = "query { bookById(id: \"99\") { id } }";
    Path path = graphQlTester.document(query).execute().path("data.bookById").valueIsNull();
    assertThat(path).isNotNull();
  }

  @Test
  @Order(9)
  void deleteAuthor() {
    String mutation = String.format("mutation { deleteAuthor(id: \"%s\") }", AUTHOR_ID_1);
    Boolean isDeleted = graphQlTester.document(mutation).execute().path("data.deleteAuthor")
        .entity(new ParameterizedTypeReference<Boolean>() {
        }).get();
    assertThat(isDeleted).isTrue();
    assertThat(getBooks().size()).isEqualTo(2);
    assertThat(getAuthors().size()).isEqualTo(1);
  }

  @Test
  @Order(10)
  void deleteNonexistingAuthor() {
    String mutation = "mutation { deleteAuthor(id: \"99\") }";
    Boolean isDeleted = graphQlTester.document(mutation).execute().path("data.deleteAuthor")
        .entity(new ParameterizedTypeReference<Boolean>() {
        }).get();
    assertThat(isDeleted).isFalse();
  }

  @Test
  @Order(10)
  void deleteBook() {
    String mutation = String.format("mutation { deleteBook(id: \"%s\") }", BOOK_ID_3);
    Boolean isDeleted = graphQlTester.document(mutation).execute().path("data.deleteBook")
        .entity(new ParameterizedTypeReference<Boolean>() {
        }).get();
    assertThat(isDeleted).isTrue();
    assertThat(getBooks().size()).isEqualTo(1);
    FileSystemAuthor author = getAuthorById(AUTHOR_ID_2);
    assertThat(author.getPublishedBookIds().size()).isEqualTo(1);
  }

  @Test
  @Order(11)
  void deleteNonexistingBook() {
    String mutation = "mutation { deleteBook(id: \"99\") }";
    Boolean isDeleted = graphQlTester.document(mutation).execute().path("data.deleteBook")
        .entity(new ParameterizedTypeReference<Boolean>() {
        }).get();
    assertThat(isDeleted).isFalse();
  }
}
