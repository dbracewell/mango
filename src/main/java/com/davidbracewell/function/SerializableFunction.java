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

package com.davidbracewell.function;

import lombok.NonNull;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Version of Function that is serializable
 *
 * @param <T> Functional parameter
 * @param <R> Functional parameter
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {


   default <F> SerializableFunction<T, F> andThen(@NonNull SerializableFunction<? super R, ? extends F> function) {
      return t -> function.apply(this.apply(t));
   }

   default <V> SerializableFunction<V, R> compose(@NonNull SerializableFunction<? super V, ? extends T> function) {
      return v -> this.apply(function.apply(v));
   }


}//END OF SerializableFunction
