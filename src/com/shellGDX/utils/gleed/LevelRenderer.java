package com.shellGDX.utils.gleed;

//import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class LevelRenderer implements Disposable, ILevelRenderer
{	
	private Level m_level;
	//private SpriteBatch m_batch = new SpriteBatch();
	private float m_mpp;
	/*private Rectangle m_box = new Rectangle();
	private Rectangle m_cameraRectangle = new Rectangle();
	
	private Vector2 m_a = new Vector2();
	private Vector2 m_b = new Vector2();
	private Vector2 m_c = new Vector2();
	private Vector2 m_d = new Vector2();*/
	
	public LevelRenderer(Level level)
  {
		this(level, null);
	}
	
	public LevelRenderer(Level level, ShaderProgram shader)
  {
		this(level, shader, 1.0f);
	}
	
	public LevelRenderer(Level level, ShaderProgram shader, float mpp)
  {
		m_level = level;
		m_mpp = mpp;
		
		/*if (shader != null)
    {
			m_batch.setShader(shader);
		}*/
	}

	public Level getLevel()
  {
		return m_level;
	}

	@Override
	public void render(Camera camera, Batch batch)
  {
		int[] layers = new int[m_level.getLayers().size];
		
		for (int i = 0; i < layers.length; ++i)
    {
			layers[i] = i;
		}
		
		render(camera, batch, layers);
	}

	@Override
	public void render(Camera camera, Batch batch, int[] layers)
  {
		
		Array<Layer> layersArray = m_level.getLayers();
		
		//m_batch.setProjectionMatrix(camera.combined);
		
		/*m_cameraRectangle.x = camera.position.x - camera.viewportWidth * 0.5f * camera.zoom;
		m_cameraRectangle.y = camera.position.y - camera.viewportHeight * 0.5f * camera.zoom;
		m_cameraRectangle.width = camera.viewportWidth *  camera.zoom;
		m_cameraRectangle.height = camera.viewportHeight *  camera.zoom;*/
		
		//m_batch.begin();
		//m_batch.enableBlending();
		
		for (int i = 0; i < layers.length; ++i)
    {
			Layer layer = layersArray.get(layers[i]);
			
			/*if (!layer.visible)
      {
				continue;
			}*/
			
			Array<TextureElement> texures = layer.getTextures((int)camera.position.x / 10000, (int)camera.position.y / 10000);
			
			
			for (int j = 0; j < texures.size; ++j)
      {
				TextureElement texture = texures.get(j);
				
				//setBounds(texture);

				// If the image is in the frustum, draw it (culling)
				/*if (m_cameraRectangle.overlaps(m_box) ||
					m_cameraRectangle.contains(m_box) ||
					m_box.contains(m_cameraRectangle))
        {*/
				
				batch.draw(texture.region,
               texture.position.x * m_mpp - texture.originX,
               texture.position.y * m_mpp - texture.originY,
               texture.originX,
               texture.originY,
               texture.region.getRegionWidth(),
               texture.region.getRegionHeight(),
               texture.scaleX * m_mpp,
               texture.scaleY * m_mpp,
               -MathUtils.radiansToDegrees * texture.rotation);
				/*}*/
			}
		}
		
		//m_batch.end();
	}

	@Override
	public void dispose()
  {
		//m_batch.dispose();
	}
	
	/*private void setBounds(TextureElement texture)
  {
		float x1 = -texture.region.getRegionWidth() * 0.5f * m_mpp * texture.scaleX;
		float x2 = -x1;
		float y1 = -texture.region.getRegionHeight() * 0.5f * m_mpp * texture.scaleY;
		float y2 = -y1;
		
		float sin = (float)Math.sin(texture.rotation);
		float cos = (float)Math.cos(texture.rotation);
		
		m_a.x = x1 * cos - y1 * sin;
		m_a.y = x1 * sin - y1 * cos;
		
		m_b.x = x2 * cos - y1 * sin;
		m_b.y = x2 * sin - y1 * cos;
		
		m_c.x = x2 * cos - y2 * sin;
		m_c.y = x2 * sin - y2 * cos;
		
		m_d.x = x1 * cos - y2 * sin;
		m_d.y = x1 * sin - y2 * cos;
		
		m_a.x += texture.position.x * m_mpp;
		m_a.y += texture.position.y * m_mpp;
		m_b.x += texture.position.x * m_mpp;
		m_b.y += texture.position.y * m_mpp;
		m_c.x += texture.position.x * m_mpp;
		m_c.y += texture.position.y * m_mpp;
		m_d.x += texture.position.x * m_mpp;
		m_d.y += texture.position.y * m_mpp;
		
		float minX = Math.min(Math.min(Math.min(m_a.x, m_b.x), m_c.x), m_d.x);
		float minY = Math.min(Math.min(Math.min(m_a.y, m_b.y), m_c.y), m_d.y);
		float maxX = Math.max(Math.max(Math.max(m_a.x, m_b.x), m_c.x), m_d.x);
		float maxY = Math.max(Math.max(Math.max(m_a.y, m_b.y), m_c.y), m_d.y);
		
		m_box.x = minX;
		m_box.y = minY;
		m_box.width = maxX - minX;
		m_box.height = maxY - minY;
	}*/
}

