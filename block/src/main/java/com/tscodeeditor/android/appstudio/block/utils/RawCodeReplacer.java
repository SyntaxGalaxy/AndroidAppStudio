/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.block.utils;

public class RawCodeReplacer {
  public static String getReplacer(String name) {
    StringBuilder builder = new StringBuilder();
    builder.append("%%%%DevKumar AndroidAppBuilder ");
    builder.append(new String(name));
    builder.append(" DevKumar%%%%");
    return builder.toString();
  }

  public static String removeAndroidAppBuilderString(String input) {
    return input.replaceAll("%%%%DevKumar AndroidAppBuilder .*? DevKumar%%%%", "");
  }
}