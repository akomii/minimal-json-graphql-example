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

import java.util.List;
import org.example.graphql.filesystem.MyApp;
import org.example.graphql.filesystem.models.FileSystemAuthor;
import org.example.graphql.filesystem.models.FileSystemBook;
import org.example.graphql.server.AbstractGraphQlTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

/**
 * A concrete implementation of {@link AbstractGraphQlTest} tailored for testing GraphQL operations within a file system-based environment. It
 * overrides abstract methods from {@link AbstractGraphQlTest} to fetch and manipulate {@link FileSystemAuthor} and {@link FileSystemBook} entities
 * through GraphQL queries and mutations, ensuring that the GraphQL schema and resolvers function as expected when interacting with a file system
 * storage.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MyApp.class)
@AutoConfigureGraphQlTester
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileSystemGraphQlTest extends AbstractGraphQlTest<FileSystemAuthor, FileSystemBook> {

// TODO remove dirs after tests

  @Override
  protected List<FileSystemAuthor> fetchAuthors() {
    String query = "query { authors { firstName lastName publishedBookIds } }";
    return graphQlTester.document(query).execute().path("data.authors")
        .entity(new ParameterizedTypeReference<List<FileSystemAuthor>>() {
        }).get();
  }

  @Override
  protected List<FileSystemBook> fetchBooks() {
    String query = "query { books { title author { id } } }";
    return graphQlTester.document(query).execute().path("data.books")
        .entity(new ParameterizedTypeReference<List<FileSystemBook>>() {
        }).get();
  }

  @Override
  protected FileSystemAuthor getAuthorById(Long id) {
    String query = String.format(
        "query { authorById(id: \"%s\") { firstName lastName publishedBookIds } }", id);
    return graphQlTester.document(query).execute().path("data.authorById")
        .entity(new ParameterizedTypeReference<FileSystemAuthor>() {
        }).get();
  }

  @Override
  protected FileSystemBook getBookById(Long id) {
    String query = String.format(
        "query { bookById(id: \"%s\") { title publishedYear author { firstName lastName } } }", id);
    return graphQlTester.document(query).execute().path("data.bookById")
        .entity(new ParameterizedTypeReference<FileSystemBook>() {
        }).get();
  }
}
