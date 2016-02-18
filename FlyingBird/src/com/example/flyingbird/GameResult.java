package com.example.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

public class GameResult {
	
	private Bitmap bg;
	private Bitmap over;
	private Bitmap button;
	private Gaming gaming;
	//game over这个图标的坐标
	private int overX;
	private int overY;	
	//重新开始那个Button的坐标
	private int buttonX;
	private int buttonY;
	private int speed;
	private int score0;
	private RectF rectscore;
	
	public GameResult(Bitmap bg,Bitmap over,Bitmap button,Gaming gaming,int score0){
		this.score0=score0;
		this.bg=bg;
		this.over=over;
		this.button=button;
		this.gaming=gaming;
		speed=(int) MainActivity.dipToPixel(20);
		overX=(int) (MySurfaceView.screenW/2-over.getWidth()/2);
		overY=-over.getHeight();
		buttonX=(int) (MySurfaceView.screenW/2-button.getWidth()/2);
		buttonY=(int) MySurfaceView.screenH;
	}
	
	public void draw(Canvas canvas,Paint paint){
		canvas.save();
		canvas.scale(MySurfaceView.screenW/bg.getWidth(), MySurfaceView.screenH/bg.getHeight());
		canvas.drawBitmap(bg, 0, 0, paint);
		canvas.restore();
		gaming.btnY=MySurfaceView.screenH*0.78f-gaming.bird.getHeight();
		canvas.drawBitmap(gaming.bird, gaming.btnX, gaming.btnY, paint);
		canvas.drawBitmap(over, overX, overY, paint);
		canvas.drawBitmap(button, buttonX, buttonY, paint);
        if(buttonY<=MySurfaceView.screenH*0.78f-button.getHeight()){
        	paint.setColor(Color.argb(100, 200, 250, 50));
			canvas.drawRoundRect(rectscore,2,2,paint);
			paint.setColor(Color.WHITE);
			canvas.drawText("score      "+MySurfaceView.score, MySurfaceView.screenW/2-4*speed,MySurfaceView.screenH/2-1*speed, paint);
			if(score0<MySurfaceView.score)
				score0=MySurfaceView.score;
			canvas.drawText("bestscore  "+score0, MySurfaceView.screenW/2-4*speed,MySurfaceView.screenH/2+1*speed, paint);			
		}
	}
	
	public void logic(){
		if(overY<=MySurfaceView.screenH/4)
		overY+=speed;	
		if(overY>MySurfaceView.screenH/4){
			if(buttonY>=MySurfaceView.screenH*0.78f-button.getHeight())
				buttonY-=speed;
			else
				buttonY=(int) (MySurfaceView.screenH*0.78f-button.getHeight());
		}
		if(buttonY<=MySurfaceView.screenH*0.78f-button.getHeight()){
			rectscore=new RectF(MySurfaceView.screenW/2-5*speed,MySurfaceView.screenH/2-3*speed,MySurfaceView.screenW/2+5*speed,MySurfaceView.screenH/2+3*speed);
		}
	}
	
	public void OnTouchEvent(MotionEvent event){
		MySurfaceView.gameState=MySurfaceView.GAME_BEGIN;
	}

}
