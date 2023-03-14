package com.example// import okhttp3.ResponseBody
// import retrofit2.Call
// import retrofit2.http.*

// interface RegisterInterface {

//     @POST("https://id.itmo.ru/auth/realms/itmo/protocol/openid-connect/auth?response_type=code&scope=openid&client_id=isu&redirect_uri=https://isu.ifmo.ru/api/sso/v1/public/login")
//     @FormUrlEncoded
//     fun register(
//         // @Header("Cookie") cookie: String,
//         @Field("response_type") response_type: String,
//         @Field("scope") scope: String,
//         @Field("client_id") client_id: String,
//         @Field("redirect_uri") redirect_uri: String,
//     ): Call<ResponseBody>

// }


// import okhttp3.ResponseBody
// import retrofit2.Call
// import retrofit2.http.*

// interface com.example.APIInterface {

//     @POST("/pls/apex/wwv_flow.show")
//     @FormUrlEncoded
//     fun getRoomInfo(
//         @Header("Cookie") cookie: String,
//         @Field("p_request") p_request: String,
//         @Field("p_instance") p_instance: String,
//         @Field("p_flow_id") p_flow_id: String,
//         @Field("p_flow_step_id") p_flow_step_id: String,
//         @Field("p_arg_names") p_arg_names1: String,
//         @Field("p_arg_values") p_arg_values1: String,
//         @Field("p_arg_names") p_arg_names2: String,
//         @Field("p_arg_values") p_arg_value2: String,
//         @Field("p_arg_names") p_arg_names3: String,
//         @Field("p_arg_values") p_arg_value3: String,

//     ): Call<ResponseBody>

// }