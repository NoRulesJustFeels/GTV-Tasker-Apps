package com.entertailion.android.gtvtasker;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	private static final String LOG_TAG = "MainActivity";
	private final static List<AppInfo> apps = new ArrayList<AppInfo>();
	private AppsAdapter appsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getInstalledApps();
		appsAdapter = new AppsAdapter(this);
		setListAdapter(appsAdapter);

		getListView().setFocusable(true);
		getListView().setSelector(
				getResources().getDrawable(R.drawable.image_background));
		getListView().requestFocus();
	}

	private void getInstalledApps() {
		try {
			Log.i(LOG_TAG, "getInstalledApps");
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");

			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			PackageManager pm = getPackageManager();
			List<ResolveInfo> resolveinfo_list = pm.queryIntentActivities(
					intent, 0);
			for (ResolveInfo info : resolveinfo_list) {
				AppInfo appInfo = new AppInfo();
				String symbolicName = info.activityInfo.packageName;
				CharSequence appName = pm.getApplicationLabel(pm
						.getApplicationInfo(symbolicName, 0));
				appInfo.name = appName.toString();
				appInfo.packageName = info.activityInfo.packageName;
				appInfo.activityName = info.activityInfo.name;

				apps.add(appInfo);
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "getInstalledApps", e);
		}
	}

	/**
	 * App information
	 * 
	 */
	private static class AppInfo {
		String name;
		String packageName;
		String activityName;
	}

	/**
	 * Adapter for the ListView of apps.
	 */
	private static class AppsAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public AppsAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return apps.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.list_item, null);
			TextView appName = (TextView) convertView
					.findViewById(R.id.appName);
			TextView appPackage = (TextView) convertView
					.findViewById(R.id.appPackage);
			TextView appActivity = (TextView) convertView
					.findViewById(R.id.appActivity);
			final AppInfo appInfo = apps.get(position);
			appName.setText("Name: " + appInfo.name);
			appPackage.setText("Package: " + appInfo.packageName);
			appActivity.setText("Activity: " + appInfo.activityName);
			return convertView;
		}
	}

}
