package com.shellGDX.utils.gleed;

//import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LevelLoader extends AsynchronousAssetLoader<Level, LevelLoader.GLEEDParameter > implements Disposable
{

	static public class GLEEDParameter extends AssetLoaderParameters<Level>
  {
	}

	static private Logger logger = new Logger("LevelLoader");
	
	private Level level = null;
	private String pathRoot = "data";
	private TextureAtlas atlas = null;
	private String atlasFile = null;
	private AssetManager assetManager;
	
	public static void setLoggingLevel(int loggingLevel)
  {
		logger.setLevel(loggingLevel);
	}
	
	public LevelLoader(FileHandleResolver resolver)
  {
		super(resolver);
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, GLEEDParameter parameter)
	{
		assetManager = manager;
		
		logger.info("loading file " + fileName);
		
		try
    {
		  // Parse xml document
		  XmlReader reader = new XmlReader();
		  Element root = reader.parse(Gdx.files.internal(fileName));
			
		  // Create level and load properties
		  logger.info("loading level properties");
		  if (level == null)
      {
				level = new Level();
        level.setName(root.getAttribute("Name", ""));
				level.getProperties().load(root);
			}
			
			// Load atlas if necessary
			if (!atlasFile.isEmpty())
      {
				logger.info("fetching texture atlas " + atlasFile);
				atlas = manager.get(atlasFile, TextureAtlas.class);
			}
			
			// Load layers
			logger.info("loading layers");
			Array<Element> layerElements = root.getChildByName("Layers").getChildrenByName("Layer");
			
			for (int i = 0; i < layerElements.size; ++i)
      {
				Element layerElement = layerElements.get(i);
				loadLayer(layerElement);
			}
		}
    catch (Exception e)
    {
			logger.error("error loading file " + fileName + " " + e.getMessage());
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

		logger.info("getting asset dependencies for " + fileName);
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		
		try
    {
			// Parse xml document
			XmlReader reader = new XmlReader();
			Element root = reader.parse(Gdx.files.internal(fileName));

			if (level == null)
      {
				level = new Level();
				level.setRenderer();
        level.setName(root.getAttribute("Name", ""));
				level.getProperties().load(root);
			}
			
			// Check texture atlass
			atlasFile = level.getProperties().getString("atlas", "");
			pathRoot = level.getProperties().getString("assetRoot", "data");
			
			if (!atlasFile.isEmpty())
      {
				logger.info("texture atlas dependency " + atlasFile);
				dependencies.add(new AssetDescriptor(atlasFile, TextureAtlas.class));
			}
			else
      {
				logger.info("textures asset folder " + pathRoot);
				Array<Element> elements = root.getChildrenByNameRecursively("Item");
				
				for (int i = 0; i < elements.size; ++i)
        {
					Element element = elements.get(i);
					
					if (element.getAttribute("xsi:type", "").equals("TextureItem"))
          {
						String[] pathParts = element.getChildByName("texture_filename").getText().split("\\\\");
						logger.info("texture dependency " + pathRoot + "/" + pathParts[pathParts.length - 1]);
						dependencies.add(new AssetDescriptor(pathRoot + "/" + pathParts[pathParts.length - 1], Texture.class));
					}
				}
			}
			
		}
    catch (Exception e)
    {
			logger.error("error loading asset dependencies " + fileName + " " + e.getMessage());
		}
		
		return dependencies;
	}
	
	private void loadLayer(Element element)
  {
		Layer layer = new Layer();
		
		/*layer.properties.load(element);
		layer.name = element.getAttribute("Name", "");
		layer.visible = Boolean.parseBoolean(element.getAttribute("Visible", "true"));*/
    
    loadElement(layer, element);    
		
		//logger.info("loading layer " + layer.name);
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
				int xCoord = (int)texture.position.x / 10000;
				int yCoord = (int)texture.position.y / 10000;
				
				Array<TextureElement> array = layer.getTextures(xCoord, yCoord);
				if (array == null)
				{
				  array = new Array<TextureElement>();
				  layer.addTextureArray(xCoord, yCoord, array);
				}
				array.add(texture);
			}
			else if (type.equals("PathItem"))
      {
				PathElement path = new PathElement();
				loadElement(path, item);
				loadPathElement(path, item);
				int xCoord = (int)path.polygon.getX() / 10000;
        int yCoord = (int)path.polygon.getY() / 10000;
        
        Array<LevelObject> array = layer.getObjects(xCoord, yCoord);
        if (array == null)
        {
          array = new Array<LevelObject>();
          layer.addObjectArray(xCoord, yCoord, array);
        }
        array.add(path);
			}
			else if (type.equals("RectangleItem"))
      {
				RectangleElement rectangle = new RectangleElement();
				loadElement(rectangle, item);
				loadRectangleElement(rectangle, item);
				int xCoord = (int)rectangle.rectangle.x / 10000;
        int yCoord = (int)rectangle.rectangle.y / 10000;
        
        Array<LevelObject> array = layer.getObjects(xCoord, yCoord);
        if (array == null)
        {
          array = new Array<LevelObject>();
          layer.addObjectArray(xCoord, yCoord, array);
        }
        array.add(rectangle);
				
			}
			else if (type.equals("CircleItem"))
      {
				CircleElement circle = new CircleElement();
				loadElement(circle, item);
				loadCircleElement(circle, item);
				int xCoord = (int)circle.circle.x / 10000;
        int yCoord = (int)circle.circle.y / 10000;
        
        Array<LevelObject> array = layer.getObjects(xCoord, yCoord);
        if (array == null)
        {
          array = new Array<LevelObject>();
          layer.addObjectArray(xCoord, yCoord, array);
        }
        array.add(circle);
			}
		}
		
		level.getLayers().add(layer);
	}
	
	private void loadElement(LevelObject levelObject, Element element)
  {
		levelObject.name = element.getAttribute("Name", "");
		levelObject.visible = Boolean.parseBoolean(element.getAttribute("Visible", "true"));
		levelObject.properties.load(element);
		
		logger.info("loading element " + levelObject.name);
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
		logger.info("disposing level assets " + level.getName());
		
		for (int i = 0; i < level.getLayers().size; ++i)
    {
		  for (int x = 0; x < 100; ++x)
		    for (int y = 0; y < 100; ++y)
		    {
		      Array<TextureElement> textures = level.getLayers().get(i).getTextures(x, y);
			
		      for (int j = 0; j < textures.size; ++j)
		      {
		        TextureElement texture = textures.get(j);
				
		        if (assetManager.isLoaded(texture.path, Texture.class))
		        {
		          assetManager.unload(texture.path);
		        }
		      }
		    }
		}
	}
}
