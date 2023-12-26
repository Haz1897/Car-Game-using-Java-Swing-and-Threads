import java.awt.*;
import java.awt.font.GlyphMetrics;
import java.util.Random;
import java.util.concurrent.Phaser;

public class Enemy extends Car implements Runnable{
    final static int UPWARD=0,DOWNWARD=1;
    int type;
    Phaser p;
    Enemy(int x, int y, int width, int height, Phaser p, Color color){
        super(x,y,width,height,color);
        outOfBounds=true;
        this.p=p;
        p.register();
        windowHeight=height/5;
        windowWidth=(int)(width/1.5);
        /*windowX = x + windowWidth / 4;*/
        //windowY= y+height-windowHeight;
        window2Height=windowHeight/2;
        window2Width=windowWidth;
        /*window2X=windowX;*/
        reset();
    }
    int apperanceDelay=1000,counter=0;
    public void run(){
        while(!GamePanel.gameOver){
            if(carSpeedY<Road.speedFactor)
                carSpeedY=(int)(Road.speedFactor*Math.signum(carSpeedY));
            /*if((y>0||y<GamePanel.height)){
                outOfBounds=false;
            }*/
            counter++;
            if(counter>apperanceDelay) {
                update();
                outOfBounds=false;
            }
            if(y>GamePanel.height&&type==Enemy.DOWNWARD||(y<0-height&&type==Enemy.UPWARD)) {
                reset();
                counter=0;
                outOfBounds=true;
            }
            checkCollision();
            p.arriveAndAwaitAdvance();
        }
    }
    public void checkCollision(){
        //Check for player only.
        /*if(x>=GamePanel.player.x&& x<=GamePanel.player.x+GamePanel.carWidth //Left side X collision
                ||(x+width>=GamePanel.player.x&&x+width<=GamePanel.player.x+GamePanel.carWidth))//Right side X collision
        {
            if((y+height>=GamePanel.player.y&&y+height<=GamePanel.player.y+GamePanel.carHeight)
                    ||(y>=GamePanel.player.y&&y<=GamePanel.player.y+GamePanel.carHeight)) {
                        GamePanel.gameOver = true;//Benign data race.


            }
        }*/

        //Check for all cars.
        for(Car car: GamePanel.cars){
            if(car==this)
                continue;
            if(x>=car.x&& x<=car.x+GamePanel.carWidth //Left side X collision
                    ||(x+width>=car.x&&x+width<=car.x+GamePanel.carWidth))//Right side X collision
            {
                if((y+height>=car.y&&y+height<=car.y+GamePanel.carHeight)
                        ||(y>=car.y&&y<=car.y+GamePanel.carHeight)) {
                            if(car==GamePanel.player)
                                GamePanel.gameOver = true;//Benign data race.
                            else {
                                reset();
                                ((Enemy)car).reset();
                            }

                }
            }

        }

    }
    public void update(){
        y+=carSpeedY;
        windowY+=carSpeedY;
        window2Y+=carSpeedY;
    }
    public void reset(){
        apperanceDelay=700+new Random().nextInt(1001);
        counter=0;
        outOfBounds=true;
        Random random = new Random();
        color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256),255);
        type= new Random().nextInt(2);
        x=  Road.x+ new Random().nextInt(Road.width-width);
        windowX = x + windowWidth / 4;
        window2X=windowX;
        switch(type) {
            case UPWARD: //Facing forward.
                y=GamePanel.height+height;
                carSpeedY=-1*Road.speedFactor;
                windowY = y + windowHeight;
                window2Y=y+height-windowHeight;
                break;
            case DOWNWARD://Facing downward.
                y=0-height;
                carSpeedY=1;
                windowY = y+height-windowHeight*2;
                window2Y=y+windowHeight;
                break;
        }
    }
}
