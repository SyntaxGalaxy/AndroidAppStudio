/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.tscodeeditor.android.appstudio;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.tscodeeditor.android.appstudio.activities.CrashHandlerActivity;
import com.tscodeeditor.android.appstudio.models.SettingModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.SettingUtils;

public class MyApplication extends Application {
  private static Context mApplicationContext;

  // Social links
  public static final String YOUTUBE = "https://youtube.com/@tscodeeditor?feature=shared";
  public static final String DISCORD = "https://discord.com/invite/RM5qaZs4kd";
  public static final String INSTAGRAM =
      "https://www.instagram.com/tscode_editor?igsh=MXBkOG1va2FwZzN6dw==";
  public static final String GITHUB_APP = "https://github.com/TS-Code-Editor/AndroidAppStudio";
  public static final String GITHUB_ORG = "https://github.com/TS-Code-Editor";

  private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  public static Context getContext() {
    return mApplicationContext;
  }

  @Override
  public void onCreate() {
    mApplicationContext = getApplicationContext();
    EnvironmentUtils.init(this);

    this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(
        new Thread.UncaughtExceptionHandler() {
          @Override
          public void uncaughtException(Thread thread, Throwable throwable) {
            Intent intent = new Intent(getApplicationContext(), CrashHandlerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("error", Log.getStackTraceString(throwable));
            PendingIntent pendingIntent =
                PendingIntent.getActivity(
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
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    super.onCreate();
  }

  public void onThemeChange() {
    SettingModel settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
    if (settings.isEnabledDarkMode()) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
  }
}
