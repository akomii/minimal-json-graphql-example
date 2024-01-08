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

package org.example.graphql.redis.models;

import java.io.Serial;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * Redis implementation of the {@link Book} interface.
 * <p>
 * This class represents a book stored in a Redis database. It includes fields for the book's ID,
 * title, published year, and the author of the book. It is annotated with {@link RedisHash} to
 * indicate that it is a Redis hash object, and with {@link NoArgsConstructor}, {@link Getter},
 * {@link Setter}, and {@link FieldDefaults} from Lombok to generate boilerplate code. The author
 * field is annotated with {@link Indexed} and {@link Reference} to indicate that it should be
 * indexed and that it is a reference to another domain object.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 04-01-2024
 */
@RedisHash("book")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisBook implements Book {

  /**
   * The unique identifier for the serialized class.
   */
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier for the book.
   */
  @Id
  Long id;

  String title;

  int publishedYear;

  /**
   * The author of the book.
   * <p>
   * The {@link org.springframework.data.redis.core.index.Indexed} annotation indicates that this
   * field should be indexed, which allows for efficient searching and querying in Redis.
   * <p>
   * The {@link org.springframework.data.annotation.Reference} annotation indicates that this field
   * is a reference to another domain object. This is used in the context of Redis to store a
   * reference to the Author object associated with this book.
   */
  @Indexed
  @Reference
  Author author;
}
