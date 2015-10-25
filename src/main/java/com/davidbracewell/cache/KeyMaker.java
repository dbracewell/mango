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

package com.davidbracewell.cache;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author David B. Bracewell
 */
public interface KeyMaker {

  Object make(Class<?> clazz, Method method, Object[] args);

  /**
   * Implementation of a <code>KeyMaker</code> that uses {@link java.util.Objects#hash(Object...)}
   */
  class HashCodeKeyMaker implements KeyMaker {
    @Override
    public Object make(Class<?> clazz, Method method, Object[] args) {
      return Objects.hash(clazz, method, args);
    }
  }//END OF KeyMaker$HashCodeKeyMaker

  /**
   * Default implementation that should never be used
   */
  class DefaultKeyMaker implements KeyMaker {
    @Override
    public Object make(Class<?> clazz, Method method, Object[] args) {
      return null;
    }
  }//END OF KeyMaker$DefaultKeyMaker

}//END OF KeyMaker