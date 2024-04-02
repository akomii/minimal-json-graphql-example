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

package org.example.graphql.elasticsearch.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique identifiers in a sequence.
 * <p>
 * Uses a concurrent map to maintain a counter for each sequence name, ensuring thread-safe unique ID generation.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 18-03-2024
 */
public class IdGenerator {

  private static final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();

  // Private constructor to prevent instantiation
  private IdGenerator() {
  }

  /**
   * Generates and returns a unique ID for the specified sequence name.
   *
   * @param sequenceName the name of the sequence for which to generate a unique ID
   * @return a unique long value incremented for each call per sequence name
   */
  public static Long generateUniqueId(String sequenceName) {
    AtomicLong counter = counters.computeIfAbsent(sequenceName, k -> new AtomicLong());
    return counter.incrementAndGet();
  }
}
