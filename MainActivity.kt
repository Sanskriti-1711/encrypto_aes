package smartheard.com

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import smartheard.com.AESUtils.decrypt
import smartheard.com.AESUtils.encrypt
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object AESUtils {
    private val keyValue = byteArrayOf(
        'c'.toByte(),
        'o'.toByte(),
        'd'.toByte(),
        'i'.toByte(),
        'n'.toByte(),
        'g'.toByte(),
        'a'.toByte(),
        'f'.toByte(),
        'f'.toByte(),
        'a'.toByte(),
        'i'.toByte(),
        'r'.toByte(),
        's'.toByte(),
        'c'.toByte(),
        'o'.toByte(),
        'm'.toByte()
    )

    @Throws(Exception::class)
    fun encrypt(cleartext: String): String {
        val rawKey = rawKey
        val result = encrypt(rawKey, cleartext.toByteArray())
        return toHex(result)
    }

    @Throws(Exception::class)
    fun decrypt(encrypted: String): String {
        val enc = toByte(encrypted)
        val result = decrypt(enc)
        return String(result)
    }

    @get:Throws(Exception::class)
    private val rawKey: ByteArray
        private get() {
            val key: SecretKey = SecretKeySpec(keyValue, "AES")
            return key.encoded
        }

    @Throws(Exception::class)
    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec: SecretKey = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    @Throws(Exception::class)
    private fun decrypt(encrypted: ByteArray): ByteArray {
        val skeySpec: SecretKey =
            SecretKeySpec(keyValue, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    private fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
        return result
    }

    fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private const val HEX = "0123456789ABCDEF"
    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[b.toInt() shr 4 and 0x0f]).append(HEX[b.toInt() and 0x0f])
    }
}




class MainActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var editTextcipher : EditText? = null
        var btncipher : Button? = null
        var textViewcipher : TextView? = null


        editTextcipher = findViewById(R.id.editTextcipher)
        btncipher = findViewById(R.id.btncipher)
        textViewcipher = findViewById(R.id.textViewcipher)


        btncipher.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        var encrypted = ""
        val sourceStr = "${editTextcipher.text}"
        try {
            encrypted = encrypt(sourceStr)
            Log.d("TEST", "encrypted:$encrypted")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        textViewcipher.text = "$encrypted"

    }


}
