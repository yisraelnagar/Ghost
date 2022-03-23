package enemies;

import java.awt.Graphics2D;
import java.util.LinkedList;

import attacks.AttackInt;
import level.Level;
import tools.Collision;

public class Enemies {

	LinkedList<EnemyInt> sprite;
	LinkedList<EnemyRigidBody> Body;
	LinkedList<Collision> colliders;
	
	Level level;
	
	public Enemies(Level level) {
		this.level = level;
		Body = new LinkedList<EnemyRigidBody>();
		sprite = new LinkedList<EnemyInt>();
		colliders = new LinkedList<Collision>();
	}
	
	public Enemies(Enemies enem, Level level){
		this.level = level;
		this.Body = enem.Body;
		sprite = new LinkedList<EnemyInt>();
		colliders = new LinkedList<Collision>();
		for(int i = 0; i < Body.size(); i++) {
			sprite.add(Body.get(i).getEnemyInt());
			colliders.add(Body.get(i).getBodyCollider());
			level.addEnemyCollider(Body.get(i).getBodyCollider());
		}

	}
	
	public void addEnemy(EnemyRigidBody body) {
		
		this.Body.add(body);
		sprite.add(body.getEnemyInt());
		sprite.getLast().setBody(Body.getLast());
	}
	
	public void init(){
		
		for(int i = 0; i < Body.size(); i++)
		Body.get(i).init();
		
	}
	public void update(){

		for(int i = 0; i < Body.size(); i++) {
//		if(Body.get(i).onScreen()) {
			Body.get(i).tileCollisionCheck(this.level.getTiles());
			Body.get(i).setAttacks(this.level.getPlayerAttacks());
			sprite.get(i).update();
			level.setEnemyCollider(i, Body.get(i).getBodyCollider());
			removeEnemy(i);
		}
	}
	
	public void draw(Graphics2D g){
		
		for(int i = 0; i < Body.size(); i++)
		if(Body.get(i).onScreen())
		sprite.get(i).draw(g);	
	}
	
	public int getHealth(int i){return Body.get(i).getHealth();}
	public boolean isDead(int i) {return sprite.get(i).isDead();}
	
	public Collision getCollider(int i){return Body.get(i).getBodyCollider();}
	
	public LinkedList<Collision> getColliders(){return colliders;}
	
	public EnemyInt getSprite(int i){return sprite.get(i);}
	public EnemyRigidBody getBody(int i){return Body.get(i);}
	
	public int size() {return Body.size();}
	
	public void setWorldX(LinkedList<Collision> tiles) {
		for(int i = 0; i < Body.size(); i++) {
			Body.get(i).tileCollisionCheck(tiles);
			Body.get(i).checkBounds();		
		}
	}
	
	public void setWorldY(LinkedList<Collision> tiles) {
		for(int i = 0; i < Body.size(); i++) {
			Body.get(i).tileCollisionCheck(tiles);
			Body.get(i).checkBounds();		
		}
	}
	
	public void removeEnemy(int i) {
		
		if(sprite.get(i).isDead()) {
			Body.remove(i);
			colliders.remove(i);
			sprite.remove(i);
			level.removeEnemy(i);
		}
		
	}
	
	
	
	
}
