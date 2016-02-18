package com.example.flyingbird;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/*
 * ������õ���״̬��ģʽ��ֻ��һ��Activity,������Ϸ״̬����ʾ�����Լ�С��ȵ�λ�á�
*/

public class MainActivity extends Activity {
	public static MainActivity instance;
	public final static float DENSITY = Resources.getSystem().getDisplayMetrics().density;
	
	//Ϊ�˲�ͬ�ֱ�����Ļ����ʾ��Ч��һ��������dp��λ��������pix��dp��ת��
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
        
		//����ȫ��        
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//��ʾ�Զ����SurfaceView��ͼ
		setContentView(new MySurfaceView(this));
    }
}
