import java.awt.*;

public abstract class Mobil {
	int x, y, vel, hp, moviment;
	boolean alive=true;
	public Mobil(int x, int y, int vel, int hp){
		this.x=x;
		this.y=y;
		this.vel=vel;
		this.hp=hp;
	}
	
	abstract void moure();
	
	abstract void pinta(Graphics grafic);
}