package com.shellGDX.utils.gleed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.GameLog;

public class LevelLoader extends AsynchronousAssetLoader<Level, LevelLoader.GLEEDParameter > implements Disposable
{

	static public class GLEEDParameter extends AssetLoaderParameters<Level>
  {
	}

	private Level level = null;
	private String pathRoot = "data";
	private TextureAtlas atlas = null;
	private String atlasFile = null;
	private AssetManager assetManager;
	
	public LevelLoader(FileHandleResolver resolver)
  {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, GLEEDParameter parameter)
	{
		assetManager = manager;
		
		GameLog.instance.writeLog("Loading Gleed2D file: " + fileName);
		
		try
    {
		  // Parse xml document
		  XmlReader reader = new XmlReader();
		  Element root = reader.parse(Gdx.files.internal(fileName));
			
		  // Create level and load properties
		  GameLog.instance.writeLog("Loading level properties");
		  if (level == null)
      {
				level = new Level();
        level.setName(root.getAttribute("Name", ""));
				level.getProperties().load(root);
			}
			
			// Load atlas if necessary
			if (!atlasFile.isEmpty())
      {
			  GameLog.instance.writeLog("Fetching texture atlas: " + atlasFile);
				atlas = manager.get(atlasFile, TextureAtlas.class);
			}
			
			// Load layers
			GameLog.instance.writeLog("Loading layers");
			Array<Element> layerElements = root.getChildByName("Layers").getChildrenByName("Layer");
			
			for (int i = 0; i < layerElements.size; ++i)
      {
				Element layerElement = layerElements.get(i);
				loadLayer(layerElement);
			}
		}
    catch (Exception e)
    {
      GameLog.instance.writeLog("Error loading file: " + fileName + " " + e.getMessage());
		}
	}

	@Override
	public Level loadSync (AssetManager manager, String fileName, FileHandle file, GLEEDParameter parameter)
	{
		return level;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, GLEEDParameter parameter)
	{
	  GameLog.instance.writeLog("Getting asset dependencies for: " + fileName);
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		
		try
    {
			// Parse xml document
			XmlReader reader = new XmlReader();
			Element root = reader.parse(Gdx.files.internal(fileName));

			if (level == null)
      {
				level = new Level();
        level.setName(root.getAttribute("Name", ""));
				level.getProperties().load(root);
			}
			
			// Check texture atlass
			atlasFile = level.getProperties().getString("atlas", "");
			pathRoot = level.getProperties().getString("assetRoot", "data");
			
			if (!atlasFile.isEmpty())
      {
			  GameLog.instance.writeLog("Texture atlas dependency: " + atlasFile);
				dependencies.add(new AssetDescriptor(atlasFile, TextureAtlas.class));
			}
			else
      {
			  GameLog.instance.writeLog("Textures asset folder: " + pathRoot);
				Array<Element> elements = root.getChildrenByNameRecursively("Item");
				
				for (int i = 0; i < elements.size; ++i)
        {
					Element element = elements.get(i);
					
					if (element.getAttribute("xsi:type", "").equals("TextureItem"))
          {
						String[] pathParts = element.getChildByName("texture_filename").getText().split("\\\\");
						GameLog.instance.writeLog("Texture dependency: " + pathRoot + "/" + pathParts[pathParts.length - 1]);
						dependencies.add(new AssetDescriptor(pathRoot + "/" + pathParts[pathParts.length - 1], Texture.class));
					}
				}
			}
			
		}
    catch (Exception e)
    {
      GameLog.instance.writeLog("Error loading asset dependencies: " + fileName + " " + e.getMessage());
		}
		
		return dependencies;
	}
	
	private void loadLayer(Element element)
  {
		Layer layer = new Layer();

    loadElement(layer, element);

		Array<Element> items = element.getChildByName("Items").getChildrenByName("Item");
		
		for (int i = 0; i < items.size; ++i)
    {
			Element item = items.get(i);
			String type = item.getAttribute("xsi:type");
			
			if (type.equals("TextureItem"))
      {
				TextureElement texture = new TextureElement();
				loadElement(texture, item);
				loadTextureElement(texture, item);
				int xCoord = (int)texture.position.x / Settings.xGridSize;
				int yCoord = (int)texture.position.y / Settings.yGridSize;
        layer.addTexture(xCoord, yCoord, texture);
			}
			else if (type.equals("PathItem"))
      {
				PathElement path = new PathElement();
				loadElement(path, item);
				loadPathElement(path, item);
				int xCoord = (int)path.polygon.getX() / Settings.xGridSize;
        int yCoord = (int)path.polygon.getY() / Settings.yGridSize;
        layer.addObject(xCoord, yCoord, path);
			}
			else if (type.equals("RectangleItem"))
      {
				RectangleElement rectangle = new RectangleElement();
				loadElement(rectangle, item);
				loadRectangleElement(rectangle, item);
				int xCoord = (int)rectangle.rectangle.x / Settings.xGridSize;
        int yCoord = (int)rectangle.rectangle.y / Settings.yGridSize;
        layer.addObject(xCoord, yCoord, rectangle);
				
			}
			else if (type.equals("CircleItem"))
      {
				CircleElement circle = new CircleElement();
				loadElement(circle, item);
				loadCircleElement(circle, item);
				int xCoord = (int)circle.circle.x / Settings.xGridSize;
        int yCoord = (int)circle.circle.y / Settings.yGridSize;
        layer.addObject(xCoord, yCoord, circle);
			}
		}
		
		level.getLayers().add(layer);
	}
	
