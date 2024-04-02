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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.Getter;

/**
 * Manages a specified working directory for file storage and operations within the application. This class is responsible for ensuring the existence
 * of the working directory and provides utility methods for constructing file paths within this directory.
 * <p>
 * The working directory path is provided at construction and is immediately converted to an absolute and normalized {@link Path}. If the specified
 * directory does not exist, it is created during the initialization phase.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 02-02-2024
 */
@Getter
public class DirectoryManager {

  private final Path workingDir;

  public DirectoryManager(String workingDir) {
    this.workingDir = Paths.get(workingDir).toAbsolutePath().normalize();
    initWorkingDir();
  }

  /**
   * Validates and ensures the existence of the working directory. If the directory does not exist, it attempts to create it. Throws a
   * RuntimeException if unable to create the directory.
   *
   * @throws RuntimeException if the directory cannot be created or the path is null.
   */
  private void initWorkingDir() {
    Objects.requireNonNull(workingDir, "Working directory path must not be null");
    File dir = workingDir.toFile();
    if (!dir.exists() && !dir.mkdirs()) {
      throw new RuntimeException("Failed to create directory: " + workingDir);
    }
  }

  public String getAbsolutePathForWorkingDir() {
    return workingDir.toString();
  }

  public String getAbsoluteFilePathForEntity(Long entityId, String extension) {
    return workingDir.resolve(entityId + extension).toString();
  }
}
