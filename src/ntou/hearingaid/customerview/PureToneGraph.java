package ntou.hearingaid.customerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/*
 * �Ȩ�Ƥ���-ť�O��
 */

public class PureToneGraph extends View {

	private Bitmap background;
	private Paint paint;
	
	private int Width;	//�O���e���e��
	private int Height;	//�O���e������
	
	//���դ��P�W�a���
	private int data250 = 0;
	private int data500 = 0;
	private int data1000 = 0;
	private int data2000 = 0;
	private int data4000 = 0;
	//�k�դ��P�W�a���
	private int data250R = 0;
	private int data500R = 0;
	private int data1000R = 0;
	private int data2000R = 0;
	private int data4000R = 0;
	
	public PureToneGraph(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint();
	}

	public PureToneGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		
	}

	/*
	 * ��s�e��
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//Log.e("debug","Draw");
		DrawBackground(canvas);
		DrawData(canvas);
		
	}

	/*
	 * �����ϼe���O�_����
	 * �p���ܫh���]�w��
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		//Log.e("debug",Integer.toString(w)+","+Integer.toString(h));
		Width = w;
		Height = h;
		
	}
	//ø�sť�O�� �y�жb 
	private void DrawBackground(Canvas canvas)
	{
		//�]�w�e��
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		//�e�X�D�n��x-y�b
		canvas.drawLine(20, 20, 20, Height-20, paint);
		canvas.drawLine(20, Height-20, Width-20, Height-20, paint);
		
		//�]�w�e��
		paint.setColor(Color.GRAY);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		
		//�ФW�_�I�y�жb��
		canvas.drawText(String.valueOf(120), 0, Height-20, paint);
		int initfreq = 250;	//��lø�s�W�a�u
		int x_axis = (Width-40)/16;	//�N16���u�������t
		/*
		 * �̷Ӭ۹��mø�s16���u 250 500 750 1000 1250 .... 4000
		 */
		for(int i=0; i < 16; i++)
		{
			int tmp = 250*(i+1);	//�p���eø�s�W�a
			if(tmp == initfreq)	//�ˬd�O�_�ŦX�ҭn���U�@���W�a 250 500 1000 2000 4000
			{
				canvas.drawLine(20+((i+1)*x_axis-1), 20, 20+((i+1)*x_axis-1), Height-20, paint);
				canvas.drawText(String.valueOf(initfreq), 10+((i+1)*x_axis-1), 20 , paint);
				canvas.drawText(String.valueOf(initfreq), 10+((i+1)*x_axis-1), Height , paint);
				initfreq*=2;	//�N�ˬd�ȸ��ܤU�@�ӭnø�s���W�a
			}
		}
		
		int y_axis = (Height-40)/14;	//�N y�b����14���u
		/*
		 * �v�@�e�W14���u
		 * �üФW�����ƭ�
		 */
		for(int i=0; i < 13; i++)
		{
			canvas.drawLine(20, Height-20-(y_axis*(i+1)), Width-20, Height-20-(y_axis*(i+1)), paint);
			canvas.drawText(String.valueOf((120-((i+1)*10))), 0, Height-20-(y_axis*(i+1)), paint);
		}
	}
	
	/*
	 * �N��Ƶe�ܮy�жb�W
	 * canvas - ��ø�s�e��
	 */
	private void DrawData(Canvas canvas)
	{
		//�]�w�Ŧ�e�� ø�s����
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(10);
		//�N���ո�Ƴv�@ø�X
		int freq = 0;
		int db = 0;
		for(int i=0; i<16; i++)
		{
			switch(250*(i+1))
			{
			case 250:
				freq = 250;
				db = data250;
				break;
			case 500:
				freq = 500;
				db = data500;
				break;
			case 1000:
				freq = 1000;
				db = data1000;
				break;
			case 2000:
				freq = 2000;
				db = data2000;
				break;
			case 4000:
				freq = 4000;
				db = data4000;
				break;
				
			}
			int x_i = (freq/250)-1;
			float y_i = (120.0f-db)/10;
			int x_axis = (Width-40)/16;
			int y_axis = (Height-40)/14;
			canvas.drawPoint(20+((x_i+1)*x_axis-1), Height-20-(y_axis*(y_i)), paint);
		}
		//�]�w����e�� ø�s�k��
		paint.setColor(Color.RED);
		//�N�k�ո�Ƴv�@ø�X
		for(int i=0; i<16; i++)
		{
			switch(250*(i+1))
			{
			case 250:
				freq = 250;
				db = data250R;
				break;
			case 500:
				freq = 500;
				db = data500R;
				break;
			case 1000:
				freq = 1000;
				db = data1000R;
				break;
			case 2000:
				freq = 2000;
				db = data2000R;
				break;
			case 4000:
				freq = 4000;
				db = data4000R;
				break;
				
			}
			int x_i = (freq/250)-1;
			float y_i = (120.0f-db)/10;
			int x_axis = (Width-40)/16;
			int y_axis = (Height-40)/14;
			canvas.drawPoint(20+((x_i+1)*x_axis-1), Height-20-(y_axis*(y_i)), paint);
		}
	}
	
	/*
	 * �]�wť�O���
	 * freq - �����W�a
	 * db - ���q
	 * LeftorRight - ���թΥk��
	 */
	public void setData(int freq, int db, int LeftorRight)
	{
		
		switch(freq)
		{
		case 250:
			if(LeftorRight==0)
				data250 = db;
			else
				data250R = db;
			break;
		case 500:
			if(LeftorRight==0)
				data500 = db;
			else
				data500R = db;
			break;
		case 1000:
			if(LeftorRight==0)
				data1000 = db;
			else
				data1000R = db;
			break;
		case 2000:
			if(LeftorRight==0)
				data2000 = db;
			else
				data2000R = db;
			break;
		case 4000:
			if(LeftorRight==0)
				data4000 = db;
			else
				data4000R = db;
			break;
		}
	}
}
