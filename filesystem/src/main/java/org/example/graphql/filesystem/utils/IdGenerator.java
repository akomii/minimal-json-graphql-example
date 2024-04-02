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

/**
 * This component initializes the ID counter to one more than the highest ID found among persisted JSON files in a given directory, ensuring that each
 * generated ID is unique across application restarts. The directory to search in is provided by a {@link DirectoryManager} instance.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 02-02-2024
 */
public class IdGenerator {

  private static final Logger logger = Logger.getLogger(IdGenerator.class.getName());

  private final AtomicLong idCounter;

  /**
   * Constructs an {@code IdGenerator} with a reference to a {@link DirectoryManager}. The working directory for ID persistence is obtained from the
   * {@link DirectoryManager}, and the initial value for the ID counter is set to one more than the highest persisted ID.
   *
   * @param dirManager the directory manager used to access the working directory
   */
  public IdGenerator(DirectoryManager dirManager) {
    Path workingDir = dirManager.getWorkingDir();
    this.idCounter = new AtomicLong(findHighestPersistedId(workingDir) + 1);
  }

  /**
   * This method scans the directory for JSON files, extracts the numeric part of their filenames, and determines the maximum value. If no files are
   * found or an error occurs, it returns 0.
   *
   * @param basePath the path to the directory containing persisted IDs
   * @return the highest ID found, or 0 if no IDs are found or an error occurs
   */
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

  /**
   * This method increments the internal counter and returns the new value, ensuring that each call to this method returns a unique identifier.
   *
   * @return a unique ID
   */
  public Long generateId() {
    return idCounter.getAndIncrement();
  }
}
