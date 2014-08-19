package com.shellGDX.utils.gleed;

import com.badlogic.gdx.graphics.Color;

public class LevelObject
{
	String name = "";
	boolean visible = true;
	Color color = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	Properties properties = new Properties();

	LevelObject()
	{
	}

	public Properties getProperties()
	{
		return properties;
	}

	public String getName()
	{
		return name;
	}

	public boolean getVisible()
	{
		return visible;
	}

	public Color getColor()
	{
		return color;
	}
}
