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

package org.example.grahql.server.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Represents an author in the book store.
 * <p>
 * This class is a part of the domain model and it is stored in a Redis hash. Each instance of this
 * class corresponds to a single author.
 * <p>
 * This class is serializable, which allows it to be stored in Redis. It also includes a no-args
 * constructor and a constructor that takes all fields as arguments, getters and setters for all
 * fields, and a list of IDs of published books.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 04-01-2024
 */
@RedisHash("author")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author implements Serializable {

  /**
   * The unique identifier for the serialized class.
   */
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier for the author.
   */
  @Id
  Long id;

  String firstName;

  String lastName;

  List<Long> publishedBookIds = new ArrayList<>();
}
