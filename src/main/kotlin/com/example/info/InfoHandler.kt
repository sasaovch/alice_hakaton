package info

import com.example.util.LoggingInterceptor
import com.example.util.ScheduleParser
import com.example.util.ScheduledRoom
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class InfoHandler(IsuApCookie: String) {

    val ISU_APP_COOKIE: String = IsuApCookie
    var p_request: String = "PLUGIN="
    var p_instance: String = ""
    val p_flow_id: String = "2431"
    val p_flow_step_id: String = "4"
    val parser: ScheduleParser = ScheduleParser()
    var bookingHtmlPage: String = ""
    fun setRequest(s: String) {
        p_request = "PLUGIN=${s}"
    }

    enum class Type {
        MEETING_ROOM,
        AUDITORIUM
    }

    enum class Place {
        KRONVA, LOMO
    }

    private var client = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).followRedirects(true)
        .followSslRedirects(true).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://isu.ifmo.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val KronvCovorkingAud =
        hashMapOf<Int, Int>(Pair(1301, 18863), Pair(1311, 18865), Pair(1314, 18867), Pair(1312, 18871))
    private val KronvAuditorium = hashMapOf<Int, Int>(Pair(2337, 29), Pair(2336, 30), Pair(2326, 36), Pair(2316, 41),
        Pair(1410, 73), Pair(1419, 77), Pair(2407, 82), Pair(2412, 84),
        Pair(2414, 85), Pair(2416, 86), Pair(2426, 91), Pair(2433, 93),
        Pair(1229, 95), Pair(2304, 145), Pair(1316, 200), Pair(2201, 215),
        Pair(1404, 516), Pair(1405, 20005))
    fun getRoomInfo(roomId: Int, date: String): ScheduledRoom {
        val request = retrofit.create(RoomInfoApi::class.java)

        var parsed: ScheduledRoom = ScheduledRoom("", HashMap(), -1);
        val response = request.getRoomInfo(
            ISU_APP_COOKIE,
            p_request,
            p_instance,
            p_flow_id,
            p_flow_step_id,
            "P4_AUD_SEL",
            "-603",
            "P4_ROOMS_ID2",
            roomId.toString(),
            "P4_DATE",
            date
        )
        //no errors handling!
        parsed = parser.parseSchedule(response.execute().body()?.string(), roomId, date)


        return parsed

    }
    //time in format 8:30
    fun getFreeRooms(place: Place, time: String, date: String, type: Type) : ArrayList<Int>{
        if (place == Place.KRONVA) {
            if (type == Type.AUDITORIUM) {

                return getFreeList(KronvAuditorium, time, date) //todo map aud
            }
            if(type == Type.MEETING_ROOM) {
                return getFreeList(KronvCovorkingAud, time, date)

            }
        }
        return ArrayList()

    }

    private fun getFreeList(aud: HashMap<Int, Int>, time: String, date: String): ArrayList<Int> {
        val freeList = ArrayList<Int>()
        for (pair in aud.entries.iterator()) {
            val parsed = getRoomInfo(pair.value, date)
            if (parsed.schedule[time] == true) {
                freeList.add(pair.key)
            }
        }
        return freeList
    }

    fun checkInstance() {
        val cl = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).followRedirects(false)
            .followSslRedirects(false).build()
        val rf = Retrofit.Builder()
            .baseUrl("https://isu.ifmo.ru/")
            .client(cl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val request = rf.create(RoomInfoApi::class.java)
        val call = request.checkForRedirect(
            ISU_APP_COOKIE,
        )
        val response = call.execute()
        if (response.code() == 302) {
            println(response.headers().get("Location"))
        }
        p_instance = response.headers().get("Location")!!.split(":")[2] //todo NULL HANDLING
        println(p_instance)
        parsePlugin()
    }

    fun parsePlugin() {
        val request = retrofit.create(RoomInfoApi::class.java)
        val call = request.getHtmlWithPlugin(ISU_APP_COOKIE)
        //todo NULL CHECKING
        val pluginString = call.execute().body()?.string()!!
        bookingHtmlPage = pluginString
        var s1 = pluginString.split("muledev_server_region_refresh")[3]
        s1 = s1.substring(s1.indexOf("ajaxIdentifier"))
        val s2 = s1?.split(':')?.get(1)
        val s3 = s2?.split(',')?.get(0)
        val s4 = s3?.substring(1, s3.length - 1)
        if (s4 != null) {
            setRequest(s4)
        }
        println(s4)

    }




}

























