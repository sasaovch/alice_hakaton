package com.example.api_service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AuthorizationApi {
    @GET("/auth/realms/itmo/protocol/openid-connect/auth")
    fun getBasicCookies(
        @Query("protocol") protocol: String = "oauth2",
        @Query("response_type") responseType: String = "code",
        @Query("client_id") cl_id: String = "student-personal-cabinet",
        @Query("redirect_uri") redirect_uri: String = "https://my.itmo.ru/login/callback",
        @Query("scope") scope: String = "openid",
        @Query("state") state: String = "im_not_a_browser",
        @Query("code_challenge_method") code_challenge_method: String = "S256",
        @Query("code_challenge") code_challenge: String //todo autogeneration
    ): Call<ResponseBody>

    @POST
    @FormUrlEncoded
    fun getRoomInfo(
        @Url url: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("credentialId") credentialId: String


        ): Call<ResponseBody>

    @GET
    fun redirectHandler(
        @Url url: String
    ): Call<ResponseBody>
}