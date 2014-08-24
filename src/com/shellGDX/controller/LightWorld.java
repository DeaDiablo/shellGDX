package com.shellGDX.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.shellGDX.box2dLight.RayHandler;

public class LightWorld
{
  public static final long dayDuration = 2; // minutes

  public static float   WORLD_TO_BOX = PhysicsWorld.WORLD_TO_BOX;
  public static float   BOX_TO_WORLD = PhysicsWorld.BOX_TO_WORLD;
  
  public static RayHandler instance = null;
  
  protected static OrthographicCamera lightCamera = null;
  
  public static void init(OrthographicCamera camera)
  {
    lightCamera = camera;
    instance = new RayHandler(PhysicsWorld.instance);
    instance.setMeterToPixel(BOX_TO_WORLD);
    instance.setBlur(true);
    instance.setShadows(true);
    instance.setCulling(true);
    instance.setAmbientLight(0.1f, 0.1f, 0.1f, 0.1f);
  }
  
  public static void update()
  {
    if (instance == null || lightCamera == null)
      return;

    LightWorld.instance.setCombinedMatrix(lightCamera.combined,
                                          lightCamera.position.x,
                                          lightCamera.position.y,
                                          lightCamera.viewportWidth * lightCamera.zoom,
                                          lightCamera.viewportHeight * lightCamera.zoom);
    LightWorld.instance.update();
  }
  
  public static void destroy()
  {
    lightCamera = null;
    if (instance != null)
    {
      instance.dispose();
      instance = null;
    }
  }
}
