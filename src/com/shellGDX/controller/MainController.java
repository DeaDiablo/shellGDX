package com.shellGDX.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.manager.SoundManager;

public class MainController extends InputMultiplexer
{
  // Stages
  private Array<Stage> stages = new Array<Stage>();

  public MainController()
  {
    super();
  }

  // Stages
  public Array<Stage> getStages()
  {
    return stages;
  }

  public boolean addStage(Stage stage)
  {
    if (stages.contains(stage, true))
      return false;

    if (stage instanceof InputProcessor)
      addProcessor(stage);
    stages.add(stage);
    
    return true;
  }

  public void removeStage(Stage stage)
  {
    if (!stages.contains(stage, true))
      return;

    if (stage instanceof InputProcessor)
      removeProcessor(stage);

    stages.removeValue(stage, true);
    stage.dispose();
  }

  public void update(float deltaTime)
  {
    TimeController.update(deltaTime);
    SoundManager.instance.update();
    PhysicsWorld.update(deltaTime);

    // update elements
    for (Stage stage : stages)
    {
      stage.act(deltaTime);
    }
  }

  public void clear()
  {
    super.clear();
    
    for(Stage stage : stages)
      removeStage(stage);
    stages.clear();
    
    PhysicsWorld.destroy();
  }

  public void resize(int width, int height)
  {
    for (Stage stage : stages)
    {
      stage.getViewport().update(width, height);
    }
  }
}
