package com.shellGDX.model2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.box2dLight.RayHandler;
import com.shellGDX.controller.LightWorld;
import com.shellGDX.controller.PhysicsWorld;

public abstract class CompositeModel extends Group2D
{
  protected Actor actor = null;
  protected PhysicsModel2D body = null;
  protected LightModel2D light = null;
  protected EffectModel2D effect = null;
  
  public void init(TextureRegion textureRegion, Array<String> name)
  {
    if (initActor(textureRegion))
      super.addActor(this.actor);

    body = new PhysicsModel2D();
    if (initPhysics(PhysicsWorld.instance))
      super.addActor(this.body);
    else
      body.remove();
    
    light = new LightModel2D();
    if (initLigts(LightWorld.instance))
      super.addActor(this.light);
    else
      light.remove();
    
    effect = new EffectModel2D();
    if (initEffect(name))
      super.addActor(this.effect);
    else
      effect.remove();
  }
  
  public boolean initPhysics(World world) { return false; }
  public boolean initLigts(RayHandler rayHandler) { return false; }
  public boolean initEffect(Array<String> name) { return false; }
  public boolean initActor(TextureRegion textureRegion) { return false; }
  
  public Body getBody()
  {
    return body.getBody();
  }
  
  public void setActor (Actor actor)
  {
    if (this.actor != null)
    {
      this.actor.remove();
      this.actor = null;
    }

    this.actor = actor;
    super.addActor(this.actor);
  }
  
  @Override
  public void addActor(Actor actor) {}
}
