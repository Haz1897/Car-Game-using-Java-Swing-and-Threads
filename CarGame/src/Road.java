import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class Road extends Thread {
    static int x,y,width,height;
    static int speed;
    static int lineWidth=10,lineHeight=50,lineX,lineY;
    static int sidewalkWidth = 20,sidewalkHeight=40;
    ArrayList<Rectangle> lines = new ArrayList<Rectangle>();
    ArrayList<Rectangle> sideWalk = new ArrayList<Rectangle>();
    Phaser p;
    static ArrayList<StreetLight> lights = new ArrayList<StreetLight>();
    Road(int x, int y, int width, int height,Phaser p){
        this.p=p;
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        lineX=width-(lineWidth/2);
        prepareLines();
        prepareLights();
        p.register();
        this.start();
    }
    public void prepareLights(){
        for(int i=0; i<6;i++){
            lights.add(new StreetLight(150,20+(i*200)));
            i++;
            lights.add(new StreetLight(150,20+(i*200)));
            lights.get(i).duplicateLight(lights.get(i-1));
        }
    }
    public void prepareLines(){
        for(int i=0;i<10;i++){
            lines.add(new Rectangle(lineX,lineY+(i*(40+lineHeight)),lineWidth,lineHeight));
        }
        for(int i=0;i<22;i++){

            sideWalk.add(new Rectangle(width/2-sidewalkWidth,lineY+(i*sidewalkHeight),sidewalkWidth,sidewalkHeight));
            sideWalk.add(new Rectangle(width+width/2,lineY+(i*sidewalkHeight),sidewalkWidth,sidewalkHeight));
        }
    }
    static int lineInterval=0,speedFactor=1,speedUpInterval=10000,speedUpCounter=0;
    boolean spedUp=false;
    public void run(){
        while(!GamePanel.gameOver){
                if(lineInterval>=(GamePanel.speedThreshhold-speed)){
                    updateLines();
                    updateLamps();
                    if(speed<GamePanel.speedThreshhold-1)
                        speed++;
                    if(speedFactor<3) {
                        speedUpCounter++;
                        if (speedUpCounter == speedUpInterval) {
                            speedUpCounter = 0;
                            speedFactor++;
                        }
                    }
                }
                lineInterval=(lineInterval+1)%(GamePanel.speedThreshhold-speed+1);
                p.arriveAndAwaitAdvance();
            }

        }
    public void updateLines(){
        for(Rectangle line:lines){
            line.y+=speedFactor;
            if(line.y>height+lineHeight){
                line.y=0-lineHeight;
            }
        }
        for(Rectangle sidewalkPiece:sideWalk){
            sidewalkPiece.y+=speedFactor;
            if(sidewalkPiece.y> height+sidewalkHeight){
                sidewalkPiece.y=0-sidewalkHeight;
            }
        }
    }
    public void updateLamps(){
        for(StreetLight light:lights){
            light.poleY+=speedFactor;
            light.innerLightY+=speedFactor;
            light.outerLightY+=speedFactor;
            if(light.poleY-200>GamePanel.height){
                light.poleY=light.poleY-400-GamePanel.height;
                light.innerLightY=light.innerLightY-400-GamePanel.height;
                light.outerLightY=light.outerLightY-400-GamePanel.height;
            }
        }
    }
    public void draw(Graphics g){
        g.setColor(Color.darkGray);
        g.fillRect(x,y,width,height);
        g.setColor(Color.WHITE);
        for(Rectangle line: lines){
            g.fillRect(line.x,line.y,line.width,line.height);
        }
        boolean toggle=false;
        int count=0;
        for(Rectangle sidewalkPiece:sideWalk){
            if(count==2){
                toggle ^=true;
                count=0;
            }
            if(!toggle)
                g.setColor(Color.yellow);
            else
                g.setColor(Color.black);

            g.fillRect(sidewalkPiece.x,sidewalkPiece.y,sidewalkPiece.width,sidewalkPiece.height);
            count++;
        }
    }
}
