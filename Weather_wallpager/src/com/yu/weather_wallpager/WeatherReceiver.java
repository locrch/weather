package com.yu.weather_wallpager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class WeatherReceiver extends BroadcastReceiver
{
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		String msg = intent.getStringExtra("msg");  
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        
        boolean isServiceRunning = false;   
        
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {   
            
        //¼ì²éService×´Ì¬   
            
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);   
        for (RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)) {   
        if("com.yu.weather_wallpager.NotificationService".equals(service.service.getClassName()))   
                
         {   
         isServiceRunning = true;   
        }   
            
         }   
        if (!isServiceRunning) {   
        Intent i = new Intent(context, NotificationService.class);   
               context.startService(i);   
        }   
      
      
    } 
	}
}
