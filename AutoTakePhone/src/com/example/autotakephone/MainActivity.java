package com.example.autotakephone;

import java.lang.reflect.Method;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private TelephonyManager telMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button autoTakephone = (Button) findViewById(R.id.autoTakephone);

		autoTakephone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Class<TelephonyManager> c = TelephonyManager.class;
				Method getITelephonyMethod = null;
				try {
					getITelephonyMethod = c.getDeclaredMethod("getITelephony",(Class[]) null);
					getITelephonyMethod.setAccessible(true);
				} catch (SecurityException e) {
				} catch (NoSuchMethodException e) {
				}

				try {

					telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					telMgr.listen(new CallStateListener(),CallStateListener.LISTEN_CALL_STATE);

				}
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
			}
		});
	}

	
	public class CallStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			Log.i("CallStateListener", incomingNumber);
			Log.i("state", state + "");
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				try {

					autoAnswercall();
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			}
		}
	}

	public void autoAnswercall() 
	{
		Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
		meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
		sendOrderedBroadcast(meidaButtonIntent, null);
	}
}
