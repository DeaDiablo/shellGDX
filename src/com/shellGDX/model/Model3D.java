package com.shellGDX.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.shellGDX.manager.ResourceManager;

public class Model3D extends ModelInstance
{
  public Model3D()
  {
    super(new Model());
  }
  
  public Model3D(String fileModel)
  {
    super(ResourceManager.instance.getModel(fileModel));
  }

  protected Scene3D scene = null;
  protected Group3D parent = null;
  
  public void setScene(Scene3D newScene)
  {
    scene = newScene;
  }
  
  public Scene3D getScene()
  {
    return scene;
  }
  
  public void setParent(Group3D newParent)
  {
    parent = newParent;
  }
  
  public Group3D getParent()
  {
    return parent;
  }
  
  protected int zIndex = 0;

  public void setZIndex(int zIndex)
  {
    this.zIndex = zIndex;
  }

  public int getZIndex()
  {
    return this.zIndex;
  }
  
  protected void remove()
  {
    if (parent != null)
      parent.removeModel(this);
    parent = null;
  }

  public void update(float delta)
  {
  }
}
