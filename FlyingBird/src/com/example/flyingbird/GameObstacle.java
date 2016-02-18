package com.example.flyingbird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;

public class GameObstacle {

	private float rectX;  //第一个障碍物的横坐标
	private float rectX2; //第二个障碍物的横坐标
	private float rectY;  //第一个障碍物的底边纵坐标
	private float rectY2; //第二个障碍物的底边纵坐标
	private float rectW;  //障碍物的宽度
	private float Gaphight;  //中间空隙的高度
	private float O2spacing;  //两个障碍物之间的距离
	private float speedpx = 5;
	private float speed;
	private int flag = 1;
	private int[] random = { 170,180,190,200,210, 220, 230, 240, 250 ,260,270};
	private float[] randompx;
	private float rectY_1;
	private float rectY_2;
	private Region region;
	Gaming gameStart;

	public GameObstacle(Gaming gameStart) {
		this.gameStart=gameStart;
		rectW = MainActivity.dipToPixel(70);
		Gaphight = MainActivity.dipToPixel(100);
		O2spacing = MainActivity.dipToPixel(230);
		speed=MainActivity.dipToPixel(speedpx);
		randompx = new float[10];
		for (int i = 0; i < 10; i++)
			randompx[i] = MainActivity.dipToPixel(random[i]);
		rectX = (int) MySurfaceView.screenW;
		rectX2 = rectX + O2spacing;
		rectY = rectY2 = (int) (MySurfaceView.screenH * 0.78f);
		rectY_1 = randompx[(int) (Math.random() * 10)];
		rectY_2 = randompx[(int) (Math.random() * 10)];
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		region = new Region();
		Rect rect1 = new Rect((int)rectX, 0, (int)(rectX + rectW), (int)rectY);
		Rect rect1_1 = new Rect((int)rectX, (int)rectY_1, (int)(rectX + rectW), (int)(rectY_1 + Gaphight));
		Rect rect2 = new Rect((int)rectX2, 0, (int)(rectX2 + rectW), (int)rectY2);
		Rect rect2_1 = new Rect((int)rectX2, (int)rectY_2, (int)(rectX2 + rectW), (int)(rectY_2 + Gaphight));
		region.op(rect1, Region.Op.UNION);
		region.op(rect1_1, Region.Op.XOR);
		region.op(rect2, Region.Op.UNION);
		region.op(rect2_1, Region.Op.XOR);
		canvas.clipRegion(region);
		canvas.drawColor(Color.GREEN);
		canvas.restore();
	}

	public void logic() {
		switch (flag) {
		case 1:
			rectX -= speed;
			rectX2 = rectX + O2spacing;
			if(gameStart.btnX-speed<rectX + rectW&& rectX + rectW<=gameStart.btnX){
				MySurfaceView.score++;
			}
			if (rectX + rectW <= 0) {
				flag = 2;
				rectY_1 = randompx[(int) (Math.random() * 10)];
			}
			break;
		case 2:
			rectX2 -= speed;
			rectX = rectX2 + O2spacing;
			if(gameStart.btnX-speed<rectX2 + rectW&& rectX2 + rectW<=gameStart.btnX){
				MySurfaceView.score++;
			}
			if (rectX2 + rectW <= 0) {
				flag = 1;
				rectY_2 = randompx[(int) (Math.random() * 10)];
			}
			break;
		}
	}

	public boolean isCollision() {
		if (rectX + rectW > gameStart.btnX
				&& rectX <= gameStart.btnX + gameStart.bird.getWidth()){
			if (rectY_1 >= gameStart.btnY
					|| rectY_1 + Gaphight <= gameStart.btnY
							+ gameStart.bird.getHeight())
				return true;}
		if (rectX2 + rectW > gameStart.btnX
				&& rectX2 <= gameStart.btnX + gameStart.bird.getWidth()){
			if (rectY_2 >= gameStart.btnY
					|| rectY_2 + Gaphight <= gameStart.btnY
							+ gameStart.bird.getHeight())
				return true;}
		return false;

	}

}
