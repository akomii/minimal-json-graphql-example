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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Redis implementation of the {@link Book} interface.
 * <p>
 * This class represents a book stored in a Redis database. It includes fields for the book's ID, title, published year, and the author of the book.
 * It is annotated with {@link RedisHash} to indicate that it is a Redis hash object, and with {@link NoArgsConstructor}, {@link Getter},
 * {@link Setter}, and {@link FieldDefaults} from Lombok to generate boilerplate code. The author field is annotated with {@link JsonDeserialize} to
 * indicate that it should be deserialized as a {@link RedisAuthor} object.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.03
 * @since 04-01-2024
 */
@RedisHash("book")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisBook implements Book, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  Long id;

  String title;

  int publishedYear;

  @JsonDeserialize(as = RedisAuthor.class)
  Author author;
}
