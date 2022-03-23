package attacks;

import java.util.LinkedList;

import level.Level;
import tools.Collision;

public class Attacks {
	
	LinkedList<AttackInt> attacks;
	LinkedList<Collision> colliders;
	
	private int size;
	
	Level level;
	
	public Attacks(Level level) {
		
		this.level = level;
		attacks = new LinkedList<AttackInt>();
		colliders = new LinkedList<Collision>();
	}
	
	public void addAttack(AttackInt attack) {
		
		attacks.add(attack);
		colliders.add(new Collision(
				attacks.getLast().getX(),
				attacks.getLast().getY(),
				attacks.getLast().getW(),
				attacks.getLast().getH()
				));
		level.addAttack(attack);
		size++;
	}
	
	
	public void update() {
		
		for(int i = 0; i < attacks.size(); i++) {
			attacks.get(i).update();
			colliders.get(i).setX(attacks.get(i).getX());
			colliders.get(i).setY(attacks.get(i).getY());
			
			remove(i);	
		}
		
	}
	
	
	public void remove(int i) {
		
		if(attacks.get(i).delete()) {
			colliders.remove(i);
			level.removeAttack(attacks.remove(i));
		}
		
	}
	
	public int getSize() {return size;}

	public void Colliding(Collision coll) {
		

		for(int j = 0; j < attacks.size(); j++) {
			if(!attacks.get(j).getHit()) {
				if(coll.colliding(colliders.get(j)))
				{
					if(attacks.get(j).getDirection()>0)
						attacks.get(j).setPos(coll.getX() - attacks.get(j).getW(), attacks.get(j).getY());
					else
						attacks.get(j).setPos(coll.getX() + coll.getW() , attacks.get(j).getY());
					
					attacks.get(j).Hit(); 
					
				}
			}
		}	
	}
	
	
	
	public AttackInt get(int i) {return attacks.get(i);}
	public Collision getCollider(int i) {return colliders.get(i);}
	
	public LinkedList<AttackInt> getList(){return attacks;}
	
}
