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

package com.davidbracewell.tuple;

import com.davidbracewell.Copyable;
import com.davidbracewell.collection.Sorting;
import com.davidbracewell.collection.Streams;
import com.davidbracewell.conversion.Cast;
import com.davidbracewell.string.StringUtils;
import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>A tuple is a finite sequence of items.</p>
 *
 * @author David B. Bracewell
 */
public abstract class Tuple implements Iterable<Object>, Comparable<Tuple>, Copyable<Tuple>, Serializable {
   private static final long serialVersionUID = 1L;

   /**
    * The number of items in the tuple
    *
    * @return the number of items in the tuple
    */
   public abstract int degree();

   /**
    * The tuple as an array of objects
    *
    * @return an array representing the items in the tuple
    */
   public abstract Object[] array();

   @Override
   public Iterator<Object> iterator() {
      return Arrays.asList(array()).iterator();
   }

   /**
    * Gets a stream over the objects in the tuple
    *
    * @return A Stream of the objects in the tuple
    */
   public Stream<Object> stream() {
      return Streams.asStream(iterator());
   }

   /**
    * Maps the tuple to another a data type.
    *
    * @param <R>      the type being mapped to
    * @param function the mapping function
    * @return the result of the mapping function
    */
   public <R> R map(@NonNull Function<Tuple, R> function) {
      return function.apply(this);
   }

   /**
    * Maps the values of the tuple to another data type
    *
    * @param function the mapping function
    * @return A new tuple of same degree whose values are the result of the mapping function applied to the this tuple's
    * elements.
    */
   public Tuple mapValues(@NonNull Function<Object, ? extends Object> function) {
      return NTuple.of(Arrays.stream(array()).map(function).collect(Collectors.toList()));
   }

   /**
    * Gets the ith item of the tuple.
    *
    * @param <T> the type parameter
    * @param i   the index of the item
    * @return the item at the ith index
    */
   public <T> T get(int i) {
      return Cast.as(array()[i]);
   }

   /**
    * Shifts the first element of the tuple resulting in a tuple of degree - 1.
    *
    * @return A new tuple without the shifted element;
    */
   public Tuple shiftLeft() {
      if (degree() < 2) {
         return Tuple0.INSTANCE;
      }
      Object[] copy = new Object[degree() - 1];
      System.arraycopy(array(), 1, copy, 0, copy.length);
      return NTuple.of(copy);
   }

   /**
    * Shifts the last element of the tuple resulting in a tuple of degree - 1.
    *
    * @return A new tuple without the shifted element;
    */
   public Tuple shiftRight() {
      if (degree() < 2) {
         return Tuple0.INSTANCE;
      }
      Object[] copy = new Object[degree() - 1];
      System.arraycopy(array(), 0, copy, 0, copy.length);
      return NTuple.of(copy);
   }

   /**
    * Appends an item the end of the tuple resulting in a new tuple of degree + 1
    *
    * @param <T>    the type parameter
    * @param object the object being appended
    * @return A new tuple of degree + 1 containing the object at the end
    */
   public <T> Tuple appendRight(T object) {
      if (degree() == 0) {
         return Tuple1.of(object);
      }
      Object[] copy = new Object[degree() + 1];
      System.arraycopy(array(), 0, copy, 0, degree());
      copy[copy.length - 1] = object;
      return NTuple.of(copy);
   }

   /**
    * Takes a slice of the tuple from an inclusive start to an exclusive end index.
    *
    * @param start Where to start the slice from (inclusive)
    * @param end   Where to end the slice at (exclusive)
    * @return A new tuple of degree (end - start) with the elements of this tuple from start to end
    */
   public Tuple slice(int start, int end) {
      Preconditions.checkArgument(start >= 0, "Start index must be >= 0");
      Preconditions.checkArgument(start < end, "Start index must be < end index");
      if (start >= degree()) {
         return Tuple0.INSTANCE;
      }
      return new NTuple(Arrays.copyOfRange(array(), start, Math.min(end, degree())));
   }

   /**
    * Appends an item the beginning of the tuple resulting in a new tuple of degree + 1
    *
    * @param <T>    the type parameter
    * @param object the object being appended
    * @return A new tuple of degree + 1 containing the object at the beginning
    */
   public <T> Tuple appendLeft(T object) {
      if (degree() == 0) {
         return Tuple1.of(object);
      }
      Object[] copy = new Object[degree() + 1];
      System.arraycopy(array(), 0, copy, 1, degree());
      copy[0] = object;
      return NTuple.of(copy);
   }


   @Override
   public final int compareTo(@NonNull Tuple o) {
      if (degree() < o.degree()) {
         return -1;
      } else if (degree() > o.degree()) {
         return 1;
      }
      Object[] a1 = array();
      Object[] a2 = o.array();
      for (int i = 0; i < a1.length; i++) {
         int cmp = Sorting.compare(a1[i], a2[i]);
         if (cmp != 0) {
            return cmp;
         }
      }
      return 0;
   }

   @Override
   public final boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj instanceof Tuple) {
         Tuple tuple = Cast.as(obj);
         return degree() == tuple.degree() && Arrays.equals(array(), tuple.array());
      }
      return false;
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(array());
   }

   @Override
   public String toString() {
      return StringUtils.join(array(), ", ", "(", ")");
   }


}//END OF Tuple
