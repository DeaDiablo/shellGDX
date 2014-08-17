package com.shellGDX.manager;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.gleed.Level;
import com.badlogic.gdx.gleed.LevelLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class ResourceManager extends AssetManager
{
  public static volatile ResourceManager instance = new ResourceManager();

  public ResourceManager()
  {
    this(new InternalFileHandleResolver());
  }

  public ResourceManager(InternalFileHandleResolver resolver)
  {
    super(resolver);
    setLoader(TiledMap.class, new TmxMapLoader(resolver));
    setLoader(Level.class, new LevelLoader(resolver));
  }
  
  public void loadFolder(String folder)
  {
    File directory = new File(folder);
    
    if (!directory.isDirectory())
      return;
    
    File[] files = directory.listFiles();
                    
    for(int i = 0; i < files.length; i++)           
    {
      File file = files[i];
      if (file.isDirectory())
      {
        loadFolder(file.getPath());
        return;
      }
      
      if(file.isFile())
      {
        String path = file.getPath();
        path = path.replace("\\", "/");
        int pointPosition = path.lastIndexOf(".");
        if (pointPosition > 0)
        {
          String extension = path.substring(pointPosition);
          if (extension.compareToIgnoreCase(".png") == 0)
          {
            loadTexture(path);
          }
          else if (extension.compareToIgnoreCase(".tmx") == 0)
          {
            loadTiledMap(path);
          }
        }
      }
    }
  }

  public FileHandle loadFile(String fileName)
  {
    return Gdx.files.internal(fileName);
  }

  public void loadTiledMap(String fileName)
  {
    load(fileName, TiledMap.class);
  }

  public TiledMap getTiledMap(String fileName)
  {
    return get(fileName, TiledMap.class);
  }

  public void loadGleed2dMap(String fileName)
  {
    load(fileName, Level.class);
  }

  public Level getGleed2dMap(String fileName)
  {
    return get(fileName, Level.class);
  }

  public void loadTexture(String fileName)
  {
    TextureParameter param = new TextureParameter();
    param.minFilter = TextureFilter.Linear;
    param.genMipMaps = false;
    load(fileName, Texture.class, param);
  }
  
  public Texture getTexture(String fileName)
  {
    return get(fileName, Texture.class);
  }

  public TextureRegion getTextureRegion(String fileName)
  {
    return new TextureRegion(get(fileName, Texture.class));
  }

  public TextureRegion getTextureRegion(String fileName, float u, float v, float u2, float v2)
  {
    return new TextureRegion(get(fileName, Texture.class), u, v, u2, v2);
  }

  public TextureRegion getTextureRegion(String fileName, int x, int y, int width, int height)
  {
    return new TextureRegion(get(fileName, Texture.class), x, y, width, height);
  }

  protected void loadSound(String fileName)
  {
    load(fileName, Sound.class);
  }

  protected Sound getSound(String fileName)
  {
    return get(fileName, Sound.class);
  }

  protected void loadMusic(String fileName)
  {
    load(fileName, Music.class);
  }

  protected Music getMusic(String fileName)
  {
    return get(fileName, Music.class);
  }

  @Override
  public synchronized void clear()
  {
    FontManager.instance.clear();
    SoundManager.instance.clear();
    super.clear();
  }

  @Override
  public synchronized void dispose()
  {
    FontManager.instance.clear();
    SoundManager.instance.clear();
    super.dispose();
  }

  @Override
  public synchronized boolean update()
  {
    if (super.update())
    {
      SoundManager.instance.finishLoading();
      return true;
    }
    return false;
  }

  @Override
  public boolean update(int millis)
  {
    if (super.update(millis))
    {
      SoundManager.instance.finishLoading();
      return true;
    }
    return false;
  }

  @Override
  public void finishLoading()
  {
    super.finishLoading();
    SoundManager.instance.finishLoading();
  }
}
