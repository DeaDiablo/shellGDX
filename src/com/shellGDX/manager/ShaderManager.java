package com.shellGDX.manager;

import java.util.HashMap;

import com.shellGDX.utils.Shader;

public enum ShaderManager
{
  instance;

  protected HashMap<String, Shader> shaders = new HashMap<String, Shader>();

  public void clear()
  {
    for (Shader shader : shaders.values())
    {
      shader.dispose();
    }
    shaders.clear();
  }

  public Shader createShader(String nameShader, String vertShader, String fragShader)
  {
    if (shaders.containsKey(nameShader))
      return shaders.get(nameShader);
    Shader shader = new Shader(vertShader, fragShader);
    if (!shader.isCompiled())
      return null;
    shaders.put(nameShader, shader);
    return shader;
  }

  public Shader getShader(String nameShader)
  {
    return shaders.get(nameShader);
  }
}
