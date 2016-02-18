package com.example.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class GameBegin {
	
	private Bitmap bird;
	private Bitmap button;
	private Bitmap flyingbird;
	//С�������
	private float btnX, btnY;
	private Boolean isUp;
	//�˵���ʼ��
	public GameBegin(Bitmap bird,Bitmap button,Bitmap flyingbird) {
		
		this.bird = bird;
		this .button=button;
		this.flyingbird=flyingbird;
		//X���У�Y������Ļ�ײ�
		btnX = MySurfaceView.screenW / 2 - bird.getWidth() / 2;
		btnY = MySurfaceView.screenH/2-bird.getHeight()/2;
		isUp=true;
	}
	//�˵���ͼ����
	public void draw(Canvas canvas, Paint paint) {
		//���Ʋ˵�����ͼ
		//canvas.drawBitmap(begin, 0, 0, paint);
		//����С��
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
	
	//�˵������¼���������Ҫ���ڴ���ť�¼�
	public void onTouchEvent(MotionEvent event) {
		MySurfaceView.gameState = MySurfaceView.GAMING;
	}

}
