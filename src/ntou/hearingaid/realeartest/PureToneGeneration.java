package ntou.hearingaid.realeartest;

import android.util.Log;

/*
 * �­�����
 */

public class PureToneGeneration {
	private int SampleRate;
	private double db;
	/*
	 * �غc�禡
	 * SampleRate �����W�v
	 */
	public PureToneGeneration(int SampleRate)
	{
		this.SampleRate = SampleRate;
	}
	
	/*
	 * ���ͯ­�
	 * freqence - �һ��W�a
	 * second - ���
	 * db - ���q
	 * return ���ͪ��T��
	 */
	public short[] GeneratePureTone(int freqence, int second, double db)
	{
		this.db = Math.pow(10, db/20);	//�p��۹ﭵ�q
		//����sin�i��
		short[] sin = new short[second * SampleRate];
		double samplingInterval = (double) (SampleRate / freqence);
		for (int i = 0; i < sin.length; i++) {
            double angle = (2.0 * Math.PI * i) / samplingInterval;
            //Log.d("pure", Double.toString((Math.sin(angle) * this.db)));
            //sin[i] = (short) (Math.sin(angle) * Volume * Short.MAX_VALUE);
            
            int tmp = (int)(Math.sin(angle) * this.db);
            
            //�קKoverflow
            if(tmp > Short.MAX_VALUE)
            	sin[i] = Short.MAX_VALUE;
            else if (tmp<Short.MIN_VALUE)
            	sin[i] = Short.MIN_VALUE;
            else
            	sin[i] = (short) (Math.sin(angle) * this.db);
            //Log.d("pure", Integer.toString((int) (Math.sin(angle) * this.db)));
            
        }
		return sin;
	}
}
