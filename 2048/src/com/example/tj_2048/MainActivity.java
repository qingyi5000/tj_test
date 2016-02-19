package com.example.tj_2048;

import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;

public class MainActivity extends Activity implements OnTouchListener{

	@SuppressWarnings("rawtypes")
	List<List> gridlist;
	List<TextView> list[];
	GridView gridview;
	TextView scoreview;
	TextView bestscoreview;
	MyAdapter adapter;
	final int colume = 4;
	//记录当前分数
	static int score;
	//记录历史最高分数
	static int BestScore;
	String[] arr_num = new String[colume * colume];
	Boolean flag;
	int mMargin = 10;
	int mPadding;
	int mTextSize = 24;
	int mWidth;
	private SharedPreferences sp;
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sp = this.getSharedPreferences("saveScore", Context.MODE_PRIVATE);
		BestScore = sp.getInt("bestScore", 0);
		score=0;
		bestscoreview=(TextView)findViewById(R.id.bestscoreview);
		scoreview=(TextView)findViewById(R.id.scoreview);
		scoreview.setText("Score: "+score);
		bestscoreview.setText("Best Score: "+BestScore);
		for (int i = 0; i < colume * colume; i++)
			arr_num[i] = "0";
		mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mMargin, getResources().getDisplayMetrics());
		mPadding = mMargin;
		mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				mMargin, getResources().getDisplayMetrics());
		mWidth = (getResources().getDisplayMetrics().widthPixels - 2 * mMargin - (colume-1) * mPadding) / colume;
		mGestureDetector = new GestureDetector(this , new MyGestureDetector());

		gridview = (GridView) findViewById(R.id.gridview);
		adapter = new MyAdapter(this);
		gridview.setAdapter(adapter);
		gridview.setOnTouchListener(this);
		generate();
		generate();
	}

	public void moveRight() {
		boolean isValid=false;
		for (int i = 0; i < colume; i++) {

			List<String> row = new ArrayList<String>();
			// 将每一行的非“0”元素都添加到List里面去
			for (int j = 0; j < colume; j++) {
				if (arr_num[i * colume + colume - 1 - j] != "0")
					row.add(arr_num[i * colume + colume - 1 - j]);
			}

			// 将row里面相邻相同元素进行合并
			for (int j = 0; j < row.size() - 1; j++) {
				if (row.get(j).equals( row.get(j + 1))) {
					score+=Integer.parseInt(row.get(j)) * 2;
					scoreview.setText("Score:"+score);
					if(score>BestScore){
						BestScore=score;
					}
					bestscoreview.setText("Best Score: "+BestScore);
					row.set(j, Integer.parseInt(row.get(j)) * 2 + "");
					for (int k = j + 1; k < row.size() - 1; k++)
						row.set(k, row.get(k + 1));
					row.remove(row.size() - 1);
				}
			}
			
			for (int j = 0; j < row.size(); j++) {
				if (arr_num[i * colume + colume - 1 - j] != row.get(j))
					isValid=true;
			}

			// 将已经移动合并过的row里面的元素还原给数组
			for (int j = 0; j < colume; j++) {
				if (j < row.size())
					arr_num[i * colume + colume - 1 - j] = row.get(j);
				else
					arr_num[i * colume + colume - 1 - j] = "0";
			}

		}
		if(isValid){
		generate();
		adapter.notifyDataSetChanged();
		}else{
			Toast toast=Toast.makeText(this, "当前方向不能进行滑动操作", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	public void moveLeft() {
		boolean isValid=false;
		for (int i = 0; i < colume; i++) {

			List<String> row = new ArrayList<String>();
			// 将每一行的非“0”元素都添加到List里面去
			for (int j = 0; j < colume; j++) {
				if (arr_num[i * colume + j] != "0")
					row.add(arr_num[i * colume + j]);
			}

			// 将row里面相邻相同元素进行合并
			for (int j = 0; j < row.size() -1; j++) {
				if (Integer.parseInt(row.get(j) )== Integer.parseInt(row.get(j + 1))) {
					score+=Integer.parseInt(row.get(j)) * 2;
					scoreview.setText("Score:"+score);
					if(score>BestScore){
						BestScore=score;
					}
					bestscoreview.setText("Best Score: "+BestScore);
					row.set(j, (Integer.parseInt(row.get(j)) + Integer.parseInt(row.get(j+1))) + "");
					for (int k = j + 1; k < row.size() - 1; k++)
						row.set(k, row.get(k + 1));
					row.remove(row.size() - 1);
				}
			}
			
			for (int j = 0; j < row.size(); j++) {
				if (arr_num[i * colume + j] != row.get(j))
					isValid=true;
			}

			// 将已经移动合并过的row里面的元素还原给数组
			for (int j = 0; j < colume; j++) {
				if (j < row.size())
					arr_num[i * colume + j] = row.get(j);
				else
					arr_num[i * colume + j] = "0";
			}
		}
		if(isValid){
			generate();
			adapter.notifyDataSetChanged();
			}else{
				Toast toast=Toast.makeText(this, "当前方向不能进行滑动操作", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
	}

	public void moveUp() {
		boolean isValid=false;
		for (int i = 0; i < colume; i++) {

			List<String> row = new ArrayList<String>();
			// 将每一行的非“0”元素都添加到List里面去
			for (int j = 0; j < colume; j++) {
				if (arr_num[i + j * colume] != "0")
					row.add(arr_num[i + j * colume]);
			}

			// 将row里面相邻相同元素进行合并
			for (int j = 0; j < row.size() - 1; j++) {
				if (Integer.parseInt(row.get(j) )== Integer.parseInt(row.get(j + 1))) {
					score+=Integer.parseInt(row.get(j)) * 2;
					scoreview.setText("Score:"+score);
					if(score>BestScore){
						BestScore=score;
					}
					bestscoreview.setText("Best Score: "+BestScore);
					row.set(j, Integer.parseInt(row.get(j)) * 2 + "");
					for (int k = j + 1; k < row.size() - 1; k++)
						row.set(k, row.get(k + 1));
					row.remove(row.size() - 1);
				}
			}
			
			for (int j = 0; j < row.size(); j++) {
				if (arr_num[i + j * colume] != row.get(j))
					isValid=true;
			}

			// 将已经移动合并过的row里面的元素还原给数组
			for (int j = 0; j < colume; j++) {
				if (j < row.size())
					arr_num[i + j * colume] = row.get(j);
				else
					arr_num[i + j * colume] = "0";
			}
		}
		if(isValid){
			generate();
			adapter.notifyDataSetChanged();
			}else{
				Toast toast=Toast.makeText(this, "当前方向不能进行滑动操作", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
	}

	public void moveDown() {
		boolean isValid=false;
		for (int i = 0; i < colume; i++) {

			List<String> row = new ArrayList<String>();
			// 将每一行的非“0”元素都添加到List里面去
			for (int j = 0; j < colume; j++) {
				if (arr_num[(colume - 1-j) * colume  + i] != "0")
					row.add(arr_num[(colume - 1-j) * colume  + i]);
			}

			// 将row里面相邻相同元素进行合并
			for (int j = 0; j < row.size() - 1; j++) {
				if (Integer.parseInt(row.get(j) )== Integer.parseInt(row.get(j + 1))) {
					score+=Integer.parseInt(row.get(j)) * 2;
					scoreview.setText("Score:"+score);
					if(score>BestScore){
						BestScore=score;
					}
					bestscoreview.setText("Best Score: "+BestScore);
					row.set(j, Integer.parseInt(row.get(j)) * 2 + "");
					for (int k = j + 1; k < row.size() - 1; k++)
						row.set(k, row.get(k + 1));
					row.remove(row.size() - 1);
				}
			}
			
			for (int j = 0; j < row.size(); j++) {
				if (arr_num[(colume - 1-j) * colume  + i] != row.get(j))
					isValid=true;
			}

			// 将已经移动合并过的row里面的元素还原给数组
			for (int j = 0; j < colume; j++) {
				if (j < row.size())
					arr_num[(colume - 1-j) * colume  + i] = row.get(j);
				else
					arr_num[(colume - 1-j) * colume  + i] = "0";
			}
		}
		if(isValid){
			generate();
			adapter.notifyDataSetChanged();
			}else{
				Toast toast=Toast.makeText(this, "当前方向不能进行滑动操作", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
	}

	public void generate() {
		// 判断所有格子是不是都被占满，如果占满，游戏结束;如果没占满，生成一个2或者4
		int non_zero = 0;
		for (int i = 0; i < colume * colume; i++) {
			if (arr_num[i] != "0")
				non_zero++;
		}
		if (non_zero == colume * colume) {
			sp.edit().putInt("bestScore", BestScore).commit();
			gameover();
			return;
		} else {
			do {
				flag = true;
				int num = (int) (Math.random() * (colume * colume));
				if (arr_num[num] == "0") {
					arr_num[num] = Math.random()>0.2?"2":"4";
					flag = false;
				}
			} while (flag);
		}
	}
	
	public void gameover(){
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("Game Over");
        builder.setMessage("Score:"+score+"\n"+"Best Score:"+BestScore);
        builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	for (int i = 0; i < colume * colume; i++)
        			arr_num[i] = "0";
            	generate();
            	generate();
        		adapter.notifyDataSetChanged();
        		score=0;
        		scoreview.setText("Score: "+score);
        		
            	
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.show();
	}

	//网格布局的适配器
	class MyAdapter extends BaseAdapter {
		Context context;
		public MyAdapter(Context context) {
			this.context = context;
		}
		@Override
		public int getCount() {
			return arr_num.length;
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		// 创建view的方法
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				textView = new TextView(context);
				textView.setLayoutParams(new GridView.LayoutParams(mWidth,
						mWidth));
			} else {
				textView = (TextView) convertView;
			}
			textView.setText(arr_num[position]);
			textView.setTextSize(mTextSize);
			textView.setGravity(Gravity.CENTER);

			String mBgColor = "";
			switch (Integer.parseInt(arr_num[position])) {
			case 0:
				mBgColor = "#CCC0B3";
				break;
			case 2:
				mBgColor = "#EEE4DA";
				break;
			case 4:
				mBgColor = "#EDE0C8";
				break;
			case 8:
				mBgColor = "#F2B179";
				break;
			case 16:
				mBgColor = "#F49563";
				break;
			case 32:
				mBgColor = "#F5794D";
				break;
			case 64:
				mBgColor = "#F55D37";
				break;
			case 128:
				mBgColor = "#EEE863";
				break;
			case 256:
				mBgColor = "#EDB04D";
				break;
			case 512:
				mBgColor = "#ECB04D";
				break;
			case 1024:
				mBgColor = "#EB9437";
				break;
			case 2048:
				mBgColor = "#EA7821";
				break;
			default:
				mBgColor = "#EA7821";
				break;
			}
			textView.setBackgroundColor(Color.parseColor(mBgColor));
			textView.setTextColor(Color.BLACK);
			if (textView.getText() == "0") {
				textView.setTextColor(Color.parseColor(mBgColor));
			}
			return textView;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub	
		mGestureDetector.onTouchEvent(event);
		return true;
	}
	
	class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
	{

		final int FLING_MIN_DISTANCE = 50;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY)
		{
			float x = e2.getX() - e1.getX();
			float y = e2.getY() - e1.getY();

			if (x > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > Math.abs(velocityY))
			{
				moveRight();

			} else if (x < -FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > Math.abs(velocityY))
			{
				moveLeft();

			} else if (y > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) < Math.abs(velocityY))
			{
				moveDown();

			} else if (y < -FLING_MIN_DISTANCE
					&& Math.abs(velocityX) < Math.abs(velocityY))
			{
				moveUp();
			}
			return true;

		}

	}
}
