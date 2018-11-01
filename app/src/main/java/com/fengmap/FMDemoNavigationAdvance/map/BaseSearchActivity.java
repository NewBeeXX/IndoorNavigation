package com.fengmap.FMDemoNavigationAdvance.map;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fengmap.FMDemoNavigationAdvance.R;
import com.fengmap.FMDemoNavigationAdvance.utils.FileUtils;
import com.fengmap.FMDemoNavigationAdvance.utils.ViewHelper;
import com.fengmap.FMDemoNavigationAdvance.widget.NavigationBar;
import com.fengmap.android.FMErrorMsg;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.data.OnFMDownloadProgressListener;
import com.fengmap.android.exception.FMObjectException;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapUpgradeInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.layer.FMImageLayer;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class BaseSearchActivity extends Activity implements OnFMMapInitListener {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    protected FMMapView mMapView;
    protected FMMap mMap;
    protected HashMap<Integer, FMImageLayer> mImageLayers = new HashMap<>();
    protected FMSearchAnalyser mSearchAnalyser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_search);
        setTitle();
    }

    /**
     * 加载地图数据
     */
    protected void openMapByPath() {
        mMapView = (FMMapView) findViewById(R.id.map_view);
        mMap = mMapView.getFMMap();
        mMap.setOnFMMapInitListener(this);
        //加载离线数据
        String path = FileUtils.getDefaultMapPath(this);
        mMap.openMapByPath(path);
    }

    /**
     * 设置标题
     */
    protected void setTitle() {
        int resId = getIntent().getIntExtra(EXTRA_TITLE, -1);
        NavigationBar navigationBarView = (NavigationBar) ViewHelper.getView(BaseSearchActivity.this, R.id.navigation_bar);
        navigationBarView.setTitle(resId);
    }

    /**
     * 地图加载成功回调事件
     *
     * @param path 地图所在sdcard路径
     */
    @Override
    public void onMapInitSuccess(String path) {
        //加载离线主题
        mMap.loadThemeByPath(FileUtils.getDefaultThemePath(this));

        //图片图层
        int groupSize = mMap.getFMMapInfo().getGroupSize();
        for (int i = 0; i < groupSize; i++) {
            int groupId = mMap.getMapGroupIds()[i];
            FMImageLayer imageLayer = mMap.getFMLayerProxy().createFMImageLayer(groupId);
            mMap.addLayer(imageLayer);

            mImageLayers.put(groupId, imageLayer);
        }

        //搜索分析
        try {
            mSearchAnalyser = FMSearchAnalyser.getFMSearchAnalyserById(FileUtils.DEFAULT_MAP_ID);
        } catch (FMObjectException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加布局
     *
     * @param layoutId 资源id
     */
    public void setContentView(int layoutId) {
        View view = View.inflate(getBaseContext(), layoutId, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, R.id.navigation_bar);
        RelativeLayout viewGroup = (RelativeLayout) findViewById(R.id.layout_root);
        viewGroup.addView(view, lp);

        openMapByPath();
    }

    /**
     * 地图销毁调用
     */
    @Override
    public void onBackPressed() {
        if (mMap != null) {
            mMap.onDestroy();
        }
        super.onBackPressed();
    }

    /**
     * 地图加载失败回调事件
     *
     * @param path      地图所在sdcard路径
     * @param errorCode 失败加载错误码，可以通过{@link FMErrorMsg#getErrorMsg(int)}获取加载地图失败详情
     */
    @Override
    public void onMapInitFailure(String path, int errorCode) {
        //TODO 可以提示用户地图加载失败原因，进行地图加载失败处理
    }

    /**
     * 当{@link FMMap#openMapById(String, boolean)}设置openMapById(String, false)时地图不自动更新会
     * 回调此事件，可以调用{@link FMMap#upgrade(FMMapUpgradeInfo, OnFMDownloadProgressListener)}进行
     * 地图下载更新
     *
     * @param upgradeInfo 地图版本更新详情,地图版本号{@link FMMapUpgradeInfo#getVersion()},<br/>
     *                    地图id{@link FMMapUpgradeInfo#getMapId()}
     * @return 如果调用了{@link FMMap#upgrade(FMMapUpgradeInfo, OnFMDownloadProgressListener)}地图下载更新，
     * 返回值return true,因为{@link FMMap#upgrade(FMMapUpgradeInfo, OnFMDownloadProgressListener)}
     * 会自动下载更新地图，更新完成后会加载地图;否则return false。
     */
    @Override
    public boolean onUpgrade(FMMapUpgradeInfo upgradeInfo) {
        //TODO 获取到最新地图更新的信息，可以进行地图的下载操作
        return false;
    }

    /**
     * 清楚图片标志
     */
    protected void clearImageMarker() {
        for (FMImageLayer imageLayer : mImageLayers.values()) {
            imageLayer.removeAll();
        }
    }

}
