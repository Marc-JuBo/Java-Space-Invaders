import java.awt.Color;
import java.awt.Graphics;

public class BalaEnemiga extends Bala {
	int impacte=0;
	Color colorBala;
	public BalaEnemiga(int x, int y, int vel, Color color) {
		super(x, y, vel);
		colorBala=color;
		moviment=1;
		
	}
	
	void impacte(Nau nau) {
		if(nau.x < this.x && this.x < nau.x+nau.width-1) {
			if(nau.y < this.y && this.y < nau.y+nau.height-1) {
				this.hp--;
				nau.hp--;
			} else if (nau.y < this.y+length && this.y+length < nau.y+nau.height-1) {
				this.hp--;
				nau.hp--;
			}
		}
	}
	
	void pinta(Graphics grafic) {
		grafic.setColor(colorBala);
		grafic.drawLine(x,y,x,y+length);
		grafic.drawLine(x-1,y+1,x-1,y+length-1);
		grafic.drawLine(x+1,y+1,x+1,y+length-1);
	}
}