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
	// 定义游戏状态常量
	public static final int GAME_BEGIN = 0;// 游戏开始
	public static final int GAMING = 1;// 游戏进行中
	public static final int GAME_RESULT = 2;// 游戏结果
	// 当前游戏状态(默认初始在游戏开始界面)
	public static int gameState = GAME_BEGIN;
	// 声明一个Resources实例便于加载图片
	private Resources res = this.getResources();
	// 声明游戏需要用到的图片资源(图片声明)
	private Bitmap bmpbg1;// 游戏背景
	private Bitmap bmpbg2;
	private Bitmap bmpBird;
	private Bitmap bmpButton;
	private Bitmap bmpGundong;
	private Bitmap bmpFlyingbird;
	private Bitmap bmpOver;

	private float textsize;
	private SharedPreferences sp;
	private int score0;
	
	//声明SoundPool
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

	// 第一次生成障碍物的时间(毫秒)
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
		// 设置背景常亮
		this.setKeepScreenOn(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		initGame();
		flag = true;
		// 实例线程
		th = new Thread(this);

		// 启动线程
		th.start();
	}

	private void initGame() {
	
		// 放置游戏切入后台重新进入游戏时，游戏被重置!
		// 当游戏状态处于菜单时，才会重置游戏
		if (gameState == GAME_BEGIN) {
			// 加载游戏资源
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
		// 触屏监听事件函数根据游戏状态不同进行不同监听		
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
				// 绘图函数根据游戏状态不同进行不同绘制
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
		// 逻辑处理根据游戏状态不同进行不同处理
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
	 * 按键按下事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 处理back返回按键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 游戏胜利、失败、进行时都默认返回菜单
			if (gameState == GAMING || gameState == GAME_RESULT) {
				gameState = GAME_BEGIN;
				initGame();
			} else if (gameState == GAME_BEGIN) {
				// 当前游戏状态在菜单界面，默认返回按键退出游戏
				MainActivity.instance.finish();
				System.exit(0);
			}
			return true;
		}
		// 按键监听事件函数根据游戏状态不同进行不同监听
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 按键抬起事件监听
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// 处理back返回按键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 游戏胜利、失败、进行时都默认返回菜单

			return true;
		}
		// 按键监听事件函数根据游戏状态不同进行不同监听

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * SurfaceView视图消亡时，响应此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

}
