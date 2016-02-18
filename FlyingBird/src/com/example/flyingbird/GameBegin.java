package com.example.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class GameBegin {
	
	private Bitmap bird;
	private Bitmap button;
	private Bitmap flyingbird;
	//小鸟的坐标
	private float btnX, btnY;
	private Boolean isUp;
	//菜单初始化
	public GameBegin(Bitmap bird,Bitmap button,Bitmap flyingbird) {
		
		this.bird = bird;
		this .button=button;
		this.flyingbird=flyingbird;
		//X居中，Y紧接屏幕底部
		btnX = MySurfaceView.screenW / 2 - bird.getWidth() / 2;
		btnY = MySurfaceView.screenH/2-bird.getHeight()/2;
		isUp=true;
	}
	//菜单绘图函数
	public void draw(Canvas canvas, Paint paint) {
		//绘制菜单背景图
		//canvas.drawBitmap(begin, 0, 0, paint);
		//绘制小鸟
		canvas.drawBitmap(flyingbird, MySurfaceView.screenW/2-flyingbird.getWidth()/2, MySurfaceView.screenH/3, paint);
		canvas.drawBitmap(bird, btnX, btnY, paint);
		canvas.drawBitmap(button, MySurfaceView.screenW/2-button.getWidth()/2, MySurfaceView.screenH*0.78613f-button.getHeight(), paint);
		
	}
	
	public void logic(){
		if(isUp){
			if(btnY<MySurfaceView.screenH/2-bird.getHeight()/2+32)
				btnY+=4;
			else if(btnY==MySurfaceView.screenH/2-bird.getHeight()/2+32)
				isUp=false;
		}
		else{
			if(btnY>MySurfaceView.screenH/2-bird.getHeight()/2)
				btnY-=4;
			else if(btnY==MySurfaceView.screenH/2-bird.getHeight()/2)
				isUp=true;
		}
	}
	
	//菜单触屏事件函数，主要用于处理按钮事件
	public void onTouchEvent(MotionEvent event) {
		MySurfaceView.gameState = MySurfaceView.GAMING;
	}

}
