package com.shellGDX.utils.gleed;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.Scene2D;

public class Level extends Group2D
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
  public boolean update(float deltaTime)
  {
    if (!super.update(deltaTime))
      return false;
    
    Scene2D scene = (Scene2D)getStage();
    if (scene == null)
      return false;
    
    clearChildren();
    
    for(Layer layer : layers)
    {
      if (!layer.isVisible())
        continue;
      
      int blockX = (int)scene.getCamera().position.x / Settings.xGridSize;
      int blockY = (int)scene.getCamera().position.y / Settings.yGridSize;
      
      for (int i = -1; i <= 1; i ++)
      {
        for (int j = -1; j <= 1; j ++)
        {
          Array<TextureElement> textures = layer.getTextures(blockX + i, blockY + j);
          if (textures != null && textures.size > 0)
            for(TextureElement texture : textures)
              addActor(texture);
        }
      }
    }
    return true;
  }
}
