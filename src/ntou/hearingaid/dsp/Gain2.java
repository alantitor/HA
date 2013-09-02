package ntou.hearingaid.dsp;

/*
 * �W�q�B�z
 */

public class Gain2 {

	private double _gain = 0.0;
	/*
	 * �غc�禡
	 * gain - �]�w�W�q��
	 */
	public Gain2(double gain)
	{
		_gain = Math.pow(10, gain/20);
		//System.out.print(_gain);
	}
	
	/*
	 * �i��W�q�p��
	 * data - ���B�z�����
	 * return �B�z�ᵲ�G
	 */
	public short[] process(short[] data)
	{
		for(int i=0;i<data.length;i++)
		{
			int tmp = data[i];
			tmp = (int) (tmp * _gain);
			//�קKoverflow
			if(tmp>Short.MAX_VALUE)
				data[i] = Short.MAX_VALUE;
			else if(tmp<Short.MIN_VALUE)
				data[i] = Short.MIN_VALUE;
			else
				data[i] = (short)tmp;
		}
		return data;
	}
}
