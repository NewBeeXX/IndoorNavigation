package com.fengmap.FMDemoNavigationAdvance.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fengmap.FMDemoNavigationAdvance.R;
import com.fengmap.FMDemoNavigationAdvance.adapter.SearchStoreAdapter;
import com.fengmap.FMDemoNavigationAdvance.bean.Store;
import com.fengmap.FMDemoNavigationAdvance.utils.FileUtils;
import com.fengmap.FMDemoNavigationAdvance.utils.JSONUtils;
import com.fengmap.FMDemoNavigationAdvance.utils.ViewHelper;
import com.fengmap.FMDemoNavigationAdvance.widget.KeyBoardUtils;
import com.fengmap.FMDemoNavigationAdvance.widget.SearchBar;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.marker.FMImageMarker;
import com.fengmap.android.map.marker.FMModel;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FMSearchAnalysisAssociateBusiness extends BaseSearchActivity implements SearchBar.OnSearchResultCallback,
        AdapterView.OnItemClickListener {
    private SearchBar mSearchBar;
    private SearchStoreAdapter mSearchAdapter;
    private List<Store> mStores;
    private FMModel mClickedModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fmsearch_analysis_associate_business);
        //搜索框
        mSearchBar = ViewHelper.getView(FMSearchAnalysisAssociateBusiness.this, R.id.search_title_bar);
        mSearchBar.setOnSearchResultCallback(this);
        mSearchBar.setOnItemClickListener(this);
    }

    @Override
    public void onMapInitSuccess(String path) {
        super.onMapInitSuccess(path);
        mStores = readStoresFromJson("data.json");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //关闭软键盘
        KeyBoardUtils.closeKeybord(mSearchBar.getCompleteText(), FMSearchAnalysisAssociateBusiness.this);

        Store store = (Store) parent.getItemAtPosition(position);
        displayStoreView(store);

        //切换楼层
        int groupId = Integer.parseInt(store.GROUP);
        if (groupId != mMap.getFocusGroupId()) {
            mMap.setFocusByGroupId(groupId, null);
        }

        //查找模型
        FMModel model = mMap.getFMLayerProxy().queryFMModelByFid(store.FID);
        FMMapCoord mapCoord = model.getCenterMapCoord();
        mMap.moveToCenter(mapCoord, false);

        //添加图片
        clearImageMarker();
        FMImageMarker imageMarker = ViewHelper.buildImageMarker(getResources(), mapCoord);
        mImageLayers.get(groupId).addMarker(imageMarker);

        clearStatus(model);
    }

    @Override
    public void onSearchCallback(String keyword) {
        //地图未显示前，不执行搜索事件
        boolean isCompleted = mMap.getMapFirstRenderCompleted();
        if (!isCompleted) {
            return;
        }

        ArrayList<Store> datas = queryStoreByKeyword(keyword);
        if (mSearchAdapter == null) {
            mSearchAdapter = new SearchStoreAdapter(FMSearchAnalysisAssociateBusiness.this, datas);
            mSearchBar.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.setDatas(datas);
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 通过关键字查询商品
     *
     * @param keyword 关键字
     * @return
     */
    private ArrayList<Store> queryStoreByKeyword(String keyword) {
        ArrayList<Store> result = new ArrayList<>();
        for (Store store : mStores) {
            if (store.NAME.contains(keyword)) {
                result.add(store);
            }
        }
        return result;
    }

    /**
     * 获取商品信息
     *
     * @param fileName 文件名称
     * @return
     */
    private List<Store> readStoresFromJson(String fileName) {
        String json = FileUtils.readStringFromAssets(getApplicationContext(), fileName);
        return JSONUtils.fromJson(json, new TypeToken<List<Store>>() {
        });
    }

    /**
     * 显示商品详情
     *
     * @param store 商品
     */
    private void displayStoreView(Store store) {
        ViewHelper.setViewVisibility(FMSearchAnalysisAssociateBusiness.this, R.id.bottom_view, View.VISIBLE);

        TextView textView = ViewHelper.getView(FMSearchAnalysisAssociateBusiness.this, R.id.txt_content);
        textView.setText("FID: " + store.FID + "\nNAME: " + store.NAME + "\n" +
                "FLOOR: " + store.FLOOR + "\nGROUP: " + store.GROUP + "\nTYPE: " + store.TYPE);
    }

    /**
     * 清除模型的点击效果
     *
     * @param model 模型
     */
    private void clearStatus(FMModel model) {
        if (mClickedModel != null) {
            mClickedModel.setSelected(false);
        }
        mClickedModel = model;
        mClickedModel.setSelected(true);
    }

}
