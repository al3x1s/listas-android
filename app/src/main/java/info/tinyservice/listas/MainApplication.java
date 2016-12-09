package info.tinyservice.listas;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import info.tinyservice.listas.database.DatabaseHelper;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MainApplication extends Application {

    public static final String DEFAULT_SERVER = "http://162.248.52.101:3020/api/";
    private OkHttpClient client;
    private WebService service;
    private Retrofit retrofit;

    private DatabaseHelper dataManager;

    public interface GetServiceCallback{
        void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service);
        boolean onFailure();
    }

    private final List<GetServiceCallback> callbacks = new LinkedList<>();
    public void getServiceAsync(GetServiceCallback callback){
        if(service != null){
            callback.onServiceReady(client, retrofit, service);
        }else{
            if (callbacks.isEmpty()){
                initService();
            }
            callbacks.add(callback);
        }
    }

    public WebService getService(){
        if(service == null){
            initService();
        }
        return service;
    }

    public DatabaseHelper getDataManager(){
        return this.dataManager;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        dataManager = new DatabaseHelper(this);
    }



    public void initService(){
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager)).build();
        try{
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(DEFAULT_SERVER)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        } catch (IllegalArgumentException e){
            Log.e("retrofit", e.getMessage());
            for(GetServiceCallback callback : callbacks){
                callback.onFailure();
            }
            callbacks.clear();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
        service = retrofit.create(WebService.class);
    }

}
