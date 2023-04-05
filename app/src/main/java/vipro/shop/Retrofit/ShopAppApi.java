package vipro.shop.Retrofit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ShopAppApi {
    @Multipart
    @POST("upload.php")
    Call<String> UploadImage(@Part MultipartBody.Part image);
}
