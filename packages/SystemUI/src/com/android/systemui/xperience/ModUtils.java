/*
 * Copyright (C) 2019 The XPerience Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.systemui.xperience;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.motorola.android.provider.MotorolaSettings.Secure;
import java.nio.ByteBuffer;
import java.util.List;

public class ModUtils {

    private static String TAG = ModUtils.class.getSimpleName();
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final Intent DYNAMIC_MOD_BATTERY_HISTORY_CHART_INTENT = new Intent("com.motorola.extensions.settings.MODS_BATTERY_HISTORY_CHART");

    public static int getModBatteryLevel(Intent batteryChangedIntent) {
        return (batteryChangedIntent.getIntExtra("mod_level", -1) * 100) / batteryChangedIntent.getIntExtra("scale", 100);
    }

    public static int getModBatteryType(Intent batteryChangedIntent) {
        return batteryChangedIntent.getIntExtra("mod_type", 0);
    }

    public static boolean isModActive(int device_level, int mod_level, int mod_type) {
        if (mod_level <= 0) {
            return false;
        }
        String str;
        if (DEBUG) {
            str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isModActive ; Battery Type is ");
            stringBuilder.append(mod_type);
            Log.d(str, stringBuilder.toString());
        }
        if (mod_type != 2) {
            return false;
        }
        str = SystemProperties.get("sys.mod.batterymode");
        String str2 = TAG;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("isModActive ; Battery Mode is ");
        stringBuilder2.append(str);
        Log.d(str2, stringBuilder2.toString());
        if ("0".equals(str)) {
            return true;
        }
        if (!"2".equals(str) && device_level <= 80) {
            return true;
        }
        return false;
    }

    public static boolean isModAttached(int modStatus, int modLevel) {
        if (modStatus == 1 || modLevel < 0) {
            return false;
        }
        return true;
    }

    public static boolean isModAttached() {
        String attached = SystemProperties.get("sys.mod.current");
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isModAttached: ");
        stringBuilder.append(attached);
        Log.d(str, stringBuilder.toString());
        if (attached != null) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("isModAttached: ");
            stringBuilder.append(attached.trim().length());
            Log.d(str, stringBuilder.toString());
        }
        if (attached == null || attached.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static boolean showModQsTile(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(DYNAMIC_MOD_BATTERY_HISTORY_CHART_INTENT, 128);
        if (activities == null) {
            return false;
        }
        for (ResolveInfo activity : activities) {
            try {
                if ((pm.getApplicationInfo(activity.activityInfo.packageName, 0).flags & 1) != 0) {
                    return true;
                }
            } catch (NameNotFoundException e) {
            }
        }
        return false;
    }

    public static boolean isAProjectorMod() {
        boolean isProjector = false;
        String current = SystemProperties.get("sys.mod.current");
        if (!TextUtils.isEmpty(current)) {
            ByteBuffer buf = ByteBuffer.wrap(Base64.decode(current.substring(0, 32), 0));
            if (buf != null) {
                int vid = buf.getInt();
                int pid = buf.getInt();
                if (vid == 296 && (983040 & pid) == 327680) {
                    isProjector = true;
                }
            }
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isAProjectorMod isProjector = ");
        stringBuilder.append(isProjector);
        Log.d(str, stringBuilder.toString());
        return isProjector;
    }

    public static boolean isSupplemental(Intent intent) {
        boolean z = false;
        if (intent == null) {
            return false;
        }
        if (getModBatteryType(intent) == 2) {
            z = true;
        }
        return z;
    }

    public static boolean togglePowerSharing(Context context) {
        boolean value = isPowerSharingEnabled(context);
        Intent intent = new Intent("com.motorola.modservice.ui.action.SETTING_CHANGE");
        intent.putExtra("powersharing", value);
        context.sendBroadcastAsUser(intent, UserHandle.CURRENT);
        return value;
    }

    public static boolean isPowerSharingEnabled(Context context) {
        int mode = Secure.getIntForUser(context.getContentResolver(), "mod_battery_mode", 0, -2);
        return mode == 0 || mode == 1;
    }

    public static boolean isChargingMod(Intent intent) {
        boolean z = false;
        if (intent == null) {
            return false;
        }
        int plugType = intent.getIntExtra("plugged_raw", intent.getIntExtra("plugged", 0));
        int modStatus = intent.getIntExtra("mod_status", 1);
        int batteryType = intent.getIntExtra("mod_type", 0);
        if ((plugType == 0 || plugType == 8) && modStatus == 2 && batteryType == 2) {
            return false;
        }
        if (modStatus == 2) {
            z = true;
        }
        return z;
    }
}