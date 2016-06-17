package com.example.baidumaptest;

import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
private MapView mapView;
private BaiduMap baiduMap;
private LocationManager locationManager;
private String provider;
private boolean isFirstLocation=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView=(MapView) findViewById(R.id.map_view);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList=locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER))
        {
        	provider=LocationManager.GPS_PROVIDER;
        }
        else if(providerList.contains(LocationManager.NETWORK_PROVIDER))
        {
        	provider=LocationManager.NETWORK_PROVIDER;
        }
        else{
        	Toast.makeText(this, "No location provider to use .", Toast.LENGTH_SHORT).show();
        	return;
        }
        Location location=locationManager.getLastKnownLocation(provider);
        if(location!=null)
        {
        	navigateTo(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationlistener);
    }
    private void navigateTo(Location location) {
		// TODO Auto-generated method stub
    	if(isFirstLocation){
    		LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
    		MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(ll);
    		baiduMap.animateMapStatus(update);
    		update=MapStatusUpdateFactory.zoomTo(16.8F);
    		baiduMap.animateMapStatus(update);
    		isFirstLocation=false;
    		
    	}
		MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
		locationBuilder.latitude(location.getLatitude());
		locationBuilder.longitude(location.getLongitude());
		MyLocationData locationData=locationBuilder.build();
		baiduMap.setMyLocationData(locationData);
	}
	protected void onDestory(){
    	super.onDestroy();
    	baiduMap.setMyLocationEnabled(false);
    	mapView.onDestroy();
    	if(locationManager!=null)
    	{
    		locationManager.removeUpdates(locationlistener);
    	}
    }
    protected void onPause(){
    	super.onPause();
    	mapView.onPause();
    }
    protected void onResume(){
    	super.onResume();
    	mapView.onResume();
    }
    LocationListener locationlistener=new LocationListener(){

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
			if(arg0!=null)
			{
				navigateTo(arg0);
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
}