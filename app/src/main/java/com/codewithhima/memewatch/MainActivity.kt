package com.codewithhima.memewatch

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.nav)
        }
        loadMeme()
    }
//    private fun loadMeme(){
//        val queue = Volley.newRequestQueue(this)
//        val url = "https://www.google.com"
//
//        // Request a string response from the provided URL.
//        val stringRequest = StringRequest(
//            Request.Method.GET, url,
//            Response.Listener<String> { response ->
//                Log.d("success",response.substring(0,500))
//            },
//            Response.ErrorListener {
//                Log.d("error", it.localizedMessage)
//            })
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest)
//    }
    private fun loadMeme(){
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        Response.Listener { response ->
            currentImageUrl = response.getString("url")
            Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

            }).into(memeImageView)
        },
        Response.ErrorListener { error ->
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    )

    // Access the RequestQueue through yo   ur singleton class.
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        val message : String = "Hey, check out this cool meme i got from Reddit"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "$message + $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}