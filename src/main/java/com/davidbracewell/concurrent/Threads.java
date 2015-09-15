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

package com.davidbracewell.concurrent;

import com.davidbracewell.logging.Logger;
import com.google.common.base.Preconditions;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * The type Threads.
 *
 * @author David B. Bracewell
 */
public interface Threads {

  Logger log = Logger.getLogger(Threads.class);


  /**
   * <p> Sleeps the thread suppressing any errors. </p>
   *
   * @param milliseconds The amount of time in milliseconds to sleep
   */
  static void sleep(long milliseconds) {
    Preconditions.checkArgument(milliseconds >= 0);
    try {
      if (log.isLoggable(Level.FINEST)) {
        log.finest("Thread {0} is going to sleep for {1} milliseconds.", Thread.currentThread()
                .getName(),
            milliseconds);
      }
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      log.warn(e);
    }
  }

  /**
   * <p> Sleeps the thread suppressing any errors for a given time unit. </p>
   *
   * @param time     The amount of time to sleep
   * @param timeUnit The TimeUnit that the time is in
   */
  static void sleep(long time, TimeUnit timeUnit) {
    Preconditions.checkArgument(time >= 0);
    sleep(timeUnit.toMillis(time));
  }


}// END OF INTERFACE Threads
