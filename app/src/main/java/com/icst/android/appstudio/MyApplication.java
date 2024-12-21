/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio;


import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.elfilibustero.uidesigner.AppLoader;
import com.icst.android.appstudio.activities.CrashHandlerActivity;
import com.icst.android.appstudio.models.SettingModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.SettingUtils;
import com.quickersilver.themeengine.ThemeEngine;
import com.quickersilver.themeengine.ThemeMode;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {
	// Social links
	public static final String YOUTUBE = "https://www.youtube.com/@Innovative-CST";
	public static final String DISCORD = "https://discord.com/invite/RM5qaZs4kd";
	public static final String INSTAGRAM = "https://www.instagram.com/innovative_cst";
	public static final String X = "https://x.com/Innovative_cst";
	public static final String GITHUB_APP = "https://github.com/Innovative-CST/AndroidAppStudio";
	public static final String GITHUB_ORG = "https://github.com/Innovative-CST";

	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
	private static Context mApplicationContext;
	private static ThemeEngine themeEngine;

	public static Context getContext() {
		return mApplicationContext;
	}

	public static ThemeEngine getThemeEngine() {
		return themeEngine;
	}

	@Override
	public void onCreate() {
		mApplicationContext = getApplicationContext();
		themeEngine = ThemeEngine.getInstance(this);
		AppLoader.setContext(getApplicationContext());
		EnvironmentUtils.init(this);
		PRDownloaderConfig config = PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).setConnectTimeout(30_000)
				.build();

		PRDownloader.initialize(getApplicationContext(), config);

		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread thread, Throwable throwable) {
						Intent intent = new Intent(getApplicationContext(), CrashHandlerActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

						StringBuilder error = new StringBuilder();
						error
								.append("App Version: ")
								.append(BuildConfig.VERSION_NAME)
								.append("\n")
								.append("CommitSHA: ")
								.append(BuildConfig.commitSha)
								.append("\n")
								.append("Build Type: ")
								.append(BuildConfig.BUILD_TYPE)
								.append("\n")
								.append("DeveloperMode: ")
								.append(BuildConfig.isDeveloperMode)
								.append("\n")
								.append("SDK: ")
								.append(Build.VERSION.SDK_INT)
								.append("\n")
								.append("Android: ")
								.append(Build.VERSION.RELEASE)
								.append("\n")
								.append("Model: ")
								.append(Build.VERSION.INCREMENTAL)
								.append("\n")
								.append("Base OS: ")
								.append(Build.VERSION.BASE_OS)
								.append("\n")
								.append("CPU ABI: ")
								.append(Build.CPU_ABI)
								.append("\n")
								.append("CPU ABI2: ")
								.append(Build.CPU_ABI2)
								.append("\n")
								.append("Manufacturer: ")
								.append(Build.MANUFACTURER)
								.append("\n")
								.append("App Storage: ")
								.append(EnvironmentUtils.STORAGE.getAbsolutePath())
								.append("\n")
								.append("Device External Storage: ")
								.append(Environment.getExternalStorageDirectory())
								.append("\n\n")
								.append(Log.getStackTraceString(throwable));

						intent.putExtra("error", error.toString());
						PendingIntent pendingIntent = PendingIntent.getActivity(
								getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT);

						AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);

						Process.killProcess(Process.myPid());
						System.exit(1);

						uncaughtExceptionHandler.uncaughtException(thread, throwable);
					}
				});

		SettingModel settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
		if (settings == null) {
			settings = new SettingModel();
		}
		if (settings.isEnabledDarkMode()) {
			themeEngine.setThemeMode(ThemeMode.DARK);
		} else {
			themeEngine.setThemeMode(ThemeMode.LIGHT);
		}

		themeEngine.setDynamicTheme(settings.isEnabledDynamicTheme());
		ThemeEngine.applyToActivities(this);
		super.onCreate();
	}
}
