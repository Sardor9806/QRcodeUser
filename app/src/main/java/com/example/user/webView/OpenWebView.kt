package com.example.user.webView

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.View
import android.webkit.WebChromeClient
import com.example.user.R
import com.example.user.databinding.ActivityOpenWebViewBinding


class OpenWebView : AppCompatActivity() {


    lateinit var binding: ActivityOpenWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        title="Welcome"
        super.onCreate(savedInstanceState)
        binding = ActivityOpenWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var  t = intent.getBooleanExtra("bool",false)
        if (t) {
                val url = intent.getStringExtra("view").toString()
                binding.image.setImageResource(R.drawable.image_go)
                Handler().postDelayed({
                    try {
                       startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        finish()
                    }catch (e:Exception)
                    {
                        d("sardor","keldi $e")
                    }

            }, 2000)

        } else {
            binding.image.setImageResource(R.drawable.image_no)
        }
    }
}