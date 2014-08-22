package com.shellGDX.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.shellGDX.box2dLight.RayHandler;

public class LightWorld
{
  public static float   WORLD_TO_BOX = PhysicsWorld.WORLD_TO_BOX;
  public static float   BOX_TO_WORLD = PhysicsWorld.BOX_TO_WORLD;
  
  public static RayHandler instance = null;
  
  protected static OrthographicCamera lightCamera = null;
  
  public static void init(OrthographicCamera camera)
  {
    lightCamera = camera;
    instance = new RayHandler(PhysicsWorld.instance);
    //instance.setBlur(true);
    //instance.setShadows(true);
    instance.setCulling(true);
    instance.setAmbientLight(0.1f, 0.01f, 0.01f, 0.01f);
    matrix = new Matrix4();
  }
  
  protected static Matrix4 matrix = null;
  
  public static void update()
  {
    if (instance == null || lightCamera == null)
      return;

    matrix.set(lightCamera.combined);
    matrix.val[Matrix4.M03] *= LightWorld.WORLD_TO_BOX;
    matrix.val[Matrix4.M13] *= LightWorld.WORLD_TO_BOX;
    LightWorld.instance.setCombinedMatrix(matrix,
                                          lightCamera.position.x * LightWorld.WORLD_TO_BOX,
                                          lightCamera.position.y * LightWorld.WORLD_TO_BOX,
                                          lightCamera.viewportWidth * lightCamera.zoom * LightWorld.WORLD_TO_BOX,
                                          lightCamera.viewportHeight * lightCamera.zoom * LightWorld.WORLD_TO_BOX);
    LightWorld.instance.update();
  }
  
  public static void destroy()
  {
    matrix = null;
    lightCamera = null;
    if (instance != null)
    {
      instance.dispose();
      instance = null;
    }
  }
}
