package com.am.aucklank;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.am.aucklank.heatmaps.Gradient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.am.aucklank.heatmaps.HeatmapTileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {


    private SupportMapFragment mapFragment;
    private boolean permissionDenied = false;

    private GoogleMap map = null;

    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int DEFAULT_ZOOM = 15;

    private final LatLng defaultLocation = new LatLng(-36.85070090495492, 174.76457777613288);


    private RelativeLayout rlAddress;
    private ListView lvAddress;

    private LinearLayout llAddress;


    private EditText etAddress;
    private EditText etAddress1;


    private List<Address> addresses;


    private ImageView iv5;
    private ImageView ivClean;


    private List<Object[]> markerItems = new ArrayList<>();
    private DBHelper dbHelper;
    private HeatmapTileProvider mProvider;
    private TileOverlay overlay;
    private HashMap<String, DataSet> mLists = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        openDatabase();
        View iv = findViewById(R.id.iv);
        View iv1 = findViewById(R.id.iv1);
        View iv2 = findViewById(R.id.iv2);

        View iv4 = findViewById(R.id.map_my_position);
        iv5 = findViewById(R.id.iv5);
        ivClean = findViewById(R.id.map_clean);

        etAddress = findViewById(R.id.map_et_address);
        etAddress1 = findViewById(R.id.map_et_address1);

        rlAddress = findViewById(R.id.map_rl_address);
        llAddress = findViewById(R.id.map_ll_address);
        lvAddress = findViewById(R.id.map_lv_address);


        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String text = textView.getText().toString();
                textView.setText(text.replaceAll("\n", ""));
                searchPlaces(text);
                return false;
            }
        };

        etAddress.setOnEditorActionListener(listener);
        etAddress1.setOnEditorActionListener(listener);

        iv.setOnClickListener(this);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        ivClean.setOnClickListener(this);

        rlAddress.setOnClickListener(this);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        markerItems.add(new Object[]{R.mipmap.battery, R.mipmap.battery1, "Battery", false});
        markerItems.add(new Object[]{R.mipmap.flood, R.mipmap.flood1, "Flooding", false});
        markerItems.add(new Object[]{R.mipmap.hurricane, R.mipmap.hurricane1, "Harasment", false});
        markerItems.add(new Object[]{R.mipmap.rain_storm, R.mipmap.rain_storm1, "Rainstorm", false});
        markerItems.add(new Object[]{R.mipmap.fire, R.mipmap.fire1, "Fire", false});


        markerItems.add(new Object[]{R.mipmap.landslide, R.mipmap.landslide1, "Landslide", false});
        markerItems.add(new Object[]{R.mipmap.robbery, R.mipmap.robbery1, "Robbery", false});
        markerItems.add(new Object[]{R.mipmap.sexual_assault, R.mipmap.sexual_assault1, "Sexasault", false});
        markerItems.add(new Object[]{R.mipmap.burglary, R.mipmap.burglary1, "Burglary", false});
        markerItems.add(new Object[]{R.mipmap.theft, R.mipmap.theft1, "Theft", false});


    }


    private void showIncidentDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_incident, null, false);
        Dialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        dialog.show();


        dialogView.findViewById(R.id.incident_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.incident_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:911");
                intent.setData(data);
                startActivity(intent);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//Set dialog background transparency(设置dialog背景透明)
        dialog.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels * 3 / 4, LinearLayout.LayoutParams.WRAP_CONTENT);//Set the size of the dialog box(设置对话框大小)

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }


    class EventGridAdapter extends ArrayAdapter<Object[]> {

        public EventGridAdapter(@NonNull Context context, int resource, List<Object[]> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Object[] item = getItem(position);
            View view;
            //Add an internal class ViewHolder for caching instances of the control(新增一个内部类 ViewHolder，用于对控件的实例进行缓存)
            ViewHolder viewHolder;
            if (convertView == null) {
                //Load a set layout for each child item (为每一个子项加载设定的布局)
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_event, parent, false);
                viewHolder = new ViewHolder();
                //Get instances of imageview and textview respectively (分别获取 imageview 和 textview 的实例)
                viewHolder.icon = view.findViewById(R.id.item_icon);
                viewHolder.label = view.findViewById(R.id.item_label);
                view.setTag(viewHolder);//Store the viewHolder in the view (将 viewHolder 存储在 view 中)
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();//Retrieve the viewHolder (重新获取 viewHolder)
            }
            // Set the image and text to be displayed (设置要显示的图片和文字)
            if ((boolean) item[3] == true) {
                viewHolder.icon.setImageDrawable(getDrawable((Integer) item[1]));
            } else {
                viewHolder.icon.setImageDrawable(getDrawable((Integer) item[0]));
            }

            viewHolder.label.setText((String) item[2]);
            return view;
        }

        private class ViewHolder {
            ImageView icon;
            TextView label;
        }

    }


    private boolean zoom = false;

    private void showEventDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_event, null, false);
        Dialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();

        dialog.getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置dialog背景透明

        dialog.findViewById(R.id.event_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        GridView view = dialog.findViewById(R.id.event_grid);


        EventGridAdapter adapter = new EventGridAdapter(this, R.layout.dialog_event, markerItems);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick:     " + i);
                if (!zoom) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6));
                }
                int[] DEFAULT_GRADIENT_COLORS = { Color.rgb(102, 225, 0),
                        Color.rgb(255, 0, 0)};
                float[] DEFAULT_GRADIENT_START_POINTS = {
                                    0.2f, 1f
                            };
                String crime = "";
                switch (i) {

                    case 0:
                        crime = "Battery";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(102, 225, 0),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 1:
                        crime = "Flooding";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(102, 225, 0),
                                Color.rgb(255, 0, 0)};
                        break;
                    case 2:
                        crime = "Harassment";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(187, 134, 252),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 3:
                        crime = "Storm";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(98, 0, 238),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 4:
                        crime = "Fire";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(244, 67, 54),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 5:
                        crime = "Landslide";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(189, 77, 121),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 6:
                        crime = "Robbery";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(33, 150, 243),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 7:
                        crime = "Sexual Assault";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(102, 255, 0),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 8:
                        crime = "Burglary";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(255, 235, 59),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                    case 9:
                        crime = "Theft";
                        DEFAULT_GRADIENT_COLORS = new int[]{
                                Color.rgb(49, 109, 228),
                                Color.rgb(255, 0, 0)
                        };
                        break;
                }
                if (dbHelper == null) {
                    dbHelper = new DBHelper(MapActivity.this);
                }


                ArrayList<LatLng> list = extracted(crime);
                Log.d(TAG, "onCheckedChanged:    " + crime + "        " +  list.size());
                if (list.size() == 0) {
                    Toast.makeText(MapActivity.this, "无数据", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }


                if (null == mProvider) {
                    mProvider = new HeatmapTileProvider.Builder().data(list).build();
                    mProvider.setGradient(new Gradient(DEFAULT_GRADIENT_COLORS,DEFAULT_GRADIENT_START_POINTS));

                    overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    // Render links
                    //  attribution.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    mProvider.setData(list);
                    mProvider.setGradient(new Gradient(DEFAULT_GRADIENT_COLORS,DEFAULT_GRADIENT_START_POINTS));
                    overlay.clearTileCache();
                }
                zoom = true;

                for(int index=0;index<markerItems.size();index++){
                    markerItems.get(index)[3] = false;
                }
                markerItems.get(i)[3] = !((boolean) markerItems.get(i)[3]);
                adapter.notifyDataSetChanged();
                //showMarker(i);
                dialog.dismiss();


            }
        });

        ((Switch) dialog.findViewById(R.id.event_show_all)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
       //         for (int i = 0; i < markerItems.size(); i++) {
       //             markerItems.get(i)[3] = b;
                int[] DEFAULT_GRADIENT_COLORS = { Color.rgb(102, 225, 0),
                        Color.rgb(255, 0, 0)};
                float[] DEFAULT_GRADIENT_START_POINTS = {
                        0.2f, 1f
                };
                    if (b) {
//                        showMarker(i);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6));
                        if (null == dbHelper) {
                            dbHelper = new DBHelper(MapActivity.this);
                        }
                        Cursor query = dbHelper.query("");
                        ArrayList<LatLng> list = new ArrayList<>();


                        if (query != null) {
                            while (query.moveToNext()) {
                                @SuppressLint("Range") String latitude = query.getString(query.getColumnIndex("latitude"));
                                @SuppressLint("Range") String Longitude = query.getString(query.getColumnIndex("longitude"));
                                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(Longitude));
                                list.add(latLng);
                            }
                        }
                        query.close();
                        dbHelper.colse();
                      //  System.gc();
                        Log.d(TAG, "onCheckedChanged: " + list.size());
                        if (mProvider == null) {
                            mProvider = new HeatmapTileProvider.Builder().data(list).build();
                            mProvider.setGradient(new Gradient(DEFAULT_GRADIENT_COLORS,DEFAULT_GRADIENT_START_POINTS));
                            overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                        } else {
                            mProvider.setData(list);
                            mProvider.setGradient(new Gradient(DEFAULT_GRADIENT_COLORS,DEFAULT_GRADIENT_START_POINTS));
                            overlay.clearTileCache();
                        }
                        for (int i = 0; i < markerItems.size(); i++) {
                                         markerItems.get(i)[3] = b;}
                    } else {
                        if (null == mProvider) {
                        } else {

                            mProvider = null;
                            overlay.remove();
                        }
                        for (int i = 0; i < markerItems.size(); i++) {
                            markerItems.get(i)[3] = false; }

                    }
       //     }
                adapter.notifyDataSetChanged();
          //  }
        }});
    }

    private ArrayList<LatLng> extracted(String robbery) {
        Cursor query = dbHelper.query(robbery);
        ArrayList<LatLng> list = new ArrayList<>();
        if (query != null) {

            while (query.moveToNext()) {
                @SuppressLint("Range") String latitude = query.getString(query.getColumnIndex("latitude"));
                @SuppressLint("Range") String Longitude = query.getString(query.getColumnIndex("longitude"));
                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(Longitude));
                list.add(latLng);
            }
        }
        query.close();
        System.gc();
        mLists.put(robbery, new DataSet(list, null));
        return list;
    }


    private void showMarker(int index) {
        if (index > 0) {
            Object[] item = markerItems.get(index);
            String label = (String) item[2];
            MarkerOptions markerOptions = null;
            if (label.equals("Volcano")) {
//                markerOptions = new MarkerOptions().position(defaultLocation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.circle));
//                map.addMarker(markerOptions);

                markerOptions = new MarkerOptions().position(defaultLocation).icon(BitmapDescriptorFactory.fromResource(R.mipmap.circle_vector)).title(label);
                map.addMarker(markerOptions);
            } else {
                markerOptions = new MarkerOptions().position(defaultLocation).icon(BitmapDescriptorFactory.fromResource((Integer) item[0]));
                map.addMarker(markerOptions);
            }

            iv5.setImageResource((Integer) item[0]);

            ivClean.setVisibility(View.VISIBLE);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    defaultLocation, DEFAULT_ZOOM));
            Toast.makeText(MapActivity.this, label, Toast.LENGTH_SHORT).show();
        } else {
            ivClean.setVisibility(View.GONE);
        }
    }


    private void searchPlaces(String address) {
        if (address == null || address.trim().length() == 0) return;
        etAddress1.setText(address);
        etAddress1.setText(address);
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            rlAddress.setVisibility(View.VISIBLE);
            addresses = geoCoder.getFromLocationName(address, 5);
            AddressListAdapter adapter = new AddressListAdapter(this, R.layout.item_adress, addresses);
            lvAddress.setAdapter(adapter);

            lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    rlAddress.setVisibility(View.GONE);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(addresses.get(i).getLatitude(),
                                    addresses.get(i).getLongitude()), DEFAULT_ZOOM));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv:
                // startActivity(new Intent(MapActivity.this, ListActivity.class));
                this.showIncidentDialog();
                break;
            case R.id.iv1:
                startActivity(new Intent(MapActivity.this, ReportActivity.class));
                break;
            case R.id.iv2:
                startActivity(new Intent(MapActivity.this, ListActivity.class));
                //this.showIncidentDialog();
                break;

            case R.id.iv5:
                this.showEventDialog();
                break;
            case R.id.map_clean:
                ivClean.setVisibility(View.GONE);
                iv5.setImageResource(R.mipmap.alarm1);
                map.clear();
                break;
            case R.id.map_my_position:
                // Prompt the user for permission.
                getLocationPermission();

                // Turn on the My Location layer and the related control on the map.
                updateLocationUI();

                // Get the current location of the device and set the position of the map.
