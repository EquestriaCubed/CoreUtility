package com.hepolite.coreutility.util;

import java.util.function.Function;

public class StringHelper
{
	/** Converts the given string to a generic type, using the conversion function func */
	private static final <T> T asT(String string, T def, Function<String, T> func)
	{
		try
		{
			return func.apply(string);
		}
		catch (Exception e)
		{
			return def;
		}
	}

	/** Converts the given string to a byte */
	public static final byte asByte(String string)
	{
		return asByte(string, (byte) 0);
	}

	/** Converts the given string to an integer */
	public static final byte asByte(String string, byte def)
	{
		return asT(string, def, Byte::parseByte);
	}

	/** Converts the given string to a float */
	public static final double asDouble(String string)
	{
		return asDouble(string, 0.0);
	}

	/** Converts the given string to a float */
	public static final double asDouble(String string, double def)
	{
		return asT(string, def, Double::parseDouble);
	}

	/** Converts the given string to a float */
	public static final float asFloat(String string)
	{
		return asFloat(string, 0.0f);
	}

	/** Converts the given string to a float */
	public static final float asFloat(String string, float def)
	{
		return asT(string, def, Float::parseFloat);
	}

	/** Converts the given string to an integer */
	public static final int asInt(String string)
	{
		return asInt(string, 0);
	}

	/** Converts the given string to an integer */
	public static final int asInt(String string, int def)
	{
		return asT(string, def, Integer::parseInt);
	}

	/** Converts the string to a string where the first letter in every word is capitalized */
	public static final String toTitleCase(final String string)
	{
		String[] arr = string.toLowerCase().split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++)
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		return sb.toString().trim();
	}
}
