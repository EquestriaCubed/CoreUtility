package com.hepolite.coreutility.settings;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Function;

public final class Field<T>
{
	private final String field;
	private final boolean useKeys;
	private final Function<String, T> func;
	private Map<String, T> values = new HashMap<String, T>();

	public Field(final String field, final boolean useKeys, final Function<String, T> func)
	{
		this.field = field;
		this.useKeys = useKeys;
		this.func = func;
	}

	/** Loads up the data in the field from the given settings */
	public final void load(final Settings settings)
	{
		if (useKeys)
		{
			for (String key : settings.getKeys(field))
				values.put(key, func.apply(field + "." + key));
		}
		else
			values.put("", func.apply(field));
	}

	/** Retrieves the data value stored in this field */
	public final T get(final int key)
	{
		return values.get(Integer.toString(key));
	}

	/** Retrieves the data value stored in this field */
	public final T get(final String key)
	{
		return values.get(key);
	}

	/** Retrieves the data value stored in this field */
	public final T get()
	{
		return get("");
	}
}
