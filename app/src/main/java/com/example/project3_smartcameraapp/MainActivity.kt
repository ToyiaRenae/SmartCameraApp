package com.example.project3_smartcameraapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Build a button that launches the camera app
        findViewById<Button>(R.id.button).setOnClickListener {
            // TODO: Launch camera app

            // Create an intent that launches the camera and takes a picture
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // Grab bitmap from image that was taken in camera
            val imageBitmap = data?.extras?.get("data") as Bitmap

            // Set bitmap as imageView image
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)

            // Prepare bitmap for ML Kit APIs
            val imageForMLKit = InputImage.fromBitmap(imageBitmap, 0)

            // Utilize image labeling API
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(imageForMLKit)
                .addOnSuccessListener { labels ->
                    Log.i("LaToyia", "Successfully processed image")
                    for (label in labels) {
                        // What was detected in the image
                        val text = label.text
                        // The confidence score of what was detected
                        val confidence = label.confidence
                        val index = label.index
                        Log.i("LaToyia", "detected" + text + "with confidence: " + confidence)
                    }

                }
                .addOnFailureListener { e ->
                    Log.e("LaToyia", "Error processing image")
                }
        }
    }
}