//                getDeviceLocation();
                map.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                map.getUiSettings().setMyLocationButtonEnabled(false);
                break;
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
       /* if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();*/

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();


        //locate to current place (定位到当前坐标)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14));


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
        // [END maps_check_location_permission]
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    class AddressListAdapter extends ArrayAdapter<Address> {

        public AddressListAdapter(@NonNull Context context, int resource, List<Address> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Address address = getItem(position);//Get the Fruit instance of the current item (获取当前项的 Fruit 实例)
            View view;
            //Add an internal class ViewHolder for caching instances of the control (新增一个内部类 ViewHolder，用于对控件的实例进行缓存)
            ViewHolder viewHolder;
            if (convertView == null) {
                //Load a set layout for each child item (为每一个子项加载设定的布局)
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_adress, parent, false);
                viewHolder = new ViewHolder();
                // Get instances of imageview and textview respectively (分别获取 imageview 和 textview 的实例)
                viewHolder.title = view.findViewById(R.id.title);
                viewHolder.desc = view.findViewById(R.id.desc);
                view.setTag(viewHolder);//将 viewHolder 存储在 view 中
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();// Retrieve the viewHolder(重新获取 viewHolder)
            }
            // Set the image and text to be displayed (设置要显示的图片和文字)
            viewHolder.title.setText(address.getCountryName() + address.getFeatureName());
            viewHolder.desc.setText(address.getAddressLine(0));
            return view;
        }

        private class ViewHolder {
            TextView title;
            TextView desc;
        }

    }

    private String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
            + "/" + "com.am.aucklank" + "/databases/"; // 在手机里存放数据库的位置

    public void openDatabase() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File db_file = new File(DB_PATH, "final.db");
        if (!db_file.exists()) {
            AssetManager am = MapActivity.this.getAssets();
            try {
                InputStream is = am.open("final.db");
                FileOutputStream fos = new FileOutputStream(db_file);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // the database injected (欲导入的数据库)
        }
    }

    private class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {
            return mDataset;
        }

        public String getUrl() {
            return mUrl;
        }
    }


}