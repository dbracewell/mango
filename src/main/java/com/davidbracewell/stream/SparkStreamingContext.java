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

package com.davidbracewell.stream;

import com.davidbracewell.collection.list.Lists;
import com.davidbracewell.config.Config;
import com.davidbracewell.conversion.Cast;
import com.davidbracewell.io.resource.Resource;
import com.davidbracewell.stream.accumulator.*;
import com.davidbracewell.string.StringUtils;
import lombok.NonNull;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.util.CollectionAccumulator;
import org.apache.spark.util.DoubleAccumulator;
import org.apache.spark.util.LongAccumulator;
import scala.Option;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Represents a distributed streaming context using Sparks's rdd classes
 *
 * @author David B. Bracewell
 */
public enum SparkStreamingContext implements StreamingContext {
   /**
    * The singleton instance of the context
    */
   INSTANCE;

   /**
    * The config property name containing the spark application name
    */
   public static final String SPARK_APPNAME = "spark.appName";
   /**
    * The config property name specifying the spark master address
    */
   public static final String SPARK_MASTER = "spark.master";

   private static volatile JavaSparkContext context;

   private volatile Broadcast<Config> configBroadcast;

   public Broadcast<Config> getConfigBroadcast() {
      if (configBroadcast == null || !configBroadcast.isValid()) {
         synchronized (SparkStreamingContext.class) {
            if (configBroadcast == null || !configBroadcast.isValid()) {
               configBroadcast = broadcast(Config.getInstance());
            }
         }
      }
      return configBroadcast;
   }

   @Override
   public synchronized void updateConfig() {
      if (configBroadcast != null && configBroadcast.isValid()) {
         configBroadcast.destroy();
      }
      configBroadcast = broadcast(Config.getInstance());
   }


   /**
    * Gets the streaming context of a given spark stream
    *
    * @param stream the stream whose context we want
    * @return the spark streaming context
    */
   public static SparkStreamingContext contextOf(@NonNull SparkStream<?> stream) {
      return contextOf(stream.getRDD().context());
   }

   /**
    * Gets the streaming context of a given spark stream
    *
    * @param stream the stream whose context we want
    * @return the spark streaming context
    */
   public static SparkStreamingContext contextOf(@NonNull SparkDoubleStream stream) {
      return contextOf(stream.getRDD().context());
   }

   /**
    * Gets the streaming context of a given spark stream
    *
    * @param stream the stream whose context we want
    * @return the spark streaming context
    */
   public static SparkStreamingContext contextOf(@NonNull SparkPairStream<?, ?> stream) {
      return contextOf(stream.getRDD().context());
   }

   private static SparkStreamingContext contextOf(SparkContext sparkContext) {
      if (context == null || context.sc().isStopped()) {
         synchronized (SparkStreamingContext.class) {
            if (context == null || context.sc().isStopped()) {
               context = new JavaSparkContext(sparkContext);
            }
         }
      }
      return SparkStreamingContext.INSTANCE;
   }

   private static JavaSparkContext getSparkContext() {
      if (context == null || context.sc().isStopped()) {
         synchronized (SparkStreamingContext.class) {
            if (context == null || context.sc().isStopped()) {
               SparkConf conf = new SparkConf();
               if (Config.hasProperty(SPARK_MASTER)) {
                  conf.setMaster(Config.get(SPARK_MASTER).asString());
               }
               conf.setAppName(Config.get(SPARK_APPNAME).asString(StringUtils.randomHexString(20)));
               context = new JavaSparkContext(conf);
            }
         }
      }
      return context;
   }

   /**
    * Broadcasts an object using Spark's broadcast functionality.
    *
    * @param <T>    the type of the object being broadcasted
    * @param object the object to broadcast
    * @return the broadcast wrapper around the object
    */
   public <T> Broadcast<T> broadcast(T object) {
      return getSparkContext().broadcast(object);
   }

   @Override
   public void close() {
      context.close();
   }

   @Override
   public <E> MCounterAccumulator<E> counterAccumulator(String name) {
      CounterAccumulatorV2<E> accumulator = new CounterAccumulatorV2<>();
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkCounterAccumulator<>(accumulator);
   }

