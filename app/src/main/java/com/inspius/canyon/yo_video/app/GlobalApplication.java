package com.inspius.canyon.yo_video.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.login.DefaultAudience;
import com.inspius.canyon.yo_video.helper.ParseUtils;
import com.inspius.coreapp.config.CoreAppEnums;
import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Billy on 9/3/15.
 */
public class GlobalApplication extends Application {
    /**
     * Log or request TAG
     */
    public static final String TAG = GlobalApplication.class.getSimpleName();
    private static GlobalApplication mInstance;
    private static Context mAppContext;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        this.mAppContext = getApplicationContext();

        // Initialize Fabric
//        if (isProductionEnvironment())
//            Fabric.with(this, new Crashlytics());

        // Initialize typeface helper
        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/Proxima-Nova-Regular.otf"))
                .set(Typeface.ITALIC, Typeface.createFromAsset(getAssets(), "fonts/Proxima-Nova-Regular-Italic.otf"))
                .set(Typeface.BOLD, Typeface.createFromAsset(getAssets(), "fonts/Proxima-Nova-Bold.otf"))
                .set(Typeface.BOLD_ITALIC, Typeface.createFromAsset(getAssets(), "fonts/Proxima-Nova-Bold-Italic.otf"))

                .create();
        TypefaceHelper.init(typeface);

        // initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // init image loader
        initImageLoader(mAppContext);

        // register with parse
        ParseUtils.registerParse(this);

        MultiDex.install(getBaseContext());

        Permission[] permissions = new Permission[]{
                Permission.EMAIL,
                Permission.PUBLISH_ACTION
        };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(AppConfig.FACEBOOK_APP_ID)
                .setNamespace(AppConfig.FACEBOOK_APP_NAMESPACE)
                .setPermissions(permissions)
                .setDefaultAudience(DefaultAudience.FRIENDS)
                .setAskForAllPermissionsAtOnce(false)
                // .setGraphVersion("v2.3")
                .build();

        SimpleFacebook.setConfiguration(configuration);
    }

    public static synchronized GlobalApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(128 * 1024 * 1024); // 128 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.diskCacheExtraOptions(480, 320, null);

        if (!isProductionEnvironment())
            config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public boolean isProductionEnvironment() {
        return (AppConfig.ENVIRONMENT == CoreAppEnums.Environment.PRODUCTION) ? true : false;
    }
}
