package com.shellGDX.utils.gleed;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Level extends Actor
{
  private Properties properties = new Properties();
  private Array<Layer> layers = new Array<Layer>();
  private String levelName = "";
  private LevelRenderer render = null;
	
	Level()
	{
	}
	
	public void setRenderer()
	{
	  render = new LevelRenderer(this);
	}
	
	public Properties getProperties()
	{
		return properties;
	}

	public int getNumLayers()
	{
		return layers.size;
	}

	public Array<Layer> getLayers()
	{
		return layers;
	}

	public Layer getLayer(int index)
	{
		if (index < 0 || index >= layers.size)
		{
			return null;
		}
		
		return layers.get(index);
	}

	public Layer getLayer(String name)
	{
		for (int i = 0; i < layers.size; ++i)
		{
			Layer layer = layers.get(i);
			
			if (layer.getProperties().getString("Name", "").equals(name))
			{
				return layer;
			}
		}
		
		return null;
	}
	
	public void setName(String name)
	{
	  levelName = name;
	}
	
	public String getName()
  {
      return levelName;
  }
	
	@Override
	public void draw (Batch batch, float parentAlpha)
	{
	  Camera cam = this.getStage().getCamera();
	  render.render(cam, batch);
  }
}
