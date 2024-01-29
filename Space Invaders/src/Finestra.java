import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")

public class Finestra extends Frame implements MouseListener, KeyListener, WindowListener{
	
	Portada portada;
	MenuDificultat menuDificultat;
	Joc joc;
	Graphics grafic;
	Image img;
	public static final int AMPLE=850, ALT=700; //Ample recomanat 850 o 1200
	boolean isOpen=true, jocActiu=false;
	int programStage=1;
	int dificultat=0;
	
	
	public static void main(String[] args){
		new Finestra(); 
	}
	Finestra() {
		this.addWindowListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		setResizable(false);
		setSize(AMPLE, ALT);
		setVisible(true);
		
		img=createImage(AMPLE, ALT);
		grafic=img.getGraphics();
		
		while(this.isOpen) {
			if(programStage==1) {
				portada=new Portada(this);
				portada.run();
			}
			if(programStage==2) {
				menuDificultat=new MenuDificultat(this);
				menuDificultat.run();
			}
			if(programStage==3) {
				joc=new Joc(this, dificultat);
				joc.run();
			}
		}
		setVisible(false);
	}
	public void paint(Graphics grafic) {
		grafic.drawImage(img, 0, 0, null);
	}
	
	public void update(Graphics grafic) {
		paint(grafic);
	}
	
	
	
	
	
	
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {
		joc.isPaused=false;
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		System.exit(0); // TODO
		joc.countdown=4;
		joc.isPaused=true;
	}
	@Override
	public void windowActivated(WindowEvent e) {
		if(jocActiu) joc.isPaused=false;
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		if(programStage==3) {
			joc.countdown=4;
			joc.isPaused=true;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private int leftClicked=0, rightClicked=0, spaceClicked=0, somethingClicked=0;
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if(programStage==3) {
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE || e.getKeyCode()==KeyEvent.VK_B) {
				programStage=1;
				joc.countdown=0;
				joc.isFinished=true;
				joc.isOpen=false;
				somethingClicked=1;
			}
			if(joc.countdown == 0) {
				if(e.getKeyCode()==KeyEvent.VK_LEFT  || e.getKeyCode()==KeyEvent.VK_A) {joc.nauPropia.defmoviment(-1); leftClicked=1;}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {joc.nauPropia.defmoviment(1); rightClicked=1;}
				if(e.getKeyCode()==KeyEvent.VK_SPACE && spaceClicked==0) {joc.nauPropia.shoot(joc); spaceClicked=1;}
				if(e.getKeyCode()==KeyEvent.VK_P) {joc.countdown=4; joc.isPaused=true;}
				if(e.getKeyCode()==KeyEvent.VK_R && joc.isFinished) {
					joc.isFinished=false;
					joc.isReseting=true;
					joc.countdown=4;
				}
			} else if(e.getKeyCode()==KeyEvent.VK_P) joc.isPaused=false;
		}
		
		if(programStage==2 && somethingClicked==0) {
			if(e.getKeyCode()==KeyEvent.VK_B || e.getKeyCode()==KeyEvent.VK_ESCAPE || e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
				programStage=1;
				menuDificultat.isOpen=false;
				somethingClicked=1;
			}
			if(e.getKeyCode()==KeyEvent.VK_1 || e.getKeyCode()==KeyEvent.VK_NUMPAD1 ) menuDificultat.dificultat=1;
			if(e.getKeyCode()==KeyEvent.VK_2 || e.getKeyCode()==KeyEvent.VK_NUMPAD2 ) menuDificultat.dificultat=2;
			if(e.getKeyCode()==KeyEvent.VK_3 || e.getKeyCode()==KeyEvent.VK_NUMPAD3 ) menuDificultat.dificultat=3;
			if((e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) && !menuDificultat.readyToPlay && menuDificultat.dificultat>1) {
				menuDificultat.dificultat--;
				menuDificultat.readyToPlay=false;
			}
			if((e.getKeyCode()==KeyEvent.VK_RIGHT  || e.getKeyCode()==KeyEvent.VK_D)  && !menuDificultat.readyToPlay && menuDificultat.dificultat<3) {
				menuDificultat.dificultat++;
				menuDificultat.readyToPlay=false;
			}
			if((e.getKeyCode()==KeyEvent.VK_DOWN  || e.getKeyCode()==KeyEvent.VK_S)  && menuDificultat.dificultat<4 && 0<menuDificultat.dificultat) 
				menuDificultat.readyToPlay=true;
			if((e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W)  && menuDificultat.readyToPlay) menuDificultat.readyToPlay=false;
			if((e.getKeyCode()==KeyEvent.VK_SPACE  || e.getKeyCode()==KeyEvent.VK_ENTER) && menuDificultat.dificultat>0) {
				if(menuDificultat.readyToPlay) {
					dificultat=menuDificultat.dificultat;
					programStage=3;
					menuDificultat.isOpen=false;
				} else menuDificultat.readyToPlay=true;
			}
		}
		
		if(programStage==1 && somethingClicked==0) {
			somethingClicked=1;
			programStage=2;
			portada.isOpen=false;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		somethingClicked=0;
		if(programStage==3) {
			if(joc.countdown == 0) {
				if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) {
					rightClicked=0;
					if(leftClicked==1) joc.nauPropia.defmoviment(-1);
					else joc.nauPropia.defmoviment(0);
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) {
					leftClicked=0;
					if(rightClicked==1) joc.nauPropia.defmoviment(1);
					else joc.nauPropia.defmoviment(0);
				}
				if(e.getKeyCode()==KeyEvent.VK_SPACE) {
					spaceClicked=0;
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	int mouseX, mouseY, mousePressed=0, mouseLeftPressed=0;
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		mouseX=e.getX(); 
		mouseY=e.getY();
		
		if(programStage==3 && e.getButton()==MouseEvent.BUTTON1 && joc.countdown == 0) {joc.nauPropia.shoot(joc); mouseLeftPressed=1;}
		if(programStage==2 && mousePressed==0) {
			if( (int) Finestra.ALT*2/3+4 < mouseY && mouseY < (int) Finestra.ALT*2/3+MenuDificultat.boto_ALT-4) {
				for(int i=0; i<3; i++) {
					if((Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC-MenuDificultat.boto_AMPLE/2+4 < mouseX &&
							mouseX < (Finestra.AMPLE-2*Joc.MARGE_BLANC)*(1+2*i)/6+Joc.MARGE_BLANC+MenuDificultat.boto_AMPLE/2-4) {
						menuDificultat.dificultat=i+1;
						menuDificultat.readyToPlay=false;
					}
				}
			}
			if(0 < menuDificultat.dificultat && menuDificultat.dificultat< 4 ) {
				if((Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC-MenuDificultat.boto_AMPLE/2+4 < mouseX &&
						mouseX < (Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC+MenuDificultat.boto_AMPLE/2-4) {
					if((int)Finestra.ALT*4/5+4 < mouseY && mouseY < (int)Finestra.ALT*4/5+MenuDificultat.boto_ALT-8) {
						menuDificultat.readyToPlay=true;
					}
				}
			}
		}
		if(programStage==1) {
			programStage=2;
			somethingClicked=1;
			portada.isOpen=false;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		somethingClicked=0;
		if(e.getButton()==MouseEvent.BUTTON1) mouseLeftPressed=0;
		mouseX=e.getX(); 
		mouseY=e.getY();
		if(programStage==2) {
			if(0 < menuDificultat.dificultat && menuDificultat.dificultat< 4 && menuDificultat.readyToPlay) {
				if((Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC-MenuDificultat.boto_AMPLE/2+4 < mouseX &&
						mouseX < (Finestra.AMPLE-2*Joc.MARGE_BLANC)/2+Joc.MARGE_BLANC+MenuDificultat.boto_AMPLE/2-4) {
					if((int)Finestra.ALT*4/5+4 < mouseY && mouseY < (int)Finestra.ALT*4/5+MenuDificultat.boto_ALT-8) {
						dificultat=menuDificultat.dificultat;
						programStage=3;
						menuDificultat.isOpen=false;
					} else menuDificultat.readyToPlay=false;
				} else menuDificultat.readyToPlay=false;
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}