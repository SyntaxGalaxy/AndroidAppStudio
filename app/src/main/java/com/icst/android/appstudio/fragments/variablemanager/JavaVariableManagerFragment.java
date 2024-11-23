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

package com.icst.android.appstudio.fragments.variablemanager;

import java.io.File;

import com.icst.android.appstudio.R.id;
import com.icst.android.appstudio.databinding.FragmentJavaVariableManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class JavaVariableManagerFragment extends Fragment {
	private FragmentJavaVariableManagerBinding binding;
	private ModuleModel module;
	private String packageName;
	private String className;

	public JavaVariableManagerFragment() {
	}

	public JavaVariableManagerFragment(ModuleModel module, String packageName, String className) {
		this.module = module;
		this.packageName = packageName;
		this.className = className;
	}

	@Override
	@MainThread
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("module", module.module);
		bundle.putString("projectRootDirectory", module.projectRootDirectory.getAbsolutePath());
		bundle.putString("className", className);
		bundle.putString("packageName", packageName);
	}

	@Override
	@MainThread
	@Nullable public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			module = new ModuleModel();
			module.init(
					savedInstanceState.getString("module"),
					new File(savedInstanceState.getString("projectRootDirectory")));
			className = savedInstanceState.getString("className");
			packageName = savedInstanceState.getString("packageName");
		}

		binding = FragmentJavaVariableManagerBinding.inflate(inflator);
		binding.bottomNavigationView.setOnItemSelectedListener(
				menu -> {
					if (menu.getItemId() == id.static_variables) {
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(
										id.fragment_container,
										new StaticVariableManagerFragment(module, packageName, className))
								.commit();
					} else if (menu.getItemId() == id.non_static_variables) {
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(
										id.fragment_container,
										new NonStaticVariableManagerFragment(module, packageName, className))
								.commit();
					} else if (menu.getItemId() == id.layout_variables) {
						LayoutVariableManagerFragment layoutManager = new LayoutVariableManagerFragment();
						Bundle args = new Bundle();
						args.putString("module", module.module);
						args.putString("projectRootDirectory", module.projectRootDirectory.getAbsolutePath());
						args.putString("className", className);
						args.putString("packageName", packageName);
						layoutManager.setArguments(args);
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(id.fragment_container, layoutManager)
								.commit();
					}
					return true;
				});
		binding.bottomNavigationView.setSelectedItemId(id.non_static_variables);
		return binding.getRoot();
	}
}
