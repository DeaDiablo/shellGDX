package com.shellGDX.utils.gleed;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TextureElement extends LevelObject
{
	String path = "";
	TextureRegion region = new TextureRegion();
	Vector2 position = new Vector2(0.0f, 0.0f);
	float scaleX = 1.0f;
	float scaleY = 1.0f;
	float originX = 1.0f;
	float originY = 1.0f;
	float rotation = 0.0f;
	
	TextureElement()
	{
		super();
	}

	public String getPath()
	{
		return path;
	}

	public TextureRegion getRegion()
	{
		return region;
	}

	public Vector2 getPosition()
	{
		return position;
	}

	public float getScaleX()
	{
		return scaleX;
	}

	public float getScaleY()
	{
		return scaleY;
	}

	public float getOriginX()
	{
		return originX;
	}

	public float getOriginY()
	{
		return originY;
	}

	public float getRotation()
	{
		return rotation;
	}
}
