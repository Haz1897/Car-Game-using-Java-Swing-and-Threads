import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.Phaser;

public class Player extends Car {
    Phaser p;
    Player(int x, int y, int width, int height,Phaser p,Color color){
        super(x,y,width,height,color);

        windowHeight=height/5;
        windowWidth=(int)(width/1.5);
        windowX=x+windowWidth/4;
        windowY=y+windowHeight;


        window2Height=windowHeight/2;
        window2Width=windowWidth;
        window2X=windowX;
        window2Y=y+height-windowHeight;

    }
    public void handleKeyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_UP){
            carSpeedY=-5;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            carSpeedY=5;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            carSpeedX=5;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            carSpeedX=-5;
        }
    }
    public void handleKeyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_UP){
            carSpeedY=0;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            carSpeedY=0;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            carSpeedX=0;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            carSpeedX=0;
        }
    }
    public void update(){
        y+=carSpeedY;
        windowY+=carSpeedY;
        if(x+carSpeedX<Road.width+Road.x-width&&x+carSpeedX>Road.x) {
            x += carSpeedX;
            windowX += carSpeedX;
            window2X += carSpeedX;
        }

        window2Y+=carSpeedY;
    }
}
