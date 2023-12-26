import java.util.concurrent.Phaser;

public class Score extends Thread{
    int x, y, fontSize,score=0;
    Phaser p;
    Score(int x,int y, int fontSize,Phaser p){
        this.x=x;
        this.y=y;
        this.fontSize=fontSize;
        this.p=p;
        p.register();
        start();
    }
    int scoreDelay=300,scoreCounter=0;
    public void run(){
        while(!GamePanel.gameOver){
            if(scoreCounter++>scoreDelay) {
                score += Road.speedFactor;
                scoreCounter=0;
            }
            p.arriveAndAwaitAdvance();
        }
    }

}
