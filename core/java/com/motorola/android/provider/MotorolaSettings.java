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

package com.motorola.android.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.IContentProvider;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.AndroidException;
import android.util.Log;
import android.util.MemoryIntArray;
import com.android.internal.annotations.GuardedBy;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class MotorolaSettings {
    public static final String AGPS_FEATURE_ENABLED = "agps_feature_enabled";
    public static final String AGPS_GPSONE_USER_PLANE = "agps_gpsone_user_plane";
    public static final String ALT_SHIFT_TOGGLE_FTR_AVAILABLE = "alt_shift_toggle_ftr_available";
    public static final String ASSISTED_DIALING_STATE = "assisted_dialing_state";
    public static final String AUDIO_ROUTING = "audio_routing";
    public static final String AUTHORITY = "com.motorola.android.providers.settings";
    public static final String AUTH_DEVICES_ENABLED = "auth_devices_enabled";
    public static final String AUTH_DEVICE_LIST = "auth_device_list";
    public static final String AUTO_DISABLE_TRACKPAD = "lapdock_disable_trackpad";
    public static final String AUTO_SYSTEM_CHECK_ENABLED = "auto_system_check";
    public static final String BACK_GROUND_DATA_BACKUP_BY_DATAMANAGER = "back_ground_data_backup_by_datamanager";
    public static final String BT_MFB_ENABLED_WHEN_LOCKED = "bluetooth_mfb_enabled_when_locked";
    public static final String CALLING_33860_ENABLED = "calling_33860_enabled";
    public static final String CALLING_GLOBAL_CONTROLS_ENABLE = "calling_global_controls_enable";
    public static final String CALLING_GSM_AD_ENABLED = "calling_gsm_ad_enabled";
    public static final String CALL_CONNECT_TONE = "call_connect_tone";
    public static final String CALL_METHOD_GENERATION_INDEX_KEY = "_generation_index";
    public static final String CALL_METHOD_GENERATION_KEY = "_generation";
    public static final String CALL_METHOD_GET_GLOBAL = "GET_global";
    public static final String CALL_METHOD_GET_SECURE = "GET_secure";
    public static final String CALL_METHOD_GET_SYSTEM = "GET_system";
    public static final String CALL_METHOD_MAKE_DEFAULT_KEY = "_make_default";
    public static final String CALL_METHOD_PUT_GLOBAL = "PUT_global";
    public static final String CALL_METHOD_PUT_SECURE = "PUT_secure";
    public static final String CALL_METHOD_PUT_SYSTEM = "PUT_system";
    public static final String CALL_METHOD_RESET_GLOBAL = "RESET_global";
    public static final String CALL_METHOD_RESET_MODE_KEY = "_reset_mode";
    public static final String CALL_METHOD_RESET_SECURE = "RESET_secure";
    public static final String CALL_METHOD_TAG_KEY = "_tag";
    public static final String CALL_METHOD_TRACK_GENERATION_KEY = "_track_generation";
    public static final String CALL_METHOD_USER_KEY = "_user";
    public static final String CUR_COUNTRY_AREA_CODE = "cur_country_area_code";
    public static final String CUR_COUNTRY_CODE = "cur_country_code";
    public static final String CUR_COUNTRY_IDD = "cur_country_idd";
    public static final String CUR_COUNTRY_MCC = "cur_country_mcc";
    public static final String CUR_COUNTRY_MDN_LEN = "cur_country_mdn_len";
    public static final String CUR_COUNTRY_NAME = "cur_country_name";
    public static final String CUR_COUNTRY_NDD = "cur_country_ndd";
    public static final String CUR_COUNTRY_UPDATED_BY_USER = "cur_country_updated_by_user";
    public static final String CUR_MDN = "cur_mdn";
    public static final String DATASWITCH_FEATURE_ENABLED = "dataswitch_feature_enabled";
    public static final String DATASWITCH_SYNC_CONNECT_VALUE = "dataswitch_sync_connect_value";
    public static final String DATA_ROAMING_ACCESS_FEATURE_ENABLED = "data_roaming_access_feature_enabled";
    public static final String DEMO_MODE_ENABLED = "demo_mode_enabled";
    public static final String DEMO_MODE_VIDEO_DOWNLOAD_URI = "demo_video_download_uri";
    public static final String DEMO_MODE_VIDEO_PATH_IN_SDCARD = "demo_video_path_in_sdcard";
    public static final String DEMO_MODE_VIDEO_PATH_IN_SYSTEM = "demo_video_path_in_system";
    public static final String DIALUP_MODEM_RESTRICTION = "dialup_modem_restriction";
    public static final String DISABLE_DATA_ECN0_THRESHOLD = "disable_data_ecn0";
    public static final String DISABLE_DATA_RSCP_THRESHOLD = "disable_data_rscp";
    public static final String DISABLE_DATA_RSSI_THRESHOLD = "disable_data_rssi";
    public static final String DOMESTIC_CALL_GUARD = "domestic_call_guard";
    public static final String DOMESTIC_CALL_GUARD_FORCED = "domestic_call_guard_forced";
    public static final String DOMESTIC_DATA_ROAMING = "domestic_data_roaming";
    public static final String DOMESTIC_DATA_ROAMING_FORCED = "domestic_data_roaming_forced";
    public static final String DOMESTIC_DATA_ROAMING_GUARD = "domestic_data_roaming_guard";
    public static final String DOMESTIC_DATA_ROAMING_GUARD_FORCED = "domestic_data_roaming_guard_forced";
    public static final String DOMESTIC_DATA_ROAMING_UI = "domestic_data_roaming_ui";
    public static final String DOMESTIC_VOICE_ROAMING = "domestic_voice_roaming";
    public static final String DOMESTIC_VOICE_ROAMING_FORCED = "domestic_voice_roaming_forced";
    public static final String DOUBLE_TAP = "double_tap";
    public static final String DOWNLOAD_WALLPAPER = "enable_download_wallpaper";
    public static final String DUN_APN_CHANGABLE_CHECK = "dun_apn_changable";
    public static final String DUN_APN_HIDE_CHECK = "dun_apn_hide";
    public static final String EMERGENCY_CALL_SHORTCUT_ENABLE = "emergency_call_shortcut_enable";
    public static final String ENABLE_DATA_ECN0_THRESHOLD = "eable_data_ecn0";
    public static final String ENABLE_DATA_RSCP_THRESHOLD = "enable_data_rscp";
    public static final String ENABLE_DATA_RSSI_THRESHOLD = "enable_data_rssi";
    public static final String ENABLE_MMS_WHEN_DATA_DISABLED = "enable_mms_when_data_disabled";
    public static final String ENABLE_MO_SMS_OVER_IMS = "enable_mo_sms_over_ims";
    public static final String ENABLE_ROAMING_BROKER_38132 = "enable_roaming_broker_38132";
    public static final String ENABLE_TEXT_MSG_REPLY = "qsms_enable_text_message_reply";
    public static final String ERI_ALERT_SOUNDS = "eri_alert_sounds";
    public static final String ERI_TEXT_BANNER = "eri_text_banner";
    public static final String FID_33463_ENABLED = "fid_33463_enabled";
    public static final String FID_34387_MULTIMODE = "fid_34387_multimode";
    public static final String FLAG_ALLOW_ACCESS_ONLY_FOR_THIS_TRIP = "flag_allow_access_only_for_this_trip";
    public static final String FTR_35605_SPRINT_ROAMING_ENABLED = "ftr_35605_sprint_roaming_enabled";
    public static final String FTR_CELL_BROADCAST_ENABLED = "ftr_cell_broadcast_enabled";
    public static final String FTR_FDN_NOTIFY_ENABLED = "ftr_fdn_notify_enabled";
    public static final String FULL_CHARGE_NOTIFICATION_ENABLE = "full_charge_notification_enable";
    public static final String GPSONE_XTRA_DOWNLOADABLE = "gpsone_xtra_downloadable";
    public static final String GSM_DATA_GUARD = "gsm_data_guard";
    public static final String GSM_DATA_GUARD_FORCED = "gsm_data_guard_forced";
    public static final String GSM_DATA_ROAMING = "gsm_data_roaming";
    public static final String GSM_DATA_ROAMING_FORCED = "gsm_data_roaming_forced";
    public static final String GSM_DATA_ROAMING_UI = "gsm_data_roaming_ui";
    public static final String GSM_OUTGOING_SMS_GUARD = "gsm_outgoing_sms_guard";
    public static final String GSM_OUTGOING_SMS_GUARD_FORCED = "gsm_outgoing_sms_guard_forced";
    public static final String GSM_VOICE_ROAMING_GUARD = "gsm_voice_roaming_guard";
    public static final String GSM_VOICE_ROAMING_GUARD_FORCED = "gsm_voice_roaming_guard_forced";
    public static final String HDMI_OVERSCAN = "hdmi_overscan";
    public static final String HFA_RUNNING = "hfa_running";
    public static final String HIDDENMENU_DDTM_DEFAULT_PREFERENCE_SETTINGS = "hiddenmenu_ddtm_default_preference_settings";
    public static final String HUXVMM_FILE_HANDLE_SETTING = "huxvmm_file_handle";
    public static final String HYPHENATION_CHECK = "hyphenation_feature_enabled";
    public static final String ICE_CONTACTS_ENABLED = "ice_contacts_enabled";
    public static final String INTERNATIONAL_CALL_GUARD = "international_call_guard";
    public static final String INTERNATIONAL_CALL_GUARD_FORCED = "international_call_guard_forced";
    public static final String INTERNATIONAL_DATA_ROAMING = "international_data_roaming";
    public static final String INTERNATIONAL_DATA_ROAMING_FORCED = "international_data_roaming_forced";
    public static final String INTERNATIONAL_DATA_ROAMING_GUARD = "international_data_roaming_guard";
    public static final String INTERNATIONAL_DATA_ROAMING_GUARD_FORCED = "international_data_roaming_guard_forced";
    public static final String INTERNATIONAL_DATA_ROAMING_UI = "international_data_roaming_ui";
    public static final String INTERNATIONAL_OUTGOINGSMS_GUARD = "international_outgoingsms_guard";
    public static final String INTERNATIONAL_OUTGOINGSMS_GUARD_FORCED = "international_outgoingsms_guard_forced";
    public static final String INTERNATIONAL_VOICE_ROAMING = "international_voice_roaming";
    public static final String INTERNATIONAL_VOICE_ROAMING_FORCED = "international_voice_roaming_forced";
    public static final String IS_TALKBACK_ON = "is_talkback_on";
    public static final String KEYBOARD_BACKLIGHT_CONTROL_MODE = "kbd_backlight_control_mode";
    public static final String KEYBOARD_BACKLIGHT_TIMEOUT = "kbd_backlight_timeout";
    public static final String KEYBOARD_BRIGHTNESS = "kbd_backlight_brightness";
    public static final String LAPDOCK_CHARGING_MODE = "lapdock_charging_mode";
    private static final boolean LOCAL_LOGV = false;
    public static final String LOCK_FINGERPRINT = "lock_fingerprint";
    public static final String LOCK_PIN_CURRENT_FAILED_ATTEMPTS = "lock_pin_current_failed_attempts";
    public static final String LOCK_TIMER = "lock_timer";
    public static final String LOCK_TYPE = "lock_type";
    public static final String MCC_WHITE_LIST = "roaming_mcc_table";
    public static final String MOBILE_DATA_COVERAGE_CONDITIONER = "mobile_data_coverage_conditioner";
    public static final String MOBILE_DATA_DISABLE = "mobile_data_disable";
    public static final String MOBILE_SYNC_WIFI_URL = "mobile_sync_wifi_url";
    private static final HashSet<String> MOVED_TO_SECURE = new HashSet();
    public static final String NETWORK_LOST_TONE = "network_lost_tone";
    public static final String NETWORK_SETTING_ON_BOOT = "network_setting_on_boot";
    public static final String NEXT_ALARM_UTC = "next_alarm_utc";
    public static final String NFC_AUTH_ENABLED = "nfc_auth_enabled";
    public static final String NFC_AUTH_FAILED_ATTEMPTS = "nfc_auth_failed_attempts";
    public static final String PDP_WATCHDOG_PING_DEADLINE = "pdp_watchdog_ping_deadline";
    public static final String PLMN_BLACK_LIST = "black_list_roaming_plmn_table";
    public static final String PLMN_FEATURE_ENABLE = "roaming_plmn_between_carriers_enabled";
    public static final String POINTER_ACCELERATION = "pointer_acceleration";
    public static final String POINTER_SPEED_LEVEL = "pointer_speed_level";
    public static final String POWER_SAVER_ENABLED = "power_saver_enabled";
    public static final String PREFERRED_MNC_MCC = "preferred_mnc_mcc";
    public static final String PREFERRED_P2P_AUTO_CONNECT_SUPPORT = "preferred_p2p_auto_connect_support";
    public static final String PREFERRED_P2P_BAND_FOR_AGO = "preferred_p2p_band_for_ago";
    public static final String PREFERRED_P2P_DEVICE_LIMIT_FOR_AGO = "preferred_p2p_device_limit_for_ago";
    public static final String PREFERRED_P2P_DEVICE_TIMEOUT_FOR_AGO = "preferred_p2p_device_timeout_for_ago";
    public static final String PRIVACY_ALWAYS_ON_VOICE = "privacy_always_on_voice";
    public static final String PRIVACY_DROID_BLAST = "privacy_droid_blast";
    public static final String PRIVACY_HELP_IMPROVE_PRODUCTS = "privacy_help_improve_products";
    public static final String PRIVACY_MOTO_MARKETING = "privacy_moto_marketing";
    public static final String PRIVACY_OTA_UPDATE = "privacy_ota_update";
    public static final String PRIVACY_SMART_ACTIONS = "privacy_smart_actions";
    public static final String PRIVACY_SMART_NOTIFICATIONS_CHROME = "privacy_smart_notifications_chrome";
    public static final String PRIVACY_SUPPORT_DEVICE = "privacy_support_device";
    public static final String REF_COUNTRY_AREA_CODE = "ref_country_area_code";
    public static final String REF_COUNTRY_CODE = "ref_country_code";
    public static final String REF_COUNTRY_IDD = "ref_country_idd";
    public static final String REF_COUNTRY_MCC = "ref_country_mcc";
    public static final String REF_COUNTRY_MDN_LEN = "ref_country_mdn_len";
    public static final String REF_COUNTRY_NAME = "ref_country_name";
    public static final String REF_COUNTRY_NDD = "ref_country_ndd";
    public static final int RESET_MODE_PACKAGE_DEFAULTS = 1;
    public static final int RESET_MODE_TRUSTED_DEFAULTS = 4;
    public static final int RESET_MODE_UNTRUSTED_CHANGES = 3;
    public static final int RESET_MODE_UNTRUSTED_DEFAULTS = 2;
    public static final String RESTRICTION_LOCK = "restriction_lock";
    public static final String RESTRICT_BG_DATA_ON_LOW_COVERAGE = "restrict_bg_data_on_low_coverage";
    public static final String ROAMING_PLMN_TABLE = "roaming_plmn_table";
    public static final String SCREEN_LOCK_ENABLED = "screen_lock";
    private static final String SEPARATOR = ";";
    public static final String SERVICE_RESET = "service_reset";
    public static final String SETTING_ALLOW_EDITING_CLASS3_APN = "allow_editing_class3_apn";
    public static final String SETTING_CHECK_CFU_POWERON = "check_cfu_poweron";
    public static final String SETTING_DEF_PLUS_CODE_DIALING_IDD_PREFIX = "def_plus_code_dialing_idd_prefix";
    public static final String SETTING_FTR_33859_ENABLED = "sim_33859_isenabled";
    public static final String SETTING_FTR_DUN_NAT_ENABLED = "dun_nat_enabled";
    public static final String SETTING_FTR_ICE_ENABLED = "ice_isenabled";
    public static final String SETTING_FTR_MULTIPLEPDP_ENABLED = "multiple_pdp_isenabled";
    public static final String SETTING_FTR_RINGER_SWITCH_ENABLE = "ftr_ringer_switch_enable";
    public static final String SETTING_FTR_SMARTDIALER_LANGUAGE_CODE = "smartdialer_language_code";
    public static final String SETTING_FTR_TETHER_REVERSE_NAT = "tether_reverse_nat_enabled";
    public static final String SETTING_NORTH_AMERICAN_DIALING_STATE = "north_american_dialing_enabled";
    public static final String SETTING_PLUS_CODE_DIALING_IDD_PREFIX = "plus_code_dialing_idd_prefix";
    public static final String SN_AON = "sn_aon";
    public static final String SN_AUTO_SELECT_INSTALLED = "sn_auto_select_installed";
    public static final String SN_NIGHT_BEGIN = "sn_night_begin";
    public static final String SN_NIGHT_END = "sn_night_end";
    public static final String SN_NIGHT_OFF = "sn_night_off";
    public static final String SN_PRIVACY = "sn_privacy";
    public static final String SN_WHITE_LIST = "sn_white_list";
    public static final String SOFTWARE_UPDATE_ALERT_ENABLED = "software_update_alert";
    public static final String SPRINT_N11_FEATURE_ENABLE = "sprint_N11_feature_enabled";
    public static final String SPRINT_OFFLOAD_FLEX = "sprint_offload_flex";
    private static final String TAG = "MotorolaSettings";
    public static final String TAP_TO_SELECT = "tap_to_select";
    public static final String TASK_BUTTON_PRESS_BEHAVIOR = "task_button_press_behavior";
    public static final String TSB_DISABLE_FLAGS = "tsb_disable_flags";
    public static final String TTS_CALLER_ID_READOUT = "tts_caller_id_readout";
    public static final String USER_NEED_ACCEPT_MOTO_AGREEMENT = "user_need_accept_moto_agreement";
    public static final String VIBRATE_PATTERNS = "vibrate_patterns";
    public static final String VIEWSERVER_IN_SECUREBUILD_ENABLED = "viewserver_in_securebuild_enabled";
    public static final String VM_NUMBER_CDMA = "vm_number_cdma";
    public static final String VM_VVM_ROAMING_SELECTION = "vm_vvm_roaming_selection";
    public static final String VM_VVM_SELECTION = "vm_vvm_selection";
    public static final String VOICE_PRIORITY_ENABLED = "att_voice_priority";
    public static final String WIFI_ADHOC_CHANNEL_NUMBER = "wifi_adhoc_channel_number";
    public static final String WIFI_AP_DHCP_END_ADDR = "wifi_ap_dhcp_end_addr";
    public static final String WIFI_AP_DHCP_START_ADDR = "wifi_ap_dhcp_start_addr";
    public static final String WIFI_AP_DNS1 = "wifi_ap_dns1";
    public static final String WIFI_AP_DNS2 = "wifi_ap_dns2";
    public static final String WIFI_AP_FREQUENCY = "wifi_ap_frequency";
    public static final String WIFI_AP_GATEWAY = "wifi_ap_gateway";
    public static final String WIFI_AP_HIDDEN = "wifi_ap_hidden";
    public static final String WIFI_AP_MAX_SCB = "wifi_ap_max_scb";
    public static final String WIFI_AP_NETMASK = "wifi_ap_netmask";
    public static final String WIFI_DISABLED_BY_ECM = "wifi_disabled_by_ecm";
    public static final String WIFI_DUAL_BAND_SUPPORT = "wifi_dual_band_support";
    public static final String WIFI_HOTSPOT_AUTOCONNECT_ON = "wifi_hotspot_autoconnect";
    public static final String WIFI_HOTSPOT_NOTIFY_ON = "wifi_hotspot_notify";
    public static final String WIFI_NETWORKS_SECURE_AVAILABLE_NOTIFICATION_ON = "wifi_networks_secure_available_notification_on";
    public static final String WIFI_OFFLOAD_FLAG = "wifi_offload_flag";
    public static final String WIFI_P2P_DEVICE_NAME = "wifi_p2p_device_name";
    public static final String WIFI_PROXY = "wifi_proxy";
    public static final String WIFI_PROXY_EXCEPTIONS = "wifi_proxy_exceptions";
    public static final String WIFI_USE_AUTO_IP = "wifi_use_auto_ip";

    private static final class ContentProviderHolder {
        @GuardedBy("mLock")
        private IContentProvider mContentProvider;
        private final Object mLock = new Object();
        @GuardedBy("mLock")
        private final Uri mUri;

        public ContentProviderHolder(Uri uri) {
            this.mUri = uri;
        }

        public IContentProvider getProvider(ContentResolver contentResolver) {
            IContentProvider iContentProvider;
            synchronized (this.mLock) {
                if (this.mContentProvider == null) {
                    this.mContentProvider = contentResolver.acquireProvider(this.mUri.getAuthority());
                }
                iContentProvider = this.mContentProvider;
            }
            return iContentProvider;
        }
    }

    private static final class GenerationTracker {
        private final MemoryIntArray mArray;
        private int mCurrentGeneration;
        private final Runnable mErrorHandler;
        private final int mIndex;

        public GenerationTracker(MemoryIntArray array, int index, int generation, Runnable errorHandler) {
            this.mArray = array;
            this.mIndex = index;
            this.mErrorHandler = errorHandler;
            this.mCurrentGeneration = generation;
        }

        public boolean isGenerationChanged() {
            int currentGeneration = readCurrentGeneration();
            if (currentGeneration >= 0) {
                if (currentGeneration == this.mCurrentGeneration) {
                    return false;
                }
                this.mCurrentGeneration = currentGeneration;
            }
            return true;
        }

        private int readCurrentGeneration() {
            try {
                return this.mArray.get(this.mIndex);
            } catch (IOException e) {
                Log.e(MotorolaSettings.TAG, "Error getting current generation", e);
                if (this.mErrorHandler != null) {
                    this.mErrorHandler.run();
                }
                return -1;
            }
        }

        public void destroy() {
            try {
                this.mArray.close();
            } catch (IOException e) {
                Log.e(MotorolaSettings.TAG, "Error closing backing array", e);
                if (this.mErrorHandler != null) {
                    this.mErrorHandler.run();
                }
            }
        }
    }

    private static class NameValueCache {
        private static final boolean DEBUG = false;
        private static final String NAME_EQ_PLACEHOLDER = "name=?";
        private static final String[] SELECT_VALUE = new String[]{"value"};
        private final String mCallGetCommand;
        private final String mCallSetCommand;
        @GuardedBy("this")
        private GenerationTracker mGenerationTracker;
        private final ContentProviderHolder mProviderHolder;
        private final Uri mUri;
        private final HashMap<String, String> mValues = new HashMap();

        public NameValueCache(Uri uri, String getCommand, String setCommand, ContentProviderHolder providerHolder) {
            this.mUri = uri;
            this.mCallGetCommand = getCommand;
            this.mCallSetCommand = setCommand;
            this.mProviderHolder = providerHolder;
        }

        public boolean putStringForUser(ContentResolver cr, String name, String value, String tag, boolean makeDefault, int userHandle) {
            try {
                Bundle arg = new Bundle();
                arg.putString("value", value);
                arg.putInt("_user", userHandle);
                if (tag != null) {
                    arg.putString("_tag", tag);
                }
                if (makeDefault) {
                    arg.putBoolean("_make_default", true);
                }
                this.mProviderHolder.getProvider(cr).call(cr.getPackageName(), this.mCallSetCommand, name, arg);
                return true;
            } catch (RemoteException e) {
                String str = MotorolaSettings.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Can't set key ");
                stringBuilder.append(name);
                stringBuilder.append(" in ");
                stringBuilder.append(this.mUri);
                Log.w(str, stringBuilder.toString(), e);
                return false;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:91:0x012c  */
        public java.lang.String getStringForUser(android.content.ContentResolver r18, java.lang.String r19, int r20) {
            throw new UnsupportedOperationException("Method not decompiled: com.motorola.android.provider.MotorolaSettings$NameValueCache.getStringForUser(android.content.ContentResolver, java.lang.String, int):java.lang.String");
        }

        public static /* synthetic */ void lambda$getStringForUser$0(NameValueCache nameValueCache) {
            synchronized (nameValueCache) {
                Log.e(MotorolaSettings.TAG, "Error accessing generation tracker - removing");
                if (nameValueCache.mGenerationTracker != null) {
                    GenerationTracker generationTracker = nameValueCache.mGenerationTracker;
                    nameValueCache.mGenerationTracker = null;
                    generationTracker.destroy();
                    nameValueCache.mValues.clear();
                }
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResetMode {
    }

    public static class NameValueTable implements BaseColumns {
        public static final String NAME = "name";
        public static final String VALUE = "value";

        public static boolean putString(ContentResolver resolver, Uri uri, String name, String value) {
            try {
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("value", value);
                resolver.insert(uri, values);
                return true;
            } catch (SQLException e) {
                String str = MotorolaSettings.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Can't set key ");
                stringBuilder.append(name);
                stringBuilder.append(" in ");
                stringBuilder.append(uri);
                Log.w(str, stringBuilder.toString(), e);
                return false;
            }
        }

        public static Uri getUriFor(Uri uri, String name) {
            return Uri.withAppendedPath(uri, name);
        }
    }

    public static class SettingNotFoundException extends AndroidException {
        public SettingNotFoundException(String msg) {
            super(msg);
        }
    }

    public static final class Global extends NameValueTable {
        public static final String ACCESSIBILITY_MAGNIFICATION_ON = "accessibility_magnification_on";
        public static final String CARRIER_SUBSIDY_LOCK_ENABLED = "carrier_subsidy_lock_enabled";
        public static final String CHANNEL_ID = "channel_id";
        public static final Uri CONTENT_URI = Uri.parse("content://com.motorola.android.providers.settings/global");
        public static final String DISABLE_HW_LID = "disable_hw_lid";
        public static final String DISPLAY_NETWORK_NAME = "display_network_name";
        public static final String DOMESTIC_CALL_GUARD = "domestic_call_guard";
        public static final String DOMESTIC_CALL_GUARD_FORCED = "domestic_call_guard_forced";
        public static final String DOMESTIC_DATA_ROAMING = "domestic_data_roaming";
        public static final String DOMESTIC_DATA_ROAMING_FORCED = "domestic_data_roaming_forced";
        public static final String DOMESTIC_DATA_ROAMING_GUARD = "domestic_data_roaming_guard";
        public static final String DOMESTIC_DATA_ROAMING_GUARD_FORCED = "domestic_data_roaming_guard_forced";
        public static final String DOMESTIC_DATA_ROAMING_UI = "domestic_data_roaming_ui";
        public static final String DOMESTIC_VOICE_ROAMING = "domestic_voice_roaming";
        public static final String DOMESTIC_VOICE_ROAMING_FORCED = "domestic_voice_roaming_forced";
        public static final String ENHANCED_4G_BETA_MODE = "volte_beta_mode";
        public static final String FEATURE_DISCOVERY_NOTIFICATION_MUTE = "feature_discovery_notification_mute";
        public static final String FEATURE_DISCOVERY_NOTIFICATION_OPT_OUT = "feature_discovery_notification_opt_out";
        public static final String FTR_35605_SPRINT_ROAMING_ENABLED = "ftr_35605_sprint_roaming_enabled";
        public static final String GLOBAL_SINGLE_HAND = "global_single_hand";
        public static final String GLOBAL_SINGLE_HAND_ON = "global_single_hand_on";
        public static final String GLOBAL_SINGLE_HAND_SCALE = "global_single_hand_scale";
        public static final String GSM_DATA_GUARD = "gsm_data_guard";
        public static final String GSM_DATA_GUARD_FORCED = "gsm_data_guard_forced";
        public static final String GSM_DATA_ROAMING = "gsm_data_roaming";
        public static final String GSM_DATA_ROAMING_FORCED = "gsm_data_roaming_forced";
        public static final String GSM_DATA_ROAMING_UI = "gsm_data_roaming_ui";
        public static final String GSM_OUTGOING_SMS_GUARD = "gsm_outgoing_sms_guard";
        public static final String GSM_OUTGOING_SMS_GUARD_FORCED = "gsm_outgoing_sms_guard_forced";
        public static final String GSM_VOICE_ROAMING_GUARD = "gsm_voice_roaming_guard";
        public static final String GSM_VOICE_ROAMING_GUARD_FORCED = "gsm_voice_roaming_guard_forced";
        public static final String HFA_RUNNING = "hfa_running";
        public static final String HOME_MOBILE_DATA = "moto_home_mobile_data";
        public static final String INTERNATIONAL_CALL_GUARD = "international_call_guard";
        public static final String INTERNATIONAL_CALL_GUARD_FORCED = "international_call_guard_forced";
        public static final String INTERNATIONAL_DATA_ROAMING = "international_data_roaming";
        public static final String INTERNATIONAL_DATA_ROAMING_FORCED = "international_data_roaming_forced";
        public static final String INTERNATIONAL_DATA_ROAMING_GUARD = "international_data_roaming_guard";
        public static final String INTERNATIONAL_DATA_ROAMING_GUARD_FORCED = "international_data_roaming_guard_forced";
        public static final String INTERNATIONAL_DATA_ROAMING_UI = "international_data_roaming_ui";
        public static final String INTERNATIONAL_OUTGOINGSMS_GUARD = "international_outgoingsms_guard";
        public static final String INTERNATIONAL_OUTGOINGSMS_GUARD_FORCED = "international_outgoingsms_guard_forced";
        public static final String INTERNATIONAL_VOICE_ROAMING = "international_voice_roaming";
        public static final String INTERNATIONAL_VOICE_ROAMING_FORCED = "international_voice_roaming_forced";
        public static final String IS_SIM_DATA_SELECTED = "is_sim_data_selected";
        public static final String LID_STATE = "lid_state";
        public static final String MULTI_SIM_SIM1_COLOR = "multi_sim_sim1_color";
        public static final String MULTI_SIM_SIM1_NAME = "multi_sim_sim1_name";
        public static final String MULTI_SIM_SIM2_COLOR = "multi_sim_sim2_color";
        public static final String MULTI_SIM_SIM2_NAME = "multi_sim_sim2_name";
        public static final String MULTI_SIM_VIDEO_CALL_SUBSCRIPTION = "multi_sim_video_call";
        public static final String MULTI_SIM_VIDEO_PROMPT = "multi_sim_video_prompt";
        public static final String MUSIC_ACTIVE_MS = "music_active_ms";
        public static final String OTA_UPDATE_COMPLETED = "ota_update_completed";
        public static final String PAKS_LOCK_STATUS = "paks_lock_status";
        public static final String POWER_BUTTON_SHOWS_LOCKSCREEN = "power_button_shows_lockscreen";
        public static final String PREFERRED_MNC_MCC = "preferred_mnc_mcc";
        public static final String QUEUE_DM_MESSAGES = "oma_dm_queue_messages";
        public static final String RESTRICT_ALL_BACKGROUND_DATA = "restrict_all_background_data";
        public static final String SETTING_CFU_VIEW_SHOW = "radio_info_query_cfu_view_show";
        public static final String SETTING_CHECK_CFU_POWERON = "check_cfu_poweron";
        public static final String SMS_DELIVERY_REPORT_ACK_ENABLED = "sms_delivery_report_ack_enabled";
        public static final String SPRINT_CP_CALL_DROP_REMINDED = "sprint_cp_call_drop_reminded";
        public static final String SPRINT_CP_USER_PREFERENCE = "sprint_cp_user_preference";
        public static final String SPRINT_N11_FEATURE_ENABLE = "sprint_N11_feature_enabled";
        public static final String SPRINT_WFC_AWARENESS_REMINDED = "sprint_wfc_awareness_reminded";
        public static final String SPRINT_WFC_CONGRATS_REMINDED = "sprint_wfc_congrats_reminded";
        public static final String SPRINT_WFC_USER_PREFERENCE = "sprint_wfc_user_preference";
        public static final String VERIZON_CONFIG_VER = "verizon_config_ver";
        public static final String VOYAGER_CLI_EMULATOR_ENABLED = "voyager_cli_emulator_enabled";
        public static final String WIFI_AVOID_BAD_CONNECTION_ENABLED = "wifi_avoid_bad_connection_enabled";
        public static final String WIFI_CHIPSET_5G_BAND_SUPPORT = "wifi_chipset_5g_band_support";
        private static final ContentProviderHolder sProviderHolder = new ContentProviderHolder(CONTENT_URI);
        private static NameValueCache sNameValueCache = new NameValueCache(CONTENT_URI, "GET_global", "PUT_global", sProviderHolder);

        public static String getString(ContentResolver resolver, String name) {
            return getStringForUser(resolver, name, UserHandle.myUserId());
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            return sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putString(ContentResolver resolver, String name, String value) {
            return putStringForUser(resolver, name, value, null, false, UserHandle.myUserId());
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, String tag, boolean makeDefault, int userHandle) {
            return sNameValueCache.putStringForUser(resolver, name, value, tag, makeDefault, userHandle);
        }

        public static void resetToDefaults(ContentResolver resolver, String tag) {
            resetToDefaultsAsUser(resolver, tag, 1, UserHandle.myUserId());
        }

        public static void resetToDefaultsAsUser(ContentResolver resolver, String tag, int mode, int userHandle) {
            try {
                Bundle arg = new Bundle();
                arg.putInt("_user", userHandle);
                if (tag != null) {
                    arg.putString("_tag", tag);
                }
                arg.putInt("_reset_mode", mode);
                sProviderHolder.getProvider(resolver).call(resolver.getPackageName(), "RESET_global", null, arg);
            } catch (RemoteException e) {
                String str = MotorolaSettings.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Can't reset do defaults for ");
                stringBuilder.append(CONTENT_URI);
                Log.w(str, stringBuilder.toString(), e);
            }
        }

        public static Uri getUriFor(String name) {
            return NameValueTable.getUriFor(CONTENT_URI, name);
        }

        public static int getInt(ContentResolver cr, String name, int def) {
            int parseInt;
            String v = getString(cr, name);
            if (v != null) {
                try {
                    parseInt = Integer.parseInt(v);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            parseInt = def;
            return parseInt;
        }

        public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
            try {
                return Integer.parseInt(getString(cr, name));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putInt(ContentResolver cr, String name, int value) {
            return putString(cr, name, Integer.toString(value));
        }

        public static long getLong(ContentResolver cr, String name, long def) {
            String valString = getString(cr, name);
            if (valString == null) {
                return def;
            }
            try {
                return Long.parseLong(valString);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        public static long getLong(ContentResolver cr, String name) throws SettingNotFoundException {
            try {
                return Long.parseLong(getString(cr, name));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putLong(ContentResolver cr, String name, long value) {
            return putString(cr, name, Long.toString(value));
        }

        public static float getFloat(ContentResolver cr, String name, float def) {
            float parseFloat;
            String v = getString(cr, name);
            if (v != null) {
                try {
                    parseFloat = Float.parseFloat(v);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            parseFloat = def;
            return parseFloat;
        }

        public static float getFloat(ContentResolver cr, String name) throws SettingNotFoundException {
            String v = getString(cr, name);
            if (v != null) {
                try {
                    return Float.parseFloat(v);
                } catch (NumberFormatException e) {
                    throw new SettingNotFoundException(name);
                }
            }
            throw new SettingNotFoundException(name);
        }

        public static boolean putFloat(ContentResolver cr, String name, float value) {
            return putString(cr, name, Float.toString(value));
        }

        public static int getIntAtIndex(ContentResolver cr, String name, int index) {
            String v = getString(cr, name);
            if (v != null) {
                String[] valArray = v.split(",");
                if (index >= 0 && index < valArray.length && valArray[index] != null) {
                    try {
                        return Integer.parseInt(valArray[index]);
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return 0;
        }

        public static boolean putIntAtIndex(ContentResolver cr, String name, int index, int value) {
            String data = "";
            String[] valArray = null;
            String v = getString(cr, name);
            if (v != null) {
                valArray = v.split(",");
            }
            int i = 0;
            while (i < index) {
                String str = "";
                if (valArray != null && i < valArray.length) {
                    str = valArray[i];
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(data);
                stringBuilder.append(str);
                stringBuilder.append(",");
                data = stringBuilder.toString();
                i++;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(data);
            stringBuilder2.append(value);
            data = stringBuilder2.toString();
            if (valArray != null) {
                for (i = index + 1; i < valArray.length; i++) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(data);
                    stringBuilder3.append(",");
                    stringBuilder3.append(valArray[i]);
                    data = stringBuilder3.toString();
                }
            }
            return putString(cr, name, data);
        }
    }

    public static final class Secure extends NameValueTable {
        public static final String ACTIONS_ENHANCED_SCREENSHOT = "actions_enhanced_screenshot";
        public static final String ACTIONS_MUSIC_MODE = "actions_music_mode";
        public static final String AGPS_FEATURE_ENABLED = "agps_feature_enabled";
        public static final String AGPS_GPSONE_USER_PLANE = "agps_gpsone_user_plane";
        public static final String ALT_SHIFT_TOGGLE_FTR_AVAILABLE = "alt_shift_toggle_ftr_available";
        public static final String ANC_MODE = "anc_mode";
        public static final String AOV_GOOGLE_NOW_COMPONENT_NAME = "aov_google_now_component_name";
        public static final String APN_CHECK_STATE = "APN_CHECK_STATE";
        public static final String ASSISTED_DIALING_STATE = "assisted_dialing_state";
        public static final String AUDIO_ROUTING = "audio_routing";
        public static final String AUTH_DEVICES_ENABLED = "auth_devices_enabled";
        public static final String AUTH_DEVICE_LIST = "auth_device_list";
        public static final String AUTO_DISABLE_TRACKPAD = "lapdock_disable_trackpad";
        public static final String AUTO_SYSTEM_CHECK_ENABLED = "auto_system_check";
        public static final String BACK_GROUND_DATA_BACKUP_BY_DATAMANAGER = "back_ground_data_backup_by_datamanager";
        public static final String BT_MFB_ENABLED_WHEN_LOCKED = "bluetooth_mfb_enabled_when_locked";
        public static final String CALLING_33860_ENABLED = "calling_33860_enabled";
        public static final String CALLING_GLOBAL_CONTROLS_ENABLE = "calling_global_controls_enable";
        public static final String CALLING_GSM_AD_ENABLED = "calling_gsm_ad_enabled";
        public static final String CALL_CONNECT_TONE = "call_connect_tone";
        public static final String CCC_DEVICE_ID = "ccc.did";
        public static final String CCC_DEVICE_SECRET = "ccc.dsec";
        public static final String CCC_DEVICE_SESSION_TOKEN = "ccc.sst";
        public static final String[] CLONE_TO_MANAGED_PROFILE = new String[0];
        public static final Uri CONTENT_URI = Uri.parse("content://com.motorola.android.providers.settings/secure");
        public static final String CUR_COUNTRY_AREA_CODE = "cur_country_area_code";
        public static final String CUR_COUNTRY_CODE = "cur_country_code";
        public static final String CUR_COUNTRY_IDD = "cur_country_idd";
        public static final String CUR_COUNTRY_MCC = "cur_country_mcc";
        public static final String CUR_COUNTRY_MDN_LEN = "cur_country_mdn_len";
        public static final String CUR_COUNTRY_NAME = "cur_country_name";
        public static final String CUR_COUNTRY_NDD = "cur_country_ndd";
        public static final String CUR_COUNTRY_UPDATED_BY_USER = "cur_country_updated_by_user";
        public static final String CUR_MDN = "cur_mdn";
        public static final String DATASWITCH_FEATURE_ENABLED = "dataswitch_feature_enabled";
        public static final String DATASWITCH_SYNC_CONNECT_VALUE = "dataswitch_sync_connect_value";
        public static final String DATA_ROAMING_ACCESS_FEATURE_ENABLED = "data_roaming_access_feature_enabled";
        public static final String DEMO_MODE_ENABLED = "demo_mode_enabled";
        public static final String DEMO_MODE_VIDEO_DOWNLOAD_URI = "demo_video_download_uri";
        public static final String DEMO_MODE_VIDEO_PATH_IN_SDCARD = "demo_video_path_in_sdcard";
        public static final String DEMO_MODE_VIDEO_PATH_IN_SYSTEM = "demo_video_path_in_system";
        public static final String DIALUP_MODEM_RESTRICTION = "dialup_modem_restriction";
        public static final String DISABLE_DATA_ECN0_THRESHOLD = "disable_data_ecn0";
        public static final String DISABLE_DATA_RSCP_THRESHOLD = "disable_data_rscp";
        public static final String DISABLE_DATA_RSSI_THRESHOLD = "disable_data_rssi";
        public static final String DISPLAY_OFF_BY_FPS = "display_off_by_fps";
        public static final String DOMESTIC_CALL_GUARD = "domestic_call_guard";
        public static final String DOMESTIC_CALL_GUARD_FORCED = "domestic_call_guard_forced";
        public static final String DOMESTIC_DATA_ROAMING_FORCED = "domestic_data_roaming_forced";
        public static final String DOMESTIC_DATA_ROAMING_GUARD = "domestic_data_roaming_guard";
        public static final String DOMESTIC_DATA_ROAMING_GUARD_FORCED = "domestic_data_roaming_guard_forced";
        public static final String DOMESTIC_DATA_ROAMING_UI = "domestic_data_roaming_ui";
        public static final String DOMESTIC_VOICE_ROAMING = "domestic_voice_roaming";
        public static final String DOMESTIC_VOICE_ROAMING_FORCED = "domestic_voice_roaming_forced";
        public static final String DOUBLE_TAP = "double_tap";
        public static final String DOWNLOAD_WALLPAPER = "enable_download_wallpaper";
        public static final String DUN_APN_CHANGABLE_CHECK = "dun_apn_changable";
        public static final String DUN_APN_HIDE_CHECK = "dun_apn_hide";
        public static final String EMERGENCY_CALL_SHORTCUT_ENABLE = "emergency_call_shortcut_enable";
        public static final String EMERGENCY_SMS_ACTIVE = "emergency_sms_active";
        public static final String ENABLE_DATA_ECN0_THRESHOLD = "eable_data_ecn0";
        public static final String ENABLE_DATA_RSCP_THRESHOLD = "enable_data_rscp";
        public static final String ENABLE_DATA_RSSI_THRESHOLD = "enable_data_rssi";
        public static final String ENABLE_MMS_WHEN_DATA_DISABLED = "enable_mms_when_data_disabled";
        public static final String ENABLE_MO_SMS_OVER_IMS = "enable_mo_sms_over_ims";
        public static final String ENABLE_ROAMING_BROKER_38132 = "enable_roaming_broker_38132";
        public static final String ENABLE_TEXT_MSG_REPLY = "qsms_enable_text_message_reply";
        public static final String ERI_ALERT_SOUNDS = "eri_alert_sounds";
        public static final String ERI_TEXT_BANNER = "eri_text_banner";
        public static final String FID_33463_ENABLED = "fid_33463_enabled";
        public static final String FID_34387_MULTIMODE = "fid_34387_multimode";
        public static final String FLAG_ALLOW_ACCESS_ONLY_FOR_THIS_TRIP = "flag_allow_access_only_for_this_trip";
        public static final String FORCE_ACTIVITIES_ABOVE_KEYGUARD = "force_activities_above_keyguard";
        public static final String FPS_IS_ENROLLMENT = "fps_is_enrollment";
        public static final String FPS_ONENAV_ENABLED = "fps_onenav_enabled";
        public static final String FPS_ONENAV_HAPTIC_FEEDBACK_ENABLED = "fps_onenav_haptic_feedback_enabled";
        public static final String FPS_ONENAV_HOLD_COUNT = "fps_onenave_hold_count";
        public static final String FPS_ONENAV_LONG_HOLD_COUNT = "fps_onenave_long_hold_count";
        public static final String FPS_ONENAV_SHOW_NAVIGATION_BAR = "fps_onenav_show_navigation_bar";
        public static final String FPS_ONENAV_SWIPE_DIRECTION = "fps_onenav_swipe_direction";
        public static final String FPS_ONENAV_SWIPE_LEFT_COUNT = "fps_onenave_swipe_left_count";
        public static final String FPS_ONENAV_SWIPE_RIGHT_COUNT = "fps_onenave_swipe_right_count";
        public static final String FPS_ONENAV_TAP_COUNT = "fps_onenave_tap_count";
        public static final String FPS_ONENAV_TUTORIAL_MODE = "fps_onenav_tutorial_mode";
        public static final String FPS_SIDE_GESTURE_APP_SCROLLING_ENABLED = "fps_slidegesture_app_scrolling_enabled";
        public static final String FPS_SIDE_GESTURE_DIRECTION_REVERSE = "fps_slidegesture_direction_reverse";
        public static final String FPS_SIDE_GESTURE_ENABLED = "fps_slidegesture_enabled";
        public static final String FPS_SIDE_GESTURE_SHOTCUT_ENABLED = "fps_slidegesture_shotcut_enabled";
        public static final String FTM_FDN_DND_TURNED_OFF = "ftm_fdn_dnd_turned_off";
        public static final String FTR_35605_SPRINT_ROAMING_ENABLED = "ftr_35605_sprint_roaming_enabled";
        public static final String FTR_CELL_BROADCAST_ENABLED = "ftr_cell_broadcast_enabled";
        public static final String FTR_FDN_NOTIFY_ENABLED = "ftr_fdn_notify_enabled";
        public static final String FULL_CHARGE_NOTIFICATION_ENABLE = "full_charge_notification_enable";
        public static final String GLOBAL_TOUCH_LISTENER = "global_touch_listener";
        public static final String GOOGLE_NOW_INTENTS_ALLOWED_ABOVE_KEYGUARD = "google_now_intents_allowed_above_keyguard";
        public static final String GPSONE_XTRA_DOWNLOADABLE = "gpsone_xtra_downloadable";
        public static final String GSM_DATA_GUARD = "gsm_data_guard";
        public static final String GSM_DATA_GUARD_FORCED = "gsm_data_guard_forced";
        public static final String GSM_DATA_ROAMING_FORCED = "gsm_data_roaming_forced";
        public static final String GSM_DATA_ROAMING_UI = "gsm_data_roaming_ui";
        public static final String GSM_OUTGOING_SMS_GUARD = "gsm_outgoing_sms_guard";
        public static final String GSM_OUTGOING_SMS_GUARD_FORCED = "gsm_outgoing_sms_guard_forced";
        public static final String GSM_VOICE_ROAMING_GUARD = "gsm_voice_roaming_guard";
        public static final String GSM_VOICE_ROAMING_GUARD_FORCED = "gsm_voice_roaming_guard_forced";
        public static final String HDMI_OVERSCAN = "hdmi_overscan";
        public static final String HFA_RUNNING = "hfa_running";
        public static final String HIDDENMENU_DDTM_DEFAULT_PREFERENCE_SETTINGS = "hiddenmenu_ddtm_default_preference_settings";
        public static final String HUXVMM_FILE_HANDLE_SETTING = "huxvmm_file_handle";
        public static final String HYPHENATION_CHECK = "hyphenation_feature_enabled";
        public static final String ICE_CONTACTS_ENABLED = "ice_contacts_enabled";
        public static final String INTERNATIONAL_CALL_GUARD = "international_call_guard";
        public static final String INTERNATIONAL_CALL_GUARD_FORCED = "international_call_guard_forced";
        public static final String INTERNATIONAL_DATA_ROAMING_FORCED = "international_data_roaming_forced";
        public static final String INTERNATIONAL_DATA_ROAMING_GUARD = "international_data_roaming_guard";
        public static final String INTERNATIONAL_DATA_ROAMING_GUARD_FORCED = "international_data_roaming_guard_forced";
        public static final String INTERNATIONAL_DATA_ROAMING_UI = "international_data_roaming_ui";
        public static final String INTERNATIONAL_OUTGOINGSMS_GUARD = "international_outgoingsms_guard";
        public static final String INTERNATIONAL_OUTGOINGSMS_GUARD_FORCED = "international_outgoingsms_guard_forced";
        public static final String INTERNATIONAL_VOICE_ROAMING = "international_voice_roaming";
        public static final String INTERNATIONAL_VOICE_ROAMING_FORCED = "international_voice_roaming_forced";
        public static final String IS_TALKBACK_ON = "is_talkback_on";
        public static final String KEYBOARD_BACKLIGHT_CONTROL_MODE = "kbd_backlight_control_mode";
        public static final String KEYBOARD_BACKLIGHT_TIMEOUT = "kbd_backlight_timeout";
        public static final String KEYBOARD_BRIGHTNESS = "kbd_backlight_brightness";
        public static final String LAPDOCK_CHARGING_MODE = "lapdock_charging_mode";
        public static final String LOCK_FINGERPRINT = "lock_fingerprint";
        public static final String LOCK_PIN_CURRENT_FAILED_ATTEMPTS = "lock_pin_current_failed_attempts";
        public static final String LOCK_TIMER = "lock_timer";
        public static final String LOCK_TYPE = "lock_type";
        public static final String MCC_WHITE_LIST = "roaming_mcc_table";
        public static final String MOBILE_DATA_COVERAGE_CONDITIONER = "mobile_data_coverage_conditioner";
        public static final String MOBILE_DATA_DISABLE = "mobile_data_disable";
        public static final String MOBILE_SYNC_WIFI_URL = "mobile_sync_wifi_url";
        public static final String MOD_BATTERY_MODE = "mod_battery_mode";
        public static final String MOTO_DT_RECOMMENDATIONS_ENABLED = "moto_dt_recommendations_enabled";
        private static final HashSet<String> MOVED_TO_GLOBAL = new HashSet();
        public static final String MULTI_SIM_SIM1_COLOR = "multi_sim_sim1_color";
        public static final String MULTI_SIM_SIM1_NAME = "multi_sim_sim1_name";
        public static final String MULTI_SIM_SIM2_COLOR = "multi_sim_sim2_color";
        public static final String MULTI_SIM_SIM2_NAME = "multi_sim_sim2_name";
        public static final String NETWORK_LOST_TONE = "network_lost_tone";
        public static final String NETWORK_SETTING_ON_BOOT = "network_setting_on_boot";
        public static final String NEXT_ALARM_UTC = "next_alarm_utc";
        public static final String NFC_AUTH_ENABLED = "nfc_auth_enabled";
        public static final String NFC_AUTH_FAILED_ATTEMPTS = "nfc_auth_failed_attempts";
        public static final String NIGHT_DISPLAY_INTENSITY = "night_display_intensity";
        public static final String PDP_WATCHDOG_PING_DEADLINE = "pdp_watchdog_ping_deadline";
        public static final String PIP_ACTIVE = "pip_active";
        public static final String PLMN_BLACK_LIST = "black_list_roaming_plmn_table";
        public static final String PLMN_FEATURE_ENABLE = "roaming_plmn_between_carriers_enabled";
        public static final String POINTER_ACCELERATION = "pointer_acceleration";
        public static final String POINTER_SPEED_LEVEL = "pointer_speed_level";
        public static final String POWER_KEY_DOUBLE_TAP_APP = "pwr_double_tap_app";
        public static final String POWER_SAVER_ENABLED = "power_saver_enabled";
        public static final String PREFERRED_MNC_MCC = "preferred_mnc_mcc";
        public static final String PREFERRED_P2P_AUTO_CONNECT_SUPPORT = "preferred_p2p_auto_connect_support";
        public static final String PREFERRED_P2P_BAND_FOR_AGO = "preferred_p2p_band_for_ago";
        public static final String PREFERRED_P2P_DEVICE_LIMIT_FOR_AGO = "preferred_p2p_device_limit_for_ago";
        public static final String PREFERRED_P2P_DEVICE_TIMEOUT_FOR_AGO = "preferred_p2p_device_timeout_for_ago";
        public static final String PRIVACY_ALWAYS_ON_VOICE = "privacy_always_on_voice";
        public static final String PRIVACY_AOV_BYPASS_KEYGUARD_GOOGLE_NOW_KILL_SWITCH = "privacy_aov_bypass_keyguard_google_now_kill_switch";
        public static final String PRIVACY_AOV_BYPASS_KEYGUARD_GOOGLE_NOW_USER_SETTING = "privacy_aov_bypass_keyguard_google_now_user_setting";
        public static final String PRIVACY_DROID_BLAST = "privacy_droid_blast";
        public static final String PRIVACY_HELP_IMPROVE_PRODUCTS = "privacy_help_improve_products";
        public static final String PRIVACY_MOTOROLA_TERMS_OF_SERVICE = "privacy_motorola_terms_of_service";
        public static final String PRIVACY_MOTO_MARKETING = "privacy_moto_marketing";
        public static final String PRIVACY_OTA_UPDATE = "privacy_ota_update";
        public static final String PRIVACY_SMART_ACTIONS = "privacy_smart_actions";
        public static final String PRIVACY_SMART_NOTIFICATIONS_CHROME = "privacy_smart_notifications_chrome";
        public static final String PRIVACY_SUPPORT_DEVICE = "privacy_support_device";
        public static final String REF_COUNTRY_AREA_CODE = "ref_country_area_code";
        public static final String REF_COUNTRY_CODE = "ref_country_code";
        public static final String REF_COUNTRY_IDD = "ref_country_idd";
        public static final String REF_COUNTRY_MCC = "ref_country_mcc";
        public static final String REF_COUNTRY_MDN_LEN = "ref_country_mdn_len";
        public static final String REF_COUNTRY_NAME = "ref_country_name";
        public static final String REF_COUNTRY_NDD = "ref_country_ndd";
        public static final String RESTRICTION_LOCK = "restriction_lock";
        public static final String RESTRICT_BG_DATA_ON_LOW_COVERAGE = "restrict_bg_data_on_low_coverage";
        public static final String ROAMING_PLMN_TABLE = "roaming_plmn_table";
        public static final String SCREENSHOT_TRIGGER_GESTURE = "screenshot_trigger_gesture";
        public static final String SCREEN_LOCK_ENABLED = "screen_lock";
        public static final String SERVICE_RESET = "service_reset";
        public static final String SETTING_ALLOW_EDITING_CLASS3_APN = "allow_editing_class3_apn";
        public static final String SETTING_CHECK_CFU_POWERON = "check_cfu_poweron";
        public static final String SETTING_DATA_UNIT_DISPLAY_GB = "data_unit_display_gb";
        public static final String SETTING_DEF_PLUS_CODE_DIALING_IDD_PREFIX = "def_plus_code_dialing_idd_prefix";
        public static final String SETTING_FTR_33859_ENABLED = "sim_33859_isenabled";
        public static final String SETTING_FTR_DUN_NAT_ENABLED = "dun_nat_enabled";
        public static final String SETTING_FTR_ICE_ENABLED = "ice_isenabled";
        public static final String SETTING_FTR_MULTIPLEPDP_ENABLED = "multiple_pdp_isenabled";
        public static final String SETTING_FTR_RINGER_SWITCH_ENABLE = "ftr_ringer_switch_enable";
        public static final String SETTING_FTR_SMARTDIALER_LANGUAGE_CODE = "smartdialer_language_code";
        public static final String SETTING_FTR_TETHER_REVERSE_NAT = "tether_reverse_nat_enabled";
        public static final String SETTING_NORTH_AMERICAN_DIALING_STATE = "north_american_dialing_enabled";
        public static final String SETTING_PLUS_CODE_DIALING_IDD_PREFIX = "plus_code_dialing_idd_prefix";
        public static final String SHOW_FPS_ENROLL_DIALOG = "show_fps_enroll_dialog";
        public static final String SHOW_FPS_LOCK_NOTIFICATION = "show_fps_lock_notification";
        public static final String SHOW_NFC_ICON_ON_SYSTEMUI = "show_nfc_icon_on_systemui";
        public static final String SN_AON = "sn_aon";
        public static final String SN_AUTO_SELECT_INSTALLED = "sn_auto_select_installed";
        public static final String SN_NIGHT_BEGIN = "sn_night_begin";
        public static final String SN_NIGHT_END = "sn_night_end";
        public static final String SN_NIGHT_OFF = "sn_night_off";
        public static final String SN_PRIVACY = "sn_privacy";
        public static final String SN_WHITE_LIST = "sn_white_list";
        public static final String SOFTONENAV_DISCOVERY = "softonenav_discovery";
        public static final String SOFTONENAV_TUTORIAL_ANIMATION = "softonenav_tutorial_animation";
        public static final String SOFTWARE_UPDATE_ALERT_ENABLED = "software_update_alert";
        public static final String SPRINT_OFFLOAD_FLEX = "sprint_offload_flex";
        public static final String STATUS_BAR_VISIBILITY = "status_bar_visibility";
        public static final String SYNCHRONIZED_VIBRATION_WITH_RINGTONE = "synchronized_vibration_with_ringtone";
        public static final String SYSTEMUI_SOFTONENAV_ENABLED = "systemui_softonenav_enabled";
        public static final String SYSTEMUI_SOFTONENAV_NAVBAR_HEIGHT = "systemui_softonenav_navbar_height";
        public static final String SYSTEMUI_SOFTONENAV_VERSION = "systemui_softonenav_version";
        public static final String TAP_TO_SELECT = "tap_to_select";
        public static final String TASK_BUTTON_PRESS_BEHAVIOR = "task_button_press_behavior";
        public static final String TSB_DISABLE_FLAGS = "tsb_disable_flags";
        public static final String TTS_CALLER_ID_READOUT = "tts_caller_id_readout";
        public static final String USER_NEED_ACCEPT_MOTO_AGREEMENT = "user_need_accept_moto_agreement";
        public static final String USER_SETUP_COMPLETE_TIMESTAMP = "user_setup_complete_timestamp";
        public static final String VIBRATE_PATTERNS = "vibrate_patterns";
        public static final String VIEWSERVER_IN_SECUREBUILD_ENABLED = "viewserver_in_securebuild_enabled";
        public static final String VM_NUMBER_CDMA = "vm_number_cdma";
        public static final String VM_VVM_ROAMING_SELECTION = "vm_vvm_roaming_selection";
        public static final String VM_VVM_SELECTION = "vm_vvm_selection";
        public static final String VOICEMAIL_HIDE_MWI_ENABLED = "voicemail_hide_mwi_enabled";
        public static final String VOICE_PRIORITY_ENABLED = "att_voice_priority";
        public static final String WIFI_ADHOC_CHANNEL_NUMBER = "wifi_adhoc_channel_number";
        public static final String WIFI_AP_DHCP_END_ADDR = "wifi_ap_dhcp_end_addr";
        public static final String WIFI_AP_DHCP_START_ADDR = "wifi_ap_dhcp_start_addr";
        public static final String WIFI_AP_DNS1 = "wifi_ap_dns1";
        public static final String WIFI_AP_DNS2 = "wifi_ap_dns2";
        public static final String WIFI_AP_FREQUENCY = "wifi_ap_frequency";
        public static final String WIFI_AP_GATEWAY = "wifi_ap_gateway";
        public static final String WIFI_AP_HIDDEN = "wifi_ap_hidden";
        public static final String WIFI_AP_MAX_SCB = "wifi_ap_max_scb";
        public static final String WIFI_AP_NETMASK = "wifi_ap_netmask";
        public static final String WIFI_DISABLED_BY_ECM = "wifi_disabled_by_ecm";
        public static final String WIFI_DUAL_BAND_SUPPORT = "wifi_dual_band_support";
        public static final String WIFI_HOTSPOT_AUTOCONNECT_ON = "wifi_hotspot_autoconnect";
        public static final String WIFI_HOTSPOT_NOTIFY_ON = "wifi_hotspot_notify";
        public static final String WIFI_NETWORKS_SECURE_AVAILABLE_NOTIFICATION_ON = "wifi_networks_secure_available_notification_on";
        public static final String WIFI_OFFLOAD_FLAG = "wifi_offload_flag";
        public static final String WIFI_P2P_DEVICE_NAME = "wifi_p2p_device_name";
        public static final String WIFI_PROXY = "wifi_proxy";
        public static final String WIFI_PROXY_EXCEPTIONS = "wifi_proxy_exceptions";
        public static final String WIFI_USE_AUTO_IP = "wifi_use_auto_ip";
        private static final ContentProviderHolder sProviderHolder = new ContentProviderHolder(CONTENT_URI);
        private static final NameValueCache sNameValueCache = new NameValueCache(CONTENT_URI, "GET_secure", "PUT_secure", sProviderHolder);

        static {
            MOVED_TO_GLOBAL.add("multi_sim_sim1_name");
            MOVED_TO_GLOBAL.add("multi_sim_sim2_name");
            MOVED_TO_GLOBAL.add("multi_sim_sim1_color");
            MOVED_TO_GLOBAL.add("multi_sim_sim2_color");
        }

        public static void getMovedToGlobalSettings(Set<String> outKeySet) {
            outKeySet.addAll(MOVED_TO_GLOBAL);
        }

        public static String getString(ContentResolver resolver, String name) {
            return getStringForUser(resolver, name, UserHandle.myUserId());
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            if (!MOVED_TO_GLOBAL.contains(name)) {
                return sNameValueCache.getStringForUser(resolver, name, userHandle);
            }
            String str = MotorolaSettings.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Motorola Setting ");
            stringBuilder.append(name);
            stringBuilder.append(" has moved from android.provider.MotorolaSettings.Secure to android.provider.MotorolaSettings.Global.");
            Log.w(str, stringBuilder.toString());
            return Global.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putString(ContentResolver resolver, String name, String value) {
            return putStringForUser(resolver, name, value, UserHandle.myUserId());
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, int userHandle) {
            return putStringForUser(resolver, name, value, null, false, userHandle);
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, String tag, boolean makeDefault, int userHandle) {
            if (!MOVED_TO_GLOBAL.contains(name)) {
                return sNameValueCache.putStringForUser(resolver, name, value, tag, makeDefault, userHandle);
            }
            String str = MotorolaSettings.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MotorolaSetting ");
            stringBuilder.append(name);
            stringBuilder.append(" has moved from android.provider.MotorolaSettings.System to android.provider.MotorolaSettings.Global");
            Log.w(str, stringBuilder.toString());
            return Global.putStringForUser(resolver, name, value, tag, makeDefault, userHandle);
        }

        public static void resetToDefaults(ContentResolver resolver, String tag) {
            resetToDefaultsAsUser(resolver, tag, 1, UserHandle.myUserId());
        }

        public static void resetToDefaultsAsUser(ContentResolver resolver, String tag, int mode, int userHandle) {
            try {
                Bundle arg = new Bundle();
                arg.putInt("_user", userHandle);
                if (tag != null) {
                    arg.putString("_tag", tag);
                }
                arg.putInt("_reset_mode", mode);
                sProviderHolder.getProvider(resolver).call(resolver.getPackageName(), "RESET_secure", null, arg);
            } catch (RemoteException e) {
                String str = MotorolaSettings.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Can't reset do defaults for ");
                stringBuilder.append(CONTENT_URI);
                Log.w(str, stringBuilder.toString(), e);
            }
        }

        public static Uri getUriFor(String name) {
            if (!MOVED_TO_GLOBAL.contains(name)) {
                return NameValueTable.getUriFor(CONTENT_URI, name);
            }
            String str = MotorolaSettings.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MotorolaSetting ");
            stringBuilder.append(name);
            stringBuilder.append(" has moved from android.provider.MotorolaSettings.Secure to android.provider.MotorolaSettings.Global, returning global URI.");
            Log.w(str, stringBuilder.toString());
            return NameValueTable.getUriFor(Global.CONTENT_URI, name);
        }

        public static int getInt(ContentResolver cr, String name, int def) {
            return getIntForUser(cr, name, def, UserHandle.myUserId());
        }

        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            int parseInt;
            String v = getStringForUser(cr, name, userHandle);
            if (v != null) {
                try {
                    parseInt = Integer.parseInt(v);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            parseInt = def;
            return parseInt;
        }

        public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
            return getIntForUser(cr, name, UserHandle.myUserId());
        }

        public static int getIntForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            try {
                return Integer.parseInt(getStringForUser(cr, name, userHandle));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putInt(ContentResolver cr, String name, int value) {
            return putIntForUser(cr, name, value, UserHandle.myUserId());
        }

        public static boolean putIntForUser(ContentResolver cr, String name, int value, int userHandle) {
            return putStringForUser(cr, name, Integer.toString(value), userHandle);
        }

        public static long getLong(ContentResolver cr, String name, long def) {
            return getLongForUser(cr, name, def, UserHandle.myUserId());
        }

        public static long getLongForUser(ContentResolver cr, String name, long def, int userHandle) {
            String valString = getStringForUser(cr, name, userHandle);
            if (valString == null) {
                return def;
            }
            try {
                return Long.parseLong(valString);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        public static long getLong(ContentResolver cr, String name) throws SettingNotFoundException {
            return getLongForUser(cr, name, UserHandle.myUserId());
        }

        public static long getLongForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            try {
                return Long.parseLong(getStringForUser(cr, name, userHandle));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putLong(ContentResolver cr, String name, long value) {
            return putLongForUser(cr, name, value, UserHandle.myUserId());
        }

        public static boolean putLongForUser(ContentResolver cr, String name, long value, int userHandle) {
            return putStringForUser(cr, name, Long.toString(value), userHandle);
        }

        public static float getFloat(ContentResolver cr, String name, float def) {
            return getFloatForUser(cr, name, def, UserHandle.myUserId());
        }

        public static float getFloatForUser(ContentResolver cr, String name, float def, int userHandle) {
            float parseFloat;
            String v = getStringForUser(cr, name, userHandle);
            if (v != null) {
                try {
                    parseFloat = Float.parseFloat(v);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            parseFloat = def;
            return parseFloat;
        }

        public static float getFloat(ContentResolver cr, String name) throws SettingNotFoundException {
            return getFloatForUser(cr, name, UserHandle.myUserId());
        }

        public static float getFloatForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            String v = getStringForUser(cr, name, userHandle);
            if (v != null) {
                try {
                    return Float.parseFloat(v);
                } catch (NumberFormatException e) {
                    throw new SettingNotFoundException(name);
                }
            }
            throw new SettingNotFoundException(name);
        }

        public static boolean putFloat(ContentResolver cr, String name, float value) {
            return putFloatForUser(cr, name, value, UserHandle.myUserId());
        }

        public static boolean putFloatForUser(ContentResolver cr, String name, float value, int userHandle) {
            return putStringForUser(cr, name, Float.toString(value), userHandle);
        }
    }

    public static final class System extends NameValueTable {
        public static final Uri CONTENT_URI = Uri.parse("content://com.motorola.android.providers.settings/system");
        private static final ContentProviderHolder sProviderHolder = new ContentProviderHolder(CONTENT_URI);
        private static final NameValueCache sNameValueCache = new NameValueCache(CONTENT_URI, "GET_system", "PUT_system", sProviderHolder);

        public static String getString(ContentResolver resolver, String name) {
            return getStringForUser(resolver, name, UserHandle.myUserId());
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            return sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putString(ContentResolver resolver, String name, String value) {
            return putStringForUser(resolver, name, value, UserHandle.myUserId());
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, int userHandle) {
            return sNameValueCache.putStringForUser(resolver, name, value, null, false, userHandle);
        }

        public static Uri getUriFor(String name) {
            return NameValueTable.getUriFor(CONTENT_URI, name);
        }

        public static int getInt(ContentResolver cr, String name, int def) {
            return getIntForUser(cr, name, def, UserHandle.myUserId());
        }

        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            int parseInt;
            String v = getStringForUser(cr, name, userHandle);
            if (v != null) {
                try {
                    parseInt = Integer.parseInt(v);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            parseInt = def;
            return parseInt;
        }

        public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
            return getIntForUser(cr, name, UserHandle.myUserId());
        }

        public static int getIntForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            try {
                return Integer.parseInt(getStringForUser(cr, name, userHandle));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putInt(ContentResolver cr, String name, int value) {
            return putIntForUser(cr, name, value, UserHandle.myUserId());
        }

        public static boolean putIntForUser(ContentResolver cr, String name, int value, int userHandle) {
            return putStringForUser(cr, name, Integer.toString(value), userHandle);
        }

        public static long getLong(ContentResolver cr, String name, long def) {
            return getLongForUser(cr, name, def, UserHandle.myUserId());
        }

        public static long getLongForUser(ContentResolver cr, String name, long def, int userHandle) {
            String valString = getStringForUser(cr, name, userHandle);
            if (valString == null) {
                return def;
            }
            try {
                return Long.parseLong(valString);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        public static long getLong(ContentResolver cr, String name) throws SettingNotFoundException {
            return getLongForUser(cr, name, UserHandle.myUserId());
        }

        public static long getLongForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            try {
                return Long.parseLong(getStringForUser(cr, name, userHandle));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putLong(ContentResolver cr, String name, long value) {
            return putLongForUser(cr, name, value, UserHandle.myUserId());
        }

        public static boolean putLongForUser(ContentResolver cr, String name, long value, int userHandle) {
            return putStringForUser(cr, name, Long.toString(value), userHandle);
        }

        public static float getFloat(ContentResolver cr, String name, float def) {
            return getFloatForUser(cr, name, def, UserHandle.myUserId());
        }

        public static float getFloatForUser(ContentResolver cr, String name, float def, int userHandle) {
            float parseFloat;
            String v = getStringForUser(cr, name, userHandle);
            if (v != null) {
                try {
                    parseFloat = Float.parseFloat(v);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            parseFloat = def;
            return parseFloat;
        }

        public static float getFloat(ContentResolver cr, String name) throws SettingNotFoundException {
            return getFloatForUser(cr, name, UserHandle.myUserId());
        }

        public static float getFloatForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            String v = getStringForUser(cr, name, userHandle);
            if (v != null) {
                try {
                    return Float.parseFloat(v);
                } catch (NumberFormatException e) {
                    throw new SettingNotFoundException(name);
                }
            }
            throw new SettingNotFoundException(name);
        }

        public static boolean putFloat(ContentResolver cr, String name, float value) {
            return putFloatForUser(cr, name, value, UserHandle.myUserId());
        }

        public static boolean putFloatForUser(ContentResolver cr, String name, float value, int userHandle) {
            return putStringForUser(cr, name, Float.toString(value), userHandle);
        }
    }

    static {
        MOVED_TO_SECURE.add("sn_white_list");
        MOVED_TO_SECURE.add("sn_privacy");
        MOVED_TO_SECURE.add("sn_night_off");
        MOVED_TO_SECURE.add("sn_night_begin");
        MOVED_TO_SECURE.add("sn_night_end");
        MOVED_TO_SECURE.add("sn_aon");
        MOVED_TO_SECURE.add("sn_auto_select_installed");
        MOVED_TO_SECURE.add("privacy_support_device");
        MOVED_TO_SECURE.add("privacy_help_improve_products");
        MOVED_TO_SECURE.add("privacy_ota_update");
        MOVED_TO_SECURE.add("privacy_moto_marketing");
        MOVED_TO_SECURE.add("privacy_smart_actions");
        MOVED_TO_SECURE.add("privacy_smart_notifications_chrome");
        MOVED_TO_SECURE.add("privacy_always_on_voice");
        MOVED_TO_SECURE.add("lock_type");
        MOVED_TO_SECURE.add("double_tap");
        MOVED_TO_SECURE.add("is_talkback_on");
        MOVED_TO_SECURE.add("tts_caller_id_readout");
        MOVED_TO_SECURE.add("wifi_offload_flag");
        MOVED_TO_SECURE.add("power_saver_enabled");
        MOVED_TO_SECURE.add("flag_allow_access_only_for_this_trip");
        MOVED_TO_SECURE.add(Secure.CCC_DEVICE_ID);
        MOVED_TO_SECURE.add(Secure.CCC_DEVICE_SESSION_TOKEN);
        MOVED_TO_SECURE.add(Secure.CCC_DEVICE_SECRET);
        MOVED_TO_SECURE.add("auth_devices_enabled");
        MOVED_TO_SECURE.add("auth_device_list");
        MOVED_TO_SECURE.add("nfc_auth_failed_attempts");
        MOVED_TO_SECURE.add("nfc_auth_enabled");
        MOVED_TO_SECURE.add("service_reset");
        MOVED_TO_SECURE.add("double_tap");
        MOVED_TO_SECURE.add("wifi_use_auto_ip");
        MOVED_TO_SECURE.add("wifi_proxy");
        MOVED_TO_SECURE.add("wifi_proxy_exceptions");
        MOVED_TO_SECURE.add("wifi_hotspot_autoconnect");
        MOVED_TO_SECURE.add("wifi_hotspot_notify");
        MOVED_TO_SECURE.add("wifi_ap_dhcp_start_addr");
        MOVED_TO_SECURE.add("wifi_ap_dhcp_end_addr");
        MOVED_TO_SECURE.add("wifi_ap_gateway");
        MOVED_TO_SECURE.add("wifi_ap_netmask");
        MOVED_TO_SECURE.add("wifi_ap_dns1");
        MOVED_TO_SECURE.add("wifi_ap_dns2");
        MOVED_TO_SECURE.add("wifi_ap_max_scb");
        MOVED_TO_SECURE.add("wifi_ap_frequency");
        MOVED_TO_SECURE.add("wifi_ap_hidden");
        MOVED_TO_SECURE.add("wifi_p2p_device_name");
        MOVED_TO_SECURE.add("wifi_networks_secure_available_notification_on");
        MOVED_TO_SECURE.add("wifi_dual_band_support");
        MOVED_TO_SECURE.add("eri_alert_sounds");
        MOVED_TO_SECURE.add("eri_text_banner");
        MOVED_TO_SECURE.add("software_update_alert");
        MOVED_TO_SECURE.add("auto_system_check");
        MOVED_TO_SECURE.add("vm_number_cdma");
        MOVED_TO_SECURE.add("next_alarm_utc");
        MOVED_TO_SECURE.add("lock_timer");
        MOVED_TO_SECURE.add("screen_lock");
        MOVED_TO_SECURE.add("lock_pin_current_failed_attempts");
        MOVED_TO_SECURE.add("lock_fingerprint");
        MOVED_TO_SECURE.add("network_lost_tone");
        MOVED_TO_SECURE.add("call_connect_tone");
        MOVED_TO_SECURE.add("viewserver_in_securebuild_enabled");
        MOVED_TO_SECURE.add("agps_feature_enabled");
        MOVED_TO_SECURE.add("agps_gpsone_user_plane");
        MOVED_TO_SECURE.add("gpsone_xtra_downloadable");
        MOVED_TO_SECURE.add("restriction_lock");
        MOVED_TO_SECURE.add("dialup_modem_restriction");
        MOVED_TO_SECURE.add("assisted_dialing_state");
        MOVED_TO_SECURE.add("cur_country_updated_by_user");
        MOVED_TO_SECURE.add("cur_country_mcc");
        MOVED_TO_SECURE.add("cur_country_code");
        MOVED_TO_SECURE.add("cur_country_name");
        MOVED_TO_SECURE.add("cur_country_idd");
        MOVED_TO_SECURE.add("cur_country_ndd");
        MOVED_TO_SECURE.add("cur_country_area_code");
        MOVED_TO_SECURE.add("cur_country_mdn_len");
        MOVED_TO_SECURE.add("ref_country_mcc");
        MOVED_TO_SECURE.add("ref_country_code");
        MOVED_TO_SECURE.add("ref_country_name");
        MOVED_TO_SECURE.add("ref_country_idd");
        MOVED_TO_SECURE.add("ref_country_ndd");
        MOVED_TO_SECURE.add("ref_country_area_code");
        MOVED_TO_SECURE.add("qsms_enable_text_message_reply");
        MOVED_TO_SECURE.add("ref_country_mdn_len");
        MOVED_TO_SECURE.add("calling_global_controls_enable");
        MOVED_TO_SECURE.add("user_need_accept_moto_agreement");
        MOVED_TO_SECURE.add("full_charge_notification_enable");
        MOVED_TO_SECURE.add("ice_isenabled");
        MOVED_TO_SECURE.add("sim_33859_isenabled");
        MOVED_TO_SECURE.add("smartdialer_language_code");
        MOVED_TO_SECURE.add("multiple_pdp_isenabled");
        MOVED_TO_SECURE.add("dun_nat_enabled");
        MOVED_TO_SECURE.add("tether_reverse_nat_enabled");
        MOVED_TO_SECURE.add("calling_33860_enabled");
        MOVED_TO_SECURE.add("calling_gsm_ad_enabled");
        MOVED_TO_SECURE.add("fid_34387_multimode");
        MOVED_TO_SECURE.add("fid_33463_enabled");
        MOVED_TO_SECURE.add("ftr_ringer_switch_enable");
        MOVED_TO_SECURE.add("vm_vvm_selection");
        MOVED_TO_SECURE.add("vm_vvm_roaming_selection");
        MOVED_TO_SECURE.add("enable_download_wallpaper");
        MOVED_TO_SECURE.add("back_ground_data_backup_by_datamanager");
        MOVED_TO_SECURE.add("pointer_speed_level");
        MOVED_TO_SECURE.add("bluetooth_mfb_enabled_when_locked");
        MOVED_TO_SECURE.add("hyphenation_feature_enabled");
        MOVED_TO_SECURE.add("dun_apn_changable");
        MOVED_TO_SECURE.add("dun_apn_hide");
        MOVED_TO_SECURE.add("wifi_disabled_by_ecm");
        MOVED_TO_SECURE.add("ice_contacts_enabled");
        MOVED_TO_SECURE.add("check_cfu_poweron");
        MOVED_TO_SECURE.add("data_roaming_access_feature_enabled");
        MOVED_TO_SECURE.add("enable_mms_when_data_disabled");
        MOVED_TO_SECURE.add("dataswitch_feature_enabled");
        MOVED_TO_SECURE.add("dataswitch_sync_connect_value");
        MOVED_TO_SECURE.add("network_setting_on_boot");
        MOVED_TO_SECURE.add("mobile_data_disable");
        MOVED_TO_SECURE.add("huxvmm_file_handle");
        MOVED_TO_SECURE.add("wifi_offload_flag");
        MOVED_TO_SECURE.add("sprint_offload_flex");
        MOVED_TO_SECURE.add("demo_mode_enabled");
        MOVED_TO_SECURE.add("demo_video_path_in_system");
        MOVED_TO_SECURE.add("demo_video_path_in_sdcard");
        MOVED_TO_SECURE.add("demo_video_download_uri");
        MOVED_TO_SECURE.add("wifi_adhoc_channel_number");
        MOVED_TO_SECURE.add("allow_editing_class3_apn");
        MOVED_TO_SECURE.add("alt_shift_toggle_ftr_available");
        MOVED_TO_SECURE.add("roaming_plmn_between_carriers_enabled");
        MOVED_TO_SECURE.add("roaming_plmn_table");
        MOVED_TO_SECURE.add("black_list_roaming_plmn_table");
        MOVED_TO_SECURE.add("roaming_mcc_table");
        MOVED_TO_SECURE.add("hdmi_overscan");
        MOVED_TO_SECURE.add("vibrate_patterns");
        MOVED_TO_SECURE.add("lapdock_charging_mode");
        MOVED_TO_SECURE.add("lapdock_disable_trackpad");
        MOVED_TO_SECURE.add("tap_to_select");
        MOVED_TO_SECURE.add("audio_routing");
        MOVED_TO_SECURE.add("kbd_backlight_control_mode");
        MOVED_TO_SECURE.add("kbd_backlight_brightness");
        MOVED_TO_SECURE.add("kbd_backlight_timeout");
        MOVED_TO_SECURE.add("enable_mo_sms_over_ims");
        MOVED_TO_SECURE.add("emergency_call_shortcut_enable");
        MOVED_TO_SECURE.add("enable_roaming_broker_38132");
        MOVED_TO_SECURE.add("preferred_mnc_mcc");
        MOVED_TO_SECURE.add("hfa_running");
        MOVED_TO_SECURE.add("cur_mdn");
        MOVED_TO_SECURE.add("north_american_dialing_enabled");
        MOVED_TO_SECURE.add("plus_code_dialing_idd_prefix");
        MOVED_TO_SECURE.add("def_plus_code_dialing_idd_prefix");
        MOVED_TO_SECURE.add("hiddenmenu_ddtm_default_preference_settings");
        MOVED_TO_SECURE.add("mobile_sync_wifi_url");
        MOVED_TO_SECURE.add("ftr_fdn_notify_enabled");
        MOVED_TO_SECURE.add("ftr_cell_broadcast_enabled");
        MOVED_TO_SECURE.add("pointer_acceleration");
        MOVED_TO_SECURE.add("ftr_35605_sprint_roaming_enabled");
        MOVED_TO_SECURE.add("domestic_voice_roaming");
        MOVED_TO_SECURE.add("domestic_voice_roaming_forced");
        MOVED_TO_SECURE.add("domestic_data_roaming_ui");
        MOVED_TO_SECURE.add("domestic_data_roaming_forced");
        MOVED_TO_SECURE.add("domestic_call_guard");
        MOVED_TO_SECURE.add("domestic_call_guard_forced");
        MOVED_TO_SECURE.add("domestic_data_roaming_guard");
        MOVED_TO_SECURE.add("domestic_data_roaming_guard_forced");
        MOVED_TO_SECURE.add("international_voice_roaming");
        MOVED_TO_SECURE.add("international_voice_roaming_forced");
        MOVED_TO_SECURE.add("international_data_roaming_ui");
        MOVED_TO_SECURE.add("international_data_roaming_forced");
        MOVED_TO_SECURE.add("international_call_guard");
        MOVED_TO_SECURE.add("international_call_guard_forced");
        MOVED_TO_SECURE.add("international_data_roaming_guard");
        MOVED_TO_SECURE.add("international_data_roaming_guard_forced");
        MOVED_TO_SECURE.add("international_outgoingsms_guard");
        MOVED_TO_SECURE.add("international_outgoingsms_guard_forced");
        MOVED_TO_SECURE.add("gsm_data_roaming_ui");
        MOVED_TO_SECURE.add("gsm_data_roaming_forced");
        MOVED_TO_SECURE.add("gsm_voice_roaming_guard");
        MOVED_TO_SECURE.add("gsm_voice_roaming_guard_forced");
        MOVED_TO_SECURE.add("gsm_data_guard");
        MOVED_TO_SECURE.add("gsm_data_guard_forced");
        MOVED_TO_SECURE.add("gsm_outgoing_sms_guard");
        MOVED_TO_SECURE.add("gsm_outgoing_sms_guard_forced");
        MOVED_TO_SECURE.add("preferred_p2p_band_for_ago");
        MOVED_TO_SECURE.add("preferred_p2p_device_limit_for_ago");
        MOVED_TO_SECURE.add("preferred_p2p_device_timeout_for_ago");
        MOVED_TO_SECURE.add("preferred_p2p_auto_connect_support");
        MOVED_TO_SECURE.add("privacy_droid_blast");
        MOVED_TO_SECURE.add("task_button_press_behavior");
        MOVED_TO_SECURE.add("tsb_disable_flags");
        MOVED_TO_SECURE.add("att_voice_priority");
        MOVED_TO_SECURE.add("disable_data_rscp");
        MOVED_TO_SECURE.add("disable_data_rssi");
        MOVED_TO_SECURE.add("disable_data_ecn0");
        MOVED_TO_SECURE.add("enable_data_rscp");
        MOVED_TO_SECURE.add("enable_data_rssi");
        MOVED_TO_SECURE.add("eable_data_ecn0");
        MOVED_TO_SECURE.add("pdp_watchdog_ping_deadline");
        MOVED_TO_SECURE.add("restrict_bg_data_on_low_coverage");
        MOVED_TO_SECURE.add("mobile_data_coverage_conditioner");
        MOVED_TO_SECURE.add(Secure.SYNCHRONIZED_VIBRATION_WITH_RINGTONE);
    }

    public static String getString(ContentResolver resolver, String name) {
        if (MOVED_TO_SECURE.contains(name)) {
            return Secure.getString(resolver, name);
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("no such table to get this name = ");
        stringBuilder.append(name);
        Log.w(str, stringBuilder.toString());
        new Exception().printStackTrace();
        return null;
    }

    public static boolean putString(ContentResolver resolver, String name, String value) {
        if (MOVED_TO_SECURE.contains(name)) {
            return Secure.putString(resolver, name, value);
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("no such table to put this name = ");
        stringBuilder.append(name);
        stringBuilder.append(" value=");
        stringBuilder.append(value);
        Log.w(str, stringBuilder.toString());
        new Exception().printStackTrace();
        return false;
    }

    public static String[] getStringArray(ContentResolver resolver, String name) {
        String concatValue = getString(resolver, name);
        if (concatValue == null) {
            return null;
        }
        return concatValue.split(SEPARATOR);
    }

    public static boolean putStringArray(ContentResolver resolver, String name, String[] values) {
        int i = 0;
        if (name == null) {
            return false;
        }
        if (values == null) {
            return putString(resolver, name, null);
        }
        StringBuilder builder = new StringBuilder();
        int length = values.length;
        while (i < length) {
            String item = values[i];
            if (item != null) {
                if (builder.length() != 0) {
                    builder.append(SEPARATOR);
                }
                builder.append(item);
            }
            i++;
        }
        return putString(resolver, name, builder.toString());
    }

    public static Uri getUriFor(String name) {
        if (MOVED_TO_SECURE.contains(name)) {
            return Secure.getUriFor(name);
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("no such table to get this URI = ");
        stringBuilder.append(name);
        Log.w(str, stringBuilder.toString());
        new Exception().printStackTrace();
        return null;
    }

    public static int getInt(ContentResolver cr, String name, int def) {
        int parseInt;
        String v = getString(cr, name);
        if (v != null) {
            try {
                parseInt = Integer.parseInt(v);
            } catch (NumberFormatException e) {
                return def;
            }
        }
        parseInt = def;
        return parseInt;
    }

    public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
        try {
            return Integer.parseInt(getString(cr, name));
        } catch (NumberFormatException e) {
            throw new SettingNotFoundException(name);
        }
    }

    public static boolean putInt(ContentResolver cr, String name, int value) {
        return putString(cr, name, Integer.toString(value));
    }

    public static long getLong(ContentResolver cr, String name, long def) {
        String valString = getString(cr, name);
        if (valString == null) {
            return def;
        }
        try {
            return Long.parseLong(valString);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static long getLong(ContentResolver cr, String name) throws SettingNotFoundException {
        try {
            return Long.parseLong(getString(cr, name));
        } catch (NumberFormatException e) {
            throw new SettingNotFoundException(name);
        }
    }

    public static boolean putLong(ContentResolver cr, String name, long value) {
        return putString(cr, name, Long.toString(value));
    }

    public static float getFloat(ContentResolver cr, String name, float def) {
        float parseFloat;
        String v = getString(cr, name);
        if (v != null) {
            try {
                parseFloat = Float.parseFloat(v);
            } catch (NumberFormatException e) {
                return def;
            }
        }
        parseFloat = def;
        return parseFloat;
    }

    public static float getFloat(ContentResolver cr, String name) throws SettingNotFoundException {
        try {
            return Float.parseFloat(getString(cr, name));
        } catch (NullPointerException e) {
            throw new SettingNotFoundException(name);
        } catch (NumberFormatException e2) {
            throw new SettingNotFoundException(name);
        }
    }

    public static boolean putFloat(ContentResolver cr, String name, float value) {
        return putString(cr, name, Float.toString(value));
    }
}