package com.shellGDX.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.shellGDX.GameLog;

public class Shader extends ShaderProgram
{

  public Shader(String vertShader, String fragShader)
  {
    super(Gdx.files.internal(vertShader).readString(),
          Gdx.files.internal(fragShader).readString());

    if (!isCompiled())
    {
      GameLog.instance.writeError("Problem loading shader: " + getLog());
    }
  }

}
