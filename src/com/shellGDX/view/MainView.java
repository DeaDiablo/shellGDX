package com.shellGDX.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.controller.PhysicsWorld;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.manager.ShaderManager;

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

  public void draw(Array<Stage> stages)
  {
    for(Stage stage : stages)
    {
      stage.draw();
      if (PhysicsWorld.debug && PhysicsWorld.instance != null)
      {
        getDebugRenderer().render(PhysicsWorld.instance, stage.getCamera().combined);
      }
    }
  }
}
