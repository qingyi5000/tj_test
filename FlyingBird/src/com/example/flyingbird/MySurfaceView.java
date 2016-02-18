package com.example.flyingbird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	public static float screenW, screenH;
	public static int score;
	// ������Ϸ״̬����
	public static final int GAME_BEGIN = 0;// ��Ϸ��ʼ
	public static final int GAMING = 1;// ��Ϸ������
	public static final int GAME_RESULT = 2;// ��Ϸ���
	// ��ǰ��Ϸ״̬(Ĭ�ϳ�ʼ����Ϸ��ʼ����)
	public static int gameState = GAME_BEGIN;
	// ����һ��Resourcesʵ�����ڼ���ͼƬ
	private Resources res = this.getResources();
	// ������Ϸ��Ҫ�õ���ͼƬ��Դ(ͼƬ����)
	private Bitmap bmpbg1;// ��Ϸ����
	private Bitmap bmpbg2;
	private Bitmap bmpBird;
	private Bitmap bmpButton;
	private Bitmap bmpGundong;
	private Bitmap bmpFlyingbird;
	private Bitmap bmpOver;

	private float textsize;
	private SharedPreferences sp;
	private int score0;
	
	//����SoundPool
	public static SoundPool soundpool;
	public static int soundId1;
	public static int soundId2;
	public static int soundId3;
	public static int soundId4;
	public static int soundId5;

	private GameBegin gameBegin;
	private GameBG gameBg;
	private Gaming gaming;
	private GameObstacle gameObstacle;
	private GameResult gameResult;

	// ��һ�������ϰ����ʱ��(����)
	private int createObstacleTime;

	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		sp = context.getSharedPreferences("saveScore", Context.MODE_PRIVATE);
		

		
		setFocusable(true);
		setFocusableInTouchMode(true);
		// ���ñ�������
		this.setKeepScreenOn(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		initGame();
		flag = true;
		// ʵ���߳�
		th = new Thread(this);

		// �����߳�
		th.start();
	}

	private void initGame() {
	
		// ������Ϸ�����̨���½�����Ϸʱ����Ϸ������!
		// ����Ϸ״̬���ڲ˵�ʱ���Ż�������Ϸ
		if (gameState == GAME_BEGIN) {
			// ������Ϸ��Դ
			bmpbg1 = BitmapFactory.decodeResource(res, R.drawable.bg1);
			bmpbg2 = BitmapFactory.decodeResource(res, R.drawable.bg2);
			bmpBird = BitmapFactory.decodeResource(res, R.drawable.bird);
			bmpButton = BitmapFactory.decodeResource(res, R.drawable.button);
			bmpGundong = BitmapFactory.decodeResource(res,
					R.drawable.gundongtiao);
			bmpFlyingbird = BitmapFactory.decodeResource(res,
					R.drawable.flyingbird);
			bmpOver = BitmapFactory.decodeResource(res, R.drawable.gameover);

			gameBegin = new GameBegin(bmpBird, bmpButton, bmpFlyingbird);
			gameBg = new GameBG(bmpGundong, bmpbg1, bmpbg2);
			gaming = new Gaming(bmpBird);
			gameObstacle = new GameObstacle(gaming);
			score0 = sp.getInt("bestScore", -1);
			gameResult = new GameResult(bmpbg1, bmpOver, bmpButton, gaming,score0);

			textsize = MainActivity.dipToPixel(30);
			
			createObstacleTime = 50;
			score = 0;
			
			soundpool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			soundId1 = soundpool.load(getContext(), R.raw.ss1, 1);
			soundId2 = soundpool.load(getContext(), R.raw.ss2, 1);
			soundId3 = soundpool.load(getContext(), R.raw.ss3, 1);
			soundId4 = soundpool.load(getContext(), R.raw.ss4, 1);
			soundId5= soundpool.load(getContext(), R.raw.ss5, 1);
		}
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
		// ���������¼�����������Ϸ״̬��ͬ���в�ͬ����		
		switch (gameState) {
		case GAME_BEGIN:
			if (event.getX() >= (screenW / 2 - bmpButton.getWidth() / 2)
					&& event.getX() <= (screenW / 2 + bmpButton.getWidth() / 2))
				if (event.getY() <= MySurfaceView.screenH * 0.78613f
						&& event.getY() >= MySurfaceView.screenH * 0.78613f
								- bmpButton.getHeight())
					gameBegin.onTouchEvent(event);
			break;
		case GAMING:
			soundpool.play(soundId1, 1f, 1f, 0, 0, 2);
			gaming.onTouchEvent(event);
			break;
		case GAME_RESULT:
			if (score > score0)
				sp.edit().putInt("bestScore", score).commit();
			if (event.getX() >= (screenW / 2 - bmpButton.getWidth() / 2)
					&& event.getX() <= (screenW / 2 + bmpButton.getWidth() / 2))
				if (event.getY() <= MySurfaceView.screenH * 0.78613f
						&& event.getY() >= MySurfaceView.screenH * 0.78613f
								- bmpButton.getHeight()) {
					gameResult.OnTouchEvent(event);
					initGame();
				}
			break;
		}
		return super.onTouchEvent(event);
	}

	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.WHITE);
				// ��ͼ����������Ϸ״̬��ͬ���в�ͬ����
				switch (gameState) {
				case GAME_BEGIN:
					gameBg.draw(canvas, paint);
					gameBegin.draw(canvas, paint);
					break;
				case GAMING:
					gameBg.draw(canvas, paint);
					gaming.draw(canvas, paint);
					if (Gaming.isTouch) {
						if (createObstacleTime <= 0) {
							gameObstacle.draw(canvas, paint);
						}
					}
					paint.setColor(Color.WHITE);
					paint.setTextSize(textsize);
					canvas.drawText(MySurfaceView.score + "",
							MySurfaceView.screenW / 2,
							MySurfaceView.screenH / 5, paint);
					break;
				case GAME_RESULT:
					gameResult.draw(canvas, paint);
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	private void logic() {
		// �߼����������Ϸ״̬��ͬ���в�ͬ����
		switch (gameState) {
		case GAME_BEGIN:
			gameBegin.logic();
			gameBg.logic();
			break;
		case GAMING:
			gameBg.logic();
			gaming.logic();
			if (Gaming.isTouch) {
				createObstacleTime--;
				if (createObstacleTime <= 0) {
					gameObstacle.logic();
				}
				if (gameObstacle.isCollision())
				{
					soundpool.play(soundId4, 1f, 1f, 0, 0, 2);
					gameState = GAME_RESULT;
				}
			}
			break;
		case GAME_RESULT:
			gameResult.logic();
			break;
		}
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���������¼�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ����back���ذ���
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// ��Ϸʤ����ʧ�ܡ�����ʱ��Ĭ�Ϸ��ز˵�
			if (gameState == GAMING || gameState == GAME_RESULT) {
				gameState = GAME_BEGIN;
				initGame();
			} else if (gameState == GAME_BEGIN) {
				// ��ǰ��Ϸ״̬�ڲ˵����棬Ĭ�Ϸ��ذ����˳���Ϸ
				MainActivity.instance.finish();
				System.exit(0);
			}
			return true;
		}
		// ���������¼�����������Ϸ״̬��ͬ���в�ͬ����
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ����̧���¼�����
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// ����back���ذ���
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// ��Ϸʤ����ʧ�ܡ�����ʱ��Ĭ�Ϸ��ز˵�

			return true;
		}
		// ���������¼�����������Ϸ״̬��ͬ���в�ͬ����

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * SurfaceView��ͼ����ʱ����Ӧ�˺���
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

}
