package com.example.booking

import com.example.cookie.CookieHandler
import com.example.util.EncodingInterceptor
import com.example.util.LoggingInterceptor
import okhttp3.OkHttpClient
import org.apache.commons.text.StringEscapeUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class BookingHandler(cookies: CookieHandler, private val pInstance: String) {
    private val bookingParams = listOf<String>(
        "p_flow_id",
        "p_flow_step_id",
        "p_instance",
        "p_page_submission_id",
        "p_request",
        "p_arg_names",
        "p_t01",
        "p_arg_checksums",
        "p_arg_names",
        "p_t02",
        "p_arg_checksums",
        "p_arg_names",
        "p_t03",
        "p_arg_checksums",
        "p_arg_names",
        "p_t04",
        "p_arg_checksums",
        "p_arg_names",
        "p_t05",
        "p_arg_checksums",
        "p_arg_names",
        "p_t06",
        "p_arg_checksums",
        "p_arg_names",
        "p_t07",
        "p_arg_checksums",
        "p_arg_names",
        "p_t08",
        "p_arg_checksums",
        "p_arg_names",
        "p_t09",
        "p_arg_checksums",
        "p_arg_names",
        "p_t10",
        "p_arg_checksums",
        "p_arg_names",
        "p_t11",
        "p_arg_checksums",
        "p_arg_names",
        "p_t12",
        "p_arg_checksums",
        "p_arg_names",
        "p_t13",
        "p_arg_names",
        "p_t14",
        "p_arg_names",
        "p_t15",
        "p_arg_names",
        "p_t16",
        "p_arg_names",
        "p_t17",
        "p_arg_names",
        "p_t18",
        "p_arg_names",
        "p_t19",
        "p_arg_names",
        "p_t20",
        "p_arg_checksums",
        "p_arg_names",
        "p_t21",
        "p_arg_names",
        "p_t22",
        "p_arg_checksums",
        "p_arg_names",
        "p_t23",
        "p_arg_names",
        "p_t24",
        "p_arg_names",
        "p_t25",
        "p_arg_names",
        "p_t26",
        "p_arg_names",
        "p_t27",
        "p_arg_names",
        "p_t28",
        "p_arg_names",
        "p_t29",
        "p_arg_checksums",
        "p_arg_names",
        "p_t30",
        "p_arg_checksums",
        "p_arg_names",
        "p_t31",
        "p_arg_names",
        "p_t32",
        "p_arg_names",
        "p_t33",
        "p_arg_names",
        "p_t34",
        "p_arg_names",
        "p_t35",
        "p_arg_names",
        "p_t36",
        "p_arg_names",
        "p_arg_names",
        "p_t38",
        "p_arg_checksums",
        "p_arg_names",
        "p_t39",
        "p_arg_checksums",
        "p_arg_names",
        "p_t40",
        "p_arg_checksums",
        "p_arg_names",
        "p_t41",
        "p_arg_checksums",
        "p_arg_names",
        "p_t42",
        "p_arg_checksums",
        "p_arg_names",
        "p_t43",
        "p_arg_names",
        "p_t44",
        "p_arg_names",
        "p_t45",
        "p_arg_names",
        "p_t46",
        "p_arg_names",
        "p_t47",
        "p_arg_names",
        "p_t48",
        "p_arg_names",
        "p_t49",
        "p_arg_checksums",
        "f44",
        "p_arg_names",
        "p_t50",
        "p_arg_names",
        "p_t51",
        "p_arg_names",
        "p_t52",
        "p_arg_names",
        "p_t53",
        "p_arg_names",
        "p_t54",
        "p_arg_names",
        "p_t55",
        "p_arg_names",
        "p_t56",
        "p_arg_names",
        "p_t57",
        "p_arg_names",
        "p_t58",
        "p_md5_checksum",
        "p_page_checksum"
    )
    private val cookieStore = cookies.cookieStore
    private val cookieJar = cookies.cookies
    private var client =
        OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(EncodingInterceptor())
            .addInterceptor(LoggingInterceptor()).followRedirects(false)
            .followSslRedirects(false).build()
    private val retrofitIsu = Retrofit.Builder()
        .baseUrl("https://isu.ifmo.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getIdentifierForAudDevices(): String { //returns AjaxIdentifier
        //можно сделать двумя сплитами, но это сильно дольше
        val s1: String =
            bookingHtml.split("{\"triggeringElement\":\"P4_AUD_SEL\",\"triggeringElementType\":\"ITEM\",\"triggeringConditionType\":\"GREATER_THAN\",\"triggeringExpression\":\"0\",\"bindType\":\"live\",\"bindEventType\":\"change\",actionList:[{\"eventResult\":true,\"executeOnPageInit\":true,\"stopExecutionOnError\":true,\"waitForResult\":true,javascriptFunction:apex.da.executePlSqlCode,\"ajaxIdentifier\":\"")[1]
        val s2 = s1.substring(0, s1.indexOf("\""))
        return s2
    }

    private var bookingHtml = ""
    private fun createBookingHtmlParams(pInstance: String): String {
        return "2431:4:${pInstance}::NO:4:P4_MIN_DATE,P4_BACK_PAGE:,"
    }

    private fun getAudDevicesHTML(id: Int): String? { //return
        val request = retrofitIsu.create(BookingApi::class.java)
        var call =
            request.postSelectedAudToIsu(
                "PLUGIN=${getIdentifierForAudDevices()}",
                selectedId = id.toString(),
                p_instance = pInstance
            )
        println(call.execute().body()?.string())
        call = request.getDevicesInfoInAud(p_instance = pInstance)
        return call.execute().body()?.string()
    }

    fun getBookingHtml() {
        val request = retrofitIsu.create(BookingApi::class.java)
        val call = request.getBookingHtml(createBookingHtmlParams(pInstance))
        bookingHtml = call.execute().body()?.string()!!
        //println(bookingHtml)
    }

    private fun getListOfF(html: String): String {
        var res = ""
        var bufHtml = html
        while ("(?<=\")f4[2-4]\".*".toRegex().find(bufHtml, 0) != null) {
            bufHtml = "(?<=\")f4[2-4]\".*".toRegex().find(bufHtml, 0)?.value!!
            val variable = bufHtml.substring(0, 3)
            val value = "(?<=value=\").*?(?=\")".toRegex().find(bufHtml, 0)?.value!!
            if (res == "") res = "$res$variable=$value"
            else
                res = "$res&$variable=$value"
        }
        println(res)
        return res
        //return "(?<=f44\" value=\").*?(?=\")".toRegex().findAll(html, 0).map { it.value }.toMutableList()
    }

    fun test(roomId: Int, date: String, begin: String, end: String) {
        getBookingHtml()
        val bufHtml = (getAudDevicesHTML(roomId))
        val listOfFParams = getListOfF(bufHtml!!)
        sendBookRequest(roomId, date, begin, end, listOfFParams, "NUMBER")
    }

    private fun sendBookRequest(
        roomId: Int,
        date: String,
        begin: String,
        end: String,
        listOfFParams: String,
        phone: String
    ) {
        val request = retrofitIsu.create(BookingApi::class.java)
        val stringBuilder = StringBuilder()
        val localDate = LocalDateTime.now()
        val strDate = localDate.toString().split('T')[0].replace("-", "") + "0000"
        val pattern = "dd.mm.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val dateWithDots = simpleDateFormat.format(Date())
        println(strDate)
        for (param in bookingParams) {
            if (stringBuilder.isNotEmpty())
                stringBuilder.append("&")
            when (param) {
                "p_request" -> stringBuilder.append("$param=${encode("CREATE")}") //set PASS_REQUEST to book
                "p_t15" -> stringBuilder.append("$param=${encode(date)}")
                "p_t16" -> stringBuilder.append("$param=${encode(roomId.toString())}")
                "p_t23" -> stringBuilder.append("$param=${encode("BOOKED_BY_ALICE_SKILL")}")
                "p_t26" -> stringBuilder.append("$param=${encode(begin)}")
                "p_t27" -> stringBuilder.append("$param=${encode(end)}")
                "p_t31" -> stringBuilder.append("$param=${encode(phone)}")
                "p_t46" -> stringBuilder.append("$param=${encode(phone)}")
                "p_t14" -> stringBuilder.append("$param=-603") //todo гибкий метод регистрации
                "p_t17" -> stringBuilder.append("$param=$strDate")
                "p_t19" -> stringBuilder.append("$param=N")
                "p_t21" -> stringBuilder.append("$param=${encode(roomId.toString())}")
                "p_t28" -> stringBuilder.append("$param=3") // by default booking for 3 people
                "p_t32" -> {
                    stringBuilder.append("$param=")
                }
                "p_t33" -> {
                    stringBuilder.append("$param=Y")
                }
                "p_t44" -> {
                    stringBuilder.append("$param=Y")
                }
                "p_t45" -> {
                    stringBuilder.append(
                        "$param=%D0%9F%D1%80%D0%B0%D0%B2%D0%B8%D0%BB%D0%B0+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F%3A+%0D%0A%3Cdiv+style%3D%22margin-left%3A15px%22%3E%0D%0A%3Cul%3E%0D%0A%3Cli%3E%D0%93%D1%80%D0%B0%D1%84%D0%B8%D0%BA+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F+-+%D1%81+10%3A00+-+22%3A00+%28%D0%B2+%D0%BF%D1%80%D0%B0%D0%B7%D0%B4%D0%BD%D0%B8%D1%87%D0%BD%D1%8B%D0%B5+%D0%B4%D0%BD%D0%B8+%D0%BD%D0%B5%D0%B4%D0%BE%D1%81%D1%82%D1%83%D0%BF%D0%BD%D0%BE%29+%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9B%D0%B8%D0%BC%D0%B8%D1%82+%D0%B7%D0%B0%D1%8F%D0%B2%D0%BE%D0%BA+-+1+%D0%B2+%D0%B4%D0%B5%D0%BD%D1%8C%2C+%D0%B4%D0%BB%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE%D1%81%D1%82%D1%8C%D1%8E+%D0%BD%D0%B5+%D0%B1%D0%BE%D0%BB%D0%B5%D0%B5+2%D1%85+%D1%87%D0%B0%D1%81%D0%BE%D0%B2%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9F%D0%B5%D1%80%D0%B8%D0%BE%D0%B4%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B0%D1%8F+%D0%B7%D0%B0%D1%8F%D0%B2%D0%BA%D0%B0+-+%D0%BD%D0%B5+%D0%B1%D0%BE%D0%BB%D0%B5%D0%B5+3%D1%85+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9+%D0%BF%D0%BE+2+%D1%87%D0%B0%D1%81%D0%B0+%D0%BA%D0%B0%D0%B6%D0%B4%D0%B0%D1%8F%3C%2Fli%3E%0D%0A%3Cli%3E%D0%92+%D0%98%D0%A1%D0%A3+%D0%BD%D0%B5%D0%BE%D0%B1%D1%85%D0%BE%D0%B4%D0%B8%D0%BC%D0%BE+%D1%83%D0%BA%D0%B0%D0%B7%D1%8B%D0%B2%D0%B0%D1%82%D1%8C+%D0%BD%D1%83%D0%B6%D0%BD%D0%BE%D0%B5+%D0%BE%D0%B1%D0%BE%D1%80%D1%83%D0%B4%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5%2C+%D0%BF%D1%80%D0%B8%D1%85%D0%BE%D0%B4%D0%B8%D1%82%D1%8C+%D0%B7%D0%B0+5-10+%D0%BC%D0%B8%D0%BD%D1%83%D1%82+%D0%B4%D0%BE+%D0%BD%D0%B0%D1%87%D0%B0%D0%BB%D0%B0+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F%3C%2Fli%3E%0D%0A%3Cli%3E%D0%92+%D1%81%D0%BB%D1%83%D1%87%D0%B0%D0%B5+%D0%BE%D1%82%D0%BC%D0%B5%D0%BD%D1%8B+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F%2C+%D0%BD%D0%B5%D0%BE%D0%B1%D1%85%D0%BE%D0%B4%D0%B8%D0%BC%D0%BE+%D0%B7%D0%B0%D0%B1%D0%BB%D0%B0%D0%B3%D0%BE%D0%B2%D1%80%D0%B5%D0%BC%D0%B5%D0%BD%D0%BD%D0%BE+%D0%BE%D1%82%D0%BC%D0%B5%D0%BD%D0%B8%D1%82%D1%8C+%D0%B1%D1%80%D0%BE%D0%BD%D1%8C%2C+%D0%BF%D1%80%D0%B8+%D0%BE%D0%BF%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D0%B8+%D0%B1%D0%BE%D0%BB%D1%8C%D1%88%D0%B5+20+%D0%BC%D0%B8%D0%BD%D1%83%D1%82%2C+%D0%B1%D1%80%D0%BE%D0%BD%D1%8C+%D0%BE%D1%82%D0%BC%D0%B5%D0%BD%D1%8F%D0%B5%D1%82%D1%81%D1%8F+%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%BE%D1%80%D0%BE%D0%BC%3C%2Fli%3E%0D%0A%3C%2Ful%3E%0D%0A%3C%2Fdiv%3E%0D%0A%0D%0A%3Cbr%3E%0D%0A%3Cb%3E%D0%92%D0%B0%D0%B6%D0%BD%D0%BE%21%0D%0A%3C%2Fb%3E%0D%0A%3Cdiv+style%3D%22margin-left%3A15px%22%3E%0D%0A%3Cul%3E%0D%0A%3Cli%3E%D0%9F%D0%B5%D1%80%D0%B5%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D0%B5+%D0%BC%D0%B5%D0%B1%D0%B5%D0%BB%D0%B8+%D0%B8+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D0%B8+%D0%B2+%D0%BF%D0%B5%D1%80%D0%B5%D0%B3%D0%BE%D0%B2%D0%BE%D1%80%D0%BD%D1%8B%D1%85+%D0%B2%D0%BE%D0%B7%D0%BC%D0%BE%D0%B6%D0%BD%D0%BE+%D1%82%D0%BE%D0%BB%D1%8C%D0%BA%D0%BE+%D0%BF%D0%BE+%D1%81%D0%BE%D0%B3%D0%BB%D0%B0%D1%81%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8E+%D1%81+%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%BE%D1%80%D0%BE%D0%BC%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9F%D0%BE%D1%81%D0%B5%D1%82%D0%B8%D1%82%D0%B5%D0%BB%D0%B8+%D0%B4%D0%BE%D0%BB%D0%B6%D0%BD%D1%8B+%D0%B2%D0%B5%D1%80%D0%BD%D1%83%D1%82%D1%8C+%D0%BC%D0%B5%D0%B1%D0%B5%D0%BB%D1%8C+%D0%B8+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D1%83+%D0%B2+%D0%B8%D1%81%D1%85%D0%BE%D0%B4%D0%BD%D0%BE%D0%B5+%D0%BF%D0%BE%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5+%D0%BF%D0%BE%D1%81%D0%BB%D0%B5+%D0%BE%D0%BA%D0%BE%D0%BD%D1%87%D0%B0%D0%BD%D0%B8%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B%2C+%D1%83%D0%B1%D1%80%D0%B0%D1%82%D1%8C+%D0%BC%D1%83%D1%81%D0%BE%D1%80%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9F%D0%BE%D0%B4%D0%BA%D0%BB%D1%8E%D1%87%D0%B0%D1%82%D1%8C+%D1%81%D0%B2%D0%BE%D0%B5+%D0%BE%D0%B1%D0%BE%D1%80%D1%83%D0%B4%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5+%D0%BA+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D0%B5+%D0%BA%D0%BE%D0%B2%D0%BE%D1%80%D0%BA%D0%B8%D0%BD%D0%B3%D0%B0+%D1%81%D0%B0%D0%BC%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE+%D1%81%D1%82%D1%80%D0%BE%D0%B3%D0%BE+%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%89%D0%B5%D0%BD%D0%BE%3C%2Fli%3E%0D%0A%3Cli%3E%D0%92+%D0%BF%D0%B5%D1%80%D0%B5%D0%B3%D0%BE%D0%B2%D0%BE%D1%80%D0%BD%D1%8B%D1%85+%D0%B5%D1%81%D1%82%D1%8C+%D0%BE%D0%B3%D1%80%D0%B0%D0%BD%D0%B8%D1%87%D0%B5%D0%BD%D0%B8%D0%B5+%D0%BF%D0%BE+%D0%B3%D1%80%D0%BE%D0%BC%D0%BA%D0%BE%D1%81%D1%82%D0%B8+%D0%B7%D0%B2%D1%83%D0%BA%D0%B0%3C%2Fli%3E%0D%0A%3C%2Ful%3E%0D%0A%3C%2Fdiv%3E%0D%0A%3Cbr%3E%0D%0A%D0%95%D1%81%D0%BB%D0%B8+%D0%B2%D0%B0%D0%BC+%D0%BD%D0%B5%D0%BE%D0%B1%D1%85%D0%BE%D0%B4%D0%B8%D0%BC%D0%BE+%D0%B7%D0%B0%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D1%82%D1%8C+%D0%BF%D0%B5%D1%80%D0%B5%D0%B3%D0%BE%D0%B2%D0%BE%D1%80%D0%BD%D1%8B%D0%B5+%D0%BD%D0%B0+%D0%B1%D0%BE%D0%BB%D0%B5%D0%B5+%D0%B4%D0%BB%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F%2C+%D0%B2%D1%8B+%D0%BC%D0%BE%D0%B6%D0%B5%D1%82%D0%B5+%D1%81%D0%BE%D0%B3%D0%BB%D0%B0%D1%81%D0%BE%D0%B2%D0%B0%D1%82%D1%8C+%D1%8D%D1%82%D0%BE+%D1%81+%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%86%D0%B8%D0%B5%D0%B9+%D0%B1%D0%B8%D0%B1%D0%BB%D0%B8%D0%BE%D1%82%D0%B5%D0%BA%D0%B8%3A+%3Cbr%3E%0D%0A%3Cdiv+style%3D%22margin-left%3A15px%22%3E%0D%0A%3Cul%3E%0D%0A%3Cli%3E%D0%9A%D0%BE%D0%B2%D0%BE%D1%80%D0%BA%D0%B8%D0%BD%D0%B3+%D0%BD%D0%B0+%D0%9A%D1%80%D0%BE%D0%BD%D0%B2%D0%B5%D1%80%D0%BA%D1%81%D0%BA%D0%BE%D0%BC+%D0%BF%D1%80.+%D0%B4.49++-+8+%28812%29+480-44-80%2C+%D0%B4%D0%BE%D0%B1.+3%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9A%D0%BE%D0%B2%D0%BE%D1%80%D0%BA%D0%B8%D0%BD%D0%B3+%D0%BD%D0%B0+%D1%83%D0%BB.+%D0%9B%D0%BE%D0%BC%D0%BE%D0%BD%D0%BE%D1%81%D0%BE%D0%B2%D0%B0+%D0%B4.+9+-+8+%28812%29+480-44-80%2C+%D0%B4%D0%BE%D0%B1.+2%3C%2Fli%3E%0D%0A%3C%2Ful%3E%0D%0A%3C%2Fdiv%3E%0D%0A%0D%0A%3Cbr%3E%0D%0A%3Cb%3E*%3C%2Fb%3E%D0%92+%D1%81%D0%B2%D1%8F%D0%B7%D0%B8+%D1%81+%D1%82%D0%B5%D0%BC%2C+%D1%87%D1%82%D0%BE++%D0%BC%D0%BD%D0%BE%D0%B3%D0%B8%D0%B5+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D0%B5+%D0%BF%D1%80%D0%BE%D1%85%D0%BE%D0%B4%D1%8F%D1%82+%D0%B4%D1%80%D1%83%D0%B3+%D0%B7%D0%B0+%D0%B4%D1%80%D1%83%D0%B3%D0%BE%D0%BC%2C+%D0%BF%D0%BE%D0%B6%D0%B0%D0%BB%D1%83%D0%B9%D1%81%D1%82%D0%B0%2C+%D1%80%D0%B0%D1%81%D1%81%D1%87%D0%B8%D1%82%D1%8B%D0%B2%D0%B0%D0%B9%D1%82%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F%2C+%D1%87%D1%82%D0%BE%D0%B1%D1%8B+%D0%B8%D0%BC%D0%B5%D1%82%D1%8C++%D0%B2%D0%BE%D0%B7%D0%BC%D0%BE%D0%B6%D0%BD%D0%BE%D1%81%D1%82%D1%8C+%D0%B7%D0%B0%D0%BA%D0%BE%D0%BD%D1%87%D0%B8%D1%82%D1%8C+%D0%B5%D0%B3%D0%BE+%D1%82%D0%BE%D1%87%D0%BD%D0%BE+%D0%B2+%D1%81%D1%80%D0%BE%D0%BA+%D0%BE%D0%BA%D0%BE%D0%BD%D1%87%D0%B0%D0%BD%D0%B8%D1%8F+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F.+%D0%A2%D0%B0%D0%BA%D0%B6%D0%B5+%D0%BF%D1%80%D0%BE%D1%81%D0%B8%D0%BC+%D0%B2%D0%B0%D1%81+%D1%83%D1%87%D0%B8%D1%82%D1%8B%D0%B2%D0%B0%D1%82%D1%8C+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D0%BD%D0%B0+%D0%BF%D0%BE%D0%B4%D0%B3%D0%BE%D1%82%D0%BE%D0%B2%D0%BA%D1%83+%D0%BF%D0%BE%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F+%D0%B4%D0%BB%D1%8F+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F+%D0%B2+%D1%80%D0%B0%D0%BC%D0%BA%D0%B0%D1%85+%D1%81%D0%B2%D0%BE%D0%B5%D0%B3%D0%BE+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F."
                    )
                }
                "p_t51" -> {
                    stringBuilder.append("$param=")
                }
                "p_t56" -> stringBuilder.append("$param=${encode("18863,18865,18867,18871")}") //todo ,,,

                "f44" -> {
                    //stringBuilder.append("f44=995&f42=&f41=996&f42=&f41=997&f44=999&f44=1150&f44=1151&f44=1152")
                    stringBuilder.append(listOfFParams)
                }

                "p_t50" -> stringBuilder.append("$param=CREATE")
                "p_t54" -> stringBuilder.append("$param=${encode(dateWithDots)}")
                "p_t54" -> stringBuilder.append("$param=${encode("18863,18865,18867,18871")}")
                else -> {
                    val prefix = ("^[\\s\\S]*?$param").toRegex().find(bookingHtml)?.value
                    bookingHtml = bookingHtml.removePrefix(prefix!!)

                    var value: String =
                        ("(^[\\s\\S]*?)(?=\")".toRegex().find(bookingHtml.split("value=\"")[1], 0)?.value!!)
                    value = StringEscapeUtils.unescapeHtml4(value)
                    if (param == "p_flow_id") {
                        stringBuilder.append(value)
                    } else
                        stringBuilder.append("${encode(param)}=${encode(value)}")

                }

            }
            //if(param == "p_request") data[param] = "PASS_REQUEST"

        }
        val result = stringBuilder.toString()
        println(result)
        val call = request.book(result)
        println(call.execute().code())
    }

    private fun encode(s: String): String {
        return URLEncoder.encode(s)!!
    }

}