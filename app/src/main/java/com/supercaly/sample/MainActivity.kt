package com.supercaly.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.supercaly.photoloader.PhotoLoader

class MainActivity : AppCompatActivity() {

    private lateinit var photoLoader: PhotoLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photoLoader = findViewById(R.id.photo_loader)
        photoLoader.setImageBuilder(this@MainActivity)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            if (photoLoader.isEmpty)
                photoLoader.error = true
            photoLoader.images { data ->
                Toast.makeText(this@MainActivity, "$data", Toast.LENGTH_LONG).show() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (PhotoLoader.REQUEST_CODE == requestCode)
            photoLoader.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
