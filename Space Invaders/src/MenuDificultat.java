import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MenuDificultat {
	
	Finestra finestra;
	boolean isOpen=true, imgIsFound, readyToPlay=false;
	BufferedImage imgFons;
	Font font, fontTitol, fontPlay, fontBotons;
	int dificultat=0;
	final static int boto_AMPLE=150, boto_ALT=80;
	
	MenuDificultat(Finestra finestra){
		this.finestra=finestra;
	}
	
	void inicialitzar() {		
		// Incicialitzem el que serà la imatge de fons.
		try {
			imgFons = ImageIO.read(new File("res/img/MenuDificultat.png"));
			imgIsFound=true;
		} catch (IOException e) {
			imgIsFound=false;
			e.printStackTrace();
		}
		
		// Inicialitzem la que serà la lletra de les dificultats
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("ARCADECLASSIC.ttf"));
			font = font.deriveFont(12f);
		} catch (Exception e) {
			font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		}
		fontTitol = font.deriveFont(Font.BOLD, 64);
		fontPlay = font.deriveFont(Font.BOLD, 40);
		fontBotons = font.deriveFont(Font.PLAIN, 32);
	}
	
	
	
	
	
	
	void run() {
		inicialitzar();
		while(this.isOpen) {
			pintarPantalla();
			Joc.sleepmsec(50);
		}
		
	}
	
	
	
	
	
	
	void pintarPantalla() {
		// Primer dibuixem la imatge de la portada, o el quadrat de fons.
		if(imgIsFound) {
			finestra.grafic.drawImage(imgFons, 0, 0, null);
			finestra.grafic.setColor(Color.WHITE);
			finestra.grafic.fillRect(0, 0,Joc.MARGE_BLANC, Finestra.ALT);
			finestra.grafic.fillRect(0, 0, Finestra.AMPLE, 2*Joc.MARGE_BLANC);
			finestra.grafic.fillRect(Finestra.AMPLE-Joc.MARGE_BLANC, 0, Finestra.AMPLE, Finestra.ALT);
			finestra.grafic.fillRect(0, Finestra.ALT-Joc.MARGE_BLANC, Finestra.AMPLE, Finestra.ALT);
		} else {
			finestra.grafic.setColor(Color.WHITE);
			finestra.grafic.fillRect(0, 0, Finestra.AMPLE, Finestra.ALT);
			finestra.grafic.setColor(Color.DARK_GRAY);
			finestra.grafic.fillRect(Joc.MARGE_BLANC, 2*Joc.MARGE_BLANC, Finestra.AMPLE-2*Joc.MARGE_BLANC, Finestra.ALT-3*Joc.MARGE_BLANC);
			finestra.grafic.setColor(Color.RED);
			finestra.grafic.drawLine(Joc.MARGE_BLANC+Joc.MARGE_JOC, Finestra.ALT-Joc.MARGE_BLANC-Joc.deadLine, 
					Finestra.AMPLE-Joc.MARGE_BLANC-Joc.MARGE_JOC, Finestra.ALT-Joc.MARGE_BLANC-Joc.deadLine);
			finestra.grafic.setColor(Color.BLACK);
			finestra.grafic.setFont(fontTitol);
			finestra.grafic.drawString("S E LE C C I O N A", (int) (Finestra.AMPLE/5), 
					(int)Finestra.ALT/3);
			finestra.grafic.drawString("D I F I C U LTA T", (int) (Finestra.AMPLE/5+80), 
					(int)Finestra.ALT/3+50);
		}
		
		// Dibuixem les caixetes de les diferents opcions.
		finestra.grafic.setFont(fontBotons);
		for(int i=0; i<3; i++) {
			finestra.grafic.setColor(Color.BLACK);
			if(dificultat==i+1) finestra.grafic.setColor(Color.BLUE);
			finestra.grafic.fillRoundRect((Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-boto_AMPLE/2, 
					(int)Finestra.ALT*2/3, boto_AMPLE, boto_ALT, 20, 20);
			finestra.grafic.setColor(Color.LIGHT_GRAY);
			finestra.grafic.fillRoundRect((Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-boto_AMPLE/2+4, 
					(int)Finestra.ALT*2/3+4, boto_AMPLE-8, boto_ALT-8, 20, 20);
			
			// Escrivim el text de dins de les caixetes
			finestra.grafic.setColor(Color.BLACK);
			finestra.grafic.drawString(""+(i+1), (Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-6, 
					(int)Finestra.ALT*2/3+30);
			if(i==0) finestra.grafic.drawString("FACIL", (Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-39, 
					(int)Finestra.ALT*2/3+60);
			if(i==1) finestra.grafic.drawString("MITJANA", (Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-60, 
					(int)Finestra.ALT*2/3+60);
			if(i==2) finestra.grafic.drawString("DIFICIL", (Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-60, 
					(int)Finestra.ALT*2/3+60);
		}
		finestra.grafic.setColor(Color.BLACK);
		if(readyToPlay) finestra.grafic.setColor(Color.BLUE);
		finestra.grafic.fillRoundRect((Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC-boto_AMPLE/2, 
				(int)Finestra.ALT*4/5, boto_AMPLE, boto_ALT, 20, 20);
		finestra.grafic.setColor(Color.LIGHT_GRAY);
		finestra.grafic.fillRoundRect((Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC-boto_AMPLE/2+4, 
				(int)Finestra.ALT*4/5+4, boto_AMPLE-8, boto_ALT-8, 20, 20);
		finestra.grafic.setFont(fontPlay);
		finestra.grafic.setColor(Color.BLACK);
		finestra.grafic.drawString("P LA Y", (Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC-50, 
				(int)Finestra.ALT*4/5+55);
		finestra.repaint();
	}
}