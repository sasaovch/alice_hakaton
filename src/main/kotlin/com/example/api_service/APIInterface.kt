package com.example.api_service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {


    @POST("/pls/apex/wwv_flow.show")
    @FormUrlEncoded
    fun getRoomInfo(
        @Header("Cookie") cookie: String,
        @Field("p_request") p_request: String,
        @Field("p_instance") p_instance: String,
        @Field("p_flow_id") p_flow_id: String,
        @Field("p_flow_step_id") p_flow_step_id: String,
        @Field("p_arg_names") p_arg_names1: String,
        @Field("p_arg_values") p_arg_values1: String,
        @Field("p_arg_names") p_arg_names2: String,
        @Field("p_arg_values") p_arg_value2: String,
        @Field("p_arg_names") p_arg_names3: String,
        @Field("p_arg_values") p_arg_value3: String

        ): Call<ResponseBody>

    @GET("/pls/apex/f?p=2431:4:1019:/")
    fun checkForRedirect(
        @Header("Cookie") cookie: String


        ): Call<ResponseBody>

    @GET("/pls/apex/f?p=2431:4:1234:/")
    fun getHtmlWithPlugin(
        @Header("Cookie") cookie: String
    ): Call<ResponseBody>
}