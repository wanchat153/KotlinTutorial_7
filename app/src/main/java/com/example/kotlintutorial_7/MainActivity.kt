package com.example.kotlintutorial_7

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData()
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate: done")

    }

    companion object {
        private class  DownloadData : AsyncTask<String, Void, String>(){
            private val TAG = "DownloadData"

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute: parameter is $result")
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()){
                    Log.d(TAG, "doInBackground: Error Downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                val xmlResult = StringBuilder()

                try {
                    val url = URL(urlPath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(TAG, "downloadXML: The reponse code was $response")

//                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//
//                    val inputBuffer = CharArray(500)
//                    var charRead = 0
//                    while (charRead >= 0) {
//                        charRead = reader.read(inputBuffer)
//                        if (charRead > 0) {
//                            xmlResult.append(String(inputBuffer, 0, charRead))
//                        }
//                    }
//                    reader.close()

//                    val stream = connection.inputStream
                    connection.inputStream.buffered().reader().use { xmlResult.append(it.readText()) }

                    Log.d(TAG, "Received ${xmlResult.length} bytes")
                    return xmlResult.toString()

//                }catch (e: MalformedURLException){
//                    Log.d(TAG, "downloadXML: Invalid URL ${e.message}")
//                }catch (e: IOException){
//                    Log.d(TAG, "downloadXML: IO Exception reading data: ${e.message}")
//                }catch (e: SecurityException){
//                    e.printStackTrace()
//                    Log.d(TAG, "downloadXML: Security exception. Needs permission? ${e.message}")
//                }catch (e: Exception){
//                    Log.d(TAG, "Unknown error: ${e.message}")
//                }

                }catch (e: Exception){
                    val errorMessage: String = when(e){
                        is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
                        is IOException -> "downloadXML: IO Exception reading data: ${e.message}"
                        is SecurityException -> { e.printStackTrace()
                            "downloadXML: Security exception. Needs permission? ${e.message}"
                        }else -> "Unknown error: ${e.message}"
                    }
                }

                return ""
            }
        }
    }
}
