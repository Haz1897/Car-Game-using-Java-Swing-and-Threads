import java.awt.*;

public class StreetLight {
    int poleX,poleY,
            width=200,height=20,
            innerLightX,innerLightY,
            outerLightX,outerLightY;
    Color lampColor,lightColor,outerLightColor;
    StreetLight(int poleX,int poleY){
        lampColor = new Color(0,0,0, 255);
        lightColor = new Color(255, 245, 208, 81);
        outerLightColor = new Color(255, 251, 239, 34);
        this.poleX=poleX;
        this.poleY=poleY;
        innerLightX=poleX+width-50;
        innerLightY=poleY-25;
        outerLightX=poleX+width-100;
        outerLightY=poleY-75;
        setupLamp();
    }
    public void setupLamp(){
    }
    public  void duplicateLight(StreetLight str){
        poleX = Road.width+str.poleX;
        innerLightX=Road.width+str.innerLightX-width;
        outerLightX=Road.width+str.outerLightX-width;
        poleY = str.poleY+50;
        innerLightY=str.innerLightY+50;
        outerLightY =str.outerLightY+50;
    }
}
