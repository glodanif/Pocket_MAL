package com.g.pocketmal.data.util

import android.util.Base64

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

class PKCEHelper {

    private val usAscii = "US-ASCII"
    private val sha256 = "SHA-256"

    private fun getBase64String(source: ByteArray): String {
        return Base64.encodeToString(source, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    @Throws(IllegalStateException::class)
    private fun getASCIIBytes(value: String): ByteArray {
        val input: ByteArray
        try {
            input = value.toByteArray(charset(usAscii))
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException("Could not convert string to an ASCII byte array", e)
        }

        return input
    }

    @Throws(IllegalStateException::class)
    private fun getSHA256(input: ByteArray): ByteArray {
        val signature: ByteArray
        try {
            val md = MessageDigest.getInstance(sha256)
            md.update(input, 0, input.size)
            signature = md.digest()
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException("Failed to get SHA-256 signature", e)
        }

        return signature
    }

    fun generateCodeVerifier(): String {
        val sr = SecureRandom()
        val code = ByteArray(32)
        sr.nextBytes(code)
        return getBase64String(code)
    }

    @Throws(IllegalStateException::class)
    fun generateCodeChallenge(codeVerifier: String): String {
        val input = getASCIIBytes(codeVerifier)
        val signature = getSHA256(input)
        return getBase64String(signature)
    }
}
