package com.shellGDX.controller;

import com.shellGDX.box2dLight.RayHandler;

public class LightWorld
{
  public static RayHandler instance = null;
  
  public static void init()
  {
    instance = new RayHandler(PhysicsWorld.instance);
    //instance.setBlur(true);
    //instance.setShadows(true);
    instance.setCulling(true);
    instance.setAmbientLight(0.1f, 0.01f, 0.01f, 0.01f);
  }
  
  public static void destroy()
  {
    if (instance != null)
    {
      instance.dispose();
      instance = null;
    }
  }
}
