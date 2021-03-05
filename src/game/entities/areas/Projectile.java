package game.entities.areas;

public class Projectile extends TimerDamageZone {
	private float baseposX,baseposY;
	private float velX,velY;
	private float distance;
	private float randomRange;
	private int startOffset;
	private int i=0;

	public Projectile(float x, float y, int width, int height, float velocityX, float velocityY, float distance, float randomRange, int startOffset, String...urls) {
		super(x, y, width, height,0,urls);
		this.randomRange=randomRange;
		this.startOffset=startOffset;
		this.baseposX=x;
		this.baseposY=y;
		this.distance = distance;
		this.velX = velocityX;
		this.velY =velocityY;
	}

	public void tick() {

		if (i<startOffset)
			i++;
		else{

			if (velY<0)
			{
				if (this.x < (this.baseposX + this.distance) && this.y > (this.baseposY -this.distance)) {
					this.x += this.velX;
					this.y += this.velY;
				}
				else
				{
					this.y = this.baseposY;
					if (randomRange==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRange + 1)) + randomRange;
					System.out.println(this.x);
				}
			}

			else if (velY>0){
				if (this.x < (this.baseposX + this.distance) && this.y < (this.baseposY +this.distance)) {
					this.x += this.velX;
					this.y += this.velY;
				}
				else
				{
					this.y = this.baseposY;
					if (randomRange==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRange + 1)) + randomRange;
					System.out.println(this.x);
				}
			}
			else if (velY==0)
			{
				if (velX<0)

				if (this.x > (this.baseposX - this.distance)) {
					this.x += this.velX;
				}
				else
				{
					this.y = this.baseposY;
					if (randomRange==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRange + 1)) + randomRange;
					System.out.println(this.x);
				}

				else

				if (this.x < (this.baseposX + this.distance)) {
					this.x += this.velX;
				}
				else
				{
					this.y = this.baseposY;
					if (randomRange==0)
						this.x=this.baseposX;
					else
						this.x = (int)(Math.random() * (this.baseposX - randomRange + 1)) + randomRange;
					System.out.println(this.x);
				}
			}
		}

	}

	public void setBaseposX(float baseposX) {
		this.baseposX = baseposX;
	}

	public void setBaseposY(float baseposY) {
		this.baseposY = baseposY;
	}



}