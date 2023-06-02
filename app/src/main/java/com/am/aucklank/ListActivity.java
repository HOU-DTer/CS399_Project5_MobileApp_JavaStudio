package com.am.aucklank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements View.OnClickListener{

    private List<IncidentBean> mShowItem = new ArrayList<>();

    private TextView tv1;
    private TextView tv2;

    private View line1;
    private View line2;
    private DBHelper dbHelper;
    private String TAG = "ListActivity";
    //  private  ArrayList<IncidentBean> list = new ArrayList<>();
    private int page = 0;
    private int page1 = 0;
    private LoadMoreAdapter loadMoreAdapter;
    private LoadMoreAdapter loadMoreAdapter1;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;


    //define a state (定义一个状态)
    private enum LOADSTATE {
        NONE,//idle state (空闲状态)
        MORE//pull down (下拉)
    }

    private LOADSTATE mCurrentState = LOADSTATE.NONE;//initialize the state (初始化空闲状态)
    private LOADSTATE mCurrentState1 = LOADSTATE.NONE;//initialize the state (初始化空闲状态)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if (null == dbHelper) {
            dbHelper = new DBHelper(ListActivity.this);
        }

        findViewById(R.id.iv_back).setOnClickListener(this);


        findViewById(R.id.list_tab1).setOnClickListener(this);
        findViewById(R.id.list_tab2).setOnClickListener(this);

        tv1 = findViewById(R.id.list_tab_text1);
        tv2 = findViewById(R.id.list_tab_text2);

        line1 = findViewById(R.id.list_tab_line1);
        line2 = findViewById(R.id.list_tab_line2);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView1 = findViewById(R.id.recyclerView1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        loadMoreAdapter = new LoadMoreAdapter();
        loadMoreAdapter1 = new LoadMoreAdapter();
        recyclerView.setAdapter(loadMoreAdapter);
        recyclerView1.setAdapter(loadMoreAdapter1);
        loadMoreAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore: ");
                if (mCurrentState == LOADSTATE.NONE) {
                    mCurrentState = LOADSTATE.MORE;
                    page++;
                    laodData(page);

                }
            }
        });

        loadMoreAdapter1.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore: ");
                if (mCurrentState1 == LOADSTATE.NONE) {
                    mCurrentState1 = LOADSTATE.MORE;
                    page1++;
                    laodData1(page1);

                }
            }
        });

        laodData(page);
        laodData1(page1);
    }

    private void laodData(int page) {
        loadMoreAdapter.getLoadMoreModule().setEnableLoadMore(true);
        ArrayList<IncidentBean> list = new ArrayList<>();
        Cursor query = dbHelper.queryDate(page);
        if (query != null) {
            while (query.moveToNext()) {
                @SuppressLint("Range") String latitude = query.getString(query.getColumnIndex("latitude"));
                @SuppressLint("Range") String Longitude = query.getString(query.getColumnIndex("longitude"));
                @SuppressLint("Range") String date = query.getString(query.getColumnIndex("date"));
                @SuppressLint("Range") String hour = query.getString(query.getColumnIndex("hour"));
                @SuppressLint("Range") String area = query.getString(query.getColumnIndex("area"));
                @SuppressLint("Range") String Incidents = query.getString(query.getColumnIndex("incidents"));
                @SuppressLint("Range") String severity = query.getString(query.getColumnIndex("severity"));
                Log.d(TAG, "onCreate: date" + date);
                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(Longitude));
                list.add(new IncidentBean(date,hour,Incidents,latLng,area,severity));
            }
        }
        query.close();
        System.gc();
        if (page == 0) {
            //if to load the first page data, use setData() (如果是加载的第一页数据，用 setData())
            loadMoreAdapter.setList(list);
        } else {
            //if not the first page use add (不是第一页，则用add)
            loadMoreAdapter.addData(list);
        }
        if (list.size() < 10) {
            //if less than one page, show not more data (如果不够一页,显示没有更多数据布局)
            loadMoreAdapter.getLoadMoreModule().loadMoreEnd();
        } else {
            loadMoreAdapter.getLoadMoreModule().loadMoreComplete();
        }
        if (mCurrentState == LOADSTATE.MORE) {
            mCurrentState = LOADSTATE.NONE;
        }

    }
    private void laodData1(int page) {
        loadMoreAdapter1.getLoadMoreModule().setEnableLoadMore(true);
        ArrayList<IncidentBean> list = new ArrayList<>();
        Cursor query = dbHelper.queryDateHis(page);
        if (query != null) {
            while (query.moveToNext()) {
                @SuppressLint("Range") String latitude = query.getString(query.getColumnIndex("latitude"));
                @SuppressLint("Range") String Longitude = query.getString(query.getColumnIndex("longitude"));
                @SuppressLint("Range") String date = query.getString(query.getColumnIndex("date"));
                @SuppressLint("Range") String hour = query.getString(query.getColumnIndex("hour"));
                @SuppressLint("Range") String area = query.getString(query.getColumnIndex("area"));
                @SuppressLint("Range") String Incidents = query.getString(query.getColumnIndex("incidents"));
                @SuppressLint("Range") String severity = query.getString(query.getColumnIndex("severity"));
                Log.d(TAG, "onCreate: date" + date);
                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(Longitude));
                list.add(new IncidentBean(date,hour,Incidents,latLng,area,severity));
            }
        }
        query.close();
        System.gc();
        if (page == 0) {
            //if to load the first page data, use setData() (如果是加载的第一页数据，用 setData())
            loadMoreAdapter1.setList(list);
        } else {
            //if not the first page use add (不是第一页，则用add)
            loadMoreAdapter1.addData(list);
        }
        if (list.size() < 10) {
            //if less than one page, show not more data (如果不够一页,显示没有更多数据布局)
            loadMoreAdapter1.getLoadMoreModule().loadMoreEnd();
        } else {
            loadMoreAdapter1.getLoadMoreModule().loadMoreComplete();
        }
        if (mCurrentState1 == LOADSTATE.MORE) {
            mCurrentState1 = LOADSTATE.NONE;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.list_tab1:
                tv1.setTextColor(Color.parseColor("#4D79FF"));
                line1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.INVISIBLE);
                tv2.setTextColor(Color.parseColor("#61687C"));
                line2.setVisibility(View.GONE);
                break;
            case R.id.list_tab2:

                tv2.setTextColor(Color.parseColor("#4D79FF"));
                line2.setVisibility(View.VISIBLE);
                tv1.setTextColor(Color.parseColor("#61687C"));
                line1.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                break;
        }
    }

   /* @Override
    public void setOnLoadMoreListener(@Nullable OnLoadMoreListener listener) {
        Log.d(TAG, "setOnLoadMoreListener: ");

        if (mAdapter.getData().size() < 20) {

        }

    }*/

    public class LoadMoreAdapter extends BaseQuickAdapter<IncidentBean, BaseViewHolder> implements LoadMoreModule {

        public LoadMoreAdapter() {
            super(R.layout.item_list_incident);
          //  super
        }
        @Override
        protected void convert(@NotNull BaseViewHolder helper, @Nullable IncidentBean incidentBean) {
            helper.setText(R.id.item_index, String.valueOf(incidentBean.getSeverity()));
            helper.setText(R.id.item_type, incidentBean.getINCIDENT_type());
            helper.setText(R.id.item_time, incidentBean.getDATE() + " "+ incidentBean.getTIME());
            helper.setText(R.id.item_desc, incidentBean.getArea());

            String incident_type = incidentBean.getINCIDENT_type();
            if (incident_type.equals("Flooding") | incident_type.equals("Rainstorm") | incident_type.equals("Fire")
                    | incident_type.equals("Landslide")) {
                helper.setText(R.id.tv_type, "Natural disaster");
            } else {
                helper.setText(R.id.tv_type, "Crime");
            }


        }


    }
}