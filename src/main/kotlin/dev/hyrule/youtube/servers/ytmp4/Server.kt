package dev.hyrule.youtube.servers.ytmp4

import dev.hyrule.youtube.exception.DownloadException
import dev.hyrule.youtube.servers.ServerInterface
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup

class Server: ServerInterface {
    override fun getDownload(code: String): String {

        val tokenGen = Jsoup.connect("https://ytmp4.top").execute()

        val connection = Jsoup.connect("https://ytmp4.top/system/action.php")
        connection.cookie("PHPSESSID", tokenGen.cookie("PHPSESSID")).ignoreHttpErrors(true).ignoreContentType(true)
        connection.data("token", tokenGen.parse().getElementById("token").attr("value"))
        connection.data("url", "https://www.youtube.com/watch?v=$code")

        val body = connection.method(Connection.Method.POST).execute().body()

        if ("error" == body) throw DownloadException("failure downloading video, response: $body")

        val json = JSONObject(body)
        val items = json.getJSONArray("links")
        val item = items.map { e -> e as JSONObject }.first { e -> "mp4" == e.optString("type") && !e.optBoolean("mute")}
        val redirection = Jsoup.connect(item.optString("url"))
            .ignoreContentType(true).ignoreHttpErrors(true).followRedirects(true)

        return redirection.execute().url().toString()
    }
}