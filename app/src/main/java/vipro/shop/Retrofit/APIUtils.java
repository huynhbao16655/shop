package vipro.shop.Retrofit;

import vipro.shop.Model.Server;

public class APIUtils {
    public static ShopAppApi getData(){
        return RetrofitInstance.getRetrofit(Server.urlBase).create(ShopAppApi.class);
    }
}
