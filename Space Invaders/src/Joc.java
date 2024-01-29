import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

//import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



public class Joc {
	Color shyGreen = new Color(0,255,0,128);
	Color shyRed = new Color(255,0,0,128);
	Font font;
	Font fontPausa, fontScore, fontFinal;
	int xTemp, yTemp, puntTemp, hpTemp, i, j;
	
	Finestra finestra;
					boolean isOpen=false, isPaused=false, isFinished=false, isReseting=false, seguirObert=true, justStarted=true;
					int dificultat;
					int countdown=4;
	final 	static 	int MARGE_BLANC=35, MARGE_JOC=(int) (Finestra.AMPLE/20);
	final	static 	int horitzontalJoc=Finestra.AMPLE-2*MARGE_BLANC-2*MARGE_JOC; 
	final 	static 	int verticalJoc=Finestra.ALT-3*MARGE_BLANC-2*MARGE_JOC; 
	final 	static 	int deadLine=150;
				 	int numFiles, numNausXFila=20, numNaus, numNausVives;
	final 	static 	int ESPAI_HOR_ENTRE_NAUS=30, ESPAI_VERT_ENTRE_NAUS=35;
	final 	static 	int numEscuts=(int) ((100+horitzontalJoc)/180);
				 	int freqTirEnemic, cooldownTirEnemic;
				 	int velocitatPropia=(int) (horitzontalJoc/220), velocitatEnemiga=1; // velEnemiga és 1/3 del valor
				 	int velocitatBalaPropia, velocitatBalaEnemiga;
	final 	static 	int hpEscuts=5;
    	  			int hpNauPropia; // Se n'afegeixen pel nombre de numFiles real, i de numNausVives;
    
	int score, highScore=0, allHighScore=0;
	File highScoreFile, allHighScoreFile;
	boolean highScoreIsFound=false, allHighScoreIsFound=false;
	
	BufferedImage imgPause;
	BufferedImage imgPlay;
	BufferedImage imgStop;
	boolean imgIsFound=false;
	
	Random rand=new Random();
	
	NauEnemiga nauEnemiga[];
	NauPropia nauPropia;
	NauPropia vides[];
	ArrayList<BalaEnemiga> balaEnemiga=new ArrayList<BalaEnemiga>();
	ArrayList<BalaPropia> balaPropia=new ArrayList<BalaPropia>();
	Escut escutExt[] = new Escut[numEscuts];
	Escut escutInt[] = new Escut[numEscuts];
	
	int temporitzador=0;
	
	Joc(Finestra finestra, int dificultat){
		this.finestra=finestra;
		this.dificultat=dificultat;
		this.finestra.jocActiu=true;
		this.isOpen=true;
	}
	
