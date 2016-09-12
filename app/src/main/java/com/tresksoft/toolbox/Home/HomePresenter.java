package com.tresksoft.toolbox.Home;

import android.content.Context;
import android.content.Intent;

import com.tresksoft.toolbox.ActivityPreferences;
import com.tresksoft.toolbox.ApplicationManager.ActivityApplicationManager;
import com.tresksoft.toolbox.CacheManager.ActivityCacheClean;
import com.tresksoft.toolbox.Move2SDManager.ActivityMove2SD;
import com.tresksoft.toolbox.NetworkManager.ActivityNetworkProfiles;
import com.tresksoft.toolbox.ProccessManager.ActivityProcessManager;
import com.tresksoft.toolbox.R;
import com.tresksoft.toolbox.SpeedSettingsManager.ActivitySpeedSettings;
import com.tresksoft.toolbox.WifiManager.ActivityWifiManager;

/**
 * Created by Salva on 13/9/16.
 */
public class HomePresenter {

    private final HomeContract.View view;
    private final HomeModel homeModel;

    public HomePresenter(HomeContract.View view, HomeModel homeModel) {

        this.view = view;
        this.homeModel = homeModel;
    }

    public void onHomeButtonClick(int id) {
        switch (id) {
            case R.id.layout_process_manager:
                onProcessManager();
                break;
            case R.id.layout_app_manager:
                onApplicationManager();
                break;
            case R.id.layout_wifi_manager:
                onWifiManager();
                break;
            case R.id.layout_network_manager:
                onNetworkProfileManager();
                break;
            case R.id.layout_speed_settings:
                onSpeedSettings();
                break;
            case R.id.layout_cache_manager:
                onCacheClean();
                break;
            case R.id.layout_move2sd:
                onMove2SD();
                break;
        }
    }

    private void onMove2SD() {
        ShowActivity(ActivityMove2SD.class);
    }

    private void onCacheClean() {
        ShowActivity(ActivityCacheClean.class);
    }

    private void onSpeedSettings() {
        ShowActivity(ActivitySpeedSettings.class);
    }

    private void onNetworkProfileManager() {
        ShowActivity(ActivityNetworkProfiles.class);
    }

    private void onWifiManager() {
        ShowActivity(ActivityWifiManager.class);
    }

    private void onApplicationManager() {
        ShowActivity(ActivityApplicationManager.class);
    }

    private void onProcessManager() {
        ShowActivity(ActivityProcessManager.class);
    }

    private void onShowPreferences() {
        ShowActivity(ActivityPreferences.class);
    }

    private void ShowActivity(Class<?> cls) {
        Intent intent = new Intent((Context) this.view, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Context) this.view).startActivity(intent);
    }

    public boolean onHomeOptionsItemSelected(int itemId) {
        switch(itemId) {
            case R.id.item_preferences:
                onShowPreferences();
                return true;
            default:
                return false;
        }
    }
}