	private void loadElement(LevelObject levelObject, Element element)
  {
		levelObject.name = element.getAttribute("Name", "");
		levelObject.visible = Boolean.parseBoolean(element.getAttribute("Visible", "true"));
		levelObject.properties.load(element);
		
		GameLog.instance.writeLog("Loading element: " + levelObject.name);
	}
	
	private void loadTextureElement(TextureElement texture, Element item)
  {
		Element positionElement = item.getChildByName("Position");
		texture.position.x = Float.parseFloat(positionElement.getChildByName("X").getText());
		texture.position.y = -Float.parseFloat(positionElement.getChildByName("Y").getText());
		
		Element origin = item.getChildByName("Origin");
		texture.originX = Float.parseFloat(origin.getChildByName("X").getText());
		texture.originY = Float.parseFloat(origin.getChildByName("Y").getText());
		
		Element scale = item.getChildByName("Scale");
		texture.scaleX = Float.parseFloat(scale.getChildByName("X").getText());
		texture.scaleY = Float.parseFloat(scale.getChildByName("Y").getText());
		
		Element colorElement = item.getChildByName("TintColor");
		texture.color.r = Float.parseFloat(colorElement.getChildByName("R").getText()) / 255.0f;
		texture.color.g = Float.parseFloat(colorElement.getChildByName("G").getText()) / 255.0f;
		texture.color.b = Float.parseFloat(colorElement.getChildByName("B").getText()) / 255.0f;
		texture.color.a = Float.parseFloat(colorElement.getChildByName("A").getText()) / 255.0f;
		
		texture.rotation = Float.parseFloat(item.getChildByName("Rotation").getText());
		
		if (atlasFile.isEmpty())
    {
			String[] pathParts = item.getChildByName("texture_filename").getText().split("\\\\");
			texture.path = pathRoot + "/" + pathParts[pathParts.length - 1];
			texture.region.setRegion(assetManager.get(texture.path, Texture.class));
		}
		else
    {
			String[] assetParts = item.getChildByName("asset_name").getText().split("\\\\");
			texture.path = assetParts[assetParts.length - 1];
			TextureRegion region = atlas.findRegion(texture.path);
			texture.region.setRegion(region);
		}
		
		texture.region.flip(Boolean.parseBoolean(item.getChildByName("FlipHorizontally").getText()),
							Boolean.parseBoolean(item.getChildByName("FlipVertically").getText()));
	}
	
	private void loadCircleElement(CircleElement circle, Element item)
  {
		Element position = item.getChildByName("Position");
		
		Element colorElement = item.getChildByName("FillColor");
		circle.color.r = Float.parseFloat(colorElement.getChildByName("R").getText()) / 255.0f;
		circle.color.g = Float.parseFloat(colorElement.getChildByName("G").getText()) / 255.0f;
		circle.color.b = Float.parseFloat(colorElement.getChildByName("B").getText()) / 255.0f;
		circle.color.a = Float.parseFloat(colorElement.getChildByName("A").getText()) / 255.0f;
		
		circle.circle = new Circle(new Vector2(Float.parseFloat(position.getChildByName("X").getText()),
											   -Float.parseFloat(position.getChildByName("Y").getText())),
											   Float.parseFloat(item.getChildByName("Radius").getText()));
	}
	
	private void loadRectangleElement(RectangleElement rectangle, Element item)
  {
		Element position = item.getChildByName("Position");
		
		Element colorElement = item.getChildByName("FillColor");
		rectangle.color.r = Float.parseFloat(colorElement.getChildByName("R").getText()) / 255.0f;
		rectangle.color.g = Float.parseFloat(colorElement.getChildByName("G").getText()) / 255.0f;
		rectangle.color.b = Float.parseFloat(colorElement.getChildByName("B").getText()) / 255.0f;
		rectangle.color.a = Float.parseFloat(colorElement.getChildByName("A").getText()) / 255.0f;
		
		rectangle.rectangle.x = Float.parseFloat(position.getChildByName("X").getText());
		rectangle.rectangle.y = -Float.parseFloat(position.getChildByName("Y").getText());
		rectangle.rectangle.width = Float.parseFloat(item.getChildByName("Width").getText());
		rectangle.rectangle.height = Float.parseFloat(item.getChildByName("Height").getText());
	}
	
	private void loadPathElement(PathElement path, Element item)
  {
		Array<Element> pointElements = item.getChildByName("WorldPoints").getChildrenByName("Vector2");
		float[] vertices = new float[pointElements.size * 2];
		
		for (int j = 0; j < pointElements.size; ++j)
    {
			Element pointElement = pointElements.get(j);
			vertices[j * 2] = Float.parseFloat(pointElement.getChildByName("X").getText());
			vertices[j * 2 + 1] = -Float.parseFloat(pointElement.getChildByName("Y").getText());
		}
		
		path.polygon = new Polygon(vertices);
		
		path.lineWidth = Integer.parseInt(item.getChildByName("LineWidth").getText());
		
		Element colorElement = item.getChildByName("LineColor");
		path.color.r = Float.parseFloat(colorElement.getChildByName("R").getText()) / 255.0f;
		path.color.g = Float.parseFloat(colorElement.getChildByName("G").getText()) / 255.0f;
		path.color.b = Float.parseFloat(colorElement.getChildByName("B").getText()) / 255.0f;
		path.color.a = Float.parseFloat(colorElement.getChildByName("A").getText()) / 255.0f;
	}
	
	@Override
	public void dispose()
  {
	  GameLog.instance.writeLog("Disposing level assets: " + level.getName());
		
		for (int i = 0; i < level.getLayers().size; ++i)
    {
		  level.getLayer(i).unload(assetManager);
		}
	}
}
