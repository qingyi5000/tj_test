package com.example.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameBG {
	
	private Bitmap gundongtiao1;
	private Bitmap gundongtiao2;
	private Bitmap bg1;
	private Bitmap bg2;
	//游戏背景坐标
	private float bg1x, bg1y, bg2x, bg2y;
	//背景滚动速度
	private float speedpx = 5;
	private float speed;

	//游戏背景构造函数
	public GameBG(Bitmap gundongtiao,Bitmap bg1,Bitmap bg2) {
		this.gundongtiao1 = gundongtiao;
		this.gundongtiao2 = gundongtiao;
		this.bg1=bg1;
		this.bg2=bg2;
		speed=MainActivity.dipToPixel(speedpx);
		bg1y=bg2y=MySurfaceView.screenH*0.78f;
		bg1x = 0;
		bg2x = bg1x + gundongtiao1.getWidth() ;
	}
	//游戏背景的绘图函数
	public void draw(Canvas canvas, Paint paint) {
		//绘制两张背景
		switch(MySurfaceView.gameState){
		case MySurfaceView.GAME_BEGIN:
			canvas.save();
			canvas.scale(MySurfaceView.screenW/bg1.getWidth(), MySurfaceView.screenH/bg1.getHeight());
			canvas.drawBitmap(bg1, 0, 0, paint);
			canvas.restore();
			break;
		case MySurfaceView.GAMING:
			canvas.save();
			if(Gaming.isTouch==false){
				canvas.scale(MySurfaceView.screenW/bg2.getWidth(), MySurfaceView.screenH/bg2.getHeight());
				canvas.drawBitmap(bg2, 0, 0, paint);
			}
			else{
				canvas.scale(MySurfaceView.screenW/bg1.getWidth(), MySurfaceView.screenH/bg1.getHeight());
				canvas.drawBitmap(bg1, 0, 0, paint);
			}
				canvas.restore();
				
				break;
		}
		canvas.drawBitmap(gundongtiao1, bg1x, bg1y, paint);
		canvas.drawBitmap(gundongtiao2, bg2x, bg2y, paint);
	}
	//游戏背景的逻辑函数
	public void logic() {
		bg1x -= speed;
		bg2x -= speed;
		//当第一张图片的X坐标超出屏幕，
		//立即将其坐标设置到第二张图的上方
		if (bg2x<=0) {
			bg1x = bg2x + gundongtiao1.getWidth() ;
		}
		//当第二张图片的X坐标超出屏幕，
		//立即将其坐标设置到第一张图的上方
		if (bg1x<=0) {
			bg2x = bg1x + gundongtiao2.getWidth();
		}
	}

}