	void inicialitzar() {
		// Resetejem els valors, per si es jugués més d'una partida
		if(this.justStarted) score=0;
		balaEnemiga.clear();
		balaPropia.clear();
		
		// Definim el tipus de lletra a ARCADE CLASSIC
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("ARCADECLASSIC.ttf"));
			font = font.deriveFont(12f);
		} catch (Exception e) {
			font = new Font("SERIF", Font.BOLD, 12);
		}
		fontPausa = font.deriveFont(Font.PLAIN, 16);
		fontScore = font.deriveFont(Font.PLAIN, 24);
		fontFinal = font.deriveFont(Font.PLAIN, 48);
		finestra.grafic.setFont(fontScore);
		
		// Definim tots els valors que venen donats per la dificultat:
		try {
			FileInputStream fis = new FileInputStream("res/doc/dificultat"+dificultat+".properties");
			Properties prop = new Properties();
			prop.load(fis);
			numFiles = Integer.parseInt(prop.getProperty("numFiles"));
			freqTirEnemic = Integer.parseInt(prop.getProperty("freqTirEnemic"));
			cooldownTirEnemic= Integer.parseInt(prop.getProperty("cooldownTirEnemic"));
			velocitatBalaEnemiga = Integer.parseInt(prop.getProperty("velocitatBalaEnemiga"));
			velocitatBalaPropia = Integer.parseInt(prop.getProperty("velocitatBalaPropia"));
		} catch (Exception e) {
			e.printStackTrace();
			numFiles = 5;
			freqTirEnemic = 15;
			cooldownTirEnemic = 25;
			velocitatBalaEnemiga = 5;
			velocitatBalaPropia = 4;
		}
		
		// Creem l'accés al highscore permanent de la dificultat.
		highScoreFile= new File("res/doc/highscore"+dificultat+".dat");
		try {
			FileReader readFile = new FileReader(highScoreFile);
			BufferedReader reader = new BufferedReader(readFile);
			highScore=Integer.parseInt(reader.readLine());
			reader.close();
			highScoreIsFound = true;
		} catch (Exception e) {
			highScoreIsFound = false;
			e.printStackTrace();
		}
		//Creem l'accés la highscore permanent general.
		allHighScoreFile=new File("res/doc/highscore0.dat");
		try {
			FileReader readFile2 = new FileReader(allHighScoreFile);
			BufferedReader reader2 = new BufferedReader(readFile2);
			allHighScore=Integer.parseInt(reader2.readLine());
			reader2.close();
			allHighScoreIsFound = true;
		} catch (Exception e) {
			allHighScoreIsFound = false;
			e.printStackTrace();
		}
		
		// Mirem si totes les naus demanades es poden posar dins el tauler.
		yTemp=2*MARGE_BLANC+50;
		for  (i = 0; i < numFiles; i++) {
			yTemp+=ESPAI_VERT_ENTRE_NAUS+NauEnemiga.height;
			if(yTemp>Finestra.ALT-Joc.MARGE_BLANC-Joc.deadLine-(NauEnemiga.height*1.6)-30) break;	
		}
		numFiles=i;
		xTemp=MARGE_BLANC+MARGE_JOC+20;
		for (i = 0; i < numNausXFila; i++) {
			xTemp+=ESPAI_HOR_ENTRE_NAUS+NauEnemiga.width;
			if(xTemp>Finestra.AMPLE-Joc.MARGE_BLANC-Joc.MARGE_JOC-NauEnemiga.width-40) break;
		}
		numNausXFila=i;
		numNaus=numFiles*numNausXFila;
		numNausVives=numNaus;
		
		// Inicialitzem totes les naus enemigues (el constructor ja les torna a matar si estan fora del terreny de joc).
		nauEnemiga= new NauEnemiga[numNaus];
		yTemp=2*MARGE_BLANC+50;
		for (int i = 0; i < numFiles; i++) {
			xTemp=MARGE_BLANC+MARGE_JOC+20;
			for(int j = 0; j < numNausXFila; j++) {
				puntTemp=20;
				if(i<(int)(numFiles+1)/2) puntTemp+=10;
		        if(numFiles >= 3 && i==0) puntTemp+=20; 
				nauEnemiga[numNaus-1] = new NauEnemiga(xTemp, yTemp, velocitatEnemiga, 1, puntTemp, this);
		        nauEnemiga[numNausXFila*i+j] = nauEnemiga[numNaus-1];
				xTemp+=ESPAI_HOR_ENTRE_NAUS+NauEnemiga.width;
			}
			yTemp+=ESPAI_VERT_ENTRE_NAUS+NauEnemiga.height;
	    }
		
		// Inicialitzem la nau Pròpia, i les entitats que formen part de l'entorn grafic, que compleixen de vides.
		if(this.justStarted) {
			hpNauPropia=(int) (1+(int) ((numFiles)/2.51));
			if(hpNauPropia+(numNaus-25)/15>0) hpNauPropia+=(numNaus-25)/15;
		} else hpNauPropia=1;
		
		nauPropia=new NauPropia(Finestra.AMPLE/2, Finestra.ALT-MARGE_BLANC-80, velocitatPropia, hpNauPropia);
		vides=new NauPropia[nauPropia.hp];
		for(int i=1; i<=nauPropia.hp; i++) {
			vides[i-1]= new NauPropia(Finestra.AMPLE-MARGE_BLANC-(NauPropia.width+15)*i, 
					Finestra.ALT-MARGE_BLANC-NauPropia.height-15, 0, -1);
		}
		
		// Inicialitzem els escuts
		if(this.justStarted) {
			for (int i = 0; i < numEscuts; i++) {
				escutExt[i] = new Escut((int) (MARGE_BLANC+MARGE_JOC+(1+2*i)*horitzontalJoc/(numEscuts*2)-Escut.width/2), 
						Finestra.ALT-MARGE_BLANC-deadLine+10, hpEscuts);
				escutInt[i] = new Escut((int) (MARGE_BLANC+MARGE_JOC+(1+2*i)*horitzontalJoc/(numEscuts*2)-Escut.width/2), 
						Finestra.ALT-MARGE_BLANC-deadLine+30, hpEscuts);
			}
		}
	}
	
	
	
	
	
	
	void run() {
		inicialitzar();
		while(this.isOpen) {
			while(!this.isFinished) {
				if(!this.isPaused) {
					if(countdown>0) {
						countdown();
					}
					
					ferMoviments();
					detectarXocs();
					pintarPantalla();
					if(nauPropia.hp==0 || numNausVives==0) {
						finalPartida();
					}
					temporitzador++;
					
					sleepmsec(10);
				}
				else {
					pintarPantalla();
					sleepmsec(50);
				}
			}

			sleepmsec(20);
		}
		
	}
	
	
	
	
	
	
	public static void sleepmsec(int i) {
		try {
		Thread.sleep(i);
		} catch (InterruptedException e) {}
	}
	
	
	
	
	
	
	private void ferMoviments() {
		
		// Naus enemigues es mouen.
		if(temporitzador%3==0) {
			for(int i=0; i<numNaus; i++) {
				if(nauEnemiga[i].alive) {
					xTemp=nauEnemiga[i].x+nauEnemiga[i].vel*nauEnemiga[i].moviment;
					if(xTemp<MARGE_BLANC+MARGE_JOC || xTemp>Finestra.AMPLE-MARGE_BLANC-MARGE_JOC-NauEnemiga.width) {
						for(int j=0; j<numNaus; j++) {
							nauEnemiga[j].moviment*=-1; 
							nauEnemiga[j].y+=18;
						}
						break;
					}
					if(nauEnemiga[i].y+(int)(NauEnemiga.height*1.6)>Finestra.ALT-MARGE_BLANC-deadLine)
						this.isFinished=true;
				}
			}
			for(int i=0; i<numNaus; i++) {
				nauEnemiga[i].checkDeath(this);
				nauEnemiga[i].moure();
			}
		}
		
		
		// Nau Propia es mou.
		xTemp=nauPropia.x+nauPropia.vel*nauPropia.moviment;
		if(xTemp>MARGE_BLANC+MARGE_JOC && xTemp<Finestra.AMPLE-MARGE_BLANC-MARGE_JOC-NauPropia.width) nauPropia.moure();
		
		
		// Es decideix si les naus enemigues disparen, i quina ho fa.
		if(rand.nextInt(freqTirEnemic)==0) {
			int r=rand.nextInt(numNausVives), cont=0, i;
			for(i=0; i<numNaus; i++) {
				if(nauEnemiga[i].alive) {
					if (cont==r) break;
					cont++;
				}
			}
			nauEnemiga[i].shoot(this);
		}
		
		
		// Es mouen les bales enemigues
		for(int i=0; i<balaEnemiga.size(); i++) {
			balaEnemiga.get(i).moure();
		}

		
		// Es mouen les bales propies
		for(int i=0; i<balaPropia.size(); i++) {
			balaPropia.get(i).moure();
		}
		
	}
	
	
	
	
	
	
	private void detectarXocs() {
		hpTemp=nauPropia.hp;
		for(int i=0; i<balaEnemiga.size(); i++){
			if(nauPropia.respawn==0) balaEnemiga.get(i).impacte(nauPropia);
			if(hpTemp!=nauPropia.hp) nauPropia.respawn=200;
			for(int j=0; j<numEscuts; j++) {
				if(escutExt[j].alive) balaEnemiga.get(i).impacte(escutExt[j]);
				if(escutInt[j].alive) balaEnemiga.get(i).impacte(escutInt[j]);
			}
			if(balaEnemiga.get(i).y > Finestra.ALT-MARGE_BLANC-Bala.length) balaEnemiga.get(i).alive=false;
			if(balaEnemiga.get(i).hp < 1) balaEnemiga.get(i).alive=false;
			if (!balaEnemiga.get(i).alive)
				balaEnemiga.remove(balaEnemiga.get(i));
			
		}
		
		for(int i=0; i<balaPropia.size(); i++){
			for(int j=0; j<numEscuts; j++) {
				if(escutExt[j].alive) balaPropia.get(i).impacte(escutExt[j]);
				if(escutInt[j].alive) balaPropia.get(i).impacte(escutInt[j]);
			}
			for(int j=0; j<numNaus; j++) {
				if(nauEnemiga[j].alive) {
					balaPropia.get(i).impacte(nauEnemiga[j]);
					if(nauEnemiga[j].hp<=0) {
						nauEnemiga[j].alive=false;
						this.score+=nauEnemiga[j].puntuacio;
						numNausVives--;
					}
				}
			}
			if(balaPropia.get(i).y < 2*MARGE_BLANC+Bala.length) {
				balaPropia.get(i).alive=false;
				score-=10;
			}
			if(balaPropia.get(i).hp < 1) balaPropia.get(i).alive=false;
			if (!balaPropia.get(i).alive) 
				balaPropia.remove(balaPropia.get(i));
		}
		
		for(int i=0; i<numEscuts; i++){
			if(escutExt[i].hp<1 && escutExt[i].alive) {
				escutExt[i].alive = false; 
				score-=15;
				try {
					Clip soEscut = AudioSystem.getClip();
					soEscut.open(AudioSystem.getAudioInputStream(new File("res/aud/soEscutNew2.wav")));
					soEscut.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(escutInt[i].hp<1 && escutInt[i].alive) {
				escutInt[i].alive = false;
				score-=25;
				try {
					Clip soEscut = AudioSystem.getClip();
					soEscut.open(AudioSystem.getAudioInputStream(new File("res/aud/soEscutNew2.wav")));
					soEscut.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
	
	
	void pintarPantalla() {
		// Dibuixem la finestra.
		finestra.grafic.setColor(Color.WHITE);
		finestra.grafic.fillRect(0, 0, Finestra.AMPLE, Finestra.ALT);
		finestra.grafic.setColor(Color.BLACK);
		if(countdown>0 || this.isFinished) finestra.grafic.setColor(Color.DARK_GRAY); 
		if(this.isFinished) finestra.grafic.setColor(Color.DARK_GRAY);
		finestra.grafic.fillRect(MARGE_BLANC, 2*MARGE_BLANC, Finestra.AMPLE-2*MARGE_BLANC, Finestra.ALT-3*MARGE_BLANC);
		
		// Dibuixem la linia horitzontal vermella que delimita el límit de les naus enemigues
		finestra.grafic.setColor(Color.RED);
		finestra.grafic.drawLine(MARGE_BLANC+MARGE_JOC, Finestra.ALT-MARGE_BLANC-deadLine, 
				Finestra.AMPLE-MARGE_BLANC-MARGE_JOC, Finestra.ALT-MARGE_BLANC-deadLine);
		
		// Pintem la nau pròpia, les naus enemigues, i els escuts
		nauPropia.pinta(finestra.grafic);
		for(int i=0; i<numNaus; i++) {
			if(nauEnemiga[i].alive) nauEnemiga[i].pinta(finestra.grafic);
		}
		for(int i=0; i<numEscuts; i++) {
			if(escutExt[i].alive) escutExt[i].pinta(finestra.grafic);
			if(escutInt[i].alive) escutInt[i].pinta(finestra.grafic);
		}
		
		// Pintem les bales
		for(int i=0; i<balaEnemiga.size(); i++) 
			balaEnemiga.get(i).pinta(finestra.grafic);
		for(int i=0; i<balaPropia.size(); i++) 
			balaPropia.get(i).pinta(finestra.grafic);
		
		// Dibuixem el que calgui si la partida està pausada.
		if(countdown>0) {
			if (!this.isPaused) {
				drawcountdown(countdown);
			}
			else {
				finestra.grafic.setColor(Color.RED);
				finestra.grafic.setFont(fontPausa);
				finestra.grafic.drawString("Apreta   P   per   continuar   jugant", 
						Finestra.AMPLE-MARGE_BLANC-260, 2*MARGE_BLANC+15);
			}
		}
		
		// Dibuixem el contador de punts i el highscore
		if (score<0) score=0;
		finestra.grafic.setFont(fontScore);
		finestra.grafic.setColor(Color.RED);
		finestra.grafic.drawString("SCORE  " + score , 
				MARGE_BLANC+50, 2*MARGE_BLANC+25);
		finestra.grafic.drawString("HIGHSCORE  " + highScore , 
				MARGE_BLANC+horitzontalJoc/2-15, 2*MARGE_BLANC+25);
		
		// Dibuixem els iconos de les vides disponibles
		for(int i=0; i<nauPropia.hp-1; i++) {
			vides[i].pinta(finestra.grafic);
		}
				
		if(this.isFinished && this.isOpen) {
			finestra.grafic.setFont(fontFinal);
			if(numNausVives==0) {
				finestra.grafic.setColor(Color.BLACK);
				finestra.grafic.fillRect(Finestra.AMPLE/2-300, 258, 600, 60);
				finestra.grafic.setColor(Color.WHITE);
				finestra.grafic.fillRect(Finestra.AMPLE/2-298, 260, 596, 56);
				finestra.grafic.setColor(Color.BLUE);
				finestra.grafic.drawString("Enhorabona   has   guanyat!", Finestra.AMPLE/2-290, 300);
				
				finestra.grafic.setColor(Color.BLACK);
				finestra.grafic.fillRect(Finestra.AMPLE/2-150, 358, 300, 60);
				finestra.grafic.setColor(Color.WHITE);
				finestra.grafic.fillRect(Finestra.AMPLE/2-148, 360, 296, 56);
				finestra.grafic.setColor(Color.BLUE); 
				finestra.grafic.drawString("Bonus   Game!", Finestra.AMPLE/2-140, 400);
				justStarted=false;
				this.isFinished=false;
				this.isReseting=true;
				countdown=4;
			} else {
				finestra.grafic.setColor(Color.BLACK);
				finestra.grafic.fillRect(Finestra.AMPLE/2-130, 258, 260, 60);
				finestra.grafic.setColor(Color.WHITE);
				finestra.grafic.fillRect(Finestra.AMPLE/2-128, 260, 256, 56);
				finestra.grafic.setColor(Color.BLUE);
				finestra.grafic.drawString("GAME   OVER", Finestra.AMPLE/2-115, 300);
				justStarted=true;
			}
			finestra.grafic.setColor(Color.RED);
			finestra.grafic.setFont(fontPausa);
			if(!this.isReseting || this.justStarted) {
				finestra.grafic.drawString("Apreta   R   per   tornar   a   jugar", 
						Finestra.AMPLE-MARGE_BLANC-240, 2*MARGE_BLANC+15);
				finestra.grafic.drawString("Apreta   B   per   tornar   al   menu", 
						Finestra.AMPLE-MARGE_BLANC-240, 2*MARGE_BLANC+30);
			}
			
		}
		finestra.repaint();
		if(countdown>0 && !this.isPaused) countdown--;
	}
	
	
	
	
	
	
	void countdown() {
		if(this.isReseting && !this.justStarted && this.isOpen) sleepmsec(1000);
		if(this.isOpen) pintarPantalla();
		if(this.isOpen) sleepmsec(1000);
		if(this.isOpen) pintarPantalla();
		if(this.isOpen) sleepmsec(1000);
		if(this.isOpen) pintarPantalla();
		if(this.isOpen) sleepmsec(1000);
		if(this.isOpen) pintarPantalla();
		if(this.isOpen) sleepmsec(1000);
		if (this.isReseting) {
			inicialitzar();
			this.isReseting=false;
		}
	}
	
	
	
	
	
	
	private void drawcountdown(int cont) {
		finestra.grafic.setColor(Color.BLACK);
		finestra.grafic.fillOval((int)1*Finestra.AMPLE/4-40,(int)Finestra.ALT/2, 81, 81);
		finestra.grafic.fillOval((int)2*Finestra.AMPLE/4-40,(int)Finestra.ALT/2, 81, 81);
		finestra.grafic.fillOval((int)3*Finestra.AMPLE/4-40,(int)Finestra.ALT/2, 81, 81);
		finestra.grafic.setColor(shyGreen);
		if(cont<2) finestra.grafic.setColor(Color.GREEN);
		finestra.grafic.fillOval((int)3*Finestra.AMPLE/4+3-40,(int)Finestra.ALT/2+3, 75, 75);
		finestra.grafic.setColor(shyRed);
		if(cont<3) finestra.grafic.setColor(Color.RED);
		finestra.grafic.fillOval((int)2*Finestra.AMPLE/4+3-40,(int)Finestra.ALT/2+3, 75, 75);
		if(cont<4) finestra.grafic.setColor(Color.RED);
		finestra.grafic.fillOval((int)1*Finestra.AMPLE/4+3-40,(int)Finestra.ALT/2+3, 75, 75);
	}
	
	
	
	
	
	
	private void finalPartida() {
		
		if(numNausVives==0) passarVidesAPunts();
		this.isFinished=true;
		pintarPantalla();
		if(highScoreIsFound && highScore<score) setHighScore();
		if(allHighScoreIsFound && allHighScore<score) setAllHighScore();
	}
	
	
	
	
	
	
	private void passarVidesAPunts() {
		sleepmsec(1000);
		
		while(1<nauPropia.hp) {
			if(nauPropia.hp==2) 
				score+=100*dificultat;
			else score+=50*dificultat;
			nauPropia.hp--;
			pintarPantalla();
			sleepmsec(500);
		}
		score+=1;
		for(int i=dificultat; i>0; i--)
			score+=i*100;
	}

	
	
	
	
	
	private void setHighScore() {
		highScore=score;
		try {
			FileWriter writeFile = new FileWriter(highScoreFile);
			BufferedWriter writer = new BufferedWriter(writeFile);
			writer.write(this.highScore+"");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	private void setAllHighScore() {
		allHighScore=score;
		try {
			FileWriter writeFile = new FileWriter(allHighScoreFile);
			BufferedWriter writer = new BufferedWriter(writeFile);
			writer.write(this.allHighScore+"");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}