package dev.hyrule.youtube.servers.yt1s

import dev.hyrule.youtube.exception.DownloadException
import dev.hyrule.youtube.servers.ServerInterface
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup

class Server: ServerInterface {
    override fun getDownload(code: String): String {
        try {
            val connection = Jsoup.connect("https://yt1s.com/api/ajaxSearch/index")
            connection.header("x-requested-with", "XMLHttpRequest").ignoreHttpErrors(true).ignoreContentType(true)
            connection.data("q", "https://www.youtube.com/watch?v=$code").data("vt", "mp4")

            var response = JSONObject(connection.method(Connection.Method.POST).execute().body())
            val key = response.optString("kc")

            if (key == null) throw DownloadException(String.format("failure fetching key token(%s)", key))

            connection.url("https://yt1s.com/api/ajaxConvert/convert")
            connection.data("vid", code).data("k", key)

            response = JSONObject(connection.method(Connection.Method.POST).execute().body())
            return response.optString("dlink") ?: throw DownloadException("failure fetching download link!")
        } catch (jsonException: JSONException) {
            throw DownloadException("bad JSON response received!", jsonException)
        }
    }
}