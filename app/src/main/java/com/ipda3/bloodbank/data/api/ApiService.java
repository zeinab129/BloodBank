package com.ipda3.bloodbank.data.api;


import com.ipda3.bloodbank.data.model.appSettings.AppSettings;
import com.ipda3.bloodbank.data.model.client.Client;
import com.ipda3.bloodbank.data.model.contactUs.ContactUs;
import com.ipda3.bloodbank.data.model.createDonationRequest.CreateDonationRequest;
import com.ipda3.bloodbank.data.model.donations.Donations;
import com.ipda3.bloodbank.data.model.editProfile.EditProfile;
import com.ipda3.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ipda3.bloodbank.data.model.newPassword.NewPassword;
import com.ipda3.bloodbank.data.model.notificationCount.NotificationCount;
import com.ipda3.bloodbank.data.model.notificationSettings.NotificationSettings;
import com.ipda3.bloodbank.data.model.notificationsList.NotificationsList;
import com.ipda3.bloodbank.data.model.post.Post;
import com.ipda3.bloodbank.data.model.postToggleFavorite.PostToggleFavorite;
import com.ipda3.bloodbank.data.model.register.Register;
import com.ipda3.bloodbank.data.model.resetPassword.ResetPassword;
import com.ipda3.bloodbank.view.fragment.homeCycle.CreateDonationFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<Client> onLogin(@Field("phone") String phone,
                        @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<Register> onRegister(@Field("name") String name,
                              @Field("email") String email,
                              @Field("birth_date") String birth_date,
                              @Field("city_id") int city_id,
                              @Field("phone") String phone,
                              @Field("donation_last_date") String donation_last_date,
                              @Field("password") String password,
                              @Field("password_confirmation") String password_confirmation,
                              @Field("blood_type_id") int blood_type_id);

    @FormUrlEncoded
    @POST("profile")
    Call<EditProfile> editProfile(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("birth_date") String birth_date,
                                  @Field("city_id") int city_id,
                                  @Field("phone") String phone,
                                  @Field("donation_last_date") String donation_last_date,
                                  @Field("password") String password,
                                  @Field("password_confirmation") String password_confirmation,
                                  @Field("blood_type_id") int blood_type_id,
                                  @Field("api_token") String api_token);




    @FormUrlEncoded
    @POST("reset-password")
    Call<ResetPassword> onForgetPassword(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("new-password")
    Call<NewPassword> newPassword(@Field("password") String password,
                                  @Field("password_confirmation") String password_confirmation,
                                  @Field("pin_code") String pin_code,
                                  @Field("phone") String phone);

    @FormUrlEncoded
    @POST("post-toggle-favourite")
    Call<PostToggleFavorite> postToggleFavourite(@Field("post_id") int post_id,
                                                 @Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("contact")
    Call<ContactUs> contact(@Field("api_token") String api_token,
                            @Field("title") String title,
                            @Field("message") String message);

    @FormUrlEncoded
    @POST("notifications-settings")
    Call<NotificationSettings> changeNotificationSettings(@Field("api_token") String api_token,
                                                          @Field("governorates[]") List<Integer> governorates,
                                                          @Field("blood_types[]") List<Integer> blood_types);

    @FormUrlEncoded
    @POST("notifications-settings")
    Call<NotificationSettings> getNotificationSettings(@Field("api_token") String api_token);

    @FormUrlEncoded
    @POST("donation-request/create")
    Call<CreateDonationRequest> createDonationRequest(@Field("api_token") String api_token,
                                                      @Field("patient_name") String patient_name,
                                                      @Field("patient_age") int patient_age,
                                                      @Field("blood_type_id") int blood_type_id,
                                                      @Field("bags_num") int bags_num,
                                                      @Field("hospital_name") String hospital_name,
                                                      @Field("hospital_address") String hospital_address,
                                                      @Field("city_id") int city_id,
                                                      @Field("phone") String phone,
                                                      @Field("notes") String notes,
                                                      @Field("latitude") double latitude,
                                                      @Field("longitude") double longitude);

    @GET("blood-types")
    Call<GeneralResponse> bloodTypesList();

    @GET("governorates")
    Call<GeneralResponse> governmentsList();

    @GET("cities")
    Call<GeneralResponse> citiesLst(@Query("governorate_id") int governorate_id);

    @GET("notifications-count")
    Call<NotificationCount> getNotificationsCounter(@Query("api_token") String api_token);

    @GET("donation-requests")
    Call<Donations> donationRequestsList(@Query("api_token") String api_token,
                                         @Query("page") int page);

    @GET("donation-requests")
    Call<Donations> donationsFilteredList(@Query("api_token") String api_token,
                                          @Query("page") int page,
                                          @Query("blood_type_id") int blood_type_id,
                                          @Query("governorate_id") int governorate_id);

    @GET("donation-request")
    Call<Donations> donationDetails(@Query("api_token") String api_token,
                                    @Query("donation_id") int donation_id);

    @GET("posts")
    Call<Post> postsList(@Query("api_token") String api_token,
                         @Query("page") int page);

    @GET("posts")
    Call<Post> postFilteredList(@Query("api_token") String api_token,
                                @Query("page") int page,
                                @Query("keyword") String keyword,
                                @Query("category_id") int category_id);

    @GET("categories")
    Call<GeneralResponse> categories();

    @GET("my-favourites")
    Call<Post> favoritePostsList(@Query("api_token") String api_token,
                                 @Query("page") int page);

    @GET("settings")
    Call<AppSettings> appSettings(@Query("api_token") String api_token);

    @GET("notifications")
    Call<NotificationsList> notificationsList(@Query("api_token") String api_token,
                                              @Query("page") int page);

}
