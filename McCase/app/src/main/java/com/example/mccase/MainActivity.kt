package com.example.mccase

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


//const val server_url = "http://10.0.2.2:8080/"

const val server_url = "http://192.168.43.239:8183/"
//var url = Uri.parse('http://192.168.1.16:8080');

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainActivityViewModel

    private lateinit var codeTv : TextView
    private lateinit var btn1 : Button
    private lateinit var btn2 : Button
    private lateinit var btn3 : Button
    private lateinit var btn4 : Button
    private lateinit var btnCloseCase : Button
    private lateinit var numCaseTv : TextView


    private lateinit var client : OkHttpClient
    val JSON: MediaType? = MediaType.parse("application/json; charset=utf-8")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn1 = findViewById(R.id.btn_1)
        btn2 = findViewById(R.id.btn_2)
        btn3 = findViewById(R.id.btn_3)
        btn4 = findViewById(R.id.btn_4)
        codeTv = findViewById(R.id.codeTv)
        numCaseTv = findViewById(R.id.nrCaseTv)
      //  btnCloseCase = findViewById(R.id.btn_closeCase)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        client = OkHttpClient()
        viewModel.code.observe(this, Observer {
            codeTv.text = it.toString()


            Log.i("code", it.toString())
        })

        btn1.setOnClickListener {
            handleButton("1")
        }
        btn2.setOnClickListener {
            handleButton("2")
        }
        btn3.setOnClickListener {
            handleButton("3")
        }
        btn4.setOnClickListener {
            handleButton("4")

        }

//        btnCloseCase.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val message = doGetRequest(server_url)
//                    val s1 = message.substringAfter("'box':")[0]
//                    val messageToSend: String = getJson("0", "0", "0")
//                    if (s1 == '1') {
//                        val postResponse: String = doPostRequest(server_url, messageToSend)
//                        withContext(Dispatchers.Main) {
//                            viewModel.code.value = 0
//                        }
//                    }
//                    Log.i("GetFunc", message)
//                }catch (e : Exception) {
//                    Log.i("Errortag", e.printStackTrace().toString())
//                }
//            }
//        }

    }
//    , box: String, errors:String
    private fun getJson(case: String, code: String, box: String): String {
        return ("{'code':$code, 'case':$case, 'box':$box}")
    }


    fun doGetRequest(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.body()!!.string()
    }


    private fun  doPostRequest(url: String?, json: String?): String {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .url(url)
                .post(body).build()
        val response = client.newCall(request).execute()
        return response.body()!!.string()
    }


    private fun handleButton(btn: String) {
            viewModel.randomCode()
            viewModel.setUpNrCase(btn)
            numCaseTv.text = "Nr skrzynki: ${btn}"
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val box : String = "0"
                    val json: String = getJson(btn, viewModel.code.value.toString(), box)
                    val postResponse: String =
                        doPostRequest(server_url, json)
                    Log.i("PostResp", postResponse)
                } catch (e : Exception) {
                    Log.i("PostErrorSending", e.toString())
                    e.printStackTrace().toString()
                }
            }
    }
}



