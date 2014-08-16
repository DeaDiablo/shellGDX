package com.shellGDX;

import com.badlogic.gdx.Gdx;

public enum GameLog
{
  instance;
  
  public boolean enableLog = true;
  public boolean logInFile = false;
  public String  fileErrorLog = "errors.log";
  public String  fileGameLog = "game.log";
  
  public void writeError(String error)
  {
    if (enableLog)
      Gdx.app.error("Error!", error);
  }
  
  public void writeLog(String message)
  {
    if (enableLog)
      Gdx.app.log("Log \"" + GameInstance.game.getClass().getSimpleName().toString() + "\"", message);
  }
  
  public void writeFPS()
  {
    Gdx.app.log("Log \"" + GameInstance.game.getClass().getSimpleName().toString() + "\"", "fps=" + Gdx.graphics.getFramesPerSecond());
  }
}
