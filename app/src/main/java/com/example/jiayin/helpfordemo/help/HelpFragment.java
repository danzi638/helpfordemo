package com.example.jiayin.helpfordemo.help;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.jiayin.helpfordemo.R;
import com.example.jiayin.helpfordemo.app.base.BaseFragment;
import com.example.jiayin.helpfordemo.help.activity.DriveRouteDetailActivity;
import com.example.jiayin.helpfordemo.help.activity.GiveMoneyActivity;
import com.example.jiayin.helpfordemo.utils.Constant;
import com.example.jiayin.helpfordemo.help.util.amap.AMapUtil;
import com.example.jiayin.helpfordemo.help.util.amap.DrivingRouteOverlay;
import com.example.jiayin.helpfordemo.help.util.amap.ToastUtil;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiayin on 2017/8/17.
 */

public class HelpFragment extends BaseFragment implements PoiSearch.OnPoiSearchListener, AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {

    private static final String TAG = "hello";

    private AMap aMap;
    private MyLocationStyle myLocationStyle;

    //poiSearch相关
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    boolean isPoiSearched = false; //是否进行poi搜索
    boolean onSearch = false; //是否打开搜索栏

    //listview
    ArrayList<PoiItem> arrayList;
    MyAdpter adapter;
    MyHandler myHandler;

    private double mStartCurrentLat;
    private double mStartCurrentLng;
    private double mEndCurrentLat;
    private double mEndCurrentLng;
    Map<String, String> currentInfo = new HashMap<>();
    int selectIndex = -1;

    ImageView currentSelectItem = null;
    private LatLonPoint llEnd;
    private LatLonPoint llStart;
    //路径规划
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private final int ROUTE_TYPE_DRIVE = 2;

    private PoiItem item;

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.search_input)
    EditText searchInput;
    @Bind(R.id.search)
    ImageView searchIv;
//    @Bind(R.id.searchLayout)
//    FrameLayout searchLayout;
    @Bind(R.id.search_go_btn)
    Button btnSeachGoto;
    @Bind(R.id.map)
    MapView mMapView = null;
    @Bind(R.id.firstline)
    TextView firstline;
    @Bind(R.id.secondline)
    TextView secondline;
    @Bind(R.id.thirdline)
    TextView thirdline;
    @Bind(R.id.btn_detail_root)
    Button btnDetailRoot;
    @Bind(R.id.btn_buy)
    Button btnBuy;
    @Bind(R.id.bottom_layout)
    LinearLayout mBottomLayout;
    @Bind(R.id.ll)
    ListView ll;
    private String startLocation;
    Menu menu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_help, null);
        ButterKnife.bind(this, view);
