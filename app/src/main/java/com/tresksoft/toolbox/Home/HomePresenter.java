package com.tresksoft.toolbox.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

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
    private final long DELAY_MILLIS_INIT = 500;
    private final long DELAY_MILLIS_DEFAULT = 10000;

    private final int EMPTY_MESSAGE_PROCESS_MANAGER = 1;
    private final int EMPTY_MESSAGE_APP_MANAGER = 2;
    private final int EMPTY_MESSAGE_CACHE_MANAGER = 3;
    private final int EMPTY_MESSAGE_SD = 4;
    private final int EMPTY_MESSAGE_WIFI_MANAGER = 0;


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

    public void onHomeResume() {
        initTimers();
    }

    private void initTimers() {
        
        mHandler.postDelayed(mUpdateTime, DELAY_MILLIS_INIT);
    }

    private Runnable mUpdateTime = new Runnable() {

        public void run() {

            // ProcessManager Thread
            (new Thread(new Runnable() {
                public void run() {
                    // Informar

                    mHandler.sendEmptyMessage(EMPTY_MESSAGE_PROCESS_MANAGER);
                }
            })).start();

            // AppManager Thread
            (new Thread(new Runnable() {
                public void run() {
                    // Informar

                    mHandler.sendEmptyMessage(EMPTY_MESSAGE_APP_MANAGER);
                }
            })).start();

            // CacheManager Thread
            (new Thread(new Runnable() {
                public void run() {
                    // Informar

                    mHandler.sendEmptyMessage(EMPTY_MESSAGE_CACHE_MANAGER);
                }
            })).start();

            // SD Thread
            (new Thread(new Runnable() {
                public void run() {
                    // Informar

                    mHandler.sendEmptyMessage(EMPTY_MESSAGE_SD);
                }
            })).start();

            // WifiManager Thread
            (new Thread(new Runnable() {
                public void run() {
                    // Informar

                    mHandler.sendEmptyMessage(EMPTY_MESSAGE_WIFI_MANAGER);
                }
            })).start();

            mHandler.removeCallbacks(mUpdateTime);
            mHandler.postDelayed(mUpdateTime, DELAY_MILLIS_DEFAULT);
        }
    };

    private Handler mHandler;

    {
        mHandler = new Handler() {
            public void handleMessage(final Message msg) {
                switch (msg.what) {
                    case EMPTY_MESSAGE_PROCESS_MANAGER:
                        view.updateProcessInfo(homeModel.getMemoryAvailable(), homeModel.getNumRunningProcess());
                        break;
                    case EMPTY_MESSAGE_APP_MANAGER:
                        view.updateAppInfo(homeModel.getInternalMemoryAvailable(), homeModel.getSDMemoryAvailable());
                        break;
                    case EMPTY_MESSAGE_CACHE_MANAGER:
                        view.updateCacheInfo(homeModel.getCacheTotal());
                        break;
                    case EMPTY_MESSAGE_SD:
                        view.updateMove2SDInfo(homeModel.getAppMoves2SD());
                        break;
                    case EMPTY_MESSAGE_WIFI_MANAGER:
                        view.updateWifiInfo(homeModel.getWifisAvailable());
                        break;
                }
            }
        };
    }
}
