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

package com.gengoai.mango;

import com.gengoai.mango.function.SerializableDoubleConsumer;
import lombok.NonNull;
import lombok.ToString;

/**
 * <p>Enhanced version of {@link java.util.DoubleSummaryStatistics} that provides average, variance, and standard
 * deviation.</p>
 *
 * @author David B. Bracewell
 */
@ToString(exclude = "sumOfSq")
public final class EnhancedDoubleStatistics implements SerializableDoubleConsumer {
   private static final long serialVersionUID = 1L;
   private double min = Double.POSITIVE_INFINITY;
   private double max = Double.NEGATIVE_INFINITY;
   private double sum = 0;
   private double sumOfSq = 0;
   private int count = 0;
   private double median = 0;
   private double median_step = 1e-9;


   @Override
   public void accept(double value) {
      if (count == 0) {
         median = value;
         median_step = Math.max(value / 2, 1e-9);
      } else {
         if (median > value) {
            median -= median_step;
         } else if (median < value) {
            median += median_step;
         }

         if (Math.abs(value - median) < median_step) {
            median_step /= 2.0;
         }
      }
      min = Math.min(min, value);
      max = Math.max(max, value);
      sum += value;
      sumOfSq += value * value;
      count++;
   }

   /**
    * Clears the accumulated values.
    */
   public void clear() {
      this.min = Double.POSITIVE_INFINITY;
      this.max = Double.NEGATIVE_INFINITY;
      this.sum = 0;
      this.sumOfSq = 0;
      this.count = 0;
   }

   /**
    * <p>Adds the statistics collected by another EnhancedDoubleStatistics object</p>
    *
    * @param other the other EnhancedDoubleStatistics to combine
    * @throws NullPointerException if the other EnhancedDoubleStatistics is null
    */
   public void combine(@NonNull EnhancedDoubleStatistics other) {
      count += other.count;
      sum += other.sum;
      sumOfSq += other.sumOfSq;
      min = Math.min(min, other.min);
      max = Math.max(max, other.max);
   }

   /**
    * Gets the number items accepted.
    *
    * @return the count
    */
   public double getCount() {
      return count;
   }

   /**
    * Gets the sum.
    *
    * @return the sum
    */
   public double getSum() {
      return sum;
   }

   /**
    * Gets the sum of squares.
    *
    * @return the sum of squares
    */
   public double getSumOfSquares() {
      return sumOfSq;
   }

   /**
    * Gets the  average.
    *
    * @return the average
    */
   public double getAverage() {
      return getCount() > 0 ? getSum() / getCount() : 0;
   }

   /**
    * Gets the min.
    *
    * @return the min
    */
   public double getMin() {
      return min;
   }

   /**
    * Gets the max.
    *
    * @return the max
    */
   public double getMax() {
      return max;
   }

   /**
    * Gets the sample standard deviation.
    *
    * @return the sample standard deviation
    */
   public double getSampleStandardDeviation() {
      if (getCount() <= 0) {
         return Double.NaN;
      } else if (getCount() == 1) {
         return 0d;
      }
      return Math.sqrt(getSampleVariance());
   }

   /**
    * Gets the sample variance.
    *
    * @return the sample variance
    */
   public double getSampleVariance() {
      if (getCount() <= 0) {
         return Double.NaN;
      } else if (getCount() == 1) {
         return 0d;
      }
      return Math.abs(getSumOfSquares() - getAverage() * getSum()) / (getCount() - 1);
   }

   /**
    * Gets the population standard deviation.
    *
    * @return the population standard deviation
    */
   public double getPopulationStandardDeviation() {
      if (getCount() <= 0) {
         return Double.NaN;
      } else if (getCount() == 1) {
         return 0d;
      }
      return Math.sqrt(getPopulationVariance());
   }

   /**
    * Gets the estimated median using the FAME algorithm
    *
    * @return The median
    */
   public double getMedian() {
      if (getCount() == 0) {
         return Double.NaN;
      }
      return median;
   }

   /**
    * Gets the population variance.
    *
    * @return the population variance
    */
   public double getPopulationVariance() {
      if (getCount() <= 0) {
         return Double.NaN;
      } else if (getCount() == 1) {
         return 0d;
      }
      return Math.abs(getSumOfSquares() - getAverage() * getSum()) / getCount();
   }


}//END OF EnhancedDoubleStatistics