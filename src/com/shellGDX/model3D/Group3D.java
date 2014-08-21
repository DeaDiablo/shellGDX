package com.shellGDX.model3D;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class Group3D extends Model3D
{
  private final SnapshotArray<Model3D> children = new SnapshotArray<Model3D>(true, 4, Model3D.class);
  public int                           visibleCount;

  public Group3D()
  {
    super();
  }

  public Group3D(Model model)
  {
    super(model);
  }

  public void update(float delta)
  {
    super.update(delta);
    Model3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      models[i].update(delta);
    }
    children.end();
  }

  /**
   * Draws the group and its children. The default implementation calls
   * {@link #applyTransform(Batch, Matrix4)} if needed, then
   * {@link #drawChildren(Batch, float)}, then {@link #resetTransform(Batch)} if
   * needed.
   */
  @Override
  public void draw(ModelBatch modelBatch, Environment environment)
  {
    super.draw(modelBatch, environment);
    drawChildren(modelBatch, environment);
  }

  public void drawChildren(ModelBatch modelBatch, Environment environment)
  {
    // modelBatch.render(children, environment); maybe faster
    SnapshotArray<Model3D> children = this.children;
    Model3D[] models = children.begin();
    visibleCount = 0;
    for (int i = 0, n = children.size; i < n; i++)
    {
      if (models[i] instanceof Group3D)
      {
        ((Group3D) models[i]).drawChildren(modelBatch, environment);
      }
      else
      {
        float offsetX = x, offsetY = y, offsetZ = z;
        float offsetScaleX = scaleX, offsetScaleY = scaleY, offsetScaleZ = scaleZ;
        float offsetYaw = yaw, offsetPitch = pitch, offsetRoll = roll;
        x = 0;
        y = 0;
        z = 0;
        scaleX = 0;
        scaleY = 0;
        scaleZ = 0;
        yaw = 0;
        pitch = 0;
        roll = 0;
        Model3D child = models[i];
        if (!child.isVisible())
          continue;
        /*
         * Matrix4 diff = sub(child.getTransform(), getTransform()); Matrix4
         * childMatrix = child.getTransform().cpy();
         * child.getTransform().set(add(diff, childMatrix));
         * child.draw(modelBatch, environment);
         */
        float cx = child.x, cy = child.y, cz = child.z;
        float sx = child.scaleX, sy = child.scaleY, sz = child.scaleZ;
        float ry = child.yaw, rp = child.pitch, rr = child.roll;
        // child.x = cx + offsetX;
        // child.y = cy + offsetY;
        // child.z = cz + offsetZ;
        child.setPosition(cx + offsetX, cy + offsetY, cz + offsetZ);
        child.setScale(sx + offsetScaleX, sy + offsetScaleY, sz + offsetScaleZ);
        child.setRotation(ry + offsetYaw, rp + offsetPitch, rr + offsetRoll);
        if (child.isCullable(getScene3d().getCamera()))
        {
          child.draw(modelBatch, environment);
          visibleCount++;
        }
        child.x = cx;
        child.y = cy;
        child.z = cz;
        x = offsetX;
        y = offsetY;
        z = offsetZ;
        child.scaleX = sx;
        child.scaleY = sy;
        child.scaleZ = sz;
        scaleX = offsetScaleX;
        scaleY = offsetScaleY;
        scaleZ = offsetScaleZ;
        child.yaw = ry;
        child.pitch = rp;
        child.roll = rr;
        yaw = offsetYaw;
        pitch = offsetPitch;
        roll = offsetRoll;
      }
    }
    children.end();
  }

  /**
   * Adds an model as a child of this group. The model is first removed from its
   * parent group, if any.
   * 
   * @see #remove()
   */
  public void addModel3D(Model3D model3D)
  {
    model3D.remove();
    children.add(model3D);
    model3D.setParent(this);
    model3D.setScene(getScene3d());
    childrenChanged();
  }

  /**
   * Removes an model from this group. If the model will not be used again and
   * has actions, they should be {@link Model#clearActions3d() cleared} so the
   * actions will be returned to their
   * {@link Action#setPool(com.badlogic.gdx.utils.Pool) pool}, if any. This is
   * not done automatically.
   */
  public boolean removeModel3D(Model3D model3D)
  {
    if (!children.removeValue(model3D, true))
      return false;
    Scene3D scene = getScene3d();
    if (scene != null)
      scene.unfocus(model3D);
    model3D.setParent(null);
    model3D.setScene(null);
    childrenChanged();
    return true;
  }

  /** Called when models are added to or removed from the group. */
  protected void childrenChanged()
  {
  }

  /** Removes all models from this group. */
  public void clearChildren()
  {
    Model3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      Model3D child = models[i];
      child.setScene(null);
      child.setParent(null);
    }
    children.end();
    children.clear();
    childrenChanged();
  }

  /** Removes all children, actions, and listeners from this group. */
  public void clear()
  {
    super.clear();
    clearChildren();
  }

  /**
   * Returns the first model found with the specified name. Note this
   * recursively compares the name of every model in the group.
   */
  public Model3D findModel(String name)
  {
    Array<Model3D> children = this.children;
    for (int i = 0, n = children.size; i < n; i++)
      if (name.equals(children.get(i).getName()))
        return children.get(i);
    for (int i = 0, n = children.size; i < n; i++)
    {
      Model3D child = children.get(i);
      if (child instanceof Group3D)
      {
        Model3D model = ((Group3D) child).findModel(name);
        if (model != null)
          return model;
      }
    }
    return null;
  }

  @Override
  protected void setScene(Scene3D scene3d)
  {
    super.setScene(scene3d);
    Array<Model3D> children = this.children;
    for (int i = 0, n = children.size; i < n; i++)
      children.get(i).setScene(scene3d);
  }

  /** Returns an ordered list of child models in this group. */
  public SnapshotArray<Model3D> getChildren()
  {
    return children;
  }

  public boolean hasChildren()
  {
    return children.size > 0;
  }

  /** Prints the model hierarchy recursively for debugging purposes. */
  public void print()
  {
    print("");
  }

  private void print(String indent)
  {
    Model3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      System.out.println(indent + models[i]);
      if (models[i] instanceof Group3D)
        ((Group3D) models[i]).print(indent + "|  ");
    }
    children.end();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    for (Model3D model3D : children)
      model3D.dispose();
  }
}