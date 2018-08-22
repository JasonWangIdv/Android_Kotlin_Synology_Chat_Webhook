package idv.jasonwang.chatbot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private var etMsg: EditText? = null
    private var btnPost: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        etMsg = findViewById(R.id.etMsg)
        btnPost = findViewById(R.id.btnPost)

        btnPost?.setOnClickListener {
            Thread(Runnable {
                postToChannel(etMsg?.text.toString())
            }).start()
        }
    }


    private fun postToChannel(msg: String) {
        val data = ("payload" + "=" + URLEncoder.encode("{\"text\": \"$msg\"}", "UTF-8")).toByteArray()

        val url = URL("Your Webhook URL")
        val http = url.openConnection() as HttpURLConnection
        http.connectTimeout = 2000
        http.doOutput = true
        http.doInput = true
        http.requestMethod = "POST"
        http.useCaches = false
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        http.setRequestProperty("Content-Length", data.size.toString())

        val out = http.outputStream
        out.write(data)

        val response = http.responseCode
        if (response == HttpURLConnection.HTTP_OK) {
            Handler(Looper.getMainLooper()).post { Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show() }
        } else {
            Handler(Looper.getMainLooper()).post { Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show() }
        }
    }

}
