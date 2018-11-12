package com.luchongbin.multithreaddownload.multithreaddownload;
import com.luchongbin.multithreaddownload.multithreaddownload.model.DataSourceModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/7/8.
 */
public class DataSource {

    private static DataSource sDataSource = new DataSource();

    private static final String[] NAMES = {
            "搜狐视频",
            "微信电话本",
            "淘宝",
            "聚美优品",
            "搜房网"
    };

    private static final String[] IMAGES = {
            "http://img.wdjimg.com/mms/icon/v1/2/bf/939a67b179e75326aa932fc476cbdbf2_512_512.png",
            "http://img.wdjimg.com/mms/icon/v1/b/fe/718d7c213ce633fd4e25c278c19acfeb_512_512.png",
            "http://img.wdjimg.com/mms/icon/v1/f/29/cf90d1294ac84da3b49561a6f304029f_512_512.png",
            "http://img.wdjimg.com/mms/icon/v1/4/43/0318ce32731600bfa66cbb5018e1a434_512_512.png",
            "http://img.wdjimg.com/mms/icon/v1/7/08/2b3858e31efdee8a7f28b06bdb83a087_512_512.png"
    };

    private static final String[] URLS = {
            "http://upgrade.m.tv.sohu.com/channels/hdv/5.0.0/SohuTV_5.0.0_47_201506112011.apk",
            "http://dldir1.qq.com/qqcontacts/100001_phonebook_4.0.0_3148.apk",
            "http://download.alicdn.com/wireless/taobao4android/latest/702757.apk",
            "http://apps.wandoujia.com/apps/com.jm.android.jumei/download",
            "http://download.3g.fang.com/soufun_android_30001_7.9.0.apk"
    };

    public static DataSource getInstance() {
        return sDataSource;
    }

    public List<DataSourceModel> getData() {
        List<DataSourceModel> mDataSourceModels = new ArrayList<DataSourceModel>();
        for (int i = 0; i < NAMES.length; i++) {
            DataSourceModel mDataSourceModel = new DataSourceModel(String.valueOf(i), NAMES[i], IMAGES[i], URLS[i]);
            mDataSourceModels.add(mDataSourceModel);
        }
        return mDataSourceModels;
    }
}
