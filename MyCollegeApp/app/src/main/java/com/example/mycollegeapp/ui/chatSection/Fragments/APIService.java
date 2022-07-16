package com.example.mycollegeapp.ui.chatSection.Fragments;


import com.example.mycollegeapp.ui.chatSection.Notifications.MyResponse;
import com.example.mycollegeapp.ui.chatSection.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
  @Headers(
          {
                  "Content-Type:application/json",
                  "Authorization:key=AAAAft325ys:APA91bE7obhi2tLB2mGn7PmNLrdif1YQz3Cfnwe-gZ7_bGdP6hXuHu00vrB-KUOHuR6wLAbZnshs7bznaJd5ZNauL_O6x6XkP1VwrLzDBve8dsMGjUtND5uguXhx72m_P9PS9bqYcx0Y"

          }



  )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
