package ntou.hearingaid.dsp;

import java.lang.ref.Reference;
import java.sql.Ref;
import java.util.ArrayList;

import android.app.Service;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import ntou.hearingaid.dsp.Gain.OnGainListener;
import ntou.hearingaid.dsp.IIRFilter.IIR;
import ntou.hearingaid.hearingaid.FilterBankSetting;
import ntou.hearingaid.hearingaid.HearingAidService;
import ntou.hearingaid.hearingaid.MainActivity;
import ntou.hearingaid.hearingaid.PrefSetting;
import ntou.hearingaid.parameter.Parameter;
import ntou.hearingaid.peformance.PerformanceParameter;
import ntou.hearingaid.sound.SoundParameter;
/*
 * ���T�B�z�Ҳ�- �o�i����
 * �i���W�a���� �� �N��J�T���i���o�i�B�z
 */
public class FilterBank extends Thread {
	
	private boolean isRunning = false;	//�P�_������O�_����
	private ArrayList<short[]> Signals = new ArrayList<short[]>();	//�O�����B�z�T��
	public static int filterorder = 3 ;	//�]�w�o�i������ ���ƷU���U��
	/*
	 * ���عw�]�W�a����
	 */
	//private IIR iir_band1 = new IIR(1);
	private IIR iir_band1 = new IIR(FilterBank.filterorder, 143.0, 280.0);
	//private IIR iir_band2 = new IIR(2);
	private IIR iir_band2 = new IIR(FilterBank.filterorder, 281.0, 561.0);
	//private IIR iir_band3 = new IIR(3);
	private IIR iir_band3 = new IIR(FilterBank.filterorder, 561.0, 1120.0);
	//private IIR iir_band4 = new IIR(4);
	private IIR iir_band4 = new IIR(FilterBank.filterorder, 1110.0, 2240.0);
	//private IIR iir_band5 = new IIR(5);
	private IIR iir_band5 = new IIR(FilterBank.filterorder, 2230.0, 3540.0);
	
	//�ʺA�W�a����
	private IIR[] iir_bands = null;
	/*
	 * �ʺA�W�q���v ���k��
	 */
	private Gain2[] gain40;
	private Gain2[] gain60;
	private Gain2[] gain80;
	private Gain2[] gain40R;
	private Gain2[] gain60R;
	private Gain2[] gain80R;
	//�Uť�����س]�w��
	private SharedPreferences setting;

	//private SharedPreferences setting;
	private int FilterBankNumber;	//�O���o�i���Ӽ�
	
