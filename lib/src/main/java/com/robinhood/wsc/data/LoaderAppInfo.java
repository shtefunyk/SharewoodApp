package com.robinhood.wsc.data;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.robinhood.wsc.interfaces.IValueListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoaderAppInfo {

    public static class Info {
        public String url;
        public String urlInvisible;
        public boolean saveLastUrl;
        public Notification notification;
    }

    public static class Notification {
        public String text;
        public Integer start; // Minutes
        public Integer interval; // Minutes
        public Integer maxCount;
    }

    public static void loadInfo(String geo, String bundle, String naming, IValueListener<Info> listener) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<EntityAppInfo> call = apiService.getInfo(geo, bundle, naming);
        call.enqueue(new Callback<EntityAppInfo>() {
            @Override
            public void onResponse(@NonNull Call<EntityAppInfo> call, @NonNull Response<EntityAppInfo> response) {
                if(response.isSuccessful() && response.body() != null) {
                    EntityAppInfo dataEntity = response.body();

                    Info info = new Info();

                    if(!TextUtils.isEmpty(dataEntity.getUrl())) info.url = dataEntity.getUrl();
                    if(!TextUtils.isEmpty(dataEntity.getUrl_invisible())) info.urlInvisible = dataEntity.getUrl_invisible();
                    info.saveLastUrl = dataEntity.getSave_last_url() != null ? dataEntity.getSave_last_url() : false;

                    if(dataEntity.getPush() != null) {
                        Notification notification = new Notification();
                        EntityPushInfo push = dataEntity.getPush();

                        notification.text = push.getText();
                        notification.start = push.getStart();
                        notification.interval = push.getInterval();
                        notification.maxCount = push.getMax_count();
                        info.notification = notification;
                    }
                    listener.value(info);
                }
                else listener.failed();
            }

            @Override
            public void onFailure(@NonNull Call<EntityAppInfo> call, @NonNull Throwable t) {
                listener.failed();
            }
        });
    }

    public static void countUniqueUser(String geo, String bundle) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.addUniqueUser(geo, bundle);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) { }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) { }
        });

    }
}
