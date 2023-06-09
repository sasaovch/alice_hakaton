package com.example.constants

import com.example.models.Place
import com.example.models.RoomType
import java.lang.StringBuilder

public final class Constants {


    companion object {

        public final val LomoAud = linkedMapOf<Int, Int>(
            Pair(1121, 13075), Pair(1122, 14143), Pair(1123, 13607), Pair(1124, 13609),
            Pair(1220, 12997), Pair(1221, 13063), Pair(1222, 13611), Pair(1223, 13061),
            Pair(1224, 14609), Pair(1226, 14579), Pair(1310, 12935), Pair(1315, 12939),
            Pair(2202, 13491), Pair(2219, 13469), Pair(2432, 13251), Pair(3407, 12775),
            Pair(34071, 14181), Pair(3408, 12753), Pair(3412, 14183), Pair(4103, 12733),
            Pair(4202, 12661), Pair(4206, 12675), Pair(4207, 12677), Pair(4208, 12679),
            Pair(4210, 12699), Pair(4213, 12683), Pair(4306, 12745), Pair(4307, 12669)
        )
        public final val KronvCoworkingAud =
            linkedMapOf<Int, Int>(Pair(1301, 18863), Pair(1311, 18865), Pair(1314, 18867), Pair(1312, 18871))


        public final val LomoCoworking = linkedMapOf<Int, Int>(
            Pair(1, 18851), Pair(2, 18849), Pair(3, 13167), Pair(4, 13161),
            Pair(5, 13159), Pair(6, 13153), Pair(7, 13155), Pair(8, 13165)
        )
        public final val KronvAuditorium = linkedMapOf<Int, Int>(
            Pair(2337, 29), Pair(2336, 30), Pair(2326, 36), Pair(2316, 41),
            Pair(1410, 73), Pair(1419, 77), Pair(2407, 82), Pair(2412, 84),
            Pair(2414, 85), Pair(2416, 86), Pair(2426, 91), Pair(2433, 93),
            Pair(1229, 95), Pair(2304, 145), Pair(1316, 200), Pair(2201, 215),
            Pair(1404, 516), Pair(1405, 20005)
        )


        public val IdToCapacity = linkedMapOf<Int, Int>(
            //Kronv Aud
            Pair(29, 100),
            Pair(30, 46),
            Pair(36, 28),
            Pair(41, 30),
            Pair(73, 40),
            Pair(77, 135),
            Pair(82, 100),
            Pair(84, 30),
            Pair(85, 30),
            Pair(86, 30),
            Pair(91, 28),
            Pair(93, 80),
            Pair(95, 95),
            Pair(145, 100),
            Pair(200, 50),
            Pair(215, 54),
            Pair(516, 164),
            Pair(20005, 140),
            //Kronv Cowork
            Pair(18863, 10), Pair(18865, 10), Pair(18867, 10), Pair(18871, 10),
            //Lomo Aud

            Pair(13075, 60),
            Pair(14143, 78),
            Pair(13607, 82),
            Pair(13609, 73),
            Pair(12997, 50),
            Pair(13063, 56),
            Pair(13611, 70),
            Pair(13061, 78),
            Pair(14609, 50),
            Pair(14579, 56),
            Pair(12935, 30),
            Pair(12939, 40),
            Pair(13491, 100),
            Pair(13469, 80),
            Pair(13251, 60),
            Pair(12775, 30),
            Pair(14181, 30),
            Pair(12753, 48),
            Pair(14183, 50),
            Pair(12733, 20),
            Pair(12661, 48),
            Pair(12675, 26),
            Pair(12677, 10),
            Pair(12679, 52),
            Pair(12699, 52),
            Pair(12683, 30),
            Pair(12745, 54),
            Pair(12669, 48),


            //Lomo Cowork
            Pair(18851, 4),
            Pair(18849, 4),
            Pair(13167, 4),
            Pair(13161, 4),
            Pair(13159, 6),
            Pair(13153, 6),
            Pair(13155, 8),
            Pair(13165, 80)
        )
        public final val TypeToTimeType =
            linkedMapOf<Pair<RoomType, Place>, Int>(Pair(Pair(RoomType.MEETINGROOM, Place.LOMONOSOVA), -812),
                Pair(Pair(RoomType.MEETINGROOM, Place.KRONVERSKY), -603),
                Pair(Pair(RoomType.AUDIENCE, Place.LOMONOSOVA), -17),
                Pair(Pair(RoomType.AUDIENCE, Place.KRONVERSKY), -17))
        public fun getStringOfRoomsIds(id: Int) : String {
            var sb : StringBuilder = StringBuilder()
            if(LomoAud.containsValue(id)) {
                for(key in LomoAud.values) {
                    if (sb.isNotEmpty()) {
                        sb.append(',')
                    }
                    sb.append(key)
                }
                return sb.toString()
            }
            if(KronvAuditorium.containsValue(id)) {
                for(key in KronvAuditorium.values) {
                    if (sb.isNotEmpty()) {
                        sb.append(',')
                    }
                    sb.append(key)
                }
                return sb.toString()
            }
            if(LomoCoworking.containsValue(id)) {
                for(key in LomoCoworking.values) {
                    if (sb.isNotEmpty()) {
                        sb.append(',')
                    }
                    sb.append(key)
                }
                return sb.toString()
            }
            if(KronvCoworkingAud.containsValue(id)) {
                for(key in KronvAuditorium.values) {
                    if (sb.isNotEmpty()) {
                        sb.append(',')
                    }
                    sb.append(key)
                }
                return sb.toString()
            }
            return ""

        }
        fun numToId(num: Int) : Int {
            if(LomoCoworking.containsKey(num)) {
                return LomoCoworking[num]!!
            }
            if(LomoAud.containsKey(num)) {
                return LomoAud[num]!!
            }
            if(KronvAuditorium.containsKey(num)) {
                return KronvAuditorium[num]!!
            }
            if(KronvCoworkingAud.containsKey(num)) {
                return KronvCoworkingAud[num]!!
            }
            return -1
        }
        fun idToNum(id: Int): Int {
            for (key in LomoCoworking.keys) {
                if (LomoCoworking[key] == id) {
                    return key
                }
            }
            for (key in LomoAud.keys) {
                if (LomoAud[key] == id) {
                    return key
                }
            }
            for (key in KronvAuditorium.keys) {
                if (KronvAuditorium[key] == id) {
                    return key
                }
            }
            for (key in KronvCoworkingAud.keys) {
                if (KronvCoworkingAud[key] == id) {
                    return key
                }
            }
            return -1

        }


    }

}

