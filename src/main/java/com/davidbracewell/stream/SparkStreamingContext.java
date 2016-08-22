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
import com.davidbracewell.stream.accumulator.Accumulatable;
import com.davidbracewell.stream.accumulator.MAccumulator;
import com.davidbracewell.stream.accumulator.SparkAccumulatable;
import com.davidbracewell.stream.accumulator.SparkAccumulator;
import com.davidbracewell.string.StringUtils;
import lombok.NonNull;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * @author David B. Bracewell
 */
public enum SparkStreamingContext implements StreamingContext {
  INSTANCE;


  /**
   * The constant SPARK_MASTER.
   */
  public static String SPARK_MASTER = "spark.master";
  /**
   * The constant SPARK_APPNAME.
   */
  public static String SPARK_APPNAME = "spark.appName";

  private static volatile JavaSparkContext context;


  private static JavaSparkContext getSparkContext() {
    if (context == null) {
      synchronized (SparkStreamingContext.class) {
        if (context == null) {
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

  public static SparkStreamingContext contextOf(@NonNull SparkStream<?> stream) {
    if (context == null) {
      synchronized (SparkStreamingContext.class) {
        if (context == null) {
          context = new JavaSparkContext(stream.asRDD().context());
        }
      }
    }
    return SparkStreamingContext.INSTANCE;
  }

  public static SparkStreamingContext contextOf(@NonNull SparkDoubleStream stream) {
    if (context == null) {
      synchronized (SparkStreamingContext.class) {
        if (context == null) {
          context = new JavaSparkContext(stream.getRDD().context());
        }
      }
    }
    return SparkStreamingContext.INSTANCE;
  }

  public static SparkStreamingContext contextOf(@NonNull SparkPairStream<?, ?> stream) {
    if (context == null) {
      synchronized (SparkStreamingContext.class) {
        if (context == null) {
          context = new JavaSparkContext(stream.getRDD().context());
        }
      }
    }
    return SparkStreamingContext.INSTANCE;
  }

  public JavaSparkContext sparkContext() {
    return getSparkContext();
  }

  @Override
  public MAccumulator<Double> accumulator(double initialValue, String name) {
    if (StringUtils.isNullOrBlank(name)) {
      return new SparkAccumulator<>(getSparkContext().doubleAccumulator(initialValue));
    }
    return new SparkAccumulator<>(getSparkContext().doubleAccumulator(initialValue, name));
  }

  @Override
  public MAccumulator<Integer> accumulator(int initialValue, String name) {
    if (StringUtils.isNullOrBlank(name)) {
      return new SparkAccumulator<>(getSparkContext().intAccumulator(initialValue));
    }
    return new SparkAccumulator<>(getSparkContext().intAccumulator(initialValue, name));
  }

  @Override
  public <T> MAccumulator<T> accumulator(T initialValue, @NonNull Accumulatable<T> accumulatable, String name) {
    if (StringUtils.isNullOrBlank(name)) {
      return new SparkAccumulator<>(
        getSparkContext().accumulator(
          initialValue,
          new SparkAccumulatable<>(accumulatable)
        )
      );
    }
    return new SparkAccumulator<>(
      getSparkContext().accumulator(
        initialValue,
        name,
        new SparkAccumulatable<>(accumulatable)
      )
    );
  }

  @Override
  public MDoubleStream doubleStream(DoubleStream doubleStream) {
    if (doubleStream == null) {
      return empty().mapToDouble(o -> Double.NaN);
    }
    return new SparkDoubleStream(
      getSparkContext().parallelizeDoubles(doubleStream.boxed().collect(Collectors.toList()))
    );
  }

  @Override
  public MDoubleStream doubleStream(double... values) {
    if (values == null) {
      return empty().mapToDouble(o -> Double.NaN);
    }
    return new SparkDoubleStream(
      getSparkContext().parallelizeDoubles(DoubleStream.of(values).boxed().collect(Collectors.toList()))
    );
  }

  @Override
  public <T> MStream<T> empty() {
    return new SparkStream<>(getSparkContext().parallelize(new ArrayList<>()));
  }

  @Override
  public void close() {
    context.close();
  }

  @Override
  public MStream<Integer> range(int startInclusive, int endExclusive) {
    return new SparkStream<Integer>(
      IntStream.range(startInclusive, endExclusive).boxed().collect(Collectors.toList())
    );
  }

  @Override
  public final <T> MStream<T> stream(T... items) {
    if (items == null) {
      return empty();
    }
    return new SparkStream<>(Arrays.asList(items));
  }

  @Override
  public <T> MStream<T> stream(@NonNull Stream<T> stream) {
    if (stream == null) {
      return empty();
    }
    return new SparkStream<>(stream.collect(Collectors.toList()));
  }

  @Override
  public <K, V> MPairStream<K, V> pairStream(Map<? extends K, ? extends V> map) {
    if (map == null) {
      return new SparkPairStream<>(new HashMap<K, V>());
    }
    return new SparkPairStream<>(map);
  }

  @Override
  public <K, V> MPairStream<K, V> pairStream(Collection<Map.Entry<K, V>> tuples) {
    return stream(tuples).mapToPair(t -> t);
  }

  @Override
  public <T> MStream<T> stream(Collection<? extends T> collection) {
    JavaRDD<T> rdd;
    if (collection == null) {
      return empty();
    } else if (collection instanceof List) {
      rdd = getSparkContext().parallelize(Cast.<List<T>>as(collection));
    } else {
      rdd = getSparkContext().parallelize(new ArrayList<T>(collection));
    }
    return new SparkStream<>(rdd);
  }

  @Override
  public <T> MStream<T> stream(Iterable<? extends T> iterable) {
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
  public MStream<String> textFile(String location) {
    if (StringUtils.isNullOrBlank(location)) {
      return empty();
    }
    return new SparkStream<>(getSparkContext().textFile(location));
  }

  @Override
  public MStream<String> textFile(Resource location) {
    if (location == null) {
      return empty();
    }
    return textFile(location.path());
  }

  public <T> Broadcast<T> broadcast(T object) {
    return getSparkContext().broadcast(object);
  }


}//END OF SparkStreamingContext
