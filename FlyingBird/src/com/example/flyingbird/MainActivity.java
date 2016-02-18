package com.example.flyingbird;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/*
 * 程序采用的是状态机模式，只有一个Activity,根据游戏状态来显示背景以及小鸟等的位置。
*/

public class MainActivity extends Activity {
	public static MainActivity instance;
	public final static float DENSITY = Resources.getSystem().getDisplayMetrics().density;
	
	//为了不同分辨率屏幕上显示的效果一样，采用dp单位，下面是pix和dp的转换
	public static float dipToPixel(float dip) {
	        return dip * DENSITY ;
	}

	public static float pixelToDip(float pixel) {
	        return pixel/ DENSITY;
	    
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        
        Log.e("density",""+DENSITY);
        
		//设置全屏        
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//显示自定义的SurfaceView视图
		setContentView(new MySurfaceView(this));
    }
}
