package com.shellGDX.model;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;

public class Group3D extends Model3D
{
  protected Array<Model3D> children = new Array<Model3D>();

  public Group3D()
  {
  }

  public Array<Model3D> getChildren()
  {
    return children;
  }
  
  public void addModel(Model3D model)
  {
    model.remove();
    children.add(model);
    model.setParent(this);
    model.setScene(scene);
  }
  
  public void addModelAt(int index, Model3D model)
  {
    model.remove();
    children.insert(index, model);
    model.setParent(this);
    model.setScene(scene);
  }
  
  public boolean removeModel(Model3D model)
  {
    if (!children.removeValue(model, true))
      return false;

    model.remove();
    model.setScene(null);
    return true;
  }

  public void clear()
  {
    for (int i = 0; i < children.size; i++)
    {
      Model3D model = children.get(i);

      if (model instanceof Group3D)
      {
        ((Group3D)model).clear();
      }

      model.remove();
      model.setScene(null);
    }
    children.clear();
  }
  
  public void draw(ModelBatch batch, float parentAlpha)
  {
    batch.render(children);
  }
}
