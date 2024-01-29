import java.awt.Color;
import java.awt.Graphics;

public class Escut extends Nau {
	int hp0;
	final static int width = (int) (1.4*NauPropia.width), height=8; 
	public Escut(int x, int y, int hp){
		super(x,y,0, hp);
		super.width=width;
		super.height=height;
		this.hp0=hp;
		
	}
	
	void pinta(Graphics grafic) {
		grafic.setColor(new Color(255, 255, 255, (int) 255*hp/hp0));
		grafic.fillRect(x, y, width, height);
		grafic.drawLine(x+1, y-1, x+width-2, y-1);
		grafic.drawLine(x+1, y+height, x+width-2, y+height);
	}
	
	void shoot(Joc joc) {} //No vull que els escuts puguin disparar, obviament.
}