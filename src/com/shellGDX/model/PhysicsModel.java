package com.shellGDX.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.shellGDX.controller.PhysicsWorld;

public abstract class PhysicsModel extends Image
{
  public static final float WORLD_TO_BOX  = PhysicsWorld.WORLD_TO_BOX;
  public static final float BOX_TO_WORLD  = PhysicsWorld.BOX_TO_WORLD;

  protected BodyDef         bodyDef       = new BodyDef();
  protected FixtureDef      fixtureDef    = new FixtureDef();
  protected Body            body          = null;
  protected Fixture         fixture       = null;
  protected boolean         fixedRotation = false;

  public PhysicsModel()
  {
    this(0, 0, 0, 1, 1);
  }

  public PhysicsModel(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public PhysicsModel(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public PhysicsModel(float x, float y, float angle, float scaleX, float scaleY)
  {
    super();
    setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);
  }

  public PhysicsModel(TextureRegion textureRegion)
  {
    this(textureRegion, 0, 0, 0, 1, 1);
  }

  public PhysicsModel(TextureRegion textureRegion, float x, float y)
  {
    this(textureRegion, x, y, 0, 1, 1);
  }

  public PhysicsModel(TextureRegion textureRegion, float x, float y, float angle)
  {
    this(textureRegion, x, y, angle, 1, 1);
  }

  public PhysicsModel(TextureRegion textureRegion, float x, float y, float angle, float scaleX, float scaleY)
  {
    super(textureRegion);
    setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);
  }

  public abstract boolean initPhysicsObject(World world);
  
  public Body getBody()
  {
    return body;
  }
  
  public Fixture getFixture()
  {
    return fixture;
  }
  
  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
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
      setPosition(position.x, position.y);

      if (!fixedRotation)
        setRotation(MathUtils.radDeg * body.getAngle());
    }
  }
  
  @Override
  protected void setStage(Stage stage)
  {
    super.setStage(stage);
    if (PhysicsWorld.instance != null)
      initPhysicsObject(PhysicsWorld.instance);
  }
  
  @Override
  public boolean remove()
  {
    if (body != null && PhysicsWorld.instance != null)
    {
      PhysicsWorld.instance.destroyBody(body);
      body = null;
    }
    return super.remove();
  }
}
