package ntou.hearingaid.hearingaid;


import ntou.hearingaid.customerview.BodePlotGraph;
import ntou.hearingaid.customerview.BodePlotGraph.BodePlotType;
import ntou.hearingaid.dsp.BodePlotGeneration;
import ntou.hearingaid.parameter.Parameter;
import ntou.hearingaid.sound.SoundControl;
import ntou.hearingaid.sound.SoundParameter;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/*
 * �D�e��
 */

public class MainActivity extends Activity {

	//Button�����ܼ�
	private Button startButton;
	private Button stopButton;
	private Button settingButton;
	private Button upVolumeButton;
	private Button downVolumeButton;
	private Button realearButton;
	private Button testButton;
	//���T�޲z��
	private AudioManager audioManager;
	private SoundControl soundControl;
	
	private HeadsetPlugReciver headsetPlugReciver;
	//�]�w��
	public static SharedPreferences setting;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting = getSharedPreferences(Parameter.PreferencesStr, 0);
        Parameter.pref = setting;
        headsetPlugReciver = new HeadsetPlugReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction("android.bluetooth.headset.action.STATE_CHANGED");
        registerReceiver(headsetPlugReciver, intentFilter);
        
        
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        soundControl = SoundControl.getSoundControl(audioManager);
        
        
        if(!soundControl.CheckSoundDeviceState())
        {
        	Toast.makeText(MainActivity.this, "���ˬd�O�_�s���վ��Ψ㦳A2DP�\���Ūަվ�", 5).show();
        	this.finish();
        	
        }
        
        //���ӥiŪ���]�w�ɫ� �ӳ]�w�w�]�Ҧ�
       
        if(setting.contains("AudioSource"))
        {
        	if(setting.getString("AudioSource", "-1").equals("0"))
        	{
	        	audioManager.setMode(AudioManager.MODE_NORMAL);
	        	//audioManager.setBluetoothA2dpOn(true);
	        	if(soundControl.setMicInput(false))
	        	{
	        		SoundParameter.frequency = 8000;
	        		Toast.makeText(MainActivity.this, "�Ū޳��J����J�˸m�]�w���\!", 5).show();
	        	}
	        	else
	        	{
	        		SoundParameter.frequency = 16000;
	        		Toast.makeText(MainActivity.this, "��אּ���س��J����J�˸m�]�w���\!", 5).show();
	        	}
        	}
        	else
        	{
        		audioManager.setMode(AudioManager.MODE_NORMAL);
	        	//audioManager.setBluetoothA2dpOn(true);
	        	if(soundControl.setMicInput(true))
	        	{
	        		SoundParameter.frequency = 16000;
	        		Toast.makeText(MainActivity.this, "���س��J����J�˸m�]�w���\!", 5).show();
	        	}
        	}
        }
        else
        {
        	Intent intent = new Intent();
			intent.setClass(MainActivity.this, PrefSetting.class);
			//intent.setClass(MainActivity.this, SettingParameter.class);
			startActivity(intent);
			Toast.makeText(MainActivity.this, "�Ĥ@���Ұʽжi�����ҳ]�w", 5).show();
        	/*audioManager.setMode(AudioManager.MODE_NORMAL);
        	//audioManager.setBluetoothA2dpOn(true);
        	if(soundControl.setMicInput(true))
        	{
        		Toast.makeText(MainActivity.this, "�w�]��J�˸m�]�w���\!", 5).show();
        	}*/
        }
        
        
        
        //�N�Ҧ�������Button�ܼ�
        startButton = (Button)findViewById(R.id.StartService);
        stopButton = (Button)findViewById(R.id.StopService);
        settingButton = (Button)findViewById(R.id.SettingButton);
        upVolumeButton = (Button)findViewById(R.id.upVolume);
        downVolumeButton = (Button)findViewById(R.id.downVolume);
        realearButton = (Button)findViewById(R.id.RealEarTestButton);
        testButton = (Button)findViewById(R.id.testButton);
        
        if(HearingAidService.isService)
        {
        	//��A�ȱҰʮ� �]�w �C��Button ���B�󪺪��A
        	startButton.setEnabled(false);
        	stopButton.setEnabled(true);
        	settingButton.setEnabled(false);
        }
        else
        {
        	//��A�������� �]�w �C��Button ���B�󪺪��A
        	startButton.setEnabled(true);
        	stopButton.setEnabled(false);
        	settingButton.setEnabled(true);
        }
        
        /*if(isServiceRunning())
        {
        	//��A�ȱҰʮ� �]�w �C��Button ���B�󪺪��A
        	startButton.setEnabled(false);
        	stopButton.setEnabled(true);
        	settingButton.setEnabled(false);
        }
        else
        {
        	//��A�������� �]�w �C��Button ���B�󪺪��A
        	startButton.setEnabled(true);
        	stopButton.setEnabled(false);
        	settingButton.setEnabled(true);
        }*/
        
        //�]�w�UButton������Ĳ�o�ƥ�
        startButton.setOnClickListener(new OnClick()); 
        stopButton.setOnClickListener(new OnClick()); 
        settingButton.setOnClickListener(new OnClick());
        upVolumeButton.setOnClickListener(new OnClick());
        downVolumeButton.setOnClickListener(new OnClick());
        realearButton.setOnClickListener(new OnClick());
        testButton.setOnClickListener(new OnClick());
		//Toast.makeText(MainActivity.this, Build.VERSION.RELEASE, 5).show();
    }
    
    //Button Ĳ�o�ƥ�갵
    private class OnClick implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.StartService)
			{
				setting = getSharedPreferences(Parameter.PreferencesStr, 0);
				startButton.setEnabled(false);
		        stopButton.setEnabled(true);
		        settingButton.setEnabled(false);
		        
		        startService(new Intent("ntou.hearingaid.hearingaid.START_HearingAid"));
			}
			else if(v.getId()==R.id.StopService)
			{
				startButton.setEnabled(true);
		        stopButton.setEnabled(false);
		        settingButton.setEnabled(true);
		       
		        stopService(new Intent("ntou.hearingaid.hearingaid.START_HearingAid"));
		        
		        //soundControl.closeSoundControl();
			}
			else if(v.getId()==R.id.SettingButton)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PrefSetting.class);
				//intent.setClass(MainActivity.this, SettingParameter.class);
				startActivity(intent);
			}
			else if(v.getId()==R.id.upVolume)
			{
				audioManager.adjustVolume(AudioManager.ADJUST_RAISE, 0);
			}
			else if(v.getId()==R.id.downVolume)
			{
				audioManager.adjustVolume(AudioManager.ADJUST_LOWER, 0);
			}
			else if(v.getId()==R.id.RealEarTestButton)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PureToneTest.class);
				//intent.setClass(MainActivity.this, SettingParameter.class);
				startActivity(intent);
			}
			else if(v.getId()==R.id.testButton)
			{
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BodePlotActivity.class);
				startActivity(intent);
			}
		}
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(headsetPlugReciver);
	}
    
	//�P�_�A�Ȫ��A
	/*private boolean isServiceRunning()
	{
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
			if ("ntou.hearingaid.hearingaid.START_HearingAid".equals(service.service.getClassName())) 
			{
	            return true;
	        }
		}
		return false;
	}*/
}
