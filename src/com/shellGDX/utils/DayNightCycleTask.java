package com.shellGDX.utils;

import java.util.TimerTask;

import com.shellGDX.controller.LightWorld;

public class DayNightCycleTask extends TimerTask
{
  private float currentTime = 1 * 60; // seconds
  
  private boolean dayToNight = false;
  
  public DayNightCycleTask()
  {
  }

  @Override
  public void run()
  {
    float rgb = currentTime / (LightWorld.dayDuration * 60);
    LightWorld.instance.setAmbientLight(rgb, rgb, rgb, 0.5f);

    if (dayToNight)
      currentTime--;
    else 
      currentTime++;
    
    if (currentTime > LightWorld.dayDuration * 30)
    {
      dayToNight = true;
    }
    else if (currentTime == 0)
    {
      dayToNight = false;
    }
  }
}
