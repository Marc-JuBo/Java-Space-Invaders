import java.awt.*;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NauPropia extends Nau {
	final static int width=50, height=20;
	int cooldown=0, respawn=100;
	public NauPropia(int x, int y, int vel, int hp){
		super(x, y, vel, hp);
		if (hp>10) hp=10;
		super.hp=hp;
		super.width=width;
		super.height=height;
		moviment=0;
	}
	
	
	void pinta(Graphics grafic) {
		grafic.setColor(Color.GREEN);
		if(respawn>0) grafic.setColor(Color.YELLOW);
		if(hp<1) grafic.setColor(Color.DARK_GRAY);
		if(hp<0) grafic.setColor(new Color(0,255,0,128));
		grafic.fillRect(x, y, width, height);
		grafic.drawLine(x+1, y-1, x+width-2, y-1);
		grafic.drawLine(x+1, y+height, x+width-2, y+height);
		grafic.drawLine(x+(int)(width*0.5)-1,y-2,x+(int)(width*0.5)-1, y-(int)(height*0.6)+2);
		grafic.drawLine(x+(int)(width*0.5),y-2,x+(int)(width*0.5), y-(int)(height*0.6));
		grafic.drawLine(x+(int)(width*0.5)+1,y-2,x+(int)(width*0.5)+1, y-(int)(height*0.6)+2);
		if(cooldown>0) cooldown--;
		if(respawn>0) respawn--;
	}


	public void shoot(Joc joc) {
		
		if(cooldown==0 && respawn==0) {
			try {
				Clip soBala = AudioSystem.getClip();
		        soBala.open(AudioSystem.getAudioInputStream(new 
		        		File("res/aud/soBalaNew2.wav")));
		        soBala.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			joc.balaPropia.add(new BalaPropia(x+(int)width/2, y-(int)(height*0.6), joc.velocitatBalaPropia));
			cooldown=20;
		}
	}
}