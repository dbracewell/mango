
package com.davidbracewell.function;
import java.io.Serializable;
import java.util.function.LongFunction;

@FunctionalInterface
public interface SerializableLongFunction<R> extends LongFunction<R>, Serializable {

}//END OF SerializableLongFunction
