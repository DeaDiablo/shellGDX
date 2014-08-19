package com.shellGDX.utils.gleed;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface ILevelRenderer
{
	public void render(Camera camera, Batch batch);
	public void render(Camera camera, Batch batch, int[] layers);
}
