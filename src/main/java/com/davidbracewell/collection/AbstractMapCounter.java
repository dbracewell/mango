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

package com.davidbracewell.collection;

import com.davidbracewell.conversion.Cast;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Predicate;

/**
 * <p>Abstract class for counters backed by <code>java.util.Map</code> implementations. </p>
 *
 * @author David B. Bracewell
 */
@EqualsAndHashCode(exclude = {"sum"})
abstract class AbstractMapCounter<T> implements Counter<T>, Serializable {
  private static final long serialVersionUID = 1L;
  private final Map<T, Double> map;
  private AtomicDouble sum = new AtomicDouble(0d);

  protected AbstractMapCounter(Map<T, Double> backingMap) {
    this.map = backingMap;
    this.sum.set(0d);
  }

  protected AbstractMapCounter(Map<T, Double> backingMap, Iterable<? extends T> items) {
    this(backingMap);
    incrementAll(items);
  }

  protected AbstractMapCounter(Map<T, Double> backingMap, Map<? extends T, ? extends Number> items) {
    this(backingMap);
    merge(items);
  }

  protected AbstractMapCounter(Map<T, Double> backingMap, Counter<? extends T> items) {
    this(backingMap);
    merge(items);
  }

  @Override
  public double get(T item) {
    Double returnValue = map.get(item);
    return returnValue == null ? 0.0d : returnValue;
  }

  @Override
  public void increment(T item, double amount) {
    if (amount == 0) {
      return;
    }
    double total = get(item) + amount;
    if (total == 0) {
      map.remove(item);
    } else {
      map.put(item, total);
    }
    sum.addAndGet(amount);
  }


  @Override
  public Map<T, Double> asMap() {
    return Collections.unmodifiableMap(map);
  }


  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }


  @Override
  public int size() {
    return map.size();
  }


  @Override
  public Set<T> items() {
    return Collections.unmodifiableSet(map.keySet());
  }


  @Override
  public Collection<Double> counts() {
    return Collections.unmodifiableCollection(map.values());
  }


  @Override
  public Counter<T> merge(Counter<? extends T> other) {
    merge(Preconditions.checkNotNull(other).asMap());
    return this;
  }


  @Override
  public void removeZeroCounts() {
    for (Iterator<Map.Entry<T, Double>> entryItr = map.entrySet().iterator(); entryItr.hasNext(); ) {
      Map.Entry<T, Double> entry = entryItr.next();
      if (entry.getValue() == 0.0d) {
        entryItr.remove();
      }
    }
  }


  @Override
  public void merge(Map<? extends T, ? extends Number> other) {
    Preconditions.checkNotNull(other);
    for (Map.Entry<? extends T, ? extends Number> entry : other.entrySet()) {
      increment(entry.getKey(), entry.getValue().doubleValue());
    }
  }

  @Override
  public boolean contains(T item) {
    return map.containsKey(item);
  }

  @Override
  public String toString() {
    return map.toString();
  }


  @Override
  public void clear() {
    map.clear();
    sum.set(0d);
  }


  @Override
  public double remove(T item) {
    Preconditions.checkNotNull(item);
    double value = get(item);
    map.remove(item);
    sum.addAndGet(-value);
    return value;
  }


  @Override
  public void set(T item, double count) {
    remove(item);
    increment(item, count);
  }


  @Override
  public void divideBySum() {
    if (map.isEmpty()) {
      return;
    }
    for (T key : map.keySet()) {
      map.put(key, map.get(key) / sum());
    }
    sum.set(1d);
  }

  @Override
  public Set<Map.Entry<T, Double>> entries() {
    return new ForwardingSet<Map.Entry<T, Double>>() {
      @Override
      protected Set<Map.Entry<T, Double>> delegate() {
        return map.entrySet();
      }

      @Override
      public boolean remove(Object object) {
        if (super.remove(object)) {
          sum.addAndGet(-Cast.<Map.Entry<T, Double>>as(object).getValue());
          return true;
        }
        return false;
      }

      @Override
      public boolean removeAll(Collection<?> collection) {
        return standardRemoveAll(collection);
      }

      @Override
      public Iterator<Map.Entry<T, Double>> iterator() {
        return new ForwardingIterator<Map.Entry<T, Double>>() {
          final Iterator<Map.Entry<T, Double>> iterator = map.entrySet().iterator();
          Map.Entry<T, Double> entry;

          @Override
          protected Iterator<Map.Entry<T, Double>> delegate() {
            return iterator;
          }

          @Override
          public Map.Entry<T, Double> next() {
            entry = super.next();
            return entry;
          }

          @Override
          public void remove() {
            super.remove();
            sum.addAndGet(-entry.getValue());
          }
        };
      }

    };
  }

  @Override
  public void removeAll(Iterable<T> items) {
    if (items != null) {
      items.forEach(this::remove);
    }
  }

  @Override
  public Counter<T> adjustValues(@NonNull DoubleUnaryOperator function) {
    Counter<T> newCounter = newInstance();
    for (Map.Entry<T, Double> entry : map.entrySet()) {
      double value = function.applyAsDouble(entry.getValue());
      if (value != 0d && Doubles.isFinite(value)) {
        newCounter.set(entry.getKey(), value);
      }
    }
    return newCounter;
  }

  @Override
  public void adjustValuesSelf(@NonNull DoubleUnaryOperator function) {
    map.entrySet().forEach(entry -> entry.setValue(function.applyAsDouble(entry.getValue())));
    sum.set(Collect.sum(map.values()));
  }

  protected abstract Counter<T> newInstance();

  @Override
  public Counter<T> topN(int n) {
    Counter<T> cprime = newInstance();
    itemsByCount(false).stream()
      .limit(n)
      .forEach(t -> cprime.set(t, get(t)));
    return cprime;
  }

  @Override
  public Counter<T> bottomN(int n) {
    Counter<T> cprime = newInstance();
    itemsByCount(true).stream()
      .limit(n)
      .forEach(t -> cprime.set(t, get(t)));
    return cprime;
  }

  @Override
  public Counter<T> filterByValue(@NonNull DoublePredicate doublePredicate) {
    Counter<T> counter = newInstance();
    map.entrySet().stream()
      .filter(e -> doublePredicate.test(e.getValue()))
      .forEach(e -> counter.set(e.getKey(), e.getValue()));
    return counter;
  }

  @Override
  public Counter<T> filterByKey(@NonNull Predicate<T> predicate) {
    Counter<T> counter = newInstance();
    map.entrySet().stream()
      .filter(e -> predicate.test(e.getKey()))
      .forEach(e -> counter.set(e.getKey(), e.getValue()));
    return counter;
  }

  @Override
  public double sum() {
    return sum.get();
  }

}//END OF AbstractMapCounter