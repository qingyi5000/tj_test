package com.example.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Gaming {

	public Bitmap bird;
	// С�������
	public float btnX, btnY;
	// �Ƿ��д����¼�
	public static Boolean isTouch;
	// �������ٶ�ǰ���Ǹ�ֵ
	private float flag;
	// �������ٶ�
	private float gravity;
	private Boolean isUp;

	// �˵���ʼ��
	public Gaming(Bitmap bird) {
		this.bird = bird;
		btnX = MySurfaceView.screenW / 3 - bird.getWidth();
		btnY = MySurfaceView.screenH / 2;
		flag = 0;
		gravity = MainActivity.dipToPixel(0.7f);
		isTouch = false;
		isUp = true;
	}

	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bird, btnX, btnY, paint);
	}

	public void logic() {
		if (!isTouch) {
			if (isUp) {
				if (btnY < MySurfaceView.screenH / 2 + 8 * gravity)
					btnY += gravity;
				else if (btnY >= MySurfaceView.screenH / 2 + 8 * gravity)
					isUp = false;
			} else {
				if (btnY > MySurfaceView.screenH / 2)
					btnY -= gravity;
				else if (btnY <= MySurfaceView.screenH / 2)
					isUp = true;
			}
		}

		else {
			btnY += flag * gravity;
			if (flag != -1 && flag != 0)
				flag += 2;
			else
				flag += 1;
		}
		
		if (btnY <= 0)
			btnY = 0;

		if (btnY >= MySurfaceView.screenH * 0.78f) {
			MySurfaceView.soundpool.play(MySurfaceView.soundId4, 1f, 1f, 0, 0,
					2);
			MySurfaceView.gameState = MySurfaceView.GAME_RESULT;
		}
	}

	// �˵������¼���������Ҫ���ڴ���ť�¼�
	public void onTouchEvent(MotionEvent event) {
		if (isTouch) {
			flag = -11;
		}
		isTouch = true;
	}

}
