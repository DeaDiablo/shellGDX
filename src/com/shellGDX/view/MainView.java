package com.shellGDX.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.controller.LightWorld;
import com.shellGDX.controller.PhysicsWorld;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.manager.ShaderManager;
import com.shellGDX.model2D.Scene2D;
import com.shellGDX.model3D.Scene3D;

public class MainView
{
  // for debug
  private ShapeRenderer      shapeRenderer = null;
  private Box2DDebugRenderer debugRenderer = null;

  public MainView()
  {
    super();
  }
  
  public ShapeRenderer getShapeRenderer()
  {
    if (shapeRenderer == null)
      shapeRenderer = new ShapeRenderer();
    return shapeRenderer;
  }
  
  public Box2DDebugRenderer getDebugRenderer()
  {
    if (debugRenderer == null)
      debugRenderer = new Box2DDebugRenderer();
    return debugRenderer;
  }

  public void dispose()
  {
    if (debugRenderer != null)
      debugRenderer.dispose();
    if (shapeRenderer != null)
      shapeRenderer.dispose();
    ResourceManager.instance.clear();
    ShaderManager.instance.clear();
  }

  public void draw(Array<Scene2D> scenes2D, Array<Scene3D> scenes3D)
  {
    for(Scene2D scene : scenes2D)
    {
      scene.draw();
      if (PhysicsWorld.debug && PhysicsWorld.instance != null)
      {
        getDebugRenderer().render(PhysicsWorld.instance, scene.getCamera().combined);
      }
      LightWorld.instance.setCombinedMatrix(scene.getCamera().combined,
          scene.getCamera().position.x,
          scene.getCamera().position.y,
          scene.getCamera().viewportWidth,
          scene.getCamera().viewportHeight);
      LightWorld.instance.update();
    }

    Gdx.gl.glClear(GL30.GL_DEPTH_BUFFER_BIT);
    
    for(Scene3D scene : scenes3D)
    {
      scene.draw();
    }
  }
}
