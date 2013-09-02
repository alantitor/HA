package ntou.hearingaid.sound;


import android.annotation.TargetApi;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Build;

public class SoundControl extends Activity {
	private AudioManager audioManager = null;
	private boolean isMicInput = true;	//true:���ئվ���J false:�Ū޿�J
	public static SoundControl soundControl = null;
	//�O���_�l���A
	private boolean isWiredHeadsetOn = false;
	private boolean isBluetoothA2DPOn = false;
	
	private SoundControl(AudioManager _audioManager)
	{
		this.audioManager = _audioManager;
		//audioManager.setMode(AudioManager.MODE_IN_CALL);
		audioManager.setMode(AudioManager.MODE_NORMAL);
		
		isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
		isBluetoothA2DPOn = audioManager.isBluetoothA2dpOn();
		
	}
	
	public static SoundControl getSoundControl(AudioManager _audioManager)
	{
		if(SoundControl.soundControl==null)
		{
			soundControl = new SoundControl(_audioManager);
		}
		return soundControl;
	}
	
	public static SoundControl getSoundControl()
	{
		return soundControl;
	}
	
	/*
	 * �ˬd�O�_�ŦX�n��ݨD
	 * return ���o�վ����Ť��վ����A
	 */
	public boolean CheckSoundDeviceState()
	{
		//�ˬd�վ����A
		if(audioManager.isWiredHeadsetOn())
		{
			return true;
		}
		
		//�ˬd�Ūު��A
		if(audioManager.isBluetoothA2dpOn())
		{
			return true;
		}
		
		return false;
	}


	/*
	 * �]�w��J�˸m
	 * state - �]�w��J�ӷ� true���س��J�� false�Ť����J��
	 * return �]�w�O�_���\
	 */
	public boolean setMicInput(boolean state)
	{
		if(state)
		{
			//�ϥγ��J����J
			isMicInput = true;
			audioManager.setBluetoothScoOn(false);
			audioManager.stopBluetoothSco();
			return true;
		}
		else
		{
			//�ϥ��Ū޳��J��
			//if(audioManager.isBluetoothA2dpOn())
			if(audioManager.isBluetoothA2dpOn())
			{
				//�P�_A2DP�O�_�ҥ�
				isMicInput = false;
				audioManager.setBluetoothScoOn(true);
				audioManager.startBluetoothSco();
				return true;
			}
		}
		return false;
	}
	//���o�ثe��J�ӷ�
	public boolean getMicInput()
	{
		return isMicInput;
	}
	
	public void closeSoundControl()
	{
		audioManager.setBluetoothA2dpOn(isBluetoothA2DPOn);
		audioManager.setWiredHeadsetOn(isWiredHeadsetOn);
		audioManager.setBluetoothScoOn(false);
		audioManager.stopBluetoothSco();
	}
}
