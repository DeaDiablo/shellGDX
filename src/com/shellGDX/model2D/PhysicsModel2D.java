package com.shellGDX.model2D;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.shellGDX.controller.PhysicsWorld;

public class PhysicsModel2D extends Actor
{
  public static final float WORLD_TO_BOX  = PhysicsWorld.WORLD_TO_BOX;
  public static final float BOX_TO_WORLD  = PhysicsWorld.BOX_TO_WORLD;

  protected Body            body          = null;
  protected boolean         fixedRotation = false;

  public PhysicsModel2D()
  {
    this(0, 0, 0, 1, 1);
  }

  public PhysicsModel2D(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public PhysicsModel2D(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public PhysicsModel2D(float x, float y, float angle, float scaleX, float scaleY)
  {
    super();
    /*setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);*/
  }
  
  public Body getBody()
  {
    return body;
  }
  
  public void setBody(Body body)
  {
    this.body = body;
  }
  
  public boolean getFixedRotation()
  {
    return fixedRotation;
  }
  
  public void setFixedRotation(boolean fixedRotation)
  {
    this.fixedRotation = fixedRotation;
  }
  
  private void setBodyTransform(float x, float y, float angle)
  {
    body.setTransform(x, y, body.getAngle());
  }
  
  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    body.setActive(visible);
  }
  
  @Override
  public float getX ()
  {
    Vector2 position = body.getPosition();
    position.scl(BOX_TO_WORLD);
    
    return position.x;
  }

  @Override
  public void setX (float x)
  {
    float bodyX = x * WORLD_TO_BOX;
    float bodyY = body.getPosition().y;
    float bodyAngle = body.getAngle();
    setBodyTransform(bodyX, bodyY, bodyAngle);

    positionChanged();
  }

  @Override
  public float getY ()
  {
    Vector2 position = body.getPosition();
    position.scl(BOX_TO_WORLD);
    
    return position.y;
  }

  @Override
  public void setY (float y)
  {
    float bodyX = body.getPosition().x;
    float bodyY = y * WORLD_TO_BOX;
    float bodyAngle = body.getAngle();
    setBodyTransform(bodyX, bodyY, bodyAngle);

    positionChanged();
  }
  
  @Override
  public void setPosition (float x, float y)
  {
    float bodyX = x * WORLD_TO_BOX;
    float bodyY = y * WORLD_TO_BOX;
    float bodyAngle = body.getAngle();
    setBodyTransform(bodyX, bodyY, bodyAngle);
    
    positionChanged();
  }
  
  @Override
  public void moveBy (float x, float y)
  {
    if (x != 0 || y != 0)
    {
      float bodyX = body.getPosition().x + x * WORLD_TO_BOX;
      float bodyY = body.getPosition().y + y * WORLD_TO_BOX;
      float bodyAngle = body.getAngle();
      setBodyTransform(bodyX, bodyY, bodyAngle);

      positionChanged();
    }
  }
  
  @Override
  public float getRotation ()
  {
    return body.getAngle();
  }

  @Override
  public void setRotation (float degrees)
  {
    float bodyX = body.getPosition().x;
    float bodyY = body.getPosition().y;
    float bodyAngle = degrees;
    setBodyTransform(bodyX, bodyY, bodyAngle);
  }

  @Override
  public void rotateBy (float amountInDegrees)
  {
    float bodyX = body.getPosition().x;
    float bodyY = body.getPosition().y;
    float bodyAngle = body.getAngle() + amountInDegrees;
    setBodyTransform(bodyX, bodyY, bodyAngle);
  }

  @Override
  public void act(float delta)
  {
    super.act(delta);

    if (body != null)
    {
      body.setActive(isVisible());

      Vector2 position = body.getPosition();
      position.scl(BOX_TO_WORLD);
      getParent().setPosition(position.x, position.y);

      if (!fixedRotation)
        getParent().setRotation(MathUtils.radDeg * body.getAngle());
    }
  }
  
  @Override
  public boolean remove()
  {
    /*if (body != null && PhysicsWorld.instance != null)
    {
      PhysicsWorld.instance.destroyBody(body);
      body = null;
    }*/
    return super.remove();
  }
}
