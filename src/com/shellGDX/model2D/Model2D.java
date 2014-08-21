package com.shellGDX.model2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Model2D extends Actor
{
  protected TextureRegion region = null;
  protected int           zIndex = 0;

  public Model2D()
  {
    this(0, 0, 0, 1, 1);
  }

  public Model2D(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public Model2D(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public Model2D(float x, float y, float angle, float scaleX, float scaleY)
  {
    this(null, x, y, angle, scaleX, scaleY);
  }
  
  public Model2D(TextureRegion textureRegion)
  {
    this(textureRegion, 0, 0, 0, 1, 1);
  }

  public Model2D(TextureRegion textureRegion, float posX, float posY)
  {
    this(textureRegion, posX, posY, 0, 1, 1);
  }

  public Model2D(TextureRegion textureRegion, float posX, float posY, float angle)
  {
    this(textureRegion, posX, posY, angle, 1, 1);
  }

  public Model2D(TextureRegion textureRegion, float posX, float posY, float angle, float scaleX, float scaleY)
  {
    super();
    setTouchable(Touchable.disabled);
    setPosition(posX, posY);
    setRotation(angle);
    setScale(scaleX, scaleY);
    setTextureRegion(textureRegion);
  }
  
  public boolean isPhysicsModel()
  {
    return false;
  }

  //position
  public void setPosition(Vector2 position)
  {
    setPosition(position.x, position.y);
  }

  public Vector2 getPosition()
  {
    return new Vector2(getX(), getY());
  }
  
  //scale
  public void setScale(Vector2 scale)
  {
    setScale(scale.x, scale.y);
  }

  public Vector2 getScale()
  {
    return new Vector2(getScaleX(), getScaleY());
  }
  
  public void setTextureRegion(TextureRegion region)
  {
    if (region != null)
    {
      this.region = region;
      setSize(region.getRegionWidth(), region.getRegionHeight());
      calcOffset(region.getRegionWidth(), region.getRegionHeight());
    }
  }
  
  public TextureRegion getTextureRegion()
  {
    return region;
  }

  protected Rectangle bound = new Rectangle(0, 0, 0, 0);
  protected Vector2 bufferVec = new Vector2();

  public void updateBound()
  {    
    bound.set(0, 0, 0, 0);

    if (region == null)
      return;
    

    float width = region.getRegionWidth();
    float height = region.getRegionHeight();
    
    bufferVec.set(offset.x, offset.y);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.set(bufferVec.x, bufferVec.y, 0, 0);

    bufferVec.set(offset.x + width, offset.y);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
    
    bufferVec.set(offset.x, offset.y + height);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
    
    bufferVec.set(offset.x + width, offset.y + height);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
  }
  
  public Rectangle getBound()
  {
    return bound;
  }
  
  @Override
  public void setZIndex(int zIndex)
  {
    this.zIndex = zIndex;
  }
  
  @Override
  public int getZIndex()
  {
    return this.zIndex;
  }
  
  //offset
  public enum Align
  {
    LEFT,
    RIGHT,
    CENTER,
    TOP,
    BOTTOM
  }

  protected Align hAlign = Align.CENTER, vAlign = Align.CENTER;
  protected Vector2 offset = new Vector2();

  public void setAlign(Align horzAlign, Align vertAlign)
  {
    hAlign = horzAlign;
    vAlign = vertAlign;
    if (region != null)
      calcOffset(region.getRegionWidth(), region.getRegionHeight());
  }

  public Align getHorzAlign()
  {
    return hAlign;
  }

  public Align getVertAlign()
  {
    return vAlign;
  }

  public float getOffsetX()
  {
    return offset.x;
  }
  
  public float getOffsetY()
  {
    return offset.y;
  }
  
  protected void calcOffset(float width, float height)
  {
    switch (hAlign)
    {
      case LEFT:
        offset.x = 0.0f;
        break;
      case RIGHT:
        offset.x = -width;
        break;
      default:
        offset.x = -width * 0.5f;
        break;
    }

    switch (vAlign)
    {
      case BOTTOM:
        offset.y = 0.0f;
        break;
      case TOP:
        offset.y = -height;
        break;
      default:
        offset.y = -height * 0.5f;
        break;
    }
  }
  
  //update and draw
  public boolean update(float deltaTime)
  {
    return isVisible();
  }
  
  @Override
  public void act(float delta)
  {
    updateBound();
    super.act(delta);
    update(delta);  
  }
  
  protected Scene2D scene = null;
  
  @Override
  protected void setStage(Stage stage)
  {
    super.setStage(stage);
    if (stage instanceof Scene2D)
      scene = (Scene2D)stage;
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (!isVisible() || region == null)
      return;
    
    if (scene != null)
    {
      if (!scene.getCameraRectangle().overlaps(bound))
        return;
    }

    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    
    batch.draw(region,
               getX() + offset.x, getY() + offset.y,
               -offset.x, -offset.y,
               region.getRegionWidth(), region.getRegionHeight(),
               getScaleX(), getScaleY(),
               getRotation());
  }
  

  //touch
  protected InputListener inputListner = null;
  
  public void setTouchEnable(boolean enable)
  {
    if (enable)
    {
      setTouchable(Touchable.enabled);
      
      inputListner = new InputListener()
      {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return Model2D.this.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          Model2D.this.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          Model2D.this.touchDragged(event, x, y, pointer);
        }

        @Override
        public boolean mouseMoved(InputEvent event, float x, float y) {
          return Model2D.this.mouseMoved(event, x, y);
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
          Model2D.this.enter(event, x, y, pointer, fromActor);
        }

        @Override
        public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
          Model2D.this.exit(event, x, y, pointer, toActor);
        }

        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
          return Model2D.this.scrolled(event, x, y, amount);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
          return Model2D.this.keyDown(event, keycode);
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
          return Model2D.this.keyUp(event, keycode);
        }

        @Override
        public boolean keyTyped(InputEvent event, char character) {
          return Model2D.this.keyTyped(event, character);
        }
      };
      
      addListener(inputListner);
      return;
    }
    
    setTouchable(Touchable.disabled);
    removeListener(inputListner);
    inputListner = null;
  }
  
  public boolean getTouchEnable()
  {
    return getTouchable() == Touchable.enabled;
  }
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return false; }
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}
  public void touchDragged(InputEvent event, float x, float y, int pointer) {}
  public boolean mouseMoved(InputEvent event, float x, float y) { return false; }

  public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {}
  public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {}
  public boolean scrolled(InputEvent event, float x, float y, int amount) { return false; }

  public boolean keyDown(InputEvent event, int keycode) { return false; }
  public boolean keyUp(InputEvent event, int keycode) { return false; }
  public boolean keyTyped(InputEvent event, char character) { return false; }
}
