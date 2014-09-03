package com.shellGDX.model2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class EffectObject2D extends Actor
{
  private final SnapshotArray<PooledEffect> effects = new SnapshotArray<PooledEffect>(false, 1, PooledEffect.class);

  public EffectObject2D(PooledEffect effect)
  {
    super();
    effects.add(effect);
  }
  
  public EffectObject2D(Array<PooledEffect> effects)
  {
    super();
    this.effects.addAll(effects);
  }
  
  @Override
  public void act(float deltaTime)
  {
    super.act(deltaTime);
    if (effects.size > 0)
    {
      PooledEffect[] effectsArray = effects.begin();
      for (int i = 0, n = effects.size; i < n; i++)
        effectsArray[i].update(deltaTime);
      effects.end();
    }
  }
  
  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (!isVisible())
      return;
    
    if (effects.size > 0)
    {
      PooledEffect[] effectsArray = effects.begin();
      for (int i = 0, n = effects.size; i < n; i++)
      {
        PooledEffect effect = effectsArray[i];
        effect.setPosition(getX(), getY());
        effect.draw(batch, Gdx.graphics.getDeltaTime());
        if (effect.isComplete())
        {
            effect.free();
            effects.removeIndex(i);
        }
        effect.setPosition(0, 0);
      }
      effects.end();
    }
  }
  
  @Override
  public boolean remove()
  {
    if (effects.size > 0)
    {
      PooledEffect[] effectsArray = effects.begin();
      for (int i = 0, n = effects.size; i < n; i++)
        effectsArray[i].free();
      effects.end();
      effects.clear();
    }
    return super.remove();
  }
}