//        toolbar.setTitle("求助");
        mMapView.onCreate(savedInstanceState);
        initAMap();
        setAllViewOnclickLinster();

        return view;
    }


    @Override
    protected void initData() {
        super.initData();
        arrayList = new ArrayList<>();
        adapter = new MyAdpter();
        ll.setAdapter(adapter);
        myHandler = new MyHandler();
        mRouteSearch = new RouteSearch(mContext);
        mRouteSearch.setRouteSearchListener(this);
    }

    /**
     * 设置点击事件
     */
    void setAllViewOnclickLinster() {
        //当搜索图标点击时，切换显示效果
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getVisibility() == View.VISIBLE) {
                    hideTitle();
                } else if (title.getVisibility() == View.GONE) {
                    showTitle();
                }
            }
        });

        //点击搜索按钮时，搜索关键字
        btnSeachGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = searchInput.getText().toString();
                if (!key.trim().isEmpty()) {
                    if (currentSelectItem != null) {
                        currentSelectItem.setVisibility(View.INVISIBLE);
                    }
                    searchPoi(key, 0, currentInfo.get("cityCode"), false);
                }
            }
        });

        //使editText监听回车事件，进行搜索，效果同上
        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String key = searchInput.getText().toString();
                    if (!key.trim().isEmpty()) {
                        if (currentSelectItem != null) {
                            currentSelectItem.setVisibility(View.INVISIBLE);
                        }
                        searchPoi(key, 0, currentInfo.get("cityCode"), false);
                    }
                    return true;
                }

                return false;
            }
        });

        //listview点击事件
        ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, i + "");
                item = arrayList.get(i);
                mEndCurrentLat = item.getLatLonPoint().getLatitude();
                mEndCurrentLng = item.getLatLonPoint().getLongitude();
                Log.e(TAG, item.getLatLonPoint().toString());
                Log.e(TAG, item.toString());
                Log.e(TAG, item.getAdName());
                Log.e(TAG, String.valueOf(mEndCurrentLat));
                Log.e(TAG, String.valueOf(mEndCurrentLng));
                //在地图上添加一个marker，并将地图中移动至此处
                MarkerOptions mk = new MarkerOptions();
                mk.icon(BitmapDescriptorFactory.defaultMarker());
                mk.title(item.getAdName());
                LatLng ll = new LatLng(mEndCurrentLat, mEndCurrentLng);
                llEnd = new LatLonPoint(mEndCurrentLat, mEndCurrentLng);
                llStart = new LatLonPoint(mStartCurrentLat, mStartCurrentLng);
                mk.position(ll);
                //清除所有marker等，保留自身
                aMap.clear(true);
                CameraUpdate cu = CameraUpdateFactory.newLatLng(ll);
                aMap.animateCamera(cu);
                aMap.addMarker(mk);

                //存储当前点击位置
                selectIndex = i;

                //存储当前点击view，并修改view和上一个选中view的定位图标
                ImageView iv = (ImageView) view.findViewById(R.id.yes);
                iv.setVisibility(View.VISIBLE);
                if (currentSelectItem != null) {
                    currentSelectItem.setVisibility(View.INVISIBLE);
                }
                currentSelectItem = iv;
                if (onSearch) {
                    //退出搜索模式，显示地图
                    showTitle();
                }
                setfromandtoMarker();
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
            }
        });
        //返回处理事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSearch) {
                    showTitle();
                }
            }
        });
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(llStart))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(llEnd))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (llStart == null) {
            TastyToast.makeText(mContext, "定位中，稍后再试..." , Toast.LENGTH_SHORT, TastyToast.DEFAULT);
            return;
        }
        if (llEnd == null) {
            TastyToast.makeText(mContext, "终点未设置" , Toast.LENGTH_SHORT, TastyToast.ERROR);
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                llStart, llEnd);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    /**
     * 初始化高德地图
     */
    void initAMap() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        //地图加载监听器
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //aMap.setMapType();
                aMap.setMyLocationEnabled(true);

            }
        });


        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        myLocationStyle.radiusFillColor(0x70f3ff);
        myLocationStyle.strokeColor(0xe3f9fd);

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setScaleControlsEnabled(true);//设置地图默认的比例尺是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        //地址监听事件
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!isPoiSearched) {
                    mStartCurrentLat = location.getLatitude();
                    mStartCurrentLng = location.getLongitude();
                    Log.e(TAG, location.toString());
                    Log.e(TAG, location.getProvider());
                    Log.e(TAG, location.getLatitude() + ":" + location.getLongitude());
                    Log.e(TAG, String.valueOf(mStartCurrentLat));
                    Log.e(TAG, String.valueOf(mStartCurrentLng));
                    //存储定位数据

                    String[] args = location.toString().split("#");
                    for (String arg : args) {
                        String[] data = arg.split("=");
                        if (data.length >= 2)
                            currentInfo.put(data[0], data[1]);
                    }
                    startLocation = currentInfo.get("address");
                    firstline.setText("您当前位于：" + startLocation);
                    secondline.setText("您还未设置您的目的地");
                    //搜索poi
                    searchPoi("", 0, currentInfo.get("cityCode"), true);
                    //latitude=41.652146#longitude=123.427205#province=辽宁省#city=沈阳市#district=浑南区#cityCode=024#adCode=210112#address=辽宁省沈阳市浑南区创新一路靠近东北大学浑南校区#country=中国#road=创新一路#poiName=东北大学浑南校区#street=创新一路#streetNum=193号#aoiName=东北大学浑南校区#poiid=#floor=#errorCode=0#errorInfo=success#locationDetail=24 #csid:1cce9508143d493182a8da7745eb07b3#locationType=5

                }
            }
        });
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
//        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";//几分钟，几公里
//                    firstline.setText(des);
                    secondline.setVisibility(View.VISIBLE);
                    btnDetailRoot.setVisibility(View.VISIBLE);
                    btnBuy.setVisibility(View.VISIBLE);
                    final int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    secondline.setText("终点：" + item.toString());
                    thirdline.setText("距离您" + AMapUtil.getFriendlyLength(dis) + "建议悬赏" + taxiCost + "元");
                    btnDetailRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result", mDriveRouteResult);
                            startActivity(intent);
                        }
                    });
                    btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, GiveMoneyActivity.class);
                            intent.putExtra(Constant.START_LOCATION, startLocation);
                            intent.putExtra(Constant.END_LOCATION, item.toString());
                            intent.putExtra(Constant.MONEY, taxiCost);
                            intent.putExtra(Constant.START_CURRENT_LAT,mStartCurrentLat);
                            intent.putExtra(Constant.START_CURRENT_LNG,mStartCurrentLng);
                            intent.putExtra(Constant.END_CURRENT_LAT,mEndCurrentLat);
                            intent.putExtra(Constant.END_CURRENT_LNG,mEndCurrentLng);
                            startActivity(intent);

                            Log.e(TAG, "onClick: " + "点击发布悬赏");
                        }
                    });

                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext.getApplicationContext(), errorCode);
        }


    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    //加载listview中数据
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 自定义adpter
     */
    class MyAdpter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //布局加载器
            LayoutInflater inflater = LayoutInflater.from(mContext);
            //加载location_item布局
            View view1 = inflater.inflate(R.layout.location, null);

            //修改文字和字体
            TextView v1 = (TextView) view1.findViewById(R.id.name);
            TextView v2 = (TextView) view1.findViewById(R.id.sub);
            ImageView iv = (ImageView) view1.findViewById(R.id.yes);
            v1.setText(arrayList.get(i).getTitle());

            v2.setText(arrayList.get(i).getSnippet());

            if (selectIndex == i) {
                iv.setVisibility(View.VISIBLE);
                currentSelectItem = iv;
            } else {
                iv.setVisibility(View.INVISIBLE);
            }
            return view1;
        }
    }

    /**
     * 搜索poi
     *
     * @param key      关键字
     * @param pageNum  页码
     * @param cityCode 城市代码，或者城市名称
     * @param nearby   是否搜索周边
     */
    void searchPoi(String key, int pageNum, String cityCode, boolean nearby) {
        Log.e(TAG, key);
        isPoiSearched = true;
        query = new PoiSearch.Query(key, "", cityCode);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(pageNum);//设置查询页码
        poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(this);
        if (nearby)
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mStartCurrentLat,
                    mStartCurrentLng), 1500));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        int index = 0;
        //填充数据，并更新listview
        ArrayList<PoiItem> result = poiResult.getPois();
        if (result.size() > 0) {
            arrayList.clear();
            selectIndex = -1;
            arrayList.addAll(result);
            myHandler.sendEmptyMessage(0x001);
        }
        for (PoiItem item : poiResult.getPois()) {
            Log.e(TAG, item.toString());
            Log.e(TAG, item.getDirection());
            Log.e(TAG, item.getAdName());
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 显示标题栏，即默认状态
     */
    void showTitle() {
        //显示标题栏
        back.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        searchInput.setVisibility(View.GONE);
        btnSeachGoto.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.VISIBLE);
        mMapView.setVisibility(View.VISIBLE);
        onSearch = false;
//        closeKeyboard(this);
    }

    /**
     * 隐藏标题栏，即进行搜索
     */
    void hideTitle() {
        //显示搜索框
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
        searchInput.setVisibility(View.VISIBLE);
        ll.setVisibility(View.VISIBLE);
        btnSeachGoto.setVisibility(View.VISIBLE);
        mMapView.setVisibility(View.GONE);
        onSearch = true;
        mBottomLayout.setVisibility(View.GONE);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        Log.i("sys", "mf onDestroy");
        super.onDestroy();
//        mMapView.onDestroy();
    }
}
