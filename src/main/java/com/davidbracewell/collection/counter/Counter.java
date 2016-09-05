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

package com.davidbracewell.collection.counter;


import com.davidbracewell.Copyable;
import com.davidbracewell.Math2;
import com.davidbracewell.collection.Sorting;
import com.davidbracewell.conversion.Convert;
import com.davidbracewell.io.CSV;
import com.davidbracewell.io.resource.Resource;
import com.davidbracewell.io.structured.StructuredFormat;
import com.davidbracewell.io.structured.StructuredWriter;
import com.davidbracewell.io.structured.csv.CSVWriter;
import lombok.NonNull;

import java.io.IOException;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>A specialized object to double map that allows basic statistics over the objects and their values.</p>
 *
 * @param <T> Component type being counted.
 * @author David B. Bracewell
 */
public interface Counter<T> extends Copyable<Counter<T>> {

   /**
    * Constructs a new counter made up of counts that are adjusted using a <code>Function</code>.
    *
    * @param function The function to use to adjust the counts
    * @return The new counter with adjusted counts.
    */
   Counter<T> adjustValues(DoubleUnaryOperator function);

   /**
    * Adjust the values in-place using the supplied function
    *
    * @param function The function to use to adjust the counts
    * @return this counter
    */
   Counter<T> adjustValuesSelf(DoubleUnaryOperator function);

   /**
    * Provides a map view of this counter
    *
    * @return The counter as a <code>Map</code>
    */
   Map<T, Double> asMap();

   /**
    * Calculates the average of the values in the counter
    *
    * @return The average count in the counter
    */
   default double average() {
      return Math2.summaryStatistics(values()).getAverage();
   }

   /**
    * Creates a new counter containing the N items with lowest values
    *
    * @param n the number of items desired
    * @return a counter containing the N items with lowest values from this counter
    */
   Counter<T> bottomN(int n);

   /**
    * Clears the counter
    */
   void clear();

   /**
    * Determines if the item is in the counter
    *
    * @param item item to check
    * @return True if item is in the counter, false otherwise
    */
   boolean contains(T item);

   /**
    * The values associated with the items in the counter
    *
    * @return The values of the items in the counter.
    */
   Collection<Double> values();

   /**
    * Decrements the count of the item by one.
    *
    * @param item The item to increment
    * @return the counter
    */
   default Counter<T> decrement(T item) {
      return decrement(item, 1);
   }

   /**
    * Decrements the count of the item by a given amount
    *
    * @param item   The item to increment
    * @param amount The amount to decrement
    * @return the counter
    */
   default Counter<T> decrement(T item, double amount) {
      return increment(item, -amount);
   }

   /**
    * Decrements all items in a given iterable by 1
    *
    * @param iterable The iterable of items to decrement
    * @return the counter
    */
   default Counter<T> decrementAll(Iterable<? extends T> iterable) {
      if (iterable != null) {
         iterable.forEach(this::decrement);
      }
      return this;
   }

   /**
    * Decrements all items in a given iterable by a given amount
    *
    * @param iterable The iterable of items to decrement
    * @param amount   The amount to decrement
    * @return the counter
    */
   default Counter<T> decrementAll(Iterable<? extends T> iterable, double amount) {
      if (iterable != null) {
         iterable.forEach(i -> decrement(i, amount));
      }
      return this;
   }

   /**
    * Divides the values in the counter by the sum and sets the sum to 1.0
    *
    * @return the counter
    */
   Counter<T> divideBySum();

   /**
    * A set of object - double entries making up the counter
    *
    * @return the set of entries
    */
   Set<Map.Entry<T, Double>> entries();

   /**
    * Creates a new counter containing only those items that evaluate true for the given predicate
    *
    * @param predicate the predicate to use to filter the keys
    * @return A new counter containing only those items that evaluate true for the given predicate
    */
   Counter<T> filterByKey(Predicate<T> predicate);

   /**
    * Creates a new counter containing only those items whose value evaluate true for the given predicate
    *
    * @param doublePredicate the predicate to use to filter the keys
    * @return A new counter containing only those items whose value evaluate true for the given predicate
    */
   Counter<T> filterByValue(DoublePredicate doublePredicate);

   /**
    * Returns the value for the given item
    *
    * @param item The item we want the count for
    * @return The value of the item or 0 if it is not in the counter.
    */
   double get(T item);

   /**
    * Increments the count of the item by one.
    *
    * @param item The item to increment
    * @return the counter
    */
   default Counter<T> increment(T item) {
      return increment(item, 1);
   }

   /**
    * Increments the count of the item by a given amount
    *
    * @param item   The item to increment
    * @param amount The amount to increment
    * @return the counter
    */
   Counter<T> increment(T item, double amount);

   /**
    * Increments all items in a given iterable by 1
    *
    * @param iterable The iterable of items to increment
    * @return the counter
    */
   default Counter<T> incrementAll(Iterable<? extends T> iterable) {
      if (iterable != null) {
         iterable.forEach(this::increment);
      }
      return this;
   }

   /**
    * Increments all items in a given iterable by a given amount
    *
    * @param iterable The iterable of items to increment
    * @param amount   The amount to increment
    * @return the counter
    */
   default Counter<T> incrementAll(Iterable<? extends T> iterable, double amount) {
      if (iterable != null) {
         iterable.forEach(i -> increment(i, amount));
      }
      return this;
   }

