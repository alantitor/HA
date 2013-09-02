package ntou.hearingaid.backuprestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;

import ntou.hearingaid.hearingaid.MainActivity;
import ntou.hearingaid.parameter.Parameter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*
 *		���ѳ]�w�ɳƥ��٭��API
 */

public class SPBackupRestore {
	
	/*
	 * �N�]�w�ɳƥ�
	 * dst - ���x�s���ؼ��ɮ׳s����m
	 * return �O�_���\
	 */
	public boolean saveSharedPreferencesToFile(File dst) {
	    boolean res = false;
	    ObjectOutputStream output = null;
	    try {
	        output = new ObjectOutputStream(new FileOutputStream(dst));	//�إߤ@��X�ɮת���
	        SharedPreferences pref = MainActivity.setting;	//���o���س]�w��
	        output.writeObject(pref.getAll());	//�N�]�w�ɤ��e�����g�J�ɮ�

	        res = true;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally {
	        try {
	            if (output != null) {
	                output.flush();
	                output.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return res;
	}
	
	/*
	 * �N�]�w���٭�
	 * src - �٭��ɩҦb��m
	 * return �O�_���\
	 */
	@SuppressWarnings({ "unchecked" })
	public boolean loadSharedPreferencesFromFile(File src) {
	    boolean res = false;
	    ObjectInputStream input = null;
	    try {
	        input = new ObjectInputStream(new FileInputStream(src));	//�إ߿�J�ɮת���
	            Editor prefEdit = MainActivity.setting.edit();	//���o�]�w�ɦ�m
	            prefEdit.clear();	//�N����]�w�ɥ����M��
	            /*
	             * �N�ƥ��ɮפ�������key,valueŪ�X
	             * �A�v�@�g�^���س]�w��
	             */
	            Map<String, ?> entries = (Map<String, ?>) input.readObject();
	            for (Entry<String, ?> entry : entries.entrySet()) {
	                Object v = entry.getValue();
	                String key = entry.getKey();
	                //�̷Ӥ��P���A�g�J��
	                if (v instanceof Boolean)
	                    prefEdit.putBoolean(key, ((Boolean) v).booleanValue());
	                else if (v instanceof Float)
	                    prefEdit.putFloat(key, ((Float) v).floatValue());
	                else if (v instanceof Integer)
	                    prefEdit.putInt(key, ((Integer) v).intValue());
	                else if (v instanceof Long)
	                    prefEdit.putLong(key, ((Long) v).longValue());
	                else if (v instanceof String)
	                    prefEdit.putString(key, ((String) v));
	            }
	            
	            prefEdit.commit();	//�ɮ׼g�J������ݨϥ�commit�N���x�s
	        res = true;         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }finally {
	        try {
	            if (input != null) {
	                input.close();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	    return res;
	}

}
