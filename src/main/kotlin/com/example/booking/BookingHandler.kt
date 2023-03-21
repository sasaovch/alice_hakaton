package booking

import com.example.api_service.LoggingInterceptor
import com.example.booking.BookingApi
import com.example.constants.Constants
import com.example.cookie.CookieHandler
import com.example.models.Place
import com.example.models.RoomType
import com.example.util.EncodingInterceptor
import okhttp3.OkHttpClient
import org.apache.commons.text.StringEscapeUtils.unescapeHtml4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class BookingHandler(cookies: CookieHandler, private val pInstance: String) {
    private val bookingParamsCreate = listOf<String>(
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
    private val bookingParamsBook = listOf<String>(
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
        "p_arg_checksums",
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
        println(bookingHtml)
    }

    private fun getListOfF(html: String): String {
        var res = ""
        var bufHtml = html
        while ("(?<=\")f44\".*".toRegex().find(bufHtml, 0) != null) {
            bufHtml = "(?<=\")f44\".*".toRegex().find(bufHtml, 0)?.value!!
            val variable = bufHtml.substring(0, 3)
            val value = "(?<=value=\").*?(?=\")".toRegex().find(bufHtml, 0)?.value!!
            if (res == "") res = "$res$variable=$value"
            else
                res = "$res&$variable=$value"
        }
        println(res)
        return res
    }

    fun book(roomNum: Int, date: String, begin: String, end: String, roomType: RoomType, place: Place) {
        val roomId = Constants.numToId(roomNum)
        println(roomId)
        getBookingHtml()
        val bufHtml = (getAudDevicesHTML(roomId))
        val listOfFParams = getListOfF(bufHtml!!)
        bookingHtml = sendBookRequest(roomId, date, begin, end, listOfFParams, "+7 (953) 2607422", roomType, place, bookingParamsCreate, "CREATE")


        sendBookRequest(roomId, date, begin, end, listOfFParams, "+7 (953) 2607422", roomType, place, bookingParamsBook, "PASS_REQUEST")
    }

    private fun sendBookRequest(
        roomId: Int,
        date: String,
        begin: String,
        end: String,
        listOfFParams: String,
        phone: String,
        type: RoomType,
        place: Place,
        bookingParams : List<String>,
        bookType : String
    ) : String {
        val request = retrofitIsu.create(BookingApi::class.java)
        val stringBuilder = StringBuilder()
        val localDate = LocalDateTime.now()
        val strDate = localDate.toString().split('T')[0].replace("-", "") + "0000"
        val pattern = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val dateWithDots = simpleDateFormat.format(Date())
        println(strDate)
        for (param in bookingParams) {
            if (stringBuilder.isNotEmpty())
                stringBuilder.append("&")
            when (param) {
                "p_request" -> stringBuilder.append("$param=${encode(bookType)}") //set PASS_REQUEST to book
                "p_t15" -> stringBuilder.append("$param=${encode(date)}")
                "p_t16" -> stringBuilder.append("$param=${encode(roomId.toString())}")
                "p_t23" -> stringBuilder.append("$param=${encode("BOOKED_BY_ALICE_SKILL")}")
                "p_t26" -> stringBuilder.append("$param=${encode(begin)}")
                "p_t27" -> stringBuilder.append("$param=${encode(end)}")
                "p_t31" -> stringBuilder.append("$param=${encode(phone)}")
                "p_t46" -> stringBuilder.append("$param=${encode(phone)}")
                "p_t14" -> stringBuilder.append(
                    "$param=${
                        Constants.TypeToTimeType[Pair(
                            type,
                            place
                        )]
                    }"
                )
                "p_t17" -> stringBuilder.append("$param=$strDate")
                "p_t19" -> stringBuilder.append("$param=N")
                "p_t21" -> stringBuilder.append("$param=${encode(roomId.toString())}")
                "p_t28" -> stringBuilder.append("$param=3")
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
                "p_t56" -> {stringBuilder.append("$param=${encode(Constants.getStringOfRoomsIds(roomId))}")}
                "f44" -> {
                    stringBuilder.append(listOfFParams)
                }

                "p_t50" -> stringBuilder.append("$param=$bookType")
                "p_t54" -> stringBuilder.append("$param=${encode(dateWithDots)}")
                //"p_t54" -> stringBuilder.append("$param=${encode(Constants.getStringOfRoomsIds(roomId))}")
                else -> {
                    try {
                        val prefix = ("^[\\s\\S]*?$param").toRegex().find(bookingHtml)?.value
                        bookingHtml = bookingHtml.removePrefix(prefix!!)

                        var value: String =
                            ("(^[\\s\\S]*?)(?=\")".toRegex().find(bookingHtml.split("value=\"")[1], 0)?.value!!)
                        value = unescapeHtml4(value)
                        if (param == "p_flow_id") {
                            stringBuilder.append(value)
                        } else
                            stringBuilder.append("${encode(param)}=${encode(value)}")
                    } catch(e : NullPointerException) {
                        println(param)
                    }
                }

            }
            //if(param == "p_request") data[param] = "PASS_REQUEST"

        }
        val result = stringBuilder.toString()
        var call = request.book(result)

        val responce = call.execute()
        if(responce.code() == 302 && bookType == "CREATE") {
            val url = "https://isu.ifmo.ru/pls/apex/" + responce.headers().get("LOCATION")
            call = request.redirectHandler(url)
            return call.execute().body()?.string()!!
        } else return ""
    }

    private fun encode(s: String): String {
        return URLEncoder.encode(s)!!
    }


}
//p_flow_id=2431&p_flow_step_id=4&p_instance=115990115451325&p_page_submission_id=102655024927138&p_request=PASS_REQUEST&p_arg_names=6079535771561000317&p_t01=100000&p_arg_checksums=6079535771561000317_5227F4D2C18D65D0FEA7906FA261F6B5&p_arg_names=7249815791246105987&p_t02=336439&p_arg_checksums=7249815791246105987_DA5199A8366966268CA9647C6F6B7C07&p_arg_names=7250105107305138507&p_t03=%5B336439%5D+%D0%92%D0%B5%D1%80%D0%B5%D1%89%D0%B0%D0%B3%D0%B8%D0%BD+%D0%95.%D0%A1.&p_arg_checksums=7250105107305138507_138471CEE560DDB14D40B5AB1EF32F73&p_arg_names=6398656977981664321&p_t04=%2Fi%2Fcis-images%2Fpk%2Fanon_male.png&p_arg_checksums=6398656977981664321_4552471E5091813F85B8395722FFD6B9&p_arg_names=6398661365963793337&p_t05=&p_arg_checksums=6398661365963793337_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=6416535553085302695&p_t06=%2Fi%2Flibraries%2Ffrontend%2Fbg%2Fclouds.jpeg&p_arg_checksums=6416535553085302695_CA787B98A23FED644EECFD9191CFBC82&p_arg_names=5036996732608235790&p_t07=N&p_arg_checksums=5036996732608235790_B19D0E0941A68E92B31BA44B8B5CB23E&p_arg_names=2885594830886489806&p_t08=&p_arg_checksums=2885594830886489806_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=8304265325172952324&p_t09=&p_arg_checksums=8304265325172952324_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=9100863337829719881&p_t10=336439&p_arg_checksums=9100863337829719881_DA5199A8366966268CA9647C6F6B7C07&p_arg_names=5803243420937831283&p_t11=267&p_arg_checksums=5803243420937831283_FF45FAB2625E8E878B2EACAD5A13DE87&p_arg_names=8084416829554276552&p_t12=1013&p_arg_checksums=8084416829554276552_8EAFDC2E296AC123484418D44F3E27A1&p_arg_names=4822772749569337704&p_t13=%D0%BD%D0%B5%D1%87%D0%B5%D1%82%D0%BD%D0%B0%D1%8F&p_arg_names=4831172929291703026&p_t14=-17&p_arg_names=4822774331090337726&p_t15=28.03.2023&p_arg_names=4880949426445227960&p_t16=77&p_arg_names=4822775525012337731&p_t17=202303210000&p_arg_names=7632223812095429360&p_t18=&p_arg_names=8308206616523634280&p_t19=N&p_arg_names=4822774541243337727&p_t20=&p_arg_checksums=4822774541243337727_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=4822773532514337718&p_t21=77&p_arg_names=4574843812243311294&p_t22=&p_arg_checksums=4574843812243311294_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=4822773144499337717&p_t23=BOOKED_BY_ALICE_SKILL&p_arg_names=4822775338408337728&p_t24=1&p_arg_names=4822774948857337727&p_t25=&p_arg_names=4822773336814337718&p_t26=08%3A20&p_arg_names=4822775154659337727&p_t27=09%3A50&p_arg_names=4822776438587337735&p_t28=3&p_arg_names=4822776050934337734&p_t29=&p_arg_checksums=4822776050934337734_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=4822775738006337732&p_t30=&p_arg_checksums=4822775738006337732_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=4822773735822337719&p_t31=%2B7+%28953%29+2607422&p_arg_names=4822774750523337727&p_t32=&p_arg_names=8382858905570009265&p_t33=Y&p_arg_names=4978627852268633129&p_t34=N&p_arg_names=4978677345737712137&p_t35=day&p_arg_names=4979202250153378572&p_t36=&p_arg_names=7717528516838283061&p_arg_names=4981744832656809005&p_t38=&p_arg_checksums=4981744832656809005_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=5048619746850425349&p_t39=&p_arg_checksums=5048619746850425349_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=5055733044489838892&p_t40=1&p_arg_checksums=5055733044489838892_23F6ECD64A8CDA85A10B49D15422FA5B&p_arg_names=5077342046700913595&p_t41=&p_arg_checksums=5077342046700913595_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=2925756011706688079&p_t42=&p_arg_checksums=2925756011706688079_11E25A0A8A8DDDC87ACA76F47530B14C&p_arg_names=4911463324874492387&p_t43=&p_arg_names=7147189329062170425&p_t44=Y&p_arg_names=7563792100598205802&p_t45=%D0%9F%D1%80%D0%B0%D0%B2%D0%B8%D0%BB%D0%B0+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F%3A+%0D%0A%3Cdiv+style=%22margin-left%3A15px%22%3E%0D%0A%3Cul%3E%0D%0A%3Cli%3E%D0%93%D1%80%D0%B0%D1%84%D0%B8%D0%BA+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F+-+%D1%81+10%3A00+-+22%3A00+%28%D0%B2+%D0%BF%D1%80%D0%B0%D0%B7%D0%B4%D0%BD%D0%B8%D1%87%D0%BD%D1%8B%D0%B5+%D0%B4%D0%BD%D0%B8+%D0%BD%D0%B5%D0%B4%D0%BE%D1%81%D1%82%D1%83%D0%BF%D0%BD%D0%BE%29+%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9B%D0%B8%D0%BC%D0%B8%D1%82+%D0%B7%D0%B0%D1%8F%D0%B2%D0%BE%D0%BA+-+1+%D0%B2+%D0%B4%D0%B5%D0%BD%D1%8C%2C+%D0%B4%D0%BB%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE%D1%81%D1%82%D1%8C%D1%8E+%D0%BD%D0%B5+%D0%B1%D0%BE%D0%BB%D0%B5%D0%B5+2%D1%85+%D1%87%D0%B0%D1%81%D0%BE%D0%B2%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9F%D0%B5%D1%80%D0%B8%D0%BE%D0%B4%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B0%D1%8F+%D0%B7%D0%B0%D1%8F%D0%B2%D0%BA%D0%B0+-+%D0%BD%D0%B5+%D0%B1%D0%BE%D0%BB%D0%B5%D0%B5+3%D1%85+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9+%D0%BF%D0%BE+2+%D1%87%D0%B0%D1%81%D0%B0+%D0%BA%D0%B0%D0%B6%D0%B4%D0%B0%D1%8F%3C%2Fli%3E%0D%0A%3Cli%3E%D0%92+%D0%98%D0%A1%D0%A3+%D0%BD%D0%B5%D0%BE%D0%B1%D1%85%D0%BE%D0%B4%D0%B8%D0%BC%D0%BE+%D1%83%D0%BA%D0%B0%D0%B7%D1%8B%D0%B2%D0%B0%D1%82%D1%8C+%D0%BD%D1%83%D0%B6%D0%BD%D0%BE%D0%B5+%D0%BE%D0%B1%D0%BE%D1%80%D1%83%D0%B4%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5%2C+%D0%BF%D1%80%D0%B8%D1%85%D0%BE%D0%B4%D0%B8%D1%82%D1%8C+%D0%B7%D0%B0+5-10+%D0%BC%D0%B8%D0%BD%D1%83%D1%82+%D0%B4%D0%BE+%D0%BD%D0%B0%D1%87%D0%B0%D0%BB%D0%B0+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F%3C%2Fli%3E%0D%0A%3Cli%3E%D0%92+%D1%81%D0%BB%D1%83%D1%87%D0%B0%D0%B5+%D0%BE%D1%82%D0%BC%D0%B5%D0%BD%D1%8B+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F%2C+%D0%BD%D0%B5%D0%BE%D0%B1%D1%85%D0%BE%D0%B4%D0%B8%D0%BC%D0%BE+%D0%B7%D0%B0%D0%B1%D0%BB%D0%B0%D0%B3%D0%BE%D0%B2%D1%80%D0%B5%D0%BC%D0%B5%D0%BD%D0%BD%D0%BE+%D0%BE%D1%82%D0%BC%D0%B5%D0%BD%D0%B8%D1%82%D1%8C+%D0%B1%D1%80%D0%BE%D0%BD%D1%8C%2C+%D0%BF%D1%80%D0%B8+%D0%BE%D0%BF%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D0%B8+%D0%B1%D0%BE%D0%BB%D1%8C%D1%88%D0%B5+20+%D0%BC%D0%B8%D0%BD%D1%83%D1%82%2C+%D0%B1%D1%80%D0%BE%D0%BD%D1%8C+%D0%BE%D1%82%D0%BC%D0%B5%D0%BD%D1%8F%D0%B5%D1%82%D1%81%D1%8F+%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%BE%D1%80%D0%BE%D0%BC%3C%2Fli%3E%0D%0A%3C%2Ful%3E%0D%0A%3C%2Fdiv%3E%0D%0A%0D%0A%3Cbr%3E%0D%0A%3Cb%3E%D0%92%D0%B0%D0%B6%D0%BD%D0%BE%21%0D%0A%3C%2Fb%3E%0D%0A%3Cdiv+style=%22margin-left%3A15px%22%3E%0D%0A%3Cul%3E%0D%0A%3Cli%3E%D0%9F%D0%B5%D1%80%D0%B5%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D0%B5+%D0%BC%D0%B5%D0%B1%D0%B5%D0%BB%D0%B8+%D0%B8+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D0%B8+%D0%B2+%D0%BF%D0%B5%D1%80%D0%B5%D0%B3%D0%BE%D0%B2%D0%BE%D1%80%D0%BD%D1%8B%D1%85+%D0%B2%D0%BE%D0%B7%D0%BC%D0%BE%D0%B6%D0%BD%D0%BE+%D1%82%D0%BE%D0%BB%D1%8C%D0%BA%D0%BE+%D0%BF%D0%BE+%D1%81%D0%BE%D0%B3%D0%BB%D0%B0%D1%81%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8E+%D1%81+%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%BE%D1%80%D0%BE%D0%BC%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9F%D0%BE%D1%81%D0%B5%D1%82%D0%B8%D1%82%D0%B5%D0%BB%D0%B8+%D0%B4%D0%BE%D0%BB%D0%B6%D0%BD%D1%8B+%D0%B2%D0%B5%D1%80%D0%BD%D1%83%D1%82%D1%8C+%D0%BC%D0%B5%D0%B1%D0%B5%D0%BB%D1%8C+%D0%B8+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D1%83+%D0%B2+%D0%B8%D1%81%D1%85%D0%BE%D0%B4%D0%BD%D0%BE%D0%B5+%D0%BF%D0%BE%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5+%D0%BF%D0%BE%D1%81%D0%BB%D0%B5+%D0%BE%D0%BA%D0%BE%D0%BD%D1%87%D0%B0%D0%BD%D0%B8%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B%2C+%D1%83%D0%B1%D1%80%D0%B0%D1%82%D1%8C+%D0%BC%D1%83%D1%81%D0%BE%D1%80%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9F%D0%BE%D0%B4%D0%BA%D0%BB%D1%8E%D1%87%D0%B0%D1%82%D1%8C+%D1%81%D0%B2%D0%BE%D0%B5+%D0%BE%D0%B1%D0%BE%D1%80%D1%83%D0%B4%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5+%D0%BA+%D1%82%D0%B5%D1%85%D0%BD%D0%B8%D0%BA%D0%B5+%D0%BA%D0%BE%D0%B2%D0%BE%D1%80%D0%BA%D0%B8%D0%BD%D0%B3%D0%B0+%D1%81%D0%B0%D0%BC%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE+%D1%81%D1%82%D1%80%D0%BE%D0%B3%D0%BE+%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%89%D0%B5%D0%BD%D0%BE%3C%2Fli%3E%0D%0A%3Cli%3E%D0%92+%D0%BF%D0%B5%D1%80%D0%B5%D0%B3%D0%BE%D0%B2%D0%BE%D1%80%D0%BD%D1%8B%D1%85+%D0%B5%D1%81%D1%82%D1%8C+%D0%BE%D0%B3%D1%80%D0%B0%D0%BD%D0%B8%D1%87%D0%B5%D0%BD%D0%B8%D0%B5+%D0%BF%D0%BE+%D0%B3%D1%80%D0%BE%D0%BC%D0%BA%D0%BE%D1%81%D1%82%D0%B8+%D0%B7%D0%B2%D1%83%D0%BA%D0%B0%3C%2Fli%3E%0D%0A%3C%2Ful%3E%0D%0A%3C%2Fdiv%3E%0D%0A%3Cbr%3E%0D%0A%D0%95%D1%81%D0%BB%D0%B8+%D0%B2%D0%B0%D0%BC+%D0%BD%D0%B5%D0%BE%D0%B1%D1%85%D0%BE%D0%B4%D0%B8%D0%BC%D0%BE+%D0%B7%D0%B0%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D1%82%D1%8C+%D0%BF%D0%B5%D1%80%D0%B5%D0%B3%D0%BE%D0%B2%D0%BE%D1%80%D0%BD%D1%8B%D0%B5+%D0%BD%D0%B0+%D0%B1%D0%BE%D0%BB%D0%B5%D0%B5+%D0%B4%D0%BB%D0%B8%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D0%BE%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F%2C+%D0%B2%D1%8B+%D0%BC%D0%BE%D0%B6%D0%B5%D1%82%D0%B5+%D1%81%D0%BE%D0%B3%D0%BB%D0%B0%D1%81%D0%BE%D0%B2%D0%B0%D1%82%D1%8C+%D1%8D%D1%82%D0%BE+%D1%81+%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%86%D0%B8%D0%B5%D0%B9+%D0%B1%D0%B8%D0%B1%D0%BB%D0%B8%D0%BE%D1%82%D0%B5%D0%BA%D0%B8%3A+%3Cbr%3E%0D%0A%3Cdiv+style=%22margin-left%3A15px%22%3E%0D%0A%3Cul%3E%0D%0A%3Cli%3E%D0%9A%D0%BE%D0%B2%D0%BE%D1%80%D0%BA%D0%B8%D0%BD%D0%B3+%D0%BD%D0%B0+%D0%9A%D1%80%D0%BE%D0%BD%D0%B2%D0%B5%D1%80%D0%BA%D1%81%D0%BA%D0%BE%D0%BC+%D0%BF%D1%80.+%D0%B4.49++-+8+%28812%29+480-44-80%2C+%D0%B4%D0%BE%D0%B1.+3%3C%2Fli%3E%0D%0A%3Cli%3E%D0%9A%D0%BE%D0%B2%D0%BE%D1%80%D0%BA%D0%B8%D0%BD%D0%B3+%D0%BD%D0%B0+%D1%83%D0%BB.+%D0%9B%D0%BE%D0%BC%D0%BE%D0%BD%D0%BE%D1%81%D0%BE%D0%B2%D0%B0+%D0%B4.+9+-+8+%28812%29+480-44-80%2C+%D0%B4%D0%BE%D0%B1.+2%3C%2Fli%3E%0D%0A%3C%2Ful%3E%0D%0A%3C%2Fdiv%3E%0D%0A%0D%0A%3Cbr%3E%0D%0A%3Cb%3E*%3C%2Fb%3E%D0%92+%D1%81%D0%B2%D1%8F%D0%B7%D0%B8+%D1%81+%D1%82%D0%B5%D0%BC%2C+%D1%87%D1%82%D0%BE++%D0%BC%D0%BD%D0%BE%D0%B3%D0%B8%D0%B5+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D0%B5+%D0%BF%D1%80%D0%BE%D1%85%D0%BE%D0%B4%D1%8F%D1%82+%D0%B4%D1%80%D1%83%D0%B3+%D0%B7%D0%B0+%D0%B4%D1%80%D1%83%D0%B3%D0%BE%D0%BC%2C+%D0%BF%D0%BE%D0%B6%D0%B0%D0%BB%D1%83%D0%B9%D1%81%D1%82%D0%B0%2C+%D1%80%D0%B0%D1%81%D1%81%D1%87%D0%B8%D1%82%D1%8B%D0%B2%D0%B0%D0%B9%D1%82%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F%2C+%D1%87%D1%82%D0%BE%D0%B1%D1%8B+%D0%B8%D0%BC%D0%B5%D1%82%D1%8C++%D0%B2%D0%BE%D0%B7%D0%BC%D0%BE%D0%B6%D0%BD%D0%BE%D1%81%D1%82%D1%8C+%D0%B7%D0%B0%D0%BA%D0%BE%D0%BD%D1%87%D0%B8%D1%82%D1%8C+%D0%B5%D0%B3%D0%BE+%D1%82%D0%BE%D1%87%D0%BD%D0%BE+%D0%B2+%D1%81%D1%80%D0%BE%D0%BA+%D0%BE%D0%BA%D0%BE%D0%BD%D1%87%D0%B0%D0%BD%D0%B8%D1%8F+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F.+%D0%A2%D0%B0%D0%BA%D0%B6%D0%B5+%D0%BF%D1%80%D0%BE%D1%81%D0%B8%D0%BC+%D0%B2%D0%B0%D1%81+%D1%83%D1%87%D0%B8%D1%82%D1%8B%D0%B2%D0%B0%D1%82%D1%8C+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D0%BD%D0%B0+%D0%BF%D0%BE%D0%B4%D0%B3%D0%BE%D1%82%D0%BE%D0%B2%D0%BA%D1%83+%D0%BF%D0%BE%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F+%D0%B4%D0%BB%D1%8F+%D0%BC%D0%B5%D1%80%D0%BE%D0%BF%D1%80%D0%B8%D1%8F%D1%82%D0%B8%D1%8F+%D0%B2+%D1%80%D0%B0%D0%BC%D0%BA%D0%B0%D1%85+%D1%81%D0%B2%D0%BE%D0%B5%D0%B3%D0%BE+%D0%B1%D1%80%D0%BE%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F.&p_arg_names=8298678403169423876&p_t46=%2B7+%28953%29+2607422&p_arg_names=8305318026805182532&p_t47=&p_arg_names=8305319116885187108&p_t48=&p_arg_names=8393146625247503561&p_t49=N&p_arg_checksums=8393146625247503561_B19D0E0941A68E92B31BA44B8B5CB23E&f43=200&f44=200&p_arg_names=7147455828106550816&p_t50=PASS_REQUEST&p_arg_names=5057750231672084753&p_t51=&p_arg_names=5060792438908484295&p_t52=&p_arg_names=4846890337767307319&p_t53=&p_arg_names=4846890545068307320&p_t54=21.03.2023&p_arg_names=4846890726595307320&p_t55=&p_arg_names=4831275224625872331&p_t56=29%2C30%2C36%2C41%2C73%2C77%2C82%2C84%2C85%2C86%2C91%2C93%2C95%2C145%2C200%2C215%2C516%2C20005&p_arg_names=5058534540049448424&p_t57=all&p_arg_names=7157074032558031811&p_t58=1+%D0%BD%D0%B5%D0%B4%D0%B5%D0%BB%D1%8E&p_md5_checksum=&p_page_checksum=2AEA2899C9CF9C381B42870AD854F370
//p_flow_id=2431&p_flow_step_id=4&p_instance=103163199226834&p_page_submission_id=116804691159098&p_request=PASS_REQUEST&p_arg_names=6079535771561000317&p_t01=100000&p_arg_checksums=6079535771561000317_6CB36CB955FB55AB25F2342779079111&p_arg_names=7249815791246105987&p_t02=336439&p_arg_checksums=7249815791246105987_16FF254FCAD2F4ACF89B3E75E2A1CEE4&p_arg_names=7250105107305138507&p_t03=%5B336439%5D+%D0%92%D0%B5%D1%80%D0%B5%D1%89%D0%B0%D0%B3%D0%B8%D0%BD+%D0%95.%D0%A1.&p_arg_checksums=7250105107305138507_EB8702B67AD12D47F6E0462389E7032E&p_arg_names=6398656977981664321&p_t04=%2Fi%2Fcis-images%2Fpk%2Fanon_male.png&p_arg_checksums=6398656977981664321_2068D0468887CA3BCFFA595E307589B9&p_arg_names=6398661365963793337&p_t05=&p_arg_checksums=6398661365963793337_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=6416535553085302695&p_t06=%2Fi%2Flibraries%2Ffrontend%2Fbg%2Fclouds.jpeg&p_arg_checksums=6416535553085302695_9E831BC76D7603259EFE9FB0D0249653&p_arg_names=5036996732608235790&p_t07=N&p_arg_checksums=5036996732608235790_A00473E748C18323B53520141D18151D&p_arg_names=2885594830886489806&p_t08=&p_arg_checksums=2885594830886489806_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=8304265325172952324&p_t09=&p_arg_checksums=8304265325172952324_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=9100863337829719881&p_t10=336439&p_arg_checksums=9100863337829719881_16FF254FCAD2F4ACF89B3E75E2A1CEE4&p_arg_names=5803243420937831283&p_t11=267&p_arg_checksums=5803243420937831283_460AF8B4FED98DECD5DDCB294C424B04&p_arg_names=8084416829554276552&p_t12=1013&p_arg_checksums=8084416829554276552_814148D326D9867ABE7B8B6A338621E5&p_arg_names=4822772749569337704&p_t13=%D0%BD%D0%B5%D1%87%D0%B5%D1%82%D0%BD%D0%B0%D1%8F&p_arg_names=4831172929291703026&p_t14=-17&p_arg_checksums=4831172929291703026_D0A4E4308B16C04A60F3B1196EA9C202&p_arg_names=4822774331090337726&p_t15=24.03.2023&p_arg_names=4880949426445227960&p_t16=20005&p_arg_names=4822775525012337731&p_t17=202303230000&p_arg_names=7632223812095429360&p_t18=&p_arg_names=8308206616523634280&p_t19=N&p_arg_names=4822774541243337727&p_t20=24214933&p_arg_checksums=4822774541243337727_C740D8D3F17BAB6331289CC97466EA9D&p_arg_names=4822773532514337718&p_t21=20005&p_arg_names=4574843812243311294&p_t22=&p_arg_checksums=4574843812243311294_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=4822773144499337717&p_t23=test&p_arg_names=4822775338408337728&p_t24=1&p_arg_names=4822774948857337727&p_t25=&p_arg_names=4822773336814337718&p_t26=20%3A20&p_arg_names=4822775154659337727&p_t27=21%3A50&p_arg_names=4822776438587337735&p_t28=10&p_arg_names=4822776050934337734&p_t29=4&p_arg_checksums=4822776050934337734_8CCA92D3EF47E28E3DCB4FEAEE336BDD&p_arg_names=4822775738006337732&p_t30=336439&p_arg_checksums=4822775738006337732_16FF254FCAD2F4ACF89B3E75E2A1CEE4&p_arg_names=4822773735822337719&p_t31=%2B7+%28953%29+2607422&p_arg_names=4822774750523337727&p_t32=&p_arg_names=8382858905570009265&p_t33=N&p_arg_names=4978627852268633129&p_t34=N&p_arg_names=4978677345737712137&p_t35=day&p_arg_names=4979202250153378572&p_t36=&p_arg_names=7717528516838283061&p_arg_names=4981744832656809005&p_t38=&p_arg_checksums=4981744832656809005_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=5048619746850425349&p_t39=&p_arg_checksums=5048619746850425349_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=5055733044489838892&p_t40=1&p_arg_checksums=5055733044489838892_0E87A530B26E1E523C49560D9A83A8C0&p_arg_names=5077342046700913595&p_t41=1009&p_arg_checksums=5077342046700913595_1EE7AEC949A809C373BFAF7DFF76388D&p_arg_names=2925756011706688079&p_t42=&p_arg_checksums=2925756011706688079_FE75859B8E0A01404F973BF54EF9104E&p_arg_names=4911463324874492387&p_t43=&p_arg_names=7147189329062170425&p_t44=Y&p_arg_names=7563792100598205802&p_t45=&p_arg_names=8298678403169423876&p_t46=%2B7+%28953%29+2607422&p_arg_names=8305318026805182532&p_t47=&p_arg_names=8305319116885187108&p_t48=&p_arg_names=8393146625247503561&p_t49=N&p_arg_checksums=8393146625247503561_A00473E748C18323B53520141D18151D&f44=1199&f44=1200&f44=1201&f44=1202&f44=1203&p_arg_names=7147455828106550816&p_t50=PASS_REQUEST&p_arg_names=5057750231672084753&p_t51=&p_arg_names=5060792438908484295&p_t52=&p_arg_names=4846890337767307319&p_t53=&p_arg_names=4846890545068307320&p_t54=21.03.2023&p_arg_names=4846890726595307320&p_t55=&p_arg_names=4831275224625872331&p_t56=29%2C30%2C36%2C41%2C73%2C77%2C82%2C84%2C85%2C86%2C91%2C93%2C95%2C145%2C200%2C215%2C516%2C20005%2C20007&p_arg_names=5058534540049448424&p_t57=all&p_arg_names=7157074032558031811&p_t58=1+%D0%BD%D0%B5%D0%B4%D0%B5%D0%BB%D1%8E&p_md5_checksum=92138E4526506049DEA555625EEEFA20&p_page_checksum=C43BEC8B1397C883B931558A813BF57A