   /**
    * Determines if the counter is empty or not
    *
    * @return True if the counter is empty
    */
   boolean isEmpty();

   /**
    * The items in the counter
    *
    * @return The items in the counter
    */
   Set<T> items();

   /**
    * Returns the items as a sorted list by their counts.
    *
    * @param ascending True if the counts are sorted in ascending order, False if in descending order.
    * @return The sorted list of items.
    */
   default List<T> itemsByCount(boolean ascending) {
      return Sorting.sortMapEntriesByValue(asMap(), ascending)
                    .stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
   }

   /**
    * Calculates the magnitude (square root of sum of squares) of the items in the Counter.
    *
    * @return the magnitude
    */
   default double magnitude() {
      return Math.sqrt(Math2.summaryStatistics(values()).getSumOfSquares());
   }

   /**
    * Creates a new counter by mapping the items of this counter using the supplied function
    *
    * @param <R>      The component type of the new counter
    * @param function the function to use to transform the keys
    * @return A new counter containing only those items that evaluate true for the given predicate
    */
   <R> Counter<R> mapKeys(Function<? super T, ? extends R> function);

   /**
    * Determines the item with the maximum value in the counter
    *
    * @return The item with max count
    */
   default T max() {
      return Collections.max(asMap().entrySet(), Map.Entry.comparingByValue()).getKey();
   }

   /**
    * Determines the maximum value in the counter
    *
    * @return The maximum count in the counter
    */
   default double maximumCount() {
      if (isEmpty()) {
         return 0d;
      }
      return Collections.max(values());
   }

   /**
    * Merges the counts in one counter with this one.
    *
    * @param other The other counter to merge.
    * @return the counter
    */
   Counter<T> merge(Counter<? extends T> other);

   /**
    * Merges the counts in a map with this counter.
    *
    * @param other The other counter to merge.
    * @return the counter
    */
   Counter<T> merge(Map<? extends T, ? extends Number> other);

   /**
    * Determines the item with the minimum value in the counter
    *
    * @return The item with min count
    */
   default T min() {
      return Collections.min(asMap().entrySet(), Map.Entry.comparingByValue()).getKey();
   }

   /**
    * Determines the minimum value in the counter
    *
    * @return The minimum count in the counter
    */
   default double minimumCount() {
      if (isEmpty()) {
         return 0d;
      }
      return Collections.min(values());
   }

   /**
    * Removes an item from the counter
    *
    * @param item The item to remove
    * @return the count of the removed item
    */
   double remove(T item);

   /**
    * Removes all the given items from the counter
    *
    * @param items The items to remove
    * @return the counter
    */
   Counter<T> removeAll(Iterable<T> items);

   /**
    * Sample an item based on its count.
    *
    * @param rnd The random number generator
    * @return the sampled item
    */
   default T sample(@NonNull Random rnd) {
      double target = rnd.nextDouble() * sum();
      double runningSum = 0;
      T lastEntry = null;
      for (Map.Entry<T, Double> entry : entries()) {
         runningSum += entry.getValue();
         if (target <= runningSum) {
            return entry.getKey();
         }
         lastEntry = entry.getKey();
      }
      return lastEntry;
   }

   /**
    * Sample an item based on its count.
    *
    * @return the sampled item
    */
   default T sample() {
      return sample(new Random());
   }

   /**
    * Sets the value of an item in the counter
    *
    * @param item  The item
    * @param count The count
    * @return the counter
    */
   Counter<T> set(T item, double count);

   /**
    * The total number of items in the counter
    *
    * @return The number of items in the counter
    */
   int size();

   /**
    * Calculates the standard deviation of the items in the counter
    *
    * @return The standard deviation of the counts in the counter
    */
   default double standardDeviation() {
      return Math2.summaryStatistics(values()).getSampleStandardDeviation();
   }

   /**
    * The sum of values in the counter
    *
    * @return The sum of the counts in the counter
    */
   double sum();

   /**
    * Creates a new counter containing the N items with highest values
    *
    * @param n the number of items desired
    * @return a counter containing the N items with highest values from this counter
    */
   Counter<T> topN(int n);

   /**
    * Writes the counter items and values to CSV
    *
    * @param output the resource to write to
    * @throws IOException Something went wrong writing
    */
   default void writeCsv(@NonNull Resource output) throws IOException {
      try (CSVWriter writer = CSV.builder().writer(output)) {
         for (Map.Entry<T, Double> entry : entries()) {
            writer.write(entry.getKey(), entry.getValue());
         }
      }
   }

   /**
    * Writes the counter items and values to Json
    *
    * @param output the resource to write to
    * @throws IOException Something went wrong writing
    */
   default void writeJson(@NonNull Resource output) throws IOException {
      try (StructuredWriter writer = StructuredFormat.JSON.createWriter(output)) {
         writer.beginDocument();
         for (Map.Entry<T, Double> entry : entries()) {
            writer.writeKeyValue(Convert.convert(entry.getKey(), String.class), entry.getValue());
         }
         writer.endDocument();
      }
   }

}//END OF Counter
