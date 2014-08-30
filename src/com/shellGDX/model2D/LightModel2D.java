package com.shellGDX.model2D;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.shellGDX.box2dLight.LightWorld2D;
import com.shellGDX.box2dLight.RayHandler;

public abstract class LightModel2D extends PhysicsModel2D
{
  protected HashMap<String, Integer> lights = new HashMap<String, Integer>();
  
  public LightModel2D()
  {
    this(0, 0, 0, 1, 1);
  }

  public LightModel2D(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public LightModel2D(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public LightModel2D(float x, float y, float angle, float scaleX, float scaleY)
  {
    super();
    setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);
  }

  public LightModel2D(TextureRegion textureRegion)
  {
    this(textureRegion, 0, 0, 0, 1, 1);
  }

  public LightModel2D(TextureRegion textureRegion, float x, float y)
  {
    this(textureRegion, x, y, 0, 1, 1);
  }

  public LightModel2D(TextureRegion textureRegion, float x, float y, float angle)
  {
    this(textureRegion, x, y, angle, 1, 1);
  }

  public LightModel2D(TextureRegion textureRegion, float x, float y, float angle, float scaleX, float scaleY)
  {
    super(textureRegion);
    setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);
  }
  
  public abstract boolean initLightObject(RayHandler rayHandler);
  
  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    for(int hash : lights.values())
    {
      LightWorld2D.instance.getLight(hash).setActive(visible);
    }
  }
  
  @Override
  protected void setStage(Stage stage)
  {
    super.setStage(stage);
    if (LightWorld2D.instance != null)
      initLightObject(LightWorld2D.instance);
  }
  
  @Override
  public boolean remove()
  {
    if (LightWorld2D.instance != null && !lights.isEmpty())
    {
      for(int hash : lights.values())
      {
        LightWorld2D.instance.getLight(hash).remove();
      }
    }
    return super.remove();
  }
}
