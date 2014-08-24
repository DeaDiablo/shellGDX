package com.shellGDX.utils.gleed;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Layer extends LevelObject
{
  private HashMap<String, Array<TextureElement>> textures = new HashMap<String, Array<TextureElement>>();
  private HashMap<String, Array<LevelObject>> objects = new HashMap<String, Array<LevelObject>>();
  
  public HashMap<String, Array<TextureElement>> getTextures()
  {
    return textures;
  }
  
  public HashMap<String, Array<LevelObject>> getObjects()
  {
    return objects;
  }
  
  public Array<TextureElement> getTextures(int x, int y)
  {
    return textures.get(String.format("%d %d", x, y));
  }
  
  public Array<LevelObject> getObjects(int x, int y)
  {
    return objects.get(String.format("%d %d", x, y));
  }
  
  public void addTexture(TextureElement texture)
  {
    String key = String.format("%d %d", (int)texture.getX() / Settings.xGridSize, (int)texture.getY() / Settings.yGridSize);
    Array<TextureElement> arrayTexture = textures.get(key);
    if (arrayTexture == null)
    {
      arrayTexture = new Array<TextureElement>();
      textures.put(key, arrayTexture);
    }
    arrayTexture.add(texture);
  }
  
  public void addObject(LevelObject object)
  {
    String key = String.format("%d %d", (int)object.getX() / Settings.xGridSize, (int)object.getY() / Settings.yGridSize);
    Array<LevelObject> arrayObject = objects.get(key);
    if (arrayObject == null)
    {
      arrayObject = new Array<LevelObject>();
      objects.put(key, arrayObject);
    }
    arrayObject.add(object);
  }

  public void unload(AssetManager assetManager)
  {
    for(Array<TextureElement> arrayTexture : textures.values())
    {
      for (int j = 0; j < arrayTexture.size; ++j)
      {
        TextureElement texture = arrayTexture.get(j);
    
        if (assetManager.isLoaded(texture.path, Texture.class))
        {
          assetManager.unload(texture.path);
        }
      }
    }
  }
}
