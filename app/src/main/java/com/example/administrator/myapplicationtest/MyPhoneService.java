package com.example.administrator.myapplicationtest;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MyPhoneService extends Service {
    private String path;
    private boolean callRecoder = false;
    private String pathSelected;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        pathSelected = intent.getStringExtra("pathSelected");
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void getContactPeople(String incomingNumber) {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        // cursor里要放的字段名称
        String[] projection = new String[] { ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        // 用来电电话号码去找该联系人
        cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                new String[] { incomingNumber }, "");
        if (cursor.getCount() == 0) {
            Constant.numberToCall = incomingNumber;
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Constant.numberToCall = cursor.getString(1);
        }
    }

    private final class PhoneListener extends PhoneStateListener{
        private MediaRecorder mediaRecorder;
        private File file;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            try{
                switch(state){
                    case TelephonyManager.CALL_STATE_RINGING: // 来电
                        getContactPeople(incomingNumber);
                        callRecoder = true;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: // 接通电话
                        if (!callRecoder) {
                            getContactPeople(Constant.numberToCall);
                        }
                        CurrentTime();
                        if(pathSelected == null){
                            path = Environment.getExternalStorageDirectory() + "/recoding";
                        }else{
                            path = pathSelected + "/recoding";
                        }
                        File f = new File(path);
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                        if(pathSelected == null){
                            path = Environment.getExternalStorageDirectory() + "/recoding/" + Constant.numberToCall;
                        }else{
                            path = pathSelected + "/recoding/" + Constant.numberToCall;
                        }
                        File f1 = new File(path);
                        if (!f1.exists()) {
                            f1.mkdirs();
                        }
                        file = new File(path, Constant.fileName + ".3gp");
                        MediaRecoder();
                        break;
                    case TelephonyManager.CALL_STATE_IDLE: // 挂断电话
                        if (mediaRecorder != null) {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                        }
                        break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void CurrentTime(){
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // 获取当前年份
            int mMonth = c.get(Calendar.MONTH);// 获取当前月份
            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
            int mHour = c.get(Calendar.HOUR_OF_DAY);// 获取当前的小时数
            int mMinute = c.get(Calendar.MINUTE);// 获取当前的分钟数
            int mSecond = c.get(Calendar.SECOND);
            Constant.fileName =mYear + "年" + mMonth + "月" + mDay + "日" + mHour + "时" + mMinute + "分" + mSecond + "秒";
        }

        private void MediaRecoder(){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            try {
                mediaRecorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mediaRecorder.start();
        }

    }
}
