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

package com.davidbracewell.concurrent;

import com.davidbracewell.logging.Logger;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>An implementation of the Producer Consumer problem in which one or more producers are generating data for one or
 * more consumers to process.</p>
 *
 * @author David B. Bracewell
 */
public class Broker<V> implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(Broker.class);
  final ArrayBlockingQueue<V> queue;
  final List<Producer<V>> producers;
  final List<java.util.function.Consumer<? super V>> consumers;
  final AtomicInteger runningProducers = new AtomicInteger();

  private Broker(ArrayBlockingQueue<V> queue, List<Producer<V>> producers, List<java.util.function.Consumer<? super V>> consumers) {
    this.queue = queue;
    this.producers = producers;
    this.consumers = consumers;
  }

  /**
   * Builder builder.
   *
   * @return the builder
   */
  public static <V> Builder<V> builder() {
    return new Builder<>();
  }

  /**
   * Run boolean.
   *
   * @return the boolean
   */
  public boolean run() {
    ExecutorService executors = Executors.newFixedThreadPool(producers.size() + consumers.size());
    runningProducers.set(producers.size());

    //create the producers
    for (Producer<V> producer : producers) {
      producer.setOwner(this);
      executors.submit(new ProducerThread(producer));
    }

    //create the consumers
    for (java.util.function.Consumer<? super V> consumer : consumers) {
      executors.submit(new ConsumerThread(consumer));
    }

    //give it some more time to process
    while (runningProducers.get() > 0 || !queue.isEmpty()) {
      Threads.sleep(10);
    }

    executors.shutdown();
    try {
      executors.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.warn(e);
      return false;
    }
    return true;
  }

  /**
   * <p>A producer generates data to be consumed. Implementations of Producer should use the {@link #start()} to begin
   * the production process, {@link #stop()} to signal production has finished, and {@link #yield(Object)} to offer an
   * item up for consumption.</p>
   */
  public abstract static class Producer<V> {

    Broker<V> owner;
    boolean isStopped = false;

    /**
     * @return True if the producer is still running.
     */
    public boolean isRunning() {
      return !isStopped;
    }

    /**
     * Logic for producing items to be consumed.
     */
    public abstract void produce();

    private void setOwner(Broker<V> owner) {
      this.owner = owner;
    }

    /**
     * Signals the production has started.
     */
    protected void start() {
      isStopped = false;
    }

    /**
     * Signals that the producer is finished and its thread can be released.
     */
    protected void stop() {
      isStopped = true;
      owner.runningProducers.decrementAndGet();
    }

    /**
     * offers an object to be consumed, blocking if the Broker's queue is full.
     *
     * @param object the object
     */
    protected final void yield(V object) {
      try {
        owner.queue.put(object);
      } catch (InterruptedException e) {
        log.warn(e);
      }
    }

  }//END OF ProducerConsumer$Producer

  /**
   * A Builder interface for constructing a Broker.
   */
  public static class Builder<V> {

    private ArrayBlockingQueue<V> queue;
    private List<Producer<V>> producers = Lists.newArrayList();
    private List<java.util.function.Consumer<? super V>> consumers = Lists.newArrayList();

    /**
     * Adds a  consumer.
     *
     * @param consumer the consumer
     * @return the builder
     */
    public Builder<V> addConsumer(java.util.function.Consumer<? super V> consumer) {
      Preconditions.checkNotNull(consumer);
      return addConsumer(consumer, 1);
    }

    /**
     * Adds a  consumer and will run it on a number of threads.
     *
     * @param consumer the consumer
     * @param number   the number of threads to run the consumer on.
     * @return the builder
     */
    public Builder<V> addConsumer(java.util.function.Consumer<? super V> consumer, int number) {
      Preconditions.checkNotNull(consumer);
      for (int i = 0; i < number; i++) {
        this.consumers.add(consumer);
      }
      return this;
    }

    /**
     * Add a collection of consumers.
     *
     * @param consumers the consumers
     * @return the builder
     */
    public Builder<V> addConsumers(Collection<java.util.function.Consumer<? super V>> consumers) {
      Preconditions.checkNotNull(consumers);
      this.consumers.addAll(consumers);
      return this;
    }

    /**
     * Adds producer and sets it to run on a number of threads. Note that the producer must be thread safe.
     *
     * @param producer the producer
     * @param number   the number of threads to run the producer on.
     * @return the builder
     */
    public Builder<V> addProducer(Producer<V> producer, int number) {
      Preconditions.checkNotNull(producer);
      for (int i = 0; i < number; i++) {
        this.producers.add(producer);
      }
      return this;
    }

    /**
     * Adds a producer
     *
     * @param producer the producer
     * @return the builder
     */
    public Builder<V> addProducer(Producer<V> producer) {
      Preconditions.checkNotNull(producer);
      return addProducer(producer, 1);
    }

    /**
     * Adds a collection of producers.
     *
     * @param producers the producers
     * @return the builder
     */
    public Builder<V> addProducers(Collection<? extends Producer<V>> producers) {
      Preconditions.checkNotNull(producers);
      this.producers.addAll(producers);
      return this;
    }

    /**
     * The size of the buffer.
     *
     * @param size the size
     * @return the builder
     */
    public Builder<V> bufferSize(int size) {
      Preconditions.checkArgument(size > 0);
      queue = new ArrayBlockingQueue<>(size);
      return this;
    }

    /**
     * Builds A Broker. If no queue size was given than it will default to <code>2 * (number of producers + number of
     * consumers)</code>
     *
     * @return the producer consumer
     */
    public Broker<V> build() {
      Preconditions.checkArgument(producers.size() > 0);
      Preconditions.checkArgument(consumers.size() > 0);
      if (queue == null) {
        queue = new ArrayBlockingQueue<>(2 * (producers.size() + consumers.size()));
      }
      return new Broker<>(queue, producers, consumers);
    }

  }//END OF ProducerConsumer$Builder

  private class ProducerThread implements Runnable {

    final Producer<V> producer;

    private ProducerThread(Producer<V> producer) {
      this.producer = producer;
    }

    @Override
    public void run() {
      while (!Thread.currentThread().isInterrupted() && producer.isRunning()) {
        try {
          producer.produce();
        } catch (Exception e) {
          log.warn(e);
        }
      }
    }

  }//END OF Broker$ProducerThread

  private class ConsumerThread implements Runnable {

    final java.util.function.Consumer<? super V> consumerAction;

    private ConsumerThread(java.util.function.Consumer<? super V> consumerAction) {
      this.consumerAction = consumerAction;
    }

    @Override
    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          V v = queue.poll(100, TimeUnit.NANOSECONDS);
          if (v != null) {
            consumerAction.accept(v);
          }
          if (runningProducers.get() <= 0 && queue.isEmpty()) {
            break;
          }
        } catch (InterruptedException e) {
          break;
        } catch (Exception e) {
          log.warn(e);
        }
      }
    }

  }//END OF Broker$ConsumerThread


}//END OF Broker
