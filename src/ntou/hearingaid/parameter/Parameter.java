package ntou.hearingaid.parameter;

import android.content.SharedPreferences;
import android.os.Build;
/*
 * APP�����Ѽ�
 */
public class Parameter {
	//���س]�w�ɦW��
	public static String PreferencesStr = "ntou.hearingaid.hearingaid_preferences";	//�ѼƳ]�w��
	//�n�骩��
	public static String SoftVersion = Build.VERSION.RELEASE;
	/*
	 * �į�p���
	 */
	public static long Mictime = 0;
	public static long FilterBanktime = 0;
	public static long SpeakerTime = 0;
	//���س]�w�����O
	public static SharedPreferences pref = null;
	
}
