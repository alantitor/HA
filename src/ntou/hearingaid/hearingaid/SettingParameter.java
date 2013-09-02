package ntou.hearingaid.hearingaid;

import ntou.hearingaid.sound.SoundControl;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RadioButton;

/*
 * �w�L�ϥ�
 * �w���PrefSetting�B�z
 */

public class SettingParameter extends Activity {
	
	private RadioButton bluetoothMic;
	private RadioButton Mic;
	private SoundControl control = SoundControl.getSoundControl();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_parameter);
        
        bluetoothMic = (RadioButton)findViewById(R.id.bluetoothMic);
        Mic = (RadioButton)findViewById(R.id.Mic);
        
        
        
    }

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(bluetoothMic.isChecked())
		{
			control.setMicInput(false);
		}
		else if(Mic.isChecked())
		{
			control.setMicInput(true);
		}
		super.finish();
	}
}
