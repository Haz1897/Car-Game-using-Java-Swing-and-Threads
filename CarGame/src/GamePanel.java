import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Phaser;

public class GamePanel extends JPanel implements Runnable{
    Phaser roadPhaser = new Phaser();
    Phaser carPhaser = new Phaser();
    Phaser scorePhaser = new Phaser();
    static int width=1000,height=800, roadWidth=(int)(width/2),carWidth=roadWidth/10,carHeight=carWidth*2;
    static int speed=10;
    Dimension screenSize = new Dimension(width,height);
    Thread gameThread;
    Graphics graphics;
    Road road;
    Image image;
    Timer t1;
    static Player player;//Playable car.
    static boolean gameOver=false; //Used to check if game is over or not.
    static ArrayList<Car> cars = new ArrayList<Car>(); //ArrayList used to draw cars.
    Score score;
    GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(new AL());

        road = new Road((width/2)-(roadWidth/2),0,roadWidth,height,roadPhaser);//Setting up road.
        gameThread = new Thread(this); //Setting the main Thread


        //Setting up cars
        player = new Player((width/2)-carWidth/2,height/2-carHeight/2,carWidth,carHeight,carPhaser,new Color(0,0,255,255));
        Enemy enemy = new Enemy(road.x+ new Random().nextInt(road.width-carWidth),200,carWidth,carHeight,carPhaser,new Color(255,0,0,255));
        Enemy enemy2 = new Enemy(road.x+ new Random().nextInt(road.width-carWidth),200,carWidth,carHeight,carPhaser,new Color(65, 211, 22,255));
        Enemy enemy3 = new Enemy(road.x+ new Random().nextInt(road.width-carWidth),200,carWidth,carHeight,carPhaser,new Color(148, 22, 211,255));
        Enemy enemy4 = new Enemy(road.x+ new Random().nextInt(road.width-carWidth),200,carWidth,carHeight,carPhaser,new Color(222, 142, 19,255));
        Enemy enemy5 = new Enemy(road.x+ new Random().nextInt(road.width-carWidth),200,carWidth,carHeight,carPhaser,new Color(222, 19, 212,255));

        //Adding cars to car list.
        cars.add(player);
        cars.add(enemy);
        cars.add(enemy2);
        cars.add(enemy3);
        cars.add(enemy4);
        cars.add(enemy5);

        roadPhaser.register();
        carPhaser.register();
        scorePhaser.register();

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                player.update();  // have player-controlled sprite update itself
                repaint();  // repaint the screen
            }
        };
        t1=new Timer(1, taskPerformer);
        t1.start();

        //Setting up enemy car threads.
        gameThread.start();
        new Thread(enemy).start();
        new Thread(enemy2).start();
        new Thread(enemy3).start();
        new Thread(enemy4).start();
        new Thread(enemy5).start();

        score = new Score(width-200,40,30,scorePhaser);
    }
    public void paint(Graphics g){
        image = createImage(getWidth(),getHeight());
        graphics=image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void run(){
        while(!gameOver){
            repaint();
        }
        t1.stop();
        repaint();
    }
    public void draw(Graphics g){
        if(gameOver){
            road.draw(g);
            for(Car car: cars) {
                drawCar(g, car);
            }
            drawStreetLight();
            drawScore(g);
            graphics.setColor(Color.YELLOW);
            graphics.setFont(new Font("Serif",Font.PLAIN,50));
            graphics.drawString("Game Over!",width/2-100,height/2-50);
            graphics.setFont(new Font("Serif",Font.PLAIN,35));
            graphics.drawString("Score: "+score.score,width/2-50,height/2);
            return;
        }
        //Draw Road
        roadPhaser.arriveAndAwaitAdvance();
        road.draw(g);

        //Draw Cars
        carPhaser.arriveAndAwaitAdvance(); //wait for cars to finish updating coordinates.
        for(Car car: cars) {
            drawCar(g, car);
        }

        //Draw Streetlight
        drawStreetLight();

        //Draw Score
        scorePhaser.arriveAndAwaitAdvance();
        drawScore(g);


    }
    public void drawStreetLight(){
        for(StreetLight light: Road.lights){
            graphics.setColor(light.outerLightColor);
            graphics.fillOval(light.outerLightX,light.outerLightY,200,200);
            graphics.setColor(light.lightColor);
            graphics.fillOval(light.innerLightX,light.innerLightY,100,100);
            graphics.setColor(light.lampColor);
            graphics.fillRect(light.poleX,light.poleY,light.width,light.height);
        }
        /*graphics.setColor(str.outerLightColor);
        graphics.fillOval(str.outerLightX,str.outerLightY,200,200);
        graphics.setColor(str.lightColor);
        graphics.fillOval(str.innerLightX,str.innerLightY,100,100);
        graphics.setColor(str.lampColor);
        graphics.fillRect(str.poleX,str.poleY,str.width,str.height);
        graphics.fillOval(str.poleX+str.width-20,str.poleY-(5),30,30);*/
    }
    public void drawScore(Graphics g){
        g.setColor(Color.yellow);
        g.setFont(new Font("Serif",Font.PLAIN,score.fontSize));
        g.drawString("Score: "+score.score,score.x,score.y);
    }
    public void drawCar(Graphics g, Car car){
        if(!car.outOfBounds) {
            g.setColor(car.color);
            g.fillRect(car.x, car.y, car.width, car.height);
            g.setColor(Color.CYAN);
            g.fillRect(car.windowX, car.windowY, car.windowWidth, car.windowHeight);
            g.fillRect(car.window2X, car.window2Y, car.window2Width, car.window2Height);
        }
        else{
            //Only Enemies are considered out of bounds.
            Enemy temp = (Enemy)car;
            switch(temp.type){
                case Enemy.DOWNWARD:
                    g.setColor(Color.red);
                    g.fillRect(temp.x+(temp.width/2),0+30,10,40);
                    g.fillRect(temp.x+(temp.width/2),0+30+40+20,10,10);
                    break;
                case Enemy.UPWARD:
                    g.setColor(Color.red);
                    g.fillRect(temp.x+(temp.width/2),height-30-20-20-40,10,40);
                    g.fillRect(temp.x+(temp.width/2),height-30-20,10,10);
                    break;
            }
        }
    }
    static int speedThreshhold=100;
    public class AL extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            player.handleKeyPressed(e);
        }
        public void keyTyped(KeyEvent e){

        }
        public void keyReleased(KeyEvent e){
            player.handleKeyReleased(e);
        }
    }
}
