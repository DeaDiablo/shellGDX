package com.shellGDX.model2D;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shellGDX.controller.LightWorld;

public class LightModel2D extends Actor
{
  protected HashMap<String, Integer> lights = new HashMap<String, Integer>();
  
  public LightModel2D()
  {
    this(0, 0, 0);
  }

  public LightModel2D(float x, float y)
  {
    this(x, y, 0);
  }

  public LightModel2D(float x, float y, float angle)
  {
    super();
    setPosition(x, y);
    setRotation(angle);
  }
  
  public void addLight(String name, Integer hash)
  {
    lights.put(name, hash);
  }
  
  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    for(int hash : lights.values())
    {
      LightWorld.instance.getLight(hash).setActive(visible);
    }
  }
  
  @Override
  public void act(float delta)
  {
    super.act(delta);

    for(int hash : lights.values())
    {
      LightWorld.instance.getLight(hash).setPosition(getX(), getY());
    }
  }
  
  @Override
  public boolean remove()
  {
    /*if (LightWorld.instance != null && !lights.isEmpty())
    {
      for(int hash : lights.values())
      {
        LightWorld.instance.getLight(hash).remove();
      }
    }*/
    return super.remove();
  }
}
