package dev.hyrule.youtube

import dev.hyrule.youtube.exception.DownloadException
import dev.hyrule.youtube.servers.ServerInterface
import org.reflections.Reflections
import java.io.IOException
import java.lang.Exception

class Downloader internal constructor() {
    private val pool = ArrayList<ServerInterface>()

    companion object {
        private var instance: Downloader? = null

        fun getInstance(): Downloader {
            if (instance == null) {
                instance = Downloader()

                val path = "com.hyrule.youtube.servers."
                Reflections(path).getSubTypesOf(ServerInterface::class.java).filter { c -> c.name.startsWith(path) }.forEach { c ->
                    instance!!.pool.add(c.getDeclaredConstructor().newInstance())
                }
            }

            return instance!!
        }
    }

    fun getDownload(code: String): String {
        val errors = ArrayList<DownloadException>()
        for (server in pool) {
            try {
                return server.getDownload(code)
            } catch (exception: DownloadException) {
                errors.add(exception)
                continue
            } catch (exception: IOException) {
                errors.add(DownloadException("networking error!", exception))
                continue
            }
        }

        throw DownloadException("failure fetching download links with pool", errors)
    }
}