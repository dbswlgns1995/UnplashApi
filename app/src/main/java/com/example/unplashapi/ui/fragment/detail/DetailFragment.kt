package com.example.unplashapi.ui.fragment.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.unplashapi.R
import com.example.unplashapi.databinding.FragmentDetailBinding
import com.example.unplashapi.models.Image
import java.io.File
import java.io.FileOutputStream
import java.util.*

// to get detail image and download
class DetailFragment : Fragment(R.layout.fragment_detail){

    // passing image data from nature, random fragment
    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)

        binding.apply {
            val image = args.image

            // image load
            Glide.with(requireContext())
                .load(image.urls.full)
                .error(R.drawable.ic_error)
                .fitCenter()
                .into(detailImageview)

            binding.detailDownloadBtn.setOnClickListener {
                download(image)
            }

        }

    }

    private fun download(image: Image) {

        Glide.with(requireContext())
            .asBitmap()
            .load(image.urls.full)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    saveImage(resource, image.id)
                    Toast.makeText(requireContext(), "Download Completed", Toast.LENGTH_SHORT).show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d("***DetailFragment", "Download Completed")
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Toast.makeText(requireContext(), "Download failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // save image
    private fun saveImage(image: Bitmap, id: String): String? {
        var savedImagePath: String? = null
        val imageFileName = "$id.jpg"
        val storageDir =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/UnsplashApi")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath

            try {
                val fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 80, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            galleryAddPic(savedImagePath)
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        requireContext().sendBroadcast(mediaScanIntent)

    }
}