package com.shellGDX.utils.gleed;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.model.Scene2D;

public class Level extends Actor
{
  private Properties properties = new Properties();
  private Array<Layer> layers = new Array<Layer>();
  private String levelName = "";
	
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
	
	protected Vector2   bufferVec = new Vector2();
	protected Rectangle rectBound = new Rectangle();

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
    Scene2D scene = (Scene2D)getStage();
    if (scene == null)
      return;
	  
	  for(Layer layer : layers)
	  {
	    if (!layer.visible)
	      continue;

	    batch.setColor(layer.color.r, layer.color.g, layer.color.b, layer.color.a * parentAlpha);
	    
	    int blockX = (int)scene.getCamera().position.x / Settings.xGridSize;
	    int blockY = (int)scene.getCamera().position.y / Settings.yGridSize;
	    
	    for (int i = -1; i <= 1; i ++)
	    {
	      for (int j = -1; j <= 1; j ++)
	      {
	        Array<TextureElement> textures = layer.getTextures(blockX + i, blockY + j);
	        if (textures != null && textures.size > 0)
	        {
	          for(TextureElement texture : textures)
	          {
	            float width = texture.region.getRegionWidth();
	            float height = texture.region.getRegionHeight();
	            
	            bufferVec.set(0, 0);
	            bufferVec = localToStageCoordinates(bufferVec);
	            rectBound.set(bufferVec.x, bufferVec.y, 0, 0);

	            bufferVec.set(width, 0);
	            bufferVec = localToStageCoordinates(bufferVec);
	            rectBound.merge(bufferVec);
	            
	            bufferVec.set(0, height);
	            bufferVec = localToStageCoordinates(bufferVec);
	            rectBound.merge(bufferVec);
	            
	            bufferVec.set(width, height);
	            bufferVec = localToStageCoordinates(bufferVec);
	            rectBound.merge(bufferVec);
	            
	            if (!scene.getCameraRectangle().overlaps(rectBound))
	              continue;
	            
	            batch.draw(texture.region,
	                       texture.position.x - texture.originX,
	                       texture.position.y - texture.originY,
	                       texture.originX,
	                       texture.originY,
	                       texture.region.getRegionWidth(),
	                       texture.region.getRegionHeight(),
	                       texture.scaleX,
	                       texture.scaleY,
	                       -MathUtils.radiansToDegrees * texture.rotation);
	          }
	        }
	      }
	    }
	  }
  }
}
