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
import java.util.function.IntPredicate;

/**
 * Version of IntPredicate that is serializable
 */
@FunctionalInterface
public interface SerializableIntPredicate extends IntPredicate, Serializable {

   @Override
   default SerializableIntPredicate negate() {
      return i -> !this.test(i);
   }

   @Override
   default SerializableIntPredicate and(@NonNull IntPredicate other) {
      return i -> this.test(i) && other.test(i);
   }

   @Override
   default SerializableIntPredicate or(@NonNull IntPredicate other) {
      return i -> this.test(i) || other.test(i);
   }

   default SerializableIntPredicate and(@NonNull SerializableIntPredicate other) {
      return i -> this.test(i) && other.test(i);
   }

   default SerializableIntPredicate or(@NonNull SerializableIntPredicate other) {
      return i -> this.test(i) || other.test(i);
   }

}//END OF SerializableIntPredicate
