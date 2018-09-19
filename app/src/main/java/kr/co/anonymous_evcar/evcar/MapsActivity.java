package kr.co.anonymous_evcar.evcar;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.anonymous_evcar.evcar.data.Evc;
import kr.co.anonymous_evcar.evcar.data.EvcCharge;
import kr.co.anonymous_evcar.evcar.singleton.MapsDataSingleTon;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ArrayList<EvcCharge> info_items = MapsDataSingleTon.getInstance().getItems();
    Intent intent;
    double latitude;
    double longitude;

    @BindView(R.id.et_addr) EditText et_addr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);


        intent = getIntent();
        latitude = intent.getDoubleExtra("targetLat", 0);
        longitude = intent.getDoubleExtra("targetLong", 0);

        Log.d("kdh", "latitude"+latitude);
        Log.d("kdh", "longitude"+longitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.btn_select)
    void selectEvcStation() {
        String searchWord = et_addr.getText().toString();
        ArrayList<EvcCharge> tmpEvc = new ArrayList<>();
        String searchNm = "";
        Double searchLat = 0.0;
        Double searchLon = 0.0;

        //for문을 돌며 전체 정보 중 검색어가 포함된 정보만 처리하는 for문
        for (int i = 0; i < info_items.size(); i++) {
            if (info_items.get(i).getAddr().contains(searchWord)) {
                EvcCharge evcCharge = new EvcCharge(info_items.get(i).getChargeTp(), info_items.get(i).getCpTp(), info_items.get(i).getAddr(),  info_items.get(i).getCsNm(), info_items.get(i).getLat(), info_items.get(i).getLongi());
                tmpEvc.add(evcCharge);
            }
        }

        //검색결과가 없는 경우 현재위치로 처리하는 조건문
        if(tmpEvc.isEmpty()){
            searchLat = latitude;
            searchLon = longitude;
            Toast.makeText(getApplicationContext(),"검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            searchNm = tmpEvc.get(0).getCsNm();
            searchLat = Double.parseDouble(tmpEvc.get(0).getLat());
            searchLon = Double.parseDouble(tmpEvc.get(0).getLongi());
        }

        //검색 결과 중 0번째 녀석을 대표마커로 띄운다
        LatLng target = new LatLng(searchLat, searchLon);
        mMap.addMarker(new MarkerOptions().position(target).title(searchNm));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // InputMethodManager를 가져옴
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // 감출 때
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Double lat;
        Double lon;
        String csNm;

        String tmp = "";
        String tmpPort = "";

        float color=0.0f;

        String connectorType  = "";

        for (int i = 0 ; i < info_items.size(); i++) {
            String str="";
            lat = Double.parseDouble(info_items.get(i).getLat());
            lon = Double.parseDouble(info_items.get(i).getLongi());
            csNm = info_items.get(i).getCsNm();

            if(info_items.get(i).ChargeTp[0] == 1) {
                tmp += "완속충전";
            }if (info_items.get(i).ChargeTp[1] == 1){
                tmp += " 급속충전";
            }

            if(info_items.get(i).cpTp[0] == 1) {
                tmpPort += " B타입(5핀)";
            } if(info_items.get(i).cpTp[1] == 1) {
                tmpPort += " C타입(5핀)";
            } if(info_items.get(i).cpTp[2] == 1) {
                tmpPort += " BC타입(5핀)";
            }if(info_items.get(i).cpTp[3] == 1) {
                tmpPort += " BC타입(7핀)";
            } if(info_items.get(i).cpTp[4] == 1) {
                tmpPort += " DC차데모";
            }if(info_items.get(i).cpTp[5] == 1) {
                tmpPort += " AC3상";
            }  if(info_items.get(i).cpTp[6] == 1) {
                tmpPort += " DC콤보";
            }if(info_items.get(i).cpTp[7] == 1) {
                tmpPort += " DC차데모";
            }  if(info_items.get(i).cpTp[8] == 1) {
                tmpPort += " (DC차데모+AC3상)";
            }if(info_items.get(i).cpTp[9] == 1) {
                tmpPort += " (DC차데모+DC콤보+AC3상)";
            }

            MarkerOptions markerOptions = new MarkerOptions();

            if(  markerOptions.position(new LatLng(lat, lon)).getSnippet()!= null)
            {
                str+=markerOptions.position(new LatLng(lat, lon)).getSnippet().toString();
            }


            if(tmp.contains("완속충전")){
                markerOptions.position(new LatLng(lat, lon))
                        .title(csNm)
                        .snippet(tmp + tmpPort)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .alpha(0.9f);

            }else {
                markerOptions.position(new LatLng(lat, lon))
                        .title(csNm)
                        .snippet(tmp + tmpPort)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                        .alpha(0.9f);
            }
            mMap.addMarker(markerOptions);

            tmp = "";
            tmpPort = "";

        }

        // Add a marker in Sydney and move the camera
        LatLng target = new LatLng(latitude, longitude);
        Log.d("jhtest", "longitude: "+longitude);
        mMap.addMarker(new MarkerOptions().position(target).title("현재위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 14));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mMap.addMarker(markerOptions);

    }

}
