package com.icst.android.appstudio.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.system.ErrnoException;
import android.system.Os;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gradle.api.JavaVersion;

public class OpenJDKSetup {

  public static void extractAndSetUpOpenJDK(Context context) {
    // Extract the JDK
    if (!new File("/data/data/com.icst.android.appstudio/files/usr/opt/openjdk").exists()) {
      new File("/data/data/com.icst.android.appstudio/files/usr/opt").mkdirs();
      unzipFromAssets(
          context, "openjdk-17.0.zip", "/data/data/com.icst.android.appstudio/files/usr/opt");
      Toast.makeText(context, "Installed", Toast.LENGTH_LONG).show();
    }
  }

  public static void unzipFromAssets(Context context, String assetName, String outputDir) {
    try {
      AssetManager assetManager = context.getAssets();
      InputStream inputStream = assetManager.open(assetName);
      ZipInputStream zipInputStream = new ZipInputStream(inputStream);
      ZipEntry zipEntry;
      byte[] buffer = new byte[1024];
      int count;

      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        String fileName = zipEntry.getName();
        File file = new File(outputDir, fileName);

        // Create directories if necessary
        if (zipEntry.isDirectory()) {
          file.mkdirs();
          continue;
        } else {
          File parent = file.getParentFile();
          if (parent != null && !parent.exists()) {
            parent.mkdirs();
          }
        }

        // Write file
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        while ((count = zipInputStream.read(buffer)) != -1) {
          fileOutputStream.write(buffer, 0, count);
        }
        fileOutputStream.close();
        zipInputStream.closeEntry();

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
          if (parent.getAbsolutePath().contains("bin")) {
            Os.chmod(file.getAbsoluteFile().getAbsolutePath(), 0700);
          }
        }
      }

      zipInputStream.close();
      inputStream.close();
    } catch (IOException | ErrnoException e) {
    }
  }
}
