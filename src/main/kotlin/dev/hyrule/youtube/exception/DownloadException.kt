package dev.hyrule.youtube.exception

import java.lang.Exception
import java.util.ArrayList

class DownloadException: Exception {
    private var errors: ArrayList<DownloadException>? = null

    constructor(message: String, exception: Exception) : super(message, exception)

    constructor(message: String) : super(message)

    constructor(message: String, errors: ArrayList<DownloadException>) : super(message) {
        this.errors = errors
    }

    fun getErrors(): ArrayList<DownloadException>? {
        return errors
    }
}