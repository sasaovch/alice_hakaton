import com.example.api_service.LoggingInterceptor
import com.example.api_service.RoomInfoApi
import com.example.constants.Constants
import com.example.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class InfoHandler(IsuApCookie: String) {

    private val isuApCookie: String = IsuApCookie
    private var pRequest: String = "PLUGIN="
    private var pInstance: String = ""
    private val pFlowId: String = "2431"
    val pFlowStepId: String = "4"
    val parser: ScheduleParser = ScheduleParser()
    var bookingHtmlPage: String = ""
    fun setRequest(s: String) {
        pRequest = "PLUGIN=${s}"
    }


    private var client = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).followRedirects(true)
        .followSslRedirects(true).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://isu.ifmo.ru/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private fun getRoomInfo(roomId: Int, date: String, type: RoomType, place: Place): ScheduledRoom {
        val request = retrofit.create(RoomInfoApi::class.java)

        val parsed: ScheduledRoom;
        val response = request.getRoomInfo(
            isuApCookie,
            pRequest,
            pInstance,
            pFlowId,
            pFlowStepId,
            "P4_AUD_SEL",
            Constants.TypeToTimeType[Pair(type, place)].toString(),
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
    fun getFreeRooms(place: Place, time: List<String>, date: String, type: RoomType): ArrayList<Int> {
        if (place == Place.KRONVERSKY) {
            if (type == RoomType.AUDIENCE) {

                return getFreeList(Constants.KronvAuditorium, time, date, type, place)
            }
            if (type == RoomType.MEETINGROOM) {
                return getFreeList(Constants.KronvCoworkingAud, time, date, type, place)

            }
        }
        if (place == Place.LOMONOSOVA) {
            if (type == RoomType.AUDIENCE) {

                return getFreeList(Constants.LomoAud, time, date, type, place)
            }
            if (type == RoomType.MEETINGROOM) {
                return getFreeList(Constants.LomoCoworking, time, date, type, place)

            }
        }
        return ArrayList()

    }

    //ReturnsIds!!!!
    private fun getFreeList(
        aud: HashMap<Int, Int>,
        time: List<String>,
        date: String,
        type: RoomType,
        place: Place
    ): ArrayList<Int> {
        val freeList = ArrayList<Int>()
        for (pair in aud.entries.iterator()) {
            val parsed = getRoomInfo(pair.value, date, type, place)
            var isFree = true
            for (t in time) {
                if (parsed.schedule[t] == false) {
                    isFree = false
                    break
                }

            }
            if (isFree) {
                freeList.add(pair.value)
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
            isuApCookie,
        )
        val response = call.execute()
        if (response.code() == 302) {
            println(response.headers().get("Location"))
        }
        pInstance = response.headers().get("Location")!!.split(":")[2] //todo NULL HANDLING
        println(pInstance)
        parsePlugin()
    }

    fun parsePlugin() {
        val request = retrofit.create(RoomInfoApi::class.java)
        val call = request.getHtmlWithPlugin(isuApCookie)
        //todo NULL CHECKING
        val pluginString = call.execute().body()?.string()!!
        bookingHtmlPage = pluginString
        var s1 = pluginString.split("muledev_server_region_refresh")[3]
        s1 = s1.substring(s1.indexOf("ajaxIdentifier"))
        val s2 = s1.split(':').get(1)
        val s3 = s2.split(',').get(0)
        val s4 = s3.substring(1, s3.length - 1)
        if (s4 != null) {
            setRequest(s4)
        }

    }


    fun getFreeRoomsByPeopleNum(
        place: Place,
        time: List<String>,
        date: String,
        type: RoomType,
        peopleNum: Int
    ): List<Int> {
        val bigRooms = ArrayList<Int>()
        val freeRooms = getFreeRooms(place, time, date, type)
        for (id in freeRooms) {
            if (Constants.IdToCapacity[id]!! >= peopleNum) {
                if (place == Place.LOMONOSOVA && type == RoomType.MEETINGROOM) {
                    val filteredKeysMap = Constants.LomoCoworking.filterValues { it == id }
                    bigRooms.add(filteredKeysMap.keys.first())
                } else
                    if (place == Place.LOMONOSOVA && type == RoomType.AUDIENCE) {
                        val filteredKeysMap = Constants.LomoAud.filterValues { it == id }
                        bigRooms.add(filteredKeysMap.keys.first())
                    } else
                        if (place == Place.KRONVERSKY && type == RoomType.MEETINGROOM) {
                            val filteredKeysMap = Constants.KronvCoworkingAud.filterValues { it == id }
                            bigRooms.add(filteredKeysMap.keys.first())

                        } else
                            if (place == Place.KRONVERSKY && type == RoomType.AUDIENCE) {
                                val filteredKeysMap = Constants.KronvAuditorium.filterValues { it == id }
                                bigRooms.add(filteredKeysMap.keys.first())

                            }
            }
        }
        return bigRooms

    }


}
