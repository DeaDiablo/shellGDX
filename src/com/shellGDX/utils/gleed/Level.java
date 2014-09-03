package com.shellGDX.utils.gleed;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.Scene2D;

public class Level extends Group2D
{
  private Properties properties = new Properties();
  private SnapshotArray<Layer> layers = new SnapshotArray<Layer>(false, 16, Layer.class);
  private String levelName = "";
  
  public Properties getProperties()
  {
    return properties;
  }

  public int getNumLayers()
  {
    return layers.size;
  }

  public SnapshotArray<Layer> getLayers()
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
    
    Layer[] layersDraw = layers.begin();
    for(int i = 0, n = layers.size; i < n; i++)
    {
      Layer layer = layersDraw[i];
      if (!layer.isVisible())
        continue;
      
      int blockX = (int)scene.getCamera().position.x / Settings.xGridSize;
      int blockY = (int)scene.getCamera().position.y / Settings.yGridSize;
      
      for (int x = -1; x <= 1; x ++)
      {
        for (int y = -1; y <= 1; y ++)
        {
          Array<TextureElement> textures = layer.getTextures(blockX + x, blockY + y);
          if (textures != null && textures.size > 0)
            for(TextureElement texture : textures)
              addActor(texture);
        }
      }
    }
    layers.end();
    return true;
  }
}
