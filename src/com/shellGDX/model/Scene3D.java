package com.shellGDX.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Scene3D implements Disposable
{
  protected Group3D     mainGroup = null;
  protected ModelBatch  batch     = null;

  public PerspectiveCamera perspectiveCamera = null;
    
  public Scene3D(PerspectiveCamera camera)
  {
    perspectiveCamera = camera;
    mainGroup = new Group3D();
    mainGroup.setScene(this);
    
    batch = new ModelBatch();

    updateViewport();
  }
  
  public void updateViewport()
  {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }
  
  public Camera getCamera()
  {
    return perspectiveCamera;
  }
  
  public void act(float delta)
  {
    mainGroup.update(delta);
  }

  public void addModel3D(Model3D model)
  {
    Array<Model3D> children = mainGroup.getChildren();
    for (int i = 0; i < children.size; i++)
    {
      if (model.getZIndex() < children.get(i).getZIndex())
      {
        mainGroup.addModelAt(i, model);
        return;
      }
    }
    mainGroup.addModel(model);
  }

  public void draw()
  {
    batch.begin(perspectiveCamera);
    mainGroup.draw(batch, 1.0f);
    batch.end();
  }

  @Override
  public void dispose()
  {
    batch.dispose();
    mainGroup.clear();
  }
}
