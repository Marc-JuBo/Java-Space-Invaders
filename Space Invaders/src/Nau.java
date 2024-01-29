import java.awt.Graphics;

public abstract class Nau extends Mobil {
	int width, height;
	public Nau(int x, int y, int vel, int hp){
		super(x, y, vel, hp);
	}
	
	void defmoviment(int moviment) {
		this.moviment=moviment;
	}
	
	void moure() {
		x+=moviment*vel;
	}
	
	void checkDeath(Joc joc) {
		if (hp<1 && alive) {
			alive=false;
			joc.numNausVives--;
		}
	}
	
	abstract void shoot(Joc joc);
	
	abstract void pinta(Graphics grafic);
}