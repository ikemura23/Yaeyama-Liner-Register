
package com.ikmr.banbara23.yaeyama_liner_register;

import android.app.Application;

import com.nifty.cloud.mb.core.NCMB;
import com.socks.library.KLog;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NCMB.initialize(this,
                BuildConfig.NCMB_APPLICATION_ID,
                BuildConfig.NCMB_CLIENT_KEY);

        Base.initialize(this);
        KLog.init(BuildConfig.DEBUG);
    }
}
