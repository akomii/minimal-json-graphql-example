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

package org.example.grahql.server.resolvers;

import graphql.schema.DataFetcher;
import java.util.ArrayList;
import java.util.List;
import org.example.grahql.server.models.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthorResolver {

  private static final Logger log = LoggerFactory.getLogger(AuthorResolver.class);

  private final List<Author> authors = new ArrayList<>();

  public AuthorResolver() {
    authors.add(new Author(1, "John", "Doe"));
    authors.add(new Author(2, "Jane", "Doe"));
  }

  public DataFetcher<Author> getAuthor() {
    return env -> {
      int id = env.getArgument("id");
      log.info("Fetching author with id: {}", id);
      return authors.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    };
  }

  public DataFetcher<List<Author>> getAuthors() {
    return env -> {
      log.info("Fetching all authors");
      return authors;
    };
  }

  public DataFetcher<Author> createAuthor() {
    return env -> {
      String firstName = env.getArgument("firstName");
      String lastName = env.getArgument("lastName");
      log.info("Creating author with firstName: {} and lastName: {}", firstName, lastName);
      Author author = new Author(authors.size() + 1, firstName, lastName);
      authors.add(author);
      return author;
    };
  }
}
