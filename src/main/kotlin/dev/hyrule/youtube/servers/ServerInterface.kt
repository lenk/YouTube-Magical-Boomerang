package dev.hyrule.youtube.servers

import dev.hyrule.youtube.exception.DownloadException
import java.io.IOException

interface ServerInterface {

    @Throws(IOException::class, DownloadException::class)
    fun getDownload(code: String): String

}