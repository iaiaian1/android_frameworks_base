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

package com.android.systemui.qs.tiles;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import com.android.systemui.Dependency;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl.ResourceIcon;
import com.android.systemui.plugins.qs.QSTile.State;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback;
import com.motorola.android.provider.MotorolaSettings.Secure;
import com.android.systemui.xperience.ModUtils;

import com.android.systemui.R;

/* renamed from: com.android.systemui.qs.tiles.ModBatteryTile */
public class ModBatteryTile extends QSTileImpl<State> implements BatteryStateChangeCallback {

    private static final boolean DEBUG = Log.isLoggable("ModBatteryTile", 3);
    private final int MOD_METRICS_CATEGORY = 999999;
    private final ActivityStarter mActivityStarter = ((ActivityStarter) Dependency.get(ActivityStarter.class));
    private final BatteryController mBatteryController = ((BatteryController) Dependency.get(BatteryController.class));
    private ContentObserver mBatteryModeObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange) {
            boolean enabled = ModUtils.isPowerSharingEnabled(ModBatteryTile.this.mContext);
            if (ModBatteryTile.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Power Sharing state changed to ");
                stringBuilder.append(enabled);
                Log.d("ModBatteryTile", stringBuilder.toString());
            }
            if (ModBatteryTile.this.mSharingEnabled != enabled) {
                ModBatteryTile.this.mSharingEnabled = enabled;
                ModBatteryTile.this.updateModState();
            }
        }
    };
    private boolean mChargingMod;
	private final Icon mIconCharging = ResourceIcon.get(R.drawable.moto_ic_qs_mod_charging);
    private final Icon mIconDefault = ResourceIcon.get(R.drawable.moto_ic_qs_mod);
    private final IntentFilter mIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    private int mModBatteryLevel = -1;
    private final BroadcastReceiver mModReceiver = new C10972();
    private boolean mSharingEnabled;
    private boolean mSupplemental;

    /* renamed from: com.android.systemui.qs.tiles.ModBatteryTile$2 */
    class C10972 extends BroadcastReceiver {
        C10972() {
        }

        public void onReceive(Context context, Intent intent) {
            if (ModBatteryTile.DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Received ");
                stringBuilder.append(intent);
                Log.d("ModBatteryTile", stringBuilder.toString());
            }
            if (intent == null) {
                return;
            }
            if ("com.motorola.mod.action.MOD_ATTACH".equals(intent.getAction())) {
                ModBatteryTile.this.resetModState(true);
                ModBatteryTile.this.mBatteryController.addCallback(ModBatteryTile.this);
            } else if ("com.motorola.mod.action.MOD_DETACH".equals(intent.getAction())) {
                ModBatteryTile.this.mBatteryController.removeCallback(ModBatteryTile.this);
                ModBatteryTile.this.updateModState();
            }
        }
    }

    public ModBatteryTile(QSHost host) {
        super(host);
    }

    public State newTileState() {
        return new State();
    }

    public int getMetricsCategory() {
        return 999999;
    }

    public void handleSetListening(boolean listening) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setListening listening is ");
            stringBuilder.append(listening);
            Log.d("ModBatteryTile", stringBuilder.toString());
        }
        if (listening) {
            resetModState(false);
            if (ModUtils.isModAttached()) {
                if (DEBUG) {
                    Log.d("ModBatteryTile", "Mod is attached, register for battery");
                }
                this.mBatteryController.addCallback(this);
            } else {
                refreshState(null);
            }
            if (DEBUG) {
                Log.d("ModBatteryTile", "Mod is attached, register for Mod attach/detach");
            }
            IntentFilter filter = new IntentFilter("com.motorola.mod.action.MOD_ATTACH");
            filter.addAction("com.motorola.mod.action.MOD_DETACH");
            this.mContext.registerReceiverAsUser(this.mModReceiver, new UserHandle(ActivityManager.getCurrentUser()), filter, "com.motorola.mod.permission.MOD_INTERNAL", null);
            this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor("mod_battery_mode"), false, this.mBatteryModeObserver, -2);
            this.mSharingEnabled = ModUtils.isPowerSharingEnabled(this.mContext);
            updateModState();
            return;
        }
        if (DEBUG) {
            Log.d("ModBatteryTile", "unregister listeners");
        }
        this.mBatteryController.removeCallback(this);
        this.mContext.unregisterReceiver(this.mModReceiver);
        this.mContext.getContentResolver().unregisterContentObserver(this.mBatteryModeObserver);
    }

    public Intent getLongClickIntent() {
        return new Intent("com.motorola.modservice.ui.action.SETTINGS");
    }

   @Override
    public boolean isAvailable() {
        return ModUtils.showModQsTile(this.mContext);
    }

    /* Access modifiers changed, original: protected */
    public void handleClick() {
        if (!ModUtils.isModAttached()) {
            return;
        }
        if (this.mSupplemental) {
            this.mSharingEnabled = ModUtils.togglePowerSharing(this.mContext);
            refreshState(null);
            return;
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("com.motorola.modservice.ui.action.SETTINGS"), 0);
    }

    public CharSequence getTileLabel() {
        return getTileLabel(ModUtils.isModAttached());
    }

    /* Access modifiers changed, original: protected */
    public void handleUpdateState(State state, Object arg) {
        if (ModUtils.isModAttached()) {
            if (this.mSupplemental) {
                state.state = this.mSharingEnabled ? 2 : 1;
            } else {
                state.state = 1;
            }
            state.icon = this.mChargingMod ? this.mIconCharging : this.mIconDefault;
            state.label = getTileLabel(true);
        } else {
            state.state = 0;
            state.icon = this.mIconDefault;
            state.label = getTileLabel(false);
        }
        state.contentDescription = state.label;
        state.expandedAccessibilityClassName = (this.mSupplemental ? Switch.class : Button.class).getName();
    }

    public void onBatteryLevelChanged(int level, boolean pluggedIn, boolean charging) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onBatteryLevelChanged ");
            stringBuilder.append(level);
            stringBuilder.append(":");
            stringBuilder.append(pluggedIn);
            stringBuilder.append(":");
            stringBuilder.append(charging);
            Log.d("ModBatteryTile", stringBuilder.toString());
        }
        updateModState();
    }

    public void onPowerSaveChanged(boolean isPowerSave) {
        updateModState();
    }

    private void updateModState() {
        if (DEBUG) {
            Log.d("ModBatteryTile", "Update Mod state");
        }
        Intent intent = getBatteryIntent();
        if (intent == null || !ModUtils.isModAttached()) {
            resetModState(true);
        } else {
            this.mModBatteryLevel = ModUtils.getModBatteryLevel(intent);
            if (this.mModBatteryLevel >= 0 || !(this.mSupplemental || this.mChargingMod)) {
                this.mSupplemental = ModUtils.isSupplemental(intent);
                this.mChargingMod = this.mModBatteryLevel >= 0 ? ModUtils.isChargingMod(intent) : false;
            } else {
                if (DEBUG) {
                    Log.v("ModBatteryTile", "Skip invalid mod state");
                }
                return;
            }
        }
        refreshState(null);
    }

    private void resetModState(boolean includeMutable) {
        this.mModBatteryLevel = -1;
        if (includeMutable) {
            this.mChargingMod = false;
        }
        this.mSupplemental = false;
    }

    private Intent getBatteryIntent() {
        Intent intent = this.mContext.registerReceiver(null, this.mIntentFilter);
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Battery intent: ");
            stringBuilder.append(intent.toUri(0));
            Log.d("ModBatteryTile", stringBuilder.toString());
        }
        return intent;
    }

    private String getTileLabel(boolean attached) {
        if (!attached || this.mModBatteryLevel < 0) {
            return this.mContext.getString(R.string.moto_mods);
        }
        return this.mContext.getString(R.string.mod_battery_format, new Object[]{Integer.valueOf(this.mModBatteryLevel)});
    }
}