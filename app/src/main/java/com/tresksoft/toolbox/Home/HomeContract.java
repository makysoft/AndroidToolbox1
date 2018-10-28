package com.tresksoft.toolbox.Home;

/**
 * Created by Salva on 13/9/16.
 */
public class HomeContract {

    public interface View {

        void updateProcessInfo(long memoryAvailable, int numRunningProcess);

        void updateAppInfo(long internalMemoryAvailable, long sdMemoryAvailable);

        void updateCacheInfo(long cacheTotal);

        void updateMove2SDInfo(int appMoves2SD);

        void updateWifiInfo(int wifisAvailable);
    }

}
