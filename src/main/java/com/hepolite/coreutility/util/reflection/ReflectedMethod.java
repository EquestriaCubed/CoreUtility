package com.hepolite.coreutility.util.reflection;

import java.lang.reflect.Method;

import com.hepolite.coreutility.log.Log;

public class ReflectedMethod
{
	private final Method handle;
	private final String name;

	public ReflectedMethod(String name, Method handle)
	{
		this.name = name;
		this.handle = handle;
		if (handle != null)
			handle.setAccessible(true);
		else
			Log.debug("[ReflectedMethod] Detected a null handle in method " + name);
	}

	/** Invokes the method */
	public Object invoke(Object handle, Object... args)
	{
		try
		{
			return this.handle.invoke(handle, args);
		}
		catch (Exception e)
		{
			Log.debug("[ReflectedMethod] Failed to invoke method " + name);
			Log.debug(e.getMessage());
		}
		return null;
	}
}
