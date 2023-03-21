package com.example.booking

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BookingApi {

    @GET( "/pls/apex/f")
    fun getBookingHtml(
        @Query("p") urlData : String
    ): Call<ResponseBody>

    @POST("/pls/apex/wwv_flow.show")
    @FormUrlEncoded
    fun postSelectedAudToIsu(
        @Field("p_request") p_request : String,
        @Field("p_flow_id") p_flow_id : String = "2431",
        @Field("p_flow_step_id") p_flow_step_id : String = "4",
        @Field("p_instance") p_instance : String,
        @Field("p_debug") p_debug : String = "",
        @Field("p_arg_names") p_arg_names : String = "P4_AUD_SEL",
        @Field("p_arg_names") blank : String = "P4_R_R_ID",
        @Field("p_arg_values") selectedId : String,
        @Field("p_arg_values") p_arg_values : String = ""

        ): Call<ResponseBody>
    @POST("/pls/apex/wwv_flow.show")
    @FormUrlEncoded
    fun getDevicesInfoInAud(
        @Field("p_request") p_request : String = "APXWGT",
        @Field("p_flow_id") p_flow_id : String = "2431",
        @Field("p_flow_step_id") p_flow_step_id : String = "4",
        @Field("p_instance") p_instance : String,
        @Field("p_debug") p_debug : String = "",
        @Field("p_widget_action") p_widget_action : String = "reset",
        @Field("x01") x01 : String = "4881620431290747483",
        @Field("p_widget_name") p_widget_name : String = "classic_report"
    ): Call<ResponseBody>
    //dont work properly and still encode & and =, so custom interceptor is needed
    @POST("/pls/apex/wwv_flow.accept")
    @FormUrlEncoded
    fun book(
        @Field(value = "p_flow_id", encoded = true)  p_flow_id: String
    ): Call<ResponseBody>

    @GET
    fun redirectHandler(
        @Url url: String
    ): Call<ResponseBody>
}