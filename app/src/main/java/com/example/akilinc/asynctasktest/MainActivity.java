package com.example.akilinc.asynctasktest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String degisken; //String değişkenimizi tanımladık.

    private ProgressDialog pDialog; //arkaplanda http işlemlerimizi yaparken kullanıcıya gösterilecek Loader

    //php connections
    JSONParser jsonParser = new JSONParser();   //JSONParser sınıfımızdan nesne oluşturduk
    private static String phpUrl = "";  //php dosyamızın yolu, eğer localhostta ise localhost yolu
    private JSONObject json;    //JSONObject nesnemiz, gelen json un içinden istediğimiz veriyi alabilmek için

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        degisken = "14";    //degiskenimize değer atadık.

        new AsyncTaskTest().execute();  //asyncTask fonksiyonumuzu çalıştırdık.
    }

    class AsyncTaskTest extends AsyncTask<String,String,String>{    //asyncTask sınıfımızı çalıştırdık

        @Override
        protected void onPreExecute() {     // asyncTask sınıfımızın ilk fonksiyonu OnPreExecute,
                                            // burada HttpRequest başlayıp bitene kadar ki kısımda
                                            // neler olmasını istediğimizi kodluyoruz.
                                            // biz dialog ile bir Loader ve mesaj gösterdik.

            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Değişken yollanıyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args){    // ikinci fonksiyonumuzda ise arkaplanda
                                                            // yapılacak işlerimiz olacak.

            // Building Parameters
            List<NameValuePair> params = new ArrayList<>(); // yeni bir arrayList oluşturduk,
                                                            // burada webservisimize yollayacağımız
                                                            // parametreler olacak.

            params.add(new BasicNameValuePair("degisken", degisken));     // parametre olarak sadece
                                                                                // degisken adlı değişkenimizi yolluyoruz.

            json = jsonParser.makeHttpRequest(phpUrl,       // HttpRequestimizin urlsini, metodunu ve parametreleri belirledik.
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            return null;
        }

        protected void onPostExecute(String file_url){      // üçüncü fonksiyonumuzda ise arkaplan
                                                            // işlemlerimiz bittikten sonraki işlemlerimiz olacak.

            pDialog.dismiss();                       // dialog'u kapatıyoruz.

            try {

                int kontrol = json.getInt("success");       // gönderdiğimiz JSON'da bir adet int değerini aldık.
                String degisken = json.getString("degisken");   // JSON içindeki String değeri de aldık.

                if (kontrol == 1){

                    Toast.makeText(getApplicationContext(), degisken, Toast.LENGTH_LONG).show();

                    //startActivity(new Intent(OlcumOrtamBilgileri.this, OlcumNoktalariEkle.class));
                }

                else{

                    Toast.makeText(getApplicationContext(), "Başarısız.", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }
}
