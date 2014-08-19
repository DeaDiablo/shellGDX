package com.shellGDX.utils.gleed;

import com.badlogic.gdx.utils.Array;

public class Layer extends LevelObject
{
  //Array<LevelObject> objects = new Array<LevelObject>();
  //Array<TextureElement> textures = new Array<TextureElement>();
  @SuppressWarnings("unchecked")
  private Array<TextureElement>[][] textures = new Array[100][100];
  @SuppressWarnings("unchecked")
  private Array<LevelObject>[][] objects = new Array[100][100];

  Layer()
  {
  }
  
  public Array<TextureElement> getTextures(int x, int y)
  {
    return textures[x + 50][y + 50];
  }
  
  public Array<LevelObject> getObjects(int x, int y)
  {
    return objects[x + 50][y + 50];
  }
  
  public void addTextureArray(int x, int y, Array<TextureElement> textureArray)
  {
    textures[x + 50][y + 50] = textureArray;
  }
  
  public void addObjectArray(int x, int y, Array<LevelObject> objectArray)
  {
    objects[x + 50][y + 50] = objectArray;
  }

  /*public Array<TextureElement> getTextures()
  {
    return textures;
  }

  public TextureElement getTexture(String name)
  {
    for (int i = 0; i < textures.size; ++i)
    {
      TextureElement texture = textures.get(i);

      if (texture.name.equals(name))
      {
        return texture;
      }
    }
    
    return null;
  }
  
  public Array<LevelObject> getObjects()
  {
    return objects;
  }
  
  public LevelObject getObject(String name)
  {
    for (int i = 0; i < objects.size; ++i)
    {
      LevelObject levelObject = objects.get(i);
      
      if (levelObject.name.equals(name))
      {
        return levelObject;
      }
    }

    return null;
  }*/
}
