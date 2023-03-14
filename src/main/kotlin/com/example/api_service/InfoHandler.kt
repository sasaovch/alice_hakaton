package com.example.api_service
import com.example.models.Room
import com.example.models.ScheduledRoom
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.regex.Pattern

object InfoHandler {
    val ISU_APP_COOKIE: String = "ISU_AP_COOKIE=ORA_WWV-9wgltnwtLiGaJR5elh7MTxvG"
    val ISU_LIB_SID: String = "ISU_LIB_SID=ORA_WWV-9wgltnwtLiGaJR5elh7MTxvG"
    val ORA_WWV_RAC_INSTANCE: String = "ORA_WWV_RAC_INSTANCE=2"
    val REMEMBER_SSO: String =
        "REMEMBER_SSO=FC84F51D11B63EA68D58127C20BBD205:D8C00EED818CD17B9CFF727951816DD5950A7AE94044D326056EEB3D5F4A4A3C451F02C5C70D9CFEAAA40DB2CF0736D0"
    val p_request: String = "PLUGIN=EAE6166322D1B57EE5653B4A4C8BA147FC2BCA7C3102BF5F9180B8EF40B2DC1F"
    val p_instance: String = "102873203866118"
    val p_flow_id: String = "2431"
    val p_flow_step_id: String = "4"

    private val client = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).followRedirects(false)
        .followSslRedirects(false).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://id.itmo.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    fun getRoomInfo(roomId: Int, date: String): ScheduledRoom {
        val request = retrofit.create(APIInterface::class.java)

        var parsed: ScheduledRoom = ScheduledRoom("", HashMap(), -1);
        val response = request.getRoomInfo(
            ISU_APP_COOKIE,
            p_request,
            p_instance,
            p_flow_id,
            p_flow_step_id,
            "P4_AUD_SEL",
            "-17",
            "P4_ROOMS_ID2",
            roomId.toString(),
            "P4_DATE",
            date
        )
        //no errors handling!
//        parsed = parser.parseSchedule(response.execute().body()?.string(), roomId, date)


        return parsed
    }

    fun getFreeRoomByDateAndTime(room: Room): List<Room> {
        return emptyList();
    }

    // fun getRoomInfo() {
    //     val request = retrofit.create(com.example.api_service.APIInterface::class.java)

    //     val call = request.getRoomInfo(
    //         ISU_APP_COOKIE,
    //         p_request,
    //         p_instance,
    //         p_flow_id,
    //         p_flow_step_id,
    //         "P4_AUD_SEL",
    //         "-17",
    //         "P4_ROOMS_ID2",
    //         "29,30,36,41,73,77,82,84,85,86,91,93,95,145,200,215,516,20005,20007",
    //         "P4_DATE",
    //         "25.03.2023"

    //     )
    //     call.enqueue(object : Callback<ResponseBody> {
    //         override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
    //             if (response.isSuccessful) {
    //                 println(response.body()?.string())
    //                 // Handle successful response
    //             } else {
    //                 val errorMessage = response.message() // Get error message
    //                 print(errorMessage)
    //             }
    //         }

    //         override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
    //             val errorMessage = t.message // Get error message
    //             print(errorMessage)
    //         }
    //     })

    // }

    fun register() {
        val request = retrofit.create(APIInterface::class.java)

        val call = request.register(
            "code",
            "openid",
            "isu",
            "https://isu.ifmo.ru/api/sso/v1/public/login",
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val resp = response.body()?.string()
                    println(resp)
                    val pattern = Pattern.compile("session_code=.+&amp;client_id=isu&amp;tab_id=.{11}")
                    val matcher = pattern.matcher(resp)
                    matcher.find()
                    val params = resp?.substring(matcher.start(), matcher.end())!!.split("=")
                    val session_code = params[1].split("&")[0]
                    var tab = params[4]
                    println(session_code)
                    println(tab)
                    val Cookielist = response.headers().values("Set-Cookie")
                    var cookies = ""
                    for (i in Cookielist) {
                        val new_c = i.split(";")[0].plus("; ")
                        cookies = cookies.plus(new_c)
                    }
                    println(cookies)
                    auth(session_code, cookies, tab)
                } else {
                    val errorMessage = response.message() // Get error message
                    print(errorMessage)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorMessage = t.message // Get error message
                print(errorMessage)
            }
        })
    }

    fun auth(session_code: String, cookies: String, tab: String) {
        val request = retrofit.create(APIInterface::class.java)

        val call = request.authenticate(
            session_code = session_code,
            cookie = cookies,
            tab_id = tab,
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 302) {
                    val resp = response.body()?.string()
                    println(resp)
                    val Cookielist = response.headers().values("Set-Cookie")
                    var cookies = ""
                    for (i in Cookielist) {
                        val new_c = i.split(";")[0].plus("; ")
                        cookies = cookies.plus(new_c)
                    }
                } else {
                    val errorMessage = response.message() // Get error message
                    println(response)
                    print(errorMessage)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorMessage = t.message // Get error message
                print(errorMessage)
            }
        })
    }
}
