package com.hepolite.coreutility.util.java;

/** First three parameters are of type T, U and V, returns type R */
@FunctionalInterface
public interface TriFunction<T, U, V, R>
{
	public abstract R apply(T t, U u, V v);
}
