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

package org.example.graphql.elasticsearch.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.graphql.elasticsearch.utils.IdGenerator;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Represents a {@link Book} entity for Elasticsearch storage.
 * <p>
 * This class is annotated with {@link Document} to define it as an Elasticsearch document with a specified index name. It implements the {@link Book}
 * interface and uses annotations like {@link Getter} and {@link Setter} from Project Lombok to auto-generate getter and setter methods. The
 * {@link FieldDefaults} annotation sets the access level of class fields to private.
 * <p>
 * The default constructor generates a unique ID for the {@link ElasticBook}, suitable for use with Elasticsearch which typically uses string UUIDs.
 * This is handled by a custom ID generator for demonstration purposes.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 18-03-2024
 */
@Document(indexName = "books")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElasticBook implements Book {

  @Id
  Long id;

  String title;

  int publishedYear;

  Author author;

  public ElasticBook() {
    this.id = IdGenerator.generateUniqueId(ElasticBook.class.getName());
  }
}
