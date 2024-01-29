import java.awt.*;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class NauEnemiga extends Nau {
	final static int width=40, height=20;
	static int cooldown=0;
	int puntuacio;
	Color colorNau;
	public NauEnemiga(int x, int y, int vel, int hp, int puntuacio, Joc joc){
		super(x, y, vel, hp);
		super.width=width;
		super.height=height;
		this.puntuacio=puntuacio;
		if(puntuacio==20) colorNau = Color.CYAN;
		if(puntuacio==30) colorNau = Color.ORANGE;
		if(puntuacio==50) colorNau = Color.RED;
		moviment=1;
		
		if(x>Finestra.AMPLE-Joc.MARGE_BLANC-Joc.MARGE_JOC-NauEnemiga.width-40) {
			alive=false;
			joc.numNausVives--;
			puntuacio=0;
		}
		// if(nauEnemiga[j].y+(int)(NauEnemiga.height*1.6)>Finestra.ALT-MARGE_BLANC-deadLine)
		if(y>Finestra.ALT-Joc.MARGE_BLANC-Joc.deadLine-(NauEnemiga.height*1.6)-30) {
			alive=false;
			joc.numNausVives--;
			puntuacio=0;
		}
	}
	
	void shoot(Joc joc) {
		if(cooldown==0) {
			try {
				Clip soBala = AudioSystem.getClip();
		        soBala.open(AudioSystem.getAudioInputStream(new 
		        		File("res/aud/soBalaNew2.wav")));
		        soBala.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			cooldown=joc.numNausVives*joc.cooldownTirEnemic;
			joc.balaEnemiga.add(new BalaEnemiga(x+(int)width/2, y+(int)(height*1.6), joc.velocitatBalaEnemiga, colorNau));
		}
	}
	
	void pinta(Graphics grafic) {
		grafic.setColor(colorNau);
		grafic.fillRect(x, y, width, height);
		grafic.drawLine(x+1, y-1, x+width-2, y-1);
		grafic.drawLine(x+1, y+height, x+width-2, y+height);
		grafic.drawLine(x+(int)(width*0.5),y+height,x+(int)(width*0.5), y+(int)(height*1.6));
		grafic.drawLine(x+(int)(width*0.5)-1,y+height,x+(int)(width*0.5)-1, y+(int)(height*1.6)-2);
		grafic.drawLine(x+(int)(width*0.5)+1,y+height,x+(int)(width*0.5)+1, y+(int)(height*1.6)-2);
		if(cooldown > 0) cooldown--;
	}
}