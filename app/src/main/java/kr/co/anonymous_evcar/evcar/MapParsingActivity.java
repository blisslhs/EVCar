package kr.co.anonymous_evcar.evcar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.otto.Bus;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.anonymous_evcar.evcar.bus.BusProvider;
import kr.co.anonymous_evcar.evcar.data.Evc;
import kr.co.anonymous_evcar.evcar.singleton.MapsDataSingleTon;

public class MapParsingActivity extends AppCompatActivity {
    ArrayList<Evc> items = new ArrayList<>();


    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_parsing);

        Log.d("yjhtest", "실행되었습니다.,");
        new GetXMLTask1().execute();



        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            long minTime = 10000*360;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            longitude = location.getLongitude();
                            latitude = location.getLatitude();

                            Log.d("yjh", "경도:" +longitude);

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch(SecurityException e) {
            e.printStackTrace();
        }





    }

    @OnClick(R.id.btn_go)
    void onCallClick(View view) {
        Intent intent = new Intent(MapParsingActivity.this, MapsActivity.class);
        intent.putExtra("targetLat", latitude);
        intent.putExtra("targetLong", longitude);
        startActivity(intent);
    }




    private class GetXMLTask1 extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {

            URL url;
            Document doc;
            try {
                url = new URL("http://openapi.kepco.co.kr/service/evInfoService/getEvSearchList?addr=%EC%84%9C%EC%9A%B8&pageNo=1&numOfRows=128&ServiceKey=tEaGlxH27VIl1Ulb35W9NPz25DYz%2FgaaXxkPE9Qw8BI8KdWR8wgytpbtozEnynLnGmvf70xz8SZMkkaghNw3Xw%3D%3D");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                return doc;
            } catch (Exception e) {
                Toast.makeText(MapParsingActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";
            NodeList nodeList = doc.getElementsByTagName("item");
            Integer num = nodeList.getLength();


            for(int i = 0; i< nodeList.getLength(); i++){


                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList addr = fstElmnt.getElementsByTagName("addr");

                NodeList chargeTp = fstElmnt.getElementsByTagName("chargeTp");


                NodeList cpTp  = fstElmnt.getElementsByTagName("cpTp");


                NodeList csNm = fstElmnt.getElementsByTagName("csNm");


                NodeList lat = fstElmnt.getElementsByTagName("lat");


                NodeList longi = fstElmnt.getElementsByTagName("longi");


                Evc evc = new Evc(addr.item(0).getChildNodes().item(0).getNodeValue(),
                        chargeTp.item(0).getChildNodes().item(0).getNodeValue(),
                        cpTp.item(0).getChildNodes().item(0).getNodeValue(),
                        csNm.item(0).getChildNodes().item(0).getNodeValue(),
                        lat.item(0).getChildNodes().item(0).getNodeValue(),
                        longi.item(0).getChildNodes().item(0).getNodeValue());
                items.add(evc);






            }
            MapsDataSingleTon mapsDataSingleton = MapsDataSingleTon.getInstance();



            //status1.setText(s);

            super.onPostExecute(doc);
        }
    }
}
