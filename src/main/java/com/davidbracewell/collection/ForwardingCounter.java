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

import lombok.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Predicate;

/**
 * @author David B. Bracewell
 */
public abstract class ForwardingCounter<TYPE> implements Counter<TYPE>, Serializable {
  private static final long serialVersionUID = 1L;

  @Override
  public Counter<TYPE> adjustValues(DoubleUnaryOperator function) {
    return delegate().adjustValues(function);
  }

  @Override
  public void adjustValuesSelf(@NonNull DoubleUnaryOperator function) {
    delegate().adjustValuesSelf(function);
  }

  @Override
  public Map<TYPE, Double> asMap() {
    return delegate().asMap();
  }

  @Override
  public double average() {
    return delegate().average();
  }

  @Override
  public Counter<TYPE> bottomN(int n) {
    return delegate().bottomN(n);
  }

  @Override
  public void clear() {
    delegate().clear();
  }

  @Override
  public boolean contains(TYPE item) {
    return delegate().contains(item);
  }

  @Override
  public Collection<Double> counts() {
    return delegate().counts();
  }

  @Override
  public void decrement(TYPE item) {
    delegate().decrement(item);
  }

  @Override
  public void decrement(TYPE item, double amount) {
    delegate().decrement(item, amount);
  }

  @Override
  public void decrementAll(Iterable<? extends TYPE> iterable) {
    delegate().decrementAll(iterable);
  }

  @Override
  public void decrementAll(Iterable<? extends TYPE> iterable, double amount) {
    delegate().decrementAll(iterable, amount);
  }

  protected abstract Counter<TYPE> delegate();

  @Override
  public void divideBySum() {
    delegate().divideBySum();
  }

  @Override
  public double get(TYPE item) {
    return delegate().get(item);
  }

  @Override
  public void increment(TYPE item) {
    delegate().increment(item);
  }

  @Override
  public void increment(TYPE item, double amount) {
    delegate().increment(item, amount);
  }

  @Override
  public void incrementAll(Iterable<? extends TYPE> iterable) {
    delegate().incrementAll(iterable);
  }

  @Override
  public void incrementAll(Iterable<? extends TYPE> iterable, double amount) {
    delegate().incrementAll(iterable, amount);
  }

  @Override
  public boolean isEmpty() {
    return delegate().isEmpty();
  }

  @Override
  public Set<TYPE> items() {
    return delegate().items();
  }

  @Override
  public List<TYPE> itemsByCount(boolean ascending) {
    return delegate().itemsByCount(ascending);
  }

  @Override
  public Set<Map.Entry<TYPE, Double>> entries() {
    return delegate().entries();
  }

  @Override
  public double magnitude() {
    return delegate().magnitude();
  }

  @Override
  public TYPE max() {
    return delegate().max();
  }

  @Override
  public double maximumCount() {
    return delegate().maximumCount();
  }

  @Override
  public Counter<TYPE> merge(Counter<? extends TYPE> other) {
    return delegate().merge(other);
  }

  @Override
  public void merge(Map<? extends TYPE, ? extends Number> other) {
    delegate().merge(other);
  }

  @Override
  public TYPE min() {
    return delegate().min();
  }

  @Override
  public double minimumCount() {
    return delegate().minimumCount();
  }

  @Override
  public double remove(TYPE item) {
    return delegate().remove(item);
  }

  @Override
  public void removeAll(Iterable<TYPE> items) {
    delegate().removeAll(items);
  }

  @Override
  public void removeZeroCounts() {
    delegate().removeZeroCounts();
  }

  @Override
  public TYPE sample() {
    return delegate().sample();
  }

  @Override
  public void set(TYPE item, double count) {
    delegate().set(item, count);
  }

  @Override
  public int size() {
    return delegate().size();
  }

  @Override
  public double standardDeviation() {
    return delegate().standardDeviation();
  }

  @Override
  public double sum() {
    return delegate().sum();
  }

  @Override
  public double sumOfSquares() {
    return delegate().sumOfSquares();
  }

  @Override
  public Counter<TYPE> topN(int n) {
    return delegate().topN(n);
  }

  @Override
  public Counter<TYPE> filterByKey(@NonNull Predicate<TYPE> predicate) {
    return delegate().filterByKey(predicate);
  }

  @Override
  public Counter<TYPE> filterByValue(@NonNull DoublePredicate doublePredicate) {
    return delegate().filterByValue(doublePredicate);
  }

}//END OF ForwardingCounter
