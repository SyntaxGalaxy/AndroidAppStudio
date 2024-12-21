/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

dependencies {
  implementation(project(":common"))
  implementation(project(":properties-parser"))
}

gradlePlugin {
  plugins {
    create("com.itsaky.androidide.build.propsparser") {
      id = "com.itsaky.androidide.build.propsparser"
      implementationClass = "com.itsaky.androidide.plugins.PropertiesParserPlugin"
    }
  }
}
