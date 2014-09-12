package com.yu.weather_wallpager;

import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.yu.weather_wallpager.LocationApplication.MyLocationListener;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class NotificationService extends Service
{
	String result;
	Gson gson;
	public static final String AK="8YlR8mk9Vvxc0VlyrTYzo9FC";
	String place = "佛山";
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="bd09ll";
	
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	
	public Vibrator mVibrator;
	public String cityname;
	public int span = 3600000;
	double longitude,latitude;
	@Override
	public IBinder onBind(Intent intent)
	{ 
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		
		
		
		
		Toast.makeText(getApplication(), "服务启动！", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplication(), "服务运行！", Toast.LENGTH_SHORT).show();
		
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		
	    mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		
		InitLocation();
				
		mLocationClient.start();
				
		return Service.START_REDELIVER_INTENT;
	}
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("err", "终止！");
		mLocationClient.stop();
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			cityname = location.getCity();
			
			longitude = location.getLongitude();
			
			latitude = location.getLatitude();
			
			Toast.makeText(getApplication(), "定位城市："+cityname, Toast.LENGTH_LONG).show();
			
			
			new AsyncTask<Void, Void, Boolean>(){
				protected void onPreExecute() {};
				
				protected Boolean doInBackground(Void[] params) {
					
					try
					{
						HttpClient hc = new DefaultHttpClient();
						
						HttpGet hg = new HttpGet("http://api.map.baidu.com/telematics/v3/weather?location="+longitude+","+latitude+"&output=json&ak="+AK+"&coord_type="+tempcoor);
						
						HttpClientParams.setCookiePolicy(hc.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
						
						gson = new Gson();
						
						HttpResponse hr = hc.execute(hg);
						
						result = EntityUtils.toString(hr.getEntity());
						
						Log.d("res", "Gresult : " + result);
							
						
						
							
						// 关闭连接
						if (hc != null)
						{
							hc.getConnectionManager().shutdown();
						}

					} catch (Exception e)
					{
						// TODO: handle exception
						//Log.e("error:", e.getMessage());
						e.printStackTrace();
						String json_false = "{\"type\":null,\"success\":false,\"msg\":\"网络错误！\"}";
							
						result = json_false;
						
					}
					
					finally{
						
					}
					
					return null;
				};
				protected void onPostExecute(Boolean result) {
					
					
				};
			}.execute();
		}


	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		option.setScanSpan(span);//设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);
	}
}
