package ntou.hearingaid.dsp;

import java.math.BigDecimal;

import android.util.Log;
import ntou.hearingaid.dsp.IIRFilter.Complex;

/*
 * �z�L�����O�p��X�o�i�� �t���W�v�T�� �W�q�Ϥάۦ��
 * ���������ݭק�!!!
 * �z�L�O�Hexcel�B���g�Ӧ�
 */

public class BodePlotGeneration {

	private double minRad;
	private double maxRad;
	private Complex[] s = new Complex[1000];
	private Complex[] Num = new Complex[1000];
	private Complex[] Den = new Complex[1000];
	private double[] db = new double[1000];
	private double[] phase = new double[1000];
	
	/*
	 * ���os<1�Ӽ�
	 * return �Ӽ�
	 */
	public int get_s_less1()
	{
		int count = 0;
		for(int i=0;i<s.length;i++)
		{
			if(s[i].im()<1.0)
				count++;
		}
		return count;
	}
	/*
	 * ���os>1�Ӽ�
	 * return �Ӽ�
	 */
	public int get_s_greater1()
	{
		int count = 0;
		for(int i=0;i<s.length;i++)
		{
			if(s[i].im()>1.0)
				count++;
		}
		return count;
	}
	/*
	 * ���os �ƭ�
	 * return s �}�C
	 */
	public double[] get_s()
	{
		double[] s = new double[this.s.length];
		for(int i=0;i<this.s.length;i++)
			s[i] = this.s[i].im();
		return s;
	}
	
	/*
	 * ���o�W�q���v��
	 * return �W�q�Ȱ}�C
	 */
	public double[] get_db()
	{
		return this.db;
	}
	/*
	 * ���o�ۦ�ϭ�
	 * return �ۦ�Ȱ}�C
	 */
	public double[] get_phase()
	{
		return this.phase;
	}
	/*
	 * �غc�禡
	 * minRad �ݭn���̤p�۵M�W�v
	 * maxRad �ݭn���̤j�۵M�W�v
	 * Num �o�i���곡�Ѽ�
	 * Den �o�i���곡�Ѽ�
	 * �z�L�o�ǰѼƥ����O�|�۰ʭp����o�i�w��
	 */
	public BodePlotGeneration(double minRad, double maxRad, double[] Num, double[] Den)
	{
		this.minRad = minRad;
		this.maxRad = maxRad;
		
		for(int i=0; i<s.length;i++)
		{
			double tmp = Math.log10(this.minRad)+i*((Math.log10(this.maxRad)-Math.log10(this.minRad))/1000);
			s[i] = new Complex(0, Math.pow(10, tmp));
			
			//��l�� Num �P Den
			this.Num[i] = new Complex(0, 0);
			this.Den[i] = new Complex(0, 0);
		}
		for(int i=0; i<s.length;i++)
		{
			for(int j=0; j<Num.length;j++)
			{
				BigDecimal bd= new BigDecimal(Num[j]); 
				bd.setScale(4, BigDecimal.ROUND_HALF_UP);
				this.Num[i] = this.Num[i].plus((s[i].pow(j)).times(bd.doubleValue()));
				//this.Num[i] = this.Num[i].plus((s[i].pow(j)).times(Num[j]));
				
			}
			
			for(int j=0; j<Den.length;j++)
			{
				BigDecimal bd= new BigDecimal(Den[j]); 
				bd.setScale(4, BigDecimal.ROUND_HALF_UP);
				this.Den[i] = this.Den[i].plus((s[i].pow(j)).times(bd.doubleValue()));
				//this.Den[i] = this.Den[i].plus((s[i].pow(j)).times(Den[j]));
			}
			

			this.db[i] = 20*Math.log10((this.Num[i].divides(this.Den[i])).abs());
			Complex tmp = (this.Num[i].divides(this.Den[i]));
			this.phase[i] = - Math.toDegrees(Math.atan2(tmp.im(),tmp.re()));
			/*if(this.phase[i]>0)
			{
				this.phase[i] = this.phase[i];
			}*/
		}
		
	}
}
