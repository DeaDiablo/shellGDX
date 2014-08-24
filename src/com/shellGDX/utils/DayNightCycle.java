package com.shellGDX.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.shellGDX.controller.LightWorld;

public class DayNightCycle
{
  private float currentTime = 0.0f;
  private int hours = 0;
  private int minutes = 0;
  private float hourDuration = 0.0f;
  
  public DayNightCycle(float startHour, float hourDuration, boolean clearWeather)
  {
    this.hourDuration = hourDuration;
    this.currentTime = (startHour % 24) * 60;
    hours = (int)currentTime / 60;
    minutes = (int)currentTime % 60;
    currentClearWeather = clearWeather;
    
    //init
    factorWeather = clearWeather ? 0.0f : -0.25f;
    updateColor(dayColor, hours);
    switchTime = 0.0f;
  }
  public int getHours()
  {
    return hours;
  }
  
  public int getMinutes()
  {
    return minutes;
  }
  
  private float switchTime = 0.0f;
  private float switchDuration = 0.0f;
  
  private boolean currentClearWeather = true;
  private float deltaWeather = 60.0f;
  private float factorWeather = 0.0f;
  private Color dayColor = new Color();
  private Color startColor = new Color();
  private Color endColor = new Color();

  public void update(float deltaTime, boolean clearWeather)
  {
    currentTime += deltaTime / hourDuration;
    if (currentTime >= 1440) //minutes in day
      currentTime = 0;
    
    hours = (int)currentTime / 60;
    minutes = (int)currentTime % 60;
    
    Gdx.app.log("time", " " + hours + ":" + minutes);
    
    switchTime -= deltaTime / hourDuration;
    if (switchTime <= 0.0f)
    {
      startColor.set(dayColor);
      updateColor(endColor, hours);
      if (!clearWeather)
        endColor.set(endColor.b, endColor.b, endColor.b, 1.0f);
    }

    //weather
    if (currentClearWeather != clearWeather)
    {
      deltaWeather = 0.0f;
      currentClearWeather = clearWeather;
    }

    deltaWeather += deltaTime / hourDuration;
    if (deltaWeather < 60.0f) //1 hour
    {
      factorWeather = -0.15f * deltaWeather / 60.0f;
      if (currentClearWeather)
        factorWeather = 0.15f - factorWeather;
    }

    dayColor.r = startColor.r + (endColor.r - startColor.r) * (switchDuration - switchTime) / switchDuration;
    dayColor.g = startColor.g + (endColor.g - startColor.g) * (switchDuration - switchTime) / switchDuration;
    dayColor.b = startColor.b + (endColor.b - startColor.b) * (switchDuration - switchTime) / switchDuration;

    LightWorld.instance.setAmbientLight(dayColor.r + factorWeather, dayColor.g + factorWeather, dayColor.b + factorWeather, 1.0f);
  }
  
  protected void updateColor(Color color, float hours)
  {
    //night
    if (hours >= 22)
    {
      color.set(0.0f, 0.0f, 0.0f, 1.0f);
      switchTime = 30 - hours;
    }
    else if (hours <= 5)
    {
      color.set(0.0f, 0.0f, 0.0f, 1.0f);
      switchTime = 6 - hours;
    }
    //early morning
    else if (hours <= 7)
    {
      color.set(0.4f, 0.3f, 0.2f, 1.0f);
      switchTime = 8 - hours;
    }
    //morning
    else if (hours <= 9)
    {
      color.set(0.5f, 0.5f, 0.5f, 1.0f);
      switchTime = 10 - hours;
    }
    //day
    else if (hours <= 17)
    {
      color.set(0.5f, 0.5f, 0.5f, 1.0f);
      switchTime = 18 - hours;
    }
    //early evening
    else if (hours <= 19)
    {
      color.set(0.4f, 0.3f, 0.2f, 1.0f);
      switchTime = 20 - hours;
    }
    //evening
    else if (hours <= 21)
    {
      color.set(0.0f, 0.0f, 0.0f, 1.0f);
      switchTime = 22 - hours;
    }
    
    switchTime *= 60.0f;
    switchDuration = switchTime;
  }
}