   @Override
   public MDoubleAccumulator doubleAccumulator(double initialValue, String name) {
      DoubleAccumulator accumulator = new DoubleAccumulator();
      accumulator.setValue(initialValue);
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkDoubleAccumulator(accumulator);
   }

   @Override
   public MDoubleStream doubleStream(DoubleStream doubleStream) {
      if (doubleStream == null) {
         return empty().mapToDouble(o -> Double.NaN);
      }
      return new SparkDoubleStream(getSparkContext().parallelizeDoubles(
         doubleStream.boxed().collect(Collectors.toList()))
      );
   }

   @Override
   public <T> SparkStream<T> empty() {
      return new SparkStream<>(getSparkContext().parallelize(new ArrayList<>()));
   }

   @Override
   public <E> MListAccumulator<E> listAccumulator(String name) {
      CollectionAccumulator<E> accumulator = new CollectionAccumulator<>();
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkListAccumulator<>(accumulator);
   }

   @Override
   public MLongAccumulator longAccumulator(long initialValue, String name) {
      LongAccumulator accumulator = new LongAccumulator();
      accumulator.setValue(initialValue);
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkLongAccumulator(accumulator);
   }

   @Override
   public <K, V> MMapAccumulator<K, V> mapAccumulator(String name) {
      MapAccumulatorV2<K, V> accumulator = new MapAccumulatorV2<>();
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkMapAccumulator<>(accumulator);
   }

   @Override
   public <K1, K2> MMultiCounterAccumulator<K1, K2> multiCounterAccumulator(String name) {
      MultiCounterAccumulatorV2<K1, K2> accumulator = new MultiCounterAccumulatorV2<>();
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkMultiCounterAccumulator<>(accumulator);
   }

   @Override
   public <K, V> SparkPairStream<K, V> pairStream(Map<? extends K, ? extends V> map) {
      if (map == null) {
         return new SparkPairStream<>(new HashMap<K, V>());
      }
      return new SparkPairStream<>(map);
   }

   @Override
   public <K, V> SparkPairStream<K, V> pairStream(Collection<Map.Entry<? extends K, ? extends V>> tuples) {
      return stream(tuples).mapToPair(t -> t);
   }

   @Override
   public SparkStream<Integer> range(int startInclusive, int endExclusive) {
      return new SparkStream<>(IntStream.range(startInclusive, endExclusive).boxed().collect(Collectors.toList()));
   }

   /**
    * Gets the wrapped Spark context
    *
    * @return the java spark context
    */
   public JavaSparkContext sparkContext() {
      return getSparkContext();
   }

   @Override
   public MStatisticsAccumulator statisticsAccumulator(String name) {
      StatisticsAccumulatorV2 accumulator = new StatisticsAccumulatorV2();
      accumulator.register(sparkContext().sc(), Option.apply(name), false);
      return new SparkStatisticsAccumulator(accumulator);
   }

   @Override
   @SafeVarargs
   public final <T> SparkStream<T> stream(T... items) {
      if (items == null) {
         return empty();
      }
      return new SparkStream<>(Arrays.asList(items));
   }

   @Override
   public <T> SparkStream<T> stream(@NonNull Stream<T> stream) {
      if (stream == null) {
         return empty();
      }
      return new SparkStream<>(stream.collect(Collectors.toList()));
   }

   @Override
   public <T> SparkStream<T> stream(Iterable<? extends T> iterable) {
      JavaRDD<T> rdd;
      if (iterable == null) {
         return empty();
      } else if (iterable instanceof List) {
         rdd = getSparkContext().parallelize(Cast.<List<T>>as(iterable));
      } else {
         rdd = getSparkContext().parallelize(Lists.asArrayList(iterable));
      }
      return new SparkStream<>(rdd);
   }

   @Override
   public SparkStream<String> textFile(String location) {
      if (StringUtils.isNullOrBlank(location)) {
         return empty();
      }
      return new SparkStream<>(getSparkContext().textFile(location));
   }

   @Override
   public SparkStream<String> textFile(Resource location) {
      if (location == null) {
         return empty();
      }
      return textFile(location.path());
   }


}//END OF SparkStreamingContext
