package com.hepolite.coreutility.util.reflection;

import java.lang.reflect.Field;

import com.hepolite.coreutility.log.Log;

public class ReflectedField
{
	private final Field handle;
	private final String name;

	public ReflectedField(String name, Field handle)
	{
		this.name = name;
		this.handle = handle;
		if (handle != null)
			handle.setAccessible(true);
		else
			Log.debug("[ReflectedField] Detected a null handle in field " + name);
	}

	/** Returns the value of the field */
	public Object get(Object handle)
	{
		try
		{
			return this.handle.get(handle);
		}
		catch (Exception e)
		{
			Log.debug("[ReflectedField] Failed to read field  " + name);
			Log.debug(e.getMessage());
		}
		return null;
	}

	/** Sets the value of the field */
	public void set(Object handle, Object value)
	{
		try
		{
			this.handle.set(handle, value);
		}
		catch (Exception e)
		{
			Log.debug("[ReflectedField] Failed to set field  " + handle);
			Log.debug(e.getMessage());
		}
	}
}
