import java.awt.*;

public abstract class Car {
    int x,y,width,height;
    int windowX,windowY,windowWidth,windowHeight;
    int window2X,window2Y,window2Width,window2Height;
    int carSpeedY=0,carSpeedX=0;
    boolean outOfBounds;
    Color color;
    Car(int x, int y, int width, int height , Color color){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.color=color;
    }
    //Update coordinates of car.
    boolean isLeft=true,isRight=false;
    public abstract void update();
}
