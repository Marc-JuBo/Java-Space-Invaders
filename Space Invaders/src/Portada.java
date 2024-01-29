import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Portada {
	
	Finestra finestra;
	boolean isOpen=true, imgIsFound;
	BufferedImage imgPortada;
	
	Portada(Finestra finestra){
		this.finestra=finestra;
	}
	
	void inicialitzar() {
		try {
			imgPortada = ImageIO.read(new File("res/img/Portada.jpg"));
			imgIsFound=true;
		} catch (Exception e) {
			imgIsFound=false;
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	void run() {
		inicialitzar();
		while(this.isOpen) {
			pintarPantalla();
			Joc.sleepmsec(50);
		}
		
	}
	
	
	
	
	
	
	void pintarPantalla() {
		// Primer accedim a la imatge de la portada.
		if(imgIsFound) {
			finestra.grafic.drawImage(imgPortada, 0, 0, null);
			finestra.grafic.setColor(Color.WHITE);
			finestra.grafic.fillRect(0, 0,Joc.MARGE_BLANC, Finestra.ALT);
			finestra.grafic.fillRect(0, 0, Finestra.AMPLE, 2*Joc.MARGE_BLANC);
			finestra.grafic.fillRect(Finestra.AMPLE-Joc.MARGE_BLANC, 0, Finestra.AMPLE, Finestra.ALT);
			finestra.grafic.fillRect(0, Finestra.ALT-Joc.MARGE_BLANC, Finestra.AMPLE, Finestra.ALT);
		} else {
			finestra.grafic.setColor(Color.WHITE);
			finestra.grafic.fillRect(0, 0, Finestra.AMPLE, Finestra.ALT);
			finestra.grafic.setColor(Color.BLACK);
			finestra.grafic.fillRect(Joc.MARGE_BLANC, 2*Joc.MARGE_BLANC, Finestra.AMPLE-2*Joc.MARGE_BLANC, Finestra.ALT-3*Joc.MARGE_BLANC);
			finestra.grafic.setColor(Color.RED);
			finestra.grafic.drawLine(Joc.MARGE_BLANC+Joc.MARGE_JOC, Finestra.ALT-Joc.MARGE_BLANC-Joc.deadLine, 
					Finestra.AMPLE-Joc.MARGE_BLANC-Joc.MARGE_JOC, Finestra.ALT-Joc.MARGE_BLANC-Joc.deadLine);
		}
		finestra.repaint();
	}
}