package ntou.hearingaid.customerview;

import ntou.hearingaid.dsp.IIRFilter.Complex;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class BodePlotGraph extends View {

	private double[] s;	//�O�� s-domain ���Ϋ�ƭ�
	private double[] db;	//�O���W�q��
	private double[] phase;	//�O���ۦ�ϭ�
	public enum BodePlotType{db,phase};	//�C�|�Ϫ�����
	private BodePlotType type = BodePlotType.db;	//�w�]�ϥμW�N��
	private Paint paint;	//�]�w�e��	
	private int Width;	//�e���e��
	private int Height;	//�e������
	//�ѩ󥪥k���Τ�Ҥ��P�A�z�Lless1Count�Pgreater1Count�O�����k��
	private int less1Count = 0;		//�O��s�p��1�� �O���Ӽ�
	private int greater1Count = 0;	//�O��s�j��1�� �O���Ӽ�
	private float db_minData = -50;	//�O���W�q�ϳ̤p��
	private float db_maxData = 50;	//�O���W�q�ϳ̤j��
	private int db_increase = 10;	//�O���W�q��increase
	private float phase_minData = 0;	//�O���ۦ�ϳ̤p��
	private float phase_maxData = 0;	//�O���ۦ�ϳ̤j��
	private int phase_increase = 180;	//�O���ۦ��increase
	
	private int max_y_axis;	//�ثe�̤jy�b��
	private int min_y_axis;	//�ثe�̤py�b��
	
	/*
	 * �i�w�Ϫ�l��
	 */
	public BodePlotGraph(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint = new Paint();
	}

	/*
	 * �i�w�Ϫ�l��
	 */
	public BodePlotGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		paint = new Paint();
	}
	
	/*
	 * ���o�ثe�i�w�ϧκA
	 */
	public BodePlotType getType()
	{
		return this.type;
	}
	
	/*
	 * �]�w�ثe�i�w�ϧκA
	 * type - dB or phase
	 */
	public void setType(BodePlotType type)
	{
		this.type = type;
	}

	/*
	 * ��s�e��
	 * canvas - ��ø�s���e��
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		DrawBackground(canvas);
		DrawData(canvas);
	}
	
	/*
	 * �����ϼe���O�_����
	 * �p���ܫh���]�w��
	 * w - �ثe�e��
	 * h - �ثe����
	 * oldw - �e�@���e��
	 * oldh - �e�@������
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		//Log.e("debug",Integer.toString(w)+","+Integer.toString(h));
		Width = w;
		Height = h;
		
	}
	
	/*
	 * ø�sBodePlot �y�жb 
	 * canvas - ��ø�s���e��
	 */
	private void DrawBackground(Canvas canvas)
	{
		
		int y_num; 
		float x_axis = (Width-40)/2;
		float y_axis;
		
		switch(this.type)
		{
		//db�y�жb
		case db:
			y_num = (max_y_axis-min_y_axis)/db_increase;//y�b���έӼ�
			y_axis = (Height-40)/y_num;
			//�]�w�e����ǽu�C��
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(5);
			
			//�e���XY��ǽu
			canvas.drawLine(20, 20, 20, Height-20, paint);
			canvas.drawLine(20, Height-20, Width-20, Height-20, paint);
			
			
			//�]�w�e�������u�C��
			paint.setColor(Color.GRAY);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			
			canvas.drawText(String.valueOf(min_y_axis), 0, ((Height-20)), paint);
			canvas.drawText(String.valueOf(0.1), 25, ((Height-20))+20, paint);
			
			//�e�Xy�b �üФWy�b�����ƭ�
			for(int i=0;i<y_num;i++)
			{
				//canvas.drawLine(20, ((Height-20)/2)+(i+1)*y_axis, Width-20, ((Height-20)/2)+(i+1)*y_axis, paint);
				canvas.drawLine(20, ((Height-20))-(i+1)*y_axis, Width-20, ((Height-20))-(i+1)*y_axis, paint);
				//canvas.drawText(String.valueOf((i+1)*10), 0, ((Height-20)/2)-(i+1)*y_axis, paint);
				canvas.drawText(String.valueOf(min_y_axis+(i+1)*db_increase), 0, ((Height-20))-(i+1)*y_axis, paint);
			}
			//�e�Xx�b �üФWx�b�����ƭ�
			for(int i=0;i<2;i++)
			{
				
				canvas.drawLine(20+(i+1)*x_axis, 20, 20+(i+1)*x_axis, Height-20, paint);
				canvas.drawText(String.valueOf((int)(0.1*Math.pow(10, i+1))), 20+(i+1)*x_axis, ((Height-20))+20, paint);
			}
			break;
		case phase:
			y_num = (max_y_axis-min_y_axis)/phase_increase;//y�b���έӼ�
			y_axis = (Height-40)/y_num;
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(5);
			
			canvas.drawLine(20, 20, 20, Height-20, paint);
			canvas.drawLine(20, Height-20, Width-20, Height-20, paint);
			
			//�]�w�e�������u�C��
			paint.setColor(Color.GRAY);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			canvas.drawText(String.valueOf(min_y_axis), 0, ((Height-20)), paint);
			canvas.drawText(String.valueOf(0.1), 25, ((Height-20))+20, paint);
			
			paint.setColor(Color.GRAY);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			
			//�e�Xy�b �üФWy�b�����ƭ�
			for(int i=0;i<y_num;i++)
			{
				//canvas.drawLine(20, ((Height-20)/2)+(i+1)*y_axis, Width-20, ((Height-20)/2)+(i+1)*y_axis, paint);
				canvas.drawLine(20, ((Height-20))-(i+1)*y_axis, Width-20, ((Height-20))-(i+1)*y_axis, paint);
				//canvas.drawText(String.valueOf((i+1)*10), 0, ((Height-20)/2)-(i+1)*y_axis, paint);
				canvas.drawText(String.valueOf(min_y_axis+(i+1)*phase_increase), 0, ((Height-20))-(i+1)*y_axis, paint);
			}
			//�e�Xx�b �üФWx�b�����ƭ�
			for(int i=0;i<2;i++)
			{
				
				canvas.drawLine(20+(i+1)*x_axis, 20, 20+(i+1)*x_axis, Height-20, paint);
				canvas.drawText(String.valueOf((int)(0.1*Math.pow(10, i+1))), 20+(i+1)*x_axis, ((Height-20))+20, paint);
			}
			break;
		}
		
		
	}
	
	/*
	 * �N��Ƶe�ܮy�жb�W
	 * canvas - ��ø�s���e��
	 */
	private void DrawData(Canvas canvas)
	{
		if(db==null && phase==null)
			return;
		
		float x_axis = (Width-40)/2;	//�N���жb��b�����o�ӧO����
		float x_axis_less1 = (((float)20+x_axis)-20)/(float)less1Count;	//�p��s<1���j�j�p
		float x_axis_greater1 = (float) ((((float)20+(2.0*x_axis))-((float)20+(1.0*x_axis)))/greater1Count);
		//�p��s>1���j�j�p
		
		float[] prePoint = new float[2];	//�O���e�@���I [0]:x [1]:y
		//�]�wData���I
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(1);
		
		switch(this.type)
		{
		//�N���ø�s�W��
		case db:	//ø�s�W�q��
			
			//Log.d("debug", String.valueOf(x_axis_less1));
			//Log.d("debug",String.valueOf(x_axis_greater1));
			
			for(int i=0;i<s.length;i++)
			{
				//�p��y�b�C�@��׶��j
				float y_axis = ((float)(Height-40))/(max_y_axis-min_y_axis);
				//Log.d("debug",String.valueOf(y_axis));
				//Log.d("debug",String.valueOf(db[i]));
				
				//��s<1��
				if(s[i]<=1.0)
				{
					if(i==0)
					{
						//�Ĥ@���I�ɱNprePoint��l��
						prePoint[0] = 20+((0)*x_axis_less1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.db[999-0]-min_y_axis)*y_axis));
					}
					else
					{
						//�N�e�@�`�I�P��e�`�I�s�u
						canvas.drawLine(prePoint[0], prePoint[1], 20+((i)*x_axis_less1-1), ((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis)), paint);
						//�N�ثe�`�I���N�e�@�`�I
						prePoint[0] = 20+((i)*x_axis_less1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis));
					}
					//canvas.drawPoint(20+((i)*x_axis_less1-1),((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis)),paint);
					
				}
				else	//��s>1��
				{
					if(i==0)
					{
						//�Ĥ@���I�ɱNprePoint��l��
						prePoint[0] = 20+((i)*x_axis_greater1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis));
					}
					else
					{
						//�N�e�@�`�I�P��e�`�I�s�u
						canvas.drawLine(prePoint[0], prePoint[1], 20+((i)*x_axis_greater1-1), ((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis)), paint);
						//�N�ثe�`�I���N�e�@�`�I
						prePoint[0] = 20+((i)*x_axis_greater1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis));
					}
					//canvas.drawPoint(20+((i)*x_axis_greater1-1),((float)(Height-20))-((float)((this.db[999-i]-min_y_axis)*y_axis)),paint);
					
				}
			}
			break;
		case phase:	//�ۦ��
			
			//Log.d("debug", String.valueOf(x_axis_less1));
			//Log.d("debug",String.valueOf(x_axis_greater1));
			for(int i=0;i<s.length;i++)
			{
				//�p��y�b�C�@��׶��j
				float y_axis = ((float)(Height-40))/(max_y_axis-min_y_axis);
				
				//Log.d("debug",String.valueOf(phase[999-i]));
				//��s<1��
				if(s[i]<=1.0)
				{
					if(i==0)
					{
						//�Ĥ@���I�ɱNprePoint��l��
						prePoint[0] = 20+((i)*x_axis_less1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis));
					}
					else
					{
						//�N�e�@�`�I�P��e�`�I�s�u
						canvas.drawLine(prePoint[0], prePoint[1], 20+((i)*x_axis_less1-1), ((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis)), paint);
						//�N�ثe�`�I���N�e�@�`�I
						prePoint[0] = 20+((i)*x_axis_less1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis));
						
					}
					//canvas.drawPoint(20+((i)*x_axis_less1-1),((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis)),paint);
					
				}
				//��s>1��
				else
				{
					if(i==0)
					{
						//�Ĥ@���I�ɱNprePoint��l��
						prePoint[0] = 20+((i)*x_axis_greater1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis));
					}
					else
					{
						//�N�e�@�`�I�P��e�`�I�s�u
						canvas.drawLine(prePoint[0], prePoint[1], 20+((i)*x_axis_less1-1), ((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis)), paint);
						//�N�ثe�`�I���N�e�@�`�I
						prePoint[0] = 20+((i)*x_axis_less1-1);
						prePoint[1] = ((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis));
					}
					//canvas.drawPoint(20+((i)*x_axis_greater1-1),((float)(Height-20))-((float)((this.phase[999-i]-min_y_axis)*y_axis)),paint);
					
				}
			}
			break;
		}
		
	}
	
	/*
	 * �N��ƶǤJ ��K����ø��
	 * s - s�����ƭ�
	 * data - ����s�������ƭ�
	 */
	public void setData(double[]s ,double[] data)
	{
		int[] tmp;
		this.s = s;
		less1Count = 0;
		greater1Count = 0;
		/*
		 * �έps<1��s>1���`�I��
		 */
		for(int i=0;i<s.length;i++)
		{
			if(s[i]<1.0)
				less1Count++;
		}
		for(int i=0;i<s.length;i++)
		{
			if(s[i]>1.0)
				greater1Count++;
		}
		/*
		 * �P�_�ǤJ������ݩ��������
		 * �è̷Ө������N�Ϫ�l��
		 */
		switch(this.type)
		{
		case db:
			this.db = data;
			tmp = getAxis_Value(this.db);
			min_y_axis = tmp[0];
			max_y_axis = tmp[1];
			break;
		case phase:
			this.phase = data;
			tmp = getAxis_Value(this.phase);
			min_y_axis = tmp[0];
			max_y_axis = tmp[1];
			break;
		}
		this.invalidate();
	}
	
	/*
	 * ���o�ݳ]�w���y�ФW�U�� 
	 * data - �����R���T�����
	 * return [0]�̤p�� [1]�̤j��
	 */
	private int[] getAxis_Value(double[] data)
	{
		//��X�ƾڳ̤j�̤p��
		double[] tmp = new double[2];
		tmp[0] = data[0];
		tmp[1] = data[0];
		for(int i=1;i<data.length;i++)
		{
			if(tmp[0]>data[i])
				tmp[0] = data[i];
			if(tmp[1]<data[i])
				tmp[1] = data[i];
		}
		
		//�w�q���жb�d��
		int[] res = new int[2];
		if(tmp[0]>=0)
		{
			switch(this.type)
			{
			case db:
				res[0] = ((int)(tmp[0]/db_increase)*db_increase);
				break;
			case phase:
				res[0] = -180;
				break;
			}
		}
		else if(tmp[0]<0)
		{
			switch(this.type)
			{
			case db:
				res[0] = (((int)(tmp[0]/db_increase)-1)*db_increase);
				break;
			case phase:
				res[0] = -180;
				break;
			}
		}
		
		if(tmp[1]>=0)
		{
			switch(this.type)
			{
			case db:
				res[1] = ((((int)(tmp[1]/db_increase))+1)*db_increase);
				break;
			case phase:
				res[1] = 180;
				break;
			}
		}
		else if(tmp[1]<0)
		{
			switch(this.type)
			{
			case db:
				res[1] = (((int)(tmp[1]/db_increase))*db_increase);
				break;
			case phase:
				res[1] = 180;
				break;
			}
		}
		return res;
	}
	
}
