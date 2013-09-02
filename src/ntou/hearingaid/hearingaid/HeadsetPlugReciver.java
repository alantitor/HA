package ntou.hearingaid.hearingaid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/*
 * �t�d��ť��������U���s��
 * �p �վ����J�ް�
 * �q��call in��
 */

public class HeadsetPlugReciver extends BroadcastReceiver {

	public final int HeadsetPlugin = 2;
	public final int HeadsetUnplugin = 0;
	
	private static final int STATE_CONNECTED = 0x00000002; 
	private static final int STATE_DISCONNECTED  = 0x00000000;  
	private static final String EXTRA_STATE = "android.bluetooth.headset.extra.STATE";
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(arg1.getAction().equals("android.intent.action.HEADSET_PLUG"))
		{
			//�s���P�_�վ������ݥ��P�_�O�_�b�A�Ȫ��A �b�A�Ȫ��A�ɻݱNisPauseByHeadsetUnplug���� �H�Q���^�վ���ҥ�
			if(arg1.getIntExtra("state", -1)== HeadsetUnplugin && HearingAidService.isService)	
			{
				HearingAidService.isPauseByHeadsetUnplug = true;	//�����P�_�O�_���s�����_
				arg0.stopService(new Intent("ntou.hearingaid.hearingaid.START_HearingAid"));	//����A��
			}
			else
			{
				if(HearingAidService.isPauseByHeadsetUnplug)	//�p�G���s�����_�A�Ȯ� 
				{
					HearingAidService.isPauseByHeadsetUnplug = false;	//�N�䤤�_�^�_
					arg0.startService(new Intent("ntou.hearingaid.hearingaid.START_HearingAid"));	//���s�ҰʪA��
				}
			}
			//Log.d("DEBUG", String.valueOf(arg1.getIntExtra("state", 0)));
		}
		else if(arg1.getAction().equals("android.bluetooth.headset.action.STATE_CHANGED"))	//�P�_�Ūު��A�O�_����
		{
			
			if(arg1.getIntExtra(EXTRA_STATE, STATE_DISCONNECTED)== STATE_DISCONNECTED && HearingAidService.isService)	
			{
				HearingAidService.isPauseByHeadsetUnplug = true;	//�����P�_�O�_���s�����_
				arg0.stopService(new Intent("ntou.hearingaid.hearingaid.START_HearingAid"));	//����A��
			}
			else
			{
				if(HearingAidService.isPauseByHeadsetUnplug)	//�p�G���s�����_�A�Ȯ� 
				{
					HearingAidService.isPauseByHeadsetUnplug = false;	//�N�䤤�_�^�_
					arg0.startService(new Intent("ntou.hearingaid.hearingaid.START_HearingAid"));	//���s�ҰʪA��
				}
			}
		}
	}

}
