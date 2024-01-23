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

package org.example.graphql.filesystem.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

  private static final Logger logger = Logger.getLogger(IdGenerator.class.getName());

  private final AtomicLong idCounter;

  public IdGenerator(DirectoryCreator dirCreator) {
    Path workingDir = dirCreator.getWorkingDir();
    this.idCounter = new AtomicLong(findHighestPersistedId(workingDir) + 1);
  }

  private long findHighestPersistedId(Path basePath) {
    try (Stream<Path> paths = Files.walk(basePath)) {
      return paths.filter(Files::isRegularFile)
          .map(Path::getFileName)
          .map(Path::toString)
          .filter(name -> name.endsWith(".json"))
          .map(name -> name.substring(0, name.length() - 5)) // Remove ".json" extension
          .mapToLong(Long::parseLong)
          .max()
          .orElse(0);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error finding highest persisted ID", e);
      return 0;
    }
  }

  public Long generateId() {
    return idCounter.getAndIncrement();
  }
}
