package com.hepolite.coreutility.util.reflection;

import com.hepolite.coreutility.log.Log;

public class ReflectedClass
{
	private final Class<?> handle;
	private final String name;

	public ReflectedClass(String name, Class<?> handle)
	{
		this.handle = handle;
		this.name = name;
	}

	/** Creates a new instance of the reflected class */
	public final Object create()
	{
		try
		{
			return handle.newInstance();
		}
		catch (Exception e)
		{
			Log.debug("[ReflectedClass] Failed to instantiate class " + getExtendedName());
			Log.debug(e.getMessage());
		}
		return null;
	}

	/** Creates a new instance of the reflected class, from the given parameters */
	public final Object create(Object... params)
	{
		@SuppressWarnings("rawtypes")
		Class[] classes = new Class[params.length];
		for (int i = 0; i < params.length; ++i)
			classes[i] = params.getClass();

		try
		{
			return handle.getConstructor(classes).newInstance(params);
		}
		catch (Exception e)
		{
			Log.debug("[ReflectedClass] Failed to instantiate class " + getExtendedName() + " with params");
			Log.debug(e.getMessage());
		}
		return null;
	}

	/** Returns the handle to the reflected class */
	public final Class<?> getHandle()
	{
		return handle;
	}

	/** Returns the name of the class */
	public final String getName()
	{
		return name;
	}

	/** Returns the extended name of the class */
	public final String getExtendedName()
	{
		return name + " [" + (handle == null ? "null" : handle.getName()) + "]";
	}
}
