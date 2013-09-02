package ntou.hearingaid.sound;

import android.media.AudioFormat;
import android.media.AudioTrack;
/*
 * ���J���ο�X�������T�ѼƳ]�w
 */

public class SoundParameter {
	public static int frequency = 16000;	//44100 SDK �ϥ��Ū� �����W�v �u�ର8000
	public static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	public static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static final int PureTonefrequency = 16000;
	public static final int Speakerfrequency = 16000;
	public static int bufferSize = AudioTrack.getMinBufferSize(SoundParameter.frequency, SoundParameter.channelConfiguration, SoundParameter.audioEncoding);;
}
