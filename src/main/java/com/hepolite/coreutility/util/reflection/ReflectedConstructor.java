package com.hepolite.coreutility.util.reflection;

import java.lang.reflect.Constructor;

public class ReflectedConstructor
{
	private final Constructor<?> handle;

	public ReflectedConstructor(Constructor<?> handle)
	{
		this.handle = handle;
		if (handle != null)
			handle.setAccessible(true);
	}

	/** Creates a new instance from the constructor */
	public final Object instantiate(Object... args)
	{
		try
		{
			return handle.newInstance(args);
		}
		catch (Exception e)
		{}
		return null;
	}
}
