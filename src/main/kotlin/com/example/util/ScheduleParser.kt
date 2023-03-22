package com.example.util
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ScheduleParser {
    fun parseSchedule(html: String?, roomId: Int, date: String): ScheduledRoom {
        //println(html)
        val doc: Document = Jsoup.parse(html!!)
        val elems: Elements = doc.body().select("tr")
        val isFree: HashMap<String, Boolean> = HashMap()
        for (element in elems) {

            if(element.select(".busy").size == 0 && element.select(".reserve").size == 0) continue
            if(element.select(".th_c").text().split(" ").size <= 1) continue
            val time: String = element.select(".th_c").text().split(" ")[1]

            var free = false
            if (element.select(".busy").size == 0) {
                free = true;
            }
            isFree[time] = free


        }
        return ScheduledRoom(date, isFree, roomId)

    }
}