	/*
	 * �غc�禡
	 * setting - ���س]�w��
	 */
	public FilterBank(SharedPreferences setting)
	{
		if(setting.contains("FilterBankNumber"))	//�P�_�O�_�]�tFilterBankNumber�]�w��
		{
			
			FilterBankNumber = setting.getInt("FilterBankNumber", -1);	//���X�ƭ�
			//Log.d("Debug", String.valueOf(FilterBankNumber));
			
			if(FilterBankNumber!=-1)
			{
				/*
				 * �ʺA���͹������o�i���μW�q���v�ܼƭӼ�
				 */
				iir_bands = new IIR[FilterBankNumber];
				gain40 = new Gain2[FilterBankNumber];
				gain60 = new Gain2[FilterBankNumber];
				gain80 = new Gain2[FilterBankNumber];
				gain40R = new Gain2[FilterBankNumber];
				gain60R = new Gain2[FilterBankNumber];
				gain80R = new Gain2[FilterBankNumber];
				for(int i = 0; i < FilterBankNumber ; i++)
				{
					String db40 = "Gain40db"+String.valueOf(i+1);
					String db60 = "Gain60db"+String.valueOf(i+1);
					String db80 = "Gain80db"+String.valueOf(i+1);
					String db40R = "Gain40db"+String.valueOf(i+1)+"R";
					String db60R = "Gain60db"+String.valueOf(i+1)+"R";
					String db80R = "Gain80db"+String.valueOf(i+1)+"R";
					
					if(setting.contains(db40)&&setting.contains(db60)&&setting.contains(db80))
					{
						/*
						 * �p�W�q���v�����]�w�s�b
						 * �h�N���l��
						 */
						double value40 = setting.getInt(db40, 0);
						double value60 = setting.getInt(db60, 0);
						double value80 = setting.getInt(db80, 0);
						double value40R = setting.getInt(db40R, 0);
						double value60R = setting.getInt(db60R, 0);
						double value80R = setting.getInt(db80R, 0);
						
						gain40[i] = new Gain2(value40);
						gain60[i] = new Gain2(value60);
						gain80[i] = new Gain2(value80);
						gain40R[i] = new Gain2(value40R);
						gain60R[i] = new Gain2(value60R);
						gain80R[i] = new Gain2(value80R);
					}
					else
					{
						/*
						 * �p�W�q���v�����]�w���s�b
						 * �h�N���l�Ƭ�0
						 */
						gain40[i] = new Gain2(0);
						gain60[i] = new Gain2(0);
						gain80[i] = new Gain2(0);
						gain40R[i] = new Gain2(0);
						gain60R[i] = new Gain2(0);
						gain80R[i] = new Gain2(0);
						
					}
					//Ū���o�i�����C�W�a��
					String band_Low_str = "FilterLow" + String.valueOf((i+1));
					String band_Hi_str = "FilterHi" + String.valueOf((i+1));
					//�N�W�a��l��
					iir_bands[i] = new IIR(FilterBank.filterorder,(double)setting.getInt(band_Low_str, 1),(double)setting.getInt(band_Hi_str, 3999));
				}
				
			}
		}
	}
	//�\��P�W
	public FilterBank()
	{
		//setting = service.getSharedPreferences(Parameter.PreferencesStr, 0);
		//setting = MainActivity.setting;
		setting = Parameter.pref;
		if(setting == null) return;
		if(setting.contains("FilterBankNumber"))
		{
			
			FilterBankNumber = setting.getInt("FilterBankNumber", -1);
			//Log.d("Debug", String.valueOf(FilterBankNumber));
			if(FilterBankNumber!=-1)
			{
				iir_bands = new IIR[FilterBankNumber];
				gain40 = new Gain2[FilterBankNumber];
				gain60 = new Gain2[FilterBankNumber];
				gain80 = new Gain2[FilterBankNumber];
				gain40R = new Gain2[FilterBankNumber];
				gain60R = new Gain2[FilterBankNumber];
				gain80R = new Gain2[FilterBankNumber];
				for(int i = 0; i < FilterBankNumber ; i++)
				{
					String db40 = "Gain40db"+String.valueOf(i+1);
					String db60 = "Gain60db"+String.valueOf(i+1);
					String db80 = "Gain80db"+String.valueOf(i+1);
					String db40R = "Gain40db"+String.valueOf(i+1)+"R";
					String db60R = "Gain60db"+String.valueOf(i+1)+"R";
					String db80R = "Gain80db"+String.valueOf(i+1)+"R";
					
					if(setting.contains(db40)&&setting.contains(db60)&&setting.contains(db80))
					{
						double value40 = setting.getInt(db40, 0);
						double value60 = setting.getInt(db60, 0);
						double value80 = setting.getInt(db80, 0);
						double value40R = setting.getInt(db40R, 0);
						double value60R = setting.getInt(db60R, 0);
						double value80R = setting.getInt(db80R, 0);
						
						gain40[i] = new Gain2(value40);
						gain60[i] = new Gain2(value60);
						gain80[i] = new Gain2(value80);
						gain40R[i] = new Gain2(value40R);
						gain60R[i] = new Gain2(value60R);
						gain80R[i] = new Gain2(value80R);
					}
					else
					{
						gain40[i] = new Gain2(0);
						gain60[i] = new Gain2(0);
						gain80[i] = new Gain2(0);
						gain40R[i] = new Gain2(0);
						gain60R[i] = new Gain2(0);
						gain80R[i] = new Gain2(0);
						
					}
					String band_Low_str = "FilterLow" + String.valueOf((i+1));
					String band_Hi_str = "FilterHi" + String.valueOf((i+1));
					iir_bands[i] = new IIR(FilterBank.filterorder,(double)setting.getInt(band_Low_str, 1),(double)setting.getInt(band_Hi_str, 3999));
				}
				
			}
		}
		
	}
	
