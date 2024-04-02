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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.graphql.server.models.Author;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Redis implementation of the {@link Author} interface.
 * <p>
 * This class represents an author stored in a Redis database. It includes fields for the author's ID, first name, last name, and the IDs of the books
 * they've published. It is annotated with {@link RedisHash} to indicate that it is a Redis hash object, and with {@link NoArgsConstructor},
 * {@link Getter}, {@link Setter}, and {@link FieldDefaults} from Lombok to generate boilerplate code.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.02
 * @since 04-01-2024
 */
@RedisHash("author")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisAuthor implements Author, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  Long id;

  String firstName;

  String lastName;

  List<Long> publishedBookIds = new ArrayList<>();

  @Override
  public void addPublishedBook(Long id) {
    publishedBookIds.add(id);
  }

  @Override
  public void removePublishedBook(Long id) {
    publishedBookIds.remove(id);
  }
}
