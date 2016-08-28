package mx.itesm.hackmty2016;

/**
 * Created by Angela on 27/08/2016.
 */
import android.graphics.Rect;

public abstract class GameObject {
    protected Vector2D vectorPosition;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;

    public Vector2D getVectorPosition() {
        return vectorPosition;
    }

    public void setVectorPosition(Vector2D vectorPosition) {
        this.vectorPosition = vectorPosition;
    }

    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }
    public Rect getRectangle()
    {
        return new Rect((int)this.vectorPosition.getX(), (int)this.vectorPosition.getY(),
                (int)this.vectorPosition.getX()+width, (int)this.vectorPosition.getY()+height);
    }

}