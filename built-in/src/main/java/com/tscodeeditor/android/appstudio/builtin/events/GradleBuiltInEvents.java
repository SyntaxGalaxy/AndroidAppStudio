
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

package com.tscodeeditor.android.appstudio.builtin.events;

import com.tscodeeditor.android.appstudio.block.model.BlockContentLayerModel;
import com.tscodeeditor.android.appstudio.block.model.BlockContentModel;
import com.tscodeeditor.android.appstudio.block.model.BlockLayerModel;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;
import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.block.utils.RawCodeReplacer;
import java.util.ArrayList;

public class GradleBuiltInEvents {
    public static Event getAppModuleAndroidBlockEvent() {
    Event androidBlockEvent = new Event();
    androidBlockEvent.setTitle("App Configration");
    androidBlockEvent.setName("androidBlock");
    androidBlockEvent.setDescription("Contains basic defination of your app");
    androidBlockEvent.setEventReplacer("blockCode");
    androidBlockEvent.setRawCode("android {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");
    androidBlockEvent.setEnableEdit(true);
    androidBlockEvent.setEnableRootBlocksDrag(false);

    BlockModel androidBlockEventBlockModel = new BlockModel();
    androidBlockEventBlockModel.setColor("#884400");
    androidBlockEventBlockModel.setFirstBlock(true);
    androidBlockEventBlockModel.setBlockType(BlockModel.Type.defaultBlock);

    ArrayList<BlockLayerModel> androidBlockEventBlockLayerModel = new ArrayList<BlockLayerModel>();

    BlockContentLayerModel androidBlockEventTextLayer = new BlockContentLayerModel();

    ArrayList<BlockContentModel> defineTextLayerContent = new ArrayList<BlockContentModel>();
    BlockContentModel defineEventText = new BlockContentModel();
    defineEventText.setText("Your app configration");

    defineTextLayerContent.add(defineEventText);
    androidBlockEventTextLayer.setBlockContents(defineTextLayerContent);

    androidBlockEventBlockLayerModel.add(androidBlockEventTextLayer);
    androidBlockEventBlockModel.setBlockLayerModel(androidBlockEventBlockLayerModel);

    androidBlockEvent.setEventTopBlock(androidBlockEventBlockModel);
    androidBlockEvent.setEnableRootBlocksValueEditing(false);
    return androidBlockEvent;
  }

  public static Event getAppModuleDependenciesBlockEvent() {
    Event dependenciesBlockEvent = new Event();
    dependenciesBlockEvent.setTitle("App Libraries");
    dependenciesBlockEvent.setName("dependenciesBlock");
    dependenciesBlockEvent.setDescription("Contains library used by your app");
    dependenciesBlockEvent.setEventReplacer("blockCode");
    dependenciesBlockEvent.setRawCode(
        "dependencies {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");
    dependenciesBlockEvent.setEnableEdit(true);
    dependenciesBlockEvent.setEnableRootBlocksDrag(false);
    dependenciesBlockEvent.setEnableRootBlocksValueEditing(false);

    return dependenciesBlockEvent;
  }
}
