import java.awt.Graphics;

public abstract class Bala extends Mobil {
	final static int length=16;
	public Bala(int x, int y, int vel) {
		super(x, y, vel, 1);
	}
	
	void moure() {
		y+=vel*moviment;
	}
	
	abstract void impacte(Nau nau);
	
	abstract void pinta(Graphics grafic);
	
}