	//����� �i���o�i�B�z
	public void run()
	{
		Signals = new ArrayList<short[]>();
		PerformanceParameter.FilterTime = new ArrayList<Long>();
		PerformanceParameter.avg_FilterTime = 0;
		
		while(isRunning)
		{
			//Log.e("debug", "FilterBank Process Time:"+String.valueOf(time1));
			/*
			 * Ū���̦����@�����
			 */
			short[] buff = null;
			yield();	//�קK���{�Ǥ@���d��X�� �y���L�k�s�W�T��
			synchronized (Signals) {
				if(Signals.size()==0)
					continue;
				buff = Signals.get(0);
				Signals.remove(0);
				
			}
			
			short[] tmp = new short[buff.length];	//�N��T���Ȧs�_��! ���ݹB�� ����
			short[] tmpR = new short[buff.length];	//�N��T���Ȧs�_��! ���ݹB�� �k��
			System.arraycopy(buff, 0, tmp, 0, buff.length);
			
			
			if(iir_bands!=null)	//�p�G�ʺA�W�a���Φ���l��
			{
				short[][] tmp_bands;
				short[][] tmp_bandsL;
				short[][] tmp_bandsR;
				tmp_bands = new short[iir_bands.length][];	//��T��
				tmp_bandsL = new short[iir_bands.length][];	//���ռƭ�
				tmp_bandsR = new short[iir_bands.length][];	//�k�ռƭ�
				for(int i=0;i<iir_bands.length;i++)
				{
					tmp_bands[i] = iir_bands[i].process(tmp.clone());	//�N��T���i��B�z
					int db = Calculatedb(tmp_bands[i]);	//�p�⭵�q
					tmp_bandsL[i] = AutoGain(i, tmp_bands[i].clone(), db, 0);	//�̷Ӥ��P���q�Υ��k�յ��P���P�W�q�B�z 0����
					tmp_bandsR[i] = AutoGain(i, tmp_bands[i].clone(), db, 1);	//�̷Ӥ��P���q�Υ��k�յ��P���P�W�q�B�z 1�k��
				}
				
				/*
				 * ���k�դ��O�[�`
				 * ��U�W�a�B�z�L���T���զX����l�T��
				 */
				for(int j=0;j<tmp.length;j++)
				{
					short sumL = 0;
					short sumR = 0;
					for(int i=0;i<iir_bands.length;i++)
					{
						sumL += tmp_bandsL[i][j];
						sumR += tmp_bandsR[i][j];
					}
					tmp[j] = sumL;
					tmpR[j] = sumR;
					//Log.d("debug", String.valueOf(tmp[j]));
				}
				
				
			}
			else	//�p�G�ʺA�W�a���Υ���l��
			{
				//�ϥιw�]�W�a�B�z
				int db;
				short[] tmp1 = iir_band1.process(tmp.clone());
				db = Calculatedb(tmp1);
				tmp1 = AutoGain(0 ,tmp1, db, 0);
				short[] tmp2 = iir_band2.process(tmp.clone());
				db = Calculatedb(tmp2);
				tmp2 = AutoGain(1, tmp2, db, 0);
				short[] tmp3 = iir_band3.process(tmp.clone());
				db = Calculatedb(tmp3);
				tmp3 = AutoGain(2, tmp3, db, 0);
				short[] tmp4 = iir_band4.process(tmp.clone());
				db = Calculatedb(tmp4);
				tmp4 = AutoGain(3, tmp4, db, 0);
				short[] tmp5 = iir_band5.process(tmp.clone());
				db = Calculatedb(tmp5);
				tmp5 = AutoGain(4, tmp5, db,0);
				/*
				 * ��U�W�a�B�z�L���T���զX����l�T��
				 */
				for(int i=0;i<tmp.length;i++)
				{
					//tmp[i] = (short)(tmp1[i] * gain);
					//tmp[i] = tmp5[i];
					tmp[i] = (short) (tmp1[i]+tmp2[i]+tmp3[i]+tmp4[i]+tmp5[i]);
				}
			}
			
			//�P�_�ثe�����W�v �����P�_�ϥΥ��k�էUť�� �� ��էUť��
			if(SoundParameter.frequency==8000)
			{
				onFilterBankListener.OnSuccess(tmp);	//�Y����իh�����e�X
			}
			else
			{
				//�Y�����իh�ݱN�T�� LRLR��J�A�e�X
				short[] tmp_out = new short[tmp.length*2];
				for(int i=0;i<tmp_out.length/2;i++)
				{
						tmp_out[i*2] = tmp[i];
						tmp_out[i*2+1] = tmpR[i];
					
				}
				onFilterBankListener.OnSuccess(tmp_out);
			}
			/*for(int i=0;i<10000;i++)
			{
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			/*for(int i=0;i<tmp.length;i++)
			{
				//tmp[i] = (short)(tmp1[i] * gain);
				//tmp[i] = tmp5[i];
				tmp[i] = (short) (tmp1[i]+tmp2[i]+tmp3[i]+tmp4[i]+tmp5[i]);
			}*/	//updat by Hamish 2013/3/14 �W�[�ʺA�W�e�]�w �N�쥻�\���ܤW��
			
			//if(tmp !=null)
			//{
				/*synchronized(PerformanceParameter.FilterTime)
				{
					long FilterTime = System.currentTimeMillis()-PerformanceParameter.FilterTime.get(0);
					//Log.d("debug","FilterBank������X����:"+String.valueOf(FilterTime));
					PerformanceParameter.FilterTime.remove(0);
					if(PerformanceParameter.avg_FilterTime ==0)
						PerformanceParameter.avg_FilterTime = FilterTime;
					else
						PerformanceParameter.avg_FilterTime = (PerformanceParameter.avg_FilterTime + FilterTime)/2;
					Log.d("debug","����FilterBank������X����:"+String.valueOf(PerformanceParameter.avg_FilterTime));
				}*/
				//onFilterBankListener.OnSuccess(tmp);
			//}
			
		}
		Signals.clear();
		PerformanceParameter.FilterTime.clear();
	}
	
