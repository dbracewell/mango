/*
 * (c) 2005 David B. Bracewell
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.davidbracewell.io;


import com.davidbracewell.SystemInfo;
import com.davidbracewell.string.StringUtils;
import lombok.NonNull;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * A set of convenience methods for handling files and file names.
 *
 * @author David B. Bracewell
 */
public class FileUtils {

   private static final char EXTENSION_SEPARATOR = '.';
   private static final char UNIX_SEPARATOR = '/';
   private static final char WINDOWS_SEPARATOR = '\\';

   /**
    * Create file pattern pattern.
    *
    * @param filePattern the file pattern
    * @return the pattern
    */
   public static Pattern createFilePattern(String filePattern) {
      filePattern = StringUtils.isNullOrBlank(filePattern) ? "\\*" : filePattern;
      filePattern = filePattern.replaceAll("\\.", "\\.");
      filePattern = filePattern.replaceAll("\\*", ".*");
      return Pattern.compile("^" + filePattern + "$");
   }

   /**
    * Is compressed boolean.
    *
    * @param pushbackInputStream the pushback input stream
    * @return the boolean
    * @throws IOException the io exception
    */
   public static boolean isCompressed(PushbackInputStream pushbackInputStream) throws IOException {
      if (pushbackInputStream == null) {
         return false;
      }
      byte[] buffer = new byte[2];
      int read = pushbackInputStream.read(buffer);
      boolean isCompressed = false;

      if (read == 2) {
         isCompressed = ((buffer[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (buffer[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
      }

      if (read != -1) {
         pushbackInputStream.unread(buffer, 0, read);
      }

      return isCompressed;
   }

   private static int indexOfLastSeparator(String spec) {
      if (spec == null) {
         return -1;
      }
      return Math.max(spec.lastIndexOf(UNIX_SEPARATOR), spec.lastIndexOf(WINDOWS_SEPARATOR));
   }

   private static int indexOfFileExtension(String spec) {
      if (spec == null) {
         return -1;
      }

      int dotIndex = spec.lastIndexOf(EXTENSION_SEPARATOR);
      if (dotIndex == -1) {
         return -1;
      }

      int pathIndex = indexOfLastSeparator(spec);
      if (pathIndex > dotIndex) {
         return -1;
      }
      return dotIndex;
   }

   /**
    * Converts file spec to unix path separators
    *
    * @param spec The file spec
    * @return Unix style path spec
    */
   public static String toUnix(String spec) {
      if (spec == null) {
         return StringUtils.EMPTY;
      }
      return spec.replaceAll("\\\\+", "/");
   }

   /**
    * Converts file spec to windows path separators
    *
    * @param spec The file spec
    * @return windows style path spec
    */
   public static String toWindows(String spec) {
      if (spec == null) {
         return StringUtils.EMPTY;
      }
      return spec.replaceAll("/+", "\\\\");
   }

   /**
    * <p>Attempts to get the base name for a given file/directory. This command should work in the same way as the unix
    * <code>basename</code> command. </p>
    *
    * @param file The file path/name
    * @return The file name if given a file, the directory name if given a directory, or null if given a null or empty
    * string.
    */
   public static String baseName(String file) {
      return baseName(file, StringUtils.EMPTY);
   }

   /**
    * <p>Attempts to get the base name for a given file/directory. Removes the suffix from the name as well. This
    * command  should work in the same way as the unix <code>basename</code> command. </p>
    *
    * @param file   The file path/name
    * @param suffix The  suffix to remove
    * @return The file name if given a file, the directory name if given a directory, or null if given a null or empty
    * string.
    */
   public static String baseName(String file, @NonNull String suffix) {
      if (StringUtils.isNullOrBlank(file)) {
         return StringUtils.EMPTY;
      }
      file = StringUtils.trim(file);
      int index = indexOfLastSeparator(file);
      if (index == -1) {
         return file.replaceAll(Pattern.quote(suffix) + "$", "");
      } else if (index == file.length() - 1) {
         return baseName(file.substring(0, file.length() - 1));
      }
      return file.substring(index + 1).replaceAll(Pattern.quote(suffix) + "$", "");
   }

   /**
    * <p>Attempts to get the file extension for a given file.</p>
    *
    * @param file The file
    * @return The file extension of the file spec or null if it is null
    */
   public static String extension(String file) {
      if (StringUtils.isNullOrBlank(file)) {
         return StringUtils.EMPTY;
      }
      file = StringUtils.trim(file);
      int index = indexOfFileExtension(file);
      if (index == -1) {
         return StringUtils.EMPTY;
      }
      return file.substring(index + 1);
   }


   /**
    * Adds a trailing slash if its needed. Tries to determine the slash style, but defaults to unix. Assumes that what
    * is passed in is a directory.
    *
    * @param directory The directory to possibly add a trailing slash to
    * @return A directory name with a trailing slash
    */
   public static String addTrailingSlashIfNeeded(String directory) {
      if (StringUtils.isNullOrBlank(directory)) {
         return StringUtils.EMPTY;
      }
      int separator = indexOfLastSeparator(directory);
      String slash = SystemInfo.isUnix() ? Character.toString(UNIX_SEPARATOR) : Character.toString(WINDOWS_SEPARATOR);
      if (separator != -1) {
         slash = Character.toString(directory.charAt(separator));
      }
      return directory.endsWith(slash) ? directory : directory + slash;
   }

   /**
    * <p>Attempts to get the directory for a given file. Will return itself if the passed in file is a directory. Will
    * always return a trailing slash.</p>
    *
    * @param file The file
    * @return The path of the file spec or null if it is null
    */
   public static String directory(String file) {
      if (StringUtils.isNullOrBlank(file)) {
         return StringUtils.EMPTY;
      }
      file = StringUtils.trim(file);
      int separator = indexOfLastSeparator(file);
      int extension = indexOfFileExtension(file);

      if (extension == -1) {
         return addTrailingSlashIfNeeded(file);
      } else if (separator == -1) {
         return StringUtils.EMPTY;
      }

      return directory(file.substring(0, separator + 1));
   }

   /**
    * Returns the parent directory for the given file. If the file passed in is actually a directory it will get the
    * directory's parent.
    *
    * @param file The file
    * @return The parent or null if the file is null or empty
    */
   public static String parent(String file) {
      if (StringUtils.isNullOrBlank(file)) {
         return StringUtils.EMPTY;
      }
      file = StringUtils.trim(file);
      String path = path(file);
      int index = indexOfLastSeparator(path);
      if (index <= 0) {
         return SystemInfo.isUnix() ? Character.toString(UNIX_SEPARATOR) : Character.toString(WINDOWS_SEPARATOR);
      }
      return path.substring(0, index);
   }

   /**
    * Returns the path of the file in the same manner as {@link java.io.File#getPath()}.
    *
    * @param file The file
    * @return The path or null if the file is null or empty
    */
   public static String path(String file) {
      if (StringUtils.isNullOrBlank(file)) {
         return StringUtils.EMPTY;
      }
      file = StringUtils.trim(file);
      int pos = indexOfLastSeparator(file);
      return pos == file.length() - 1 ? file.substring(0, file.length() - 1) : file;
   }


}//END OF FileUtils
