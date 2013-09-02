package ntou.hearingaid.sound;

import java.sql.Time;
import java.util.ArrayList;

import ntou.hearingaid.parameter.Parameter;
import ntou.hearingaid.peformance.PerformanceParameter;

import android.graphics.AvoidXfermode;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/*
 * �t�d���_�����n��
 */

public class Microphone extends Thread {

	//�إ� ��ť�ƥ�
	public interface OnMicrophoneListener
	{
		public void OnRec(short[] data);
	}
	
	private OnMicrophoneListener onMicrophoneListener;
	
	public void setOnMicrophoneListener(OnMicrophoneListener _onMicrophoneListener)
	{
		onMicrophoneListener = _onMicrophoneListener;
	}
		
	private int recBufSize;
	private AudioRecord audioRecord;	//�������O
	private boolean isRecording = false;
	
	public Microphone()
	{
		//��l�Ƴ��J�������]�w
		recBufSize = AudioRecord.getMinBufferSize(SoundParameter.frequency,
				SoundParameter.channelConfiguration, SoundParameter.audioEncoding);
		
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SoundParameter.frequency,
				SoundParameter.channelConfiguration, SoundParameter.audioEncoding, recBufSize);
		
		
	}
	
	public void open()
	{
		isRecording = true;
		this.start();
	}
	
	public void close()
	{
		isRecording = false;
		this.interrupt();
		
		
		//this.stop();
	}
	
	public void run()
	{
		
		
		try {
			short[] buffer = new short[recBufSize];
			audioRecord.startRecording();
			PerformanceParameter.MicTime = new ArrayList<Long>();
			PerformanceParameter.recvTime = new ArrayList<Long>();
			PerformanceParameter.avg_MicTime = 0;
			
			//�}�l���_�����n��
			while (isRecording) {
				/*synchronized (PerformanceParameter.MicTime) 
				{
					PerformanceParameter.MicTime.add(System.currentTimeMillis());
				}
				*/
				//�T��Ū��
				int bufferReadResult = audioRecord.read(buffer, 0,
						recBufSize);
				/*
				synchronized (PerformanceParameter.recvTime) 
				{
					PerformanceParameter.recvTime.add(System.currentTimeMillis());
				}
				*/
				//�P�_�O�_���o����
				if(bufferReadResult>0)
				{
					
					short[] tmpBuf = new short[bufferReadResult];
					System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
					onMicrophoneListener.OnRec(tmpBuf);	//�NŪ�����ưe�ܤU�@�h
					/*synchronized (PerformanceParameter.MicTime) 
					{
						long MicTime = System.currentTimeMillis()-PerformanceParameter.MicTime.get(0);
						//Log.d("debug", "���J����������:"+String.valueOf(MicTime));
						PerformanceParameter.MicTime.remove(0);
						if(PerformanceParameter.avg_MicTime ==0)
							PerformanceParameter.avg_MicTime = MicTime;
						else
							PerformanceParameter.avg_MicTime = (PerformanceParameter.avg_MicTime + MicTime)/2;
						Log.d("debug", "�������J����������:"+String.valueOf(PerformanceParameter.avg_MicTime));
					}*/
					
				}
				/*
				short[] tmpBuf = new short[bufferReadResult];
				System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
				PerformanceParameter.recvTime.add(System.currentTimeMillis());	//�O�������ʥ]�ɶ�
				onMicrophoneListener.OnRec(tmpBuf);
				time2 = System.currentTimeMillis();
				if(Parameter.Mictime==0)
					Parameter.Mictime = time2-time1;
				else
				{
					Parameter.Mictime=(Parameter.Mictime+(time2-time1))/2;
				}*/	//���ʦܤW�� �קK�����n�ʥ] �� �p��į��
				
			}
			audioRecord.stop();
			PerformanceParameter.recvTime.clear();
			PerformanceParameter.MicTime.clear();
		} catch (Throwable t) {
			
		}
		
	}
}