	//��ť�����O����
	public interface OnFilterBankListener
	{
		public void OnSuccess(short[] data);
	}
	
	private OnFilterBankListener onFilterBankListener;
	
	//�]�w��ť�ƥ�
	public void setOnFilterBankListener(OnFilterBankListener _onFilterBankListener)
	{
		onFilterBankListener = _onFilterBankListener;
	}
	
	/*
	 * �s�W�T��
	 * data - ���[�J���T��
	 */
	public void AddSignals(short[] data)
	{
		synchronized (Signals) {
			Signals.add(data);
			
		}
		/*synchronized(PerformanceParameter.FilterTime)
		{
			PerformanceParameter.FilterTime.add(System.currentTimeMillis());
		}*/
	}
	
	
	public void open()
	{
		
		isRunning = true;
		this.start();
	}
	
	
	public void close()
	{
		isRunning = false;
		this.interrupt();
		
		//this.stop();
	}
	//�ˬd�ثe�Ҧ�
	public boolean CheckMode()
	{
		if(iir_bands == null)
			return false;
		else
			return true;
	}
	
	/*
	 * �p�⭵�q
	 * data - ���p�⪺���
	 * return ���q
	 */
	private int Calculatedb(short[] data)
	{
		short min = data[0];
		double sum = 0;
		for(int i=0;i<data.length;i++)
		{
			//sum += data[i]*data[i];
			sum = sum+Math.pow(data[i],2);
		}
		/*for(int i=0;i<256;i++)
		{
			sum = sum+Math.pow(data[i],2);
		}*/
		//sum /=  (int) (Math.abs((int)(sum /(float)r)/10000) >> 1);
		sum = 10*Math.log10(sum/data.length);
		//Log.d("debug", String.valueOf(SoundParameter.bufferSize));
		//Log.d("debug", "vol"+String.valueOf(sum));
		
		return (int)sum;
	}
	/*
	 * �W�q���v�B��
	 * i - �W�a�s��
	 * data - �T���ƭ�
	 * db - �T�����q
	 * LorR - ���Υk
	 * return �B�z�����T��
	 */
	private short[] AutoGain(int i, short[] data, int db, int LorR)
	{
		//�P�_�ثe�����W�v �����P�_�ϥΥ��k�էUť�� �� ��էUť��
		if(SoundParameter.frequency==8000)
		{
			//�i���ռW�q�B�z
			short[] result = new short[data.length];
	
			if(db>=40 && db<60)
				result = gain40[i].process(data.clone());
			else if(db>=60&&db<80)
				result = gain60[i].process(data.clone());
			else if(db>=80)
				result = gain80[i].process(data.clone());
			else
				result = data.clone();
	
			return result;
		}
		else
		{
			//�i�����ռW�q�B�z
			short[] result = new short[data.length];
			short[] resultR = new short[data.length];
			
			if(db>=40 && db<60)
			{
				result = gain40[i].process(data.clone());
				resultR = gain40R[i].process(data.clone());
			}
			else if(db>=60&&db<80)
			{
				result = gain60[i].process(data.clone());
				resultR = gain60R[i].process(data.clone());
			}
			else if(db>=80)
			{
				result = gain80[i].process(data.clone());
				resultR = gain80R[i].process(data.clone());
			}
			else
			{
				result = data.clone();
				resultR = data.clone();
			}
			
	
			if(LorR==0)
				return result;
			else
				return resultR;
		}
	}
}
