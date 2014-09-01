package com.shellGDX.model2D;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.manager.ResourceManager;

public class EffectObject2D extends Actor
{
  private static final HashMap<String, ParticleEffectPool> particlePools = new HashMap<String, ParticleEffectPool>();
  private final Array<PooledEffect> effects = new Array<PooledEffect>();

  public EffectObject2D()
  {
    this(0, 0, 0, 1, 1);
  }

  public EffectObject2D(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public EffectObject2D(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public EffectObject2D(float x, float y, float angle, float scaleX, float scaleY)
  {
    super();
    setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);
  }

  public void loadEffects(Array<String> effectNames)
  {
    for (String name : effectNames)
    {
      if (!particlePools.containsKey(name))
      {
        particlePools.put(name, new ParticleEffectPool(ResourceManager.instance.get(name, ParticleEffect.class), 5, 10));
      }
    }
  }
  
  public boolean activateEffect(String effectName)
  {
    PooledEffect effect = particlePools.get(effectName).obtain();
    effect.setPosition(getX(), getY());
    effects.add(effect);
    
    return true;
  }
  
  @Override
  public void act(float delta)
  {
    super.act(delta);
    
    for (int i = effects.size - 1; i >= 0; i--)
    {
      PooledEffect effect = effects.get(i);
      effect.setPosition(getX(), getY());
      effect.update(delta);
    }
 }
  
  @Override
  public void draw (Batch batch, float parentAlpha)
  {
    super.draw(batch, parentAlpha);

    batch.begin();
    for (int i = effects.size - 1; i >= 0; i--)
    {
      PooledEffect effect = effects.get(i);
      effect.draw(batch, Gdx.graphics.getDeltaTime());
      if (effect.isComplete())
      {
          effect.free();
          effects.removeIndex(i);
      }
    }
    batch.end();
  }
  
  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    
    if (visible == false)
    {
      for (int i = effects.size - 1; i >= 0; i--)
      {
        PooledEffect effect = effects.get(i);
        effect.free();
        effects.removeIndex(i);
      }
    }
  }
  
  @Override
  public boolean remove()
  {
    for (int i = effects.size - 1; i >= 0; i--)
      effects.get(i).free();
    effects.clear();

    if (!particlePools.isEmpty())
    {
      for(ParticleEffectPool pool : particlePools.values())
      {
        pool.clear();
      }
    }

    return super.remove();
  }
}
