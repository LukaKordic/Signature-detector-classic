package com.example.myapplication.welcome.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.common.constants.*
import com.example.myapplication.common.extensions.checkPermission
import com.example.myapplication.common.extensions.onClick
import com.example.myapplication.common.utils.convertImageTo2DArray
import com.example.myapplication.common.utils.toGrayscale
import com.example.myapplication.content.ui.startContentActivity
import com.example.myapplication.welcome.WelcomeContract.WelcomePresenter
import com.example.myapplication.welcome.WelcomeContract.WelcomeView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.android.ext.android.inject
import java.io.FileNotFoundException

fun startWelcomeActivity(from: Context) = from.startActivity(Intent(from, WelcomeActivity::class.java))

class WelcomeActivity : AppCompatActivity(), WelcomeView {
  
  private val presenter: WelcomePresenter by inject()
  private lateinit var image: Bitmap
  private val currentUser by lazy { GoogleSignIn.getLastSignedInAccount(this) }
  private val signInOptions by lazy {
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
  }
  private val signInClient by lazy { GoogleSignIn.getClient(this, signInOptions) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_welcome)
    presenter.setView(this)
    
    disableRecognizeButton()
    presenter.loadData(R.raw.signature_features, R.raw.signature_labels)
    initGoogleLogin()
    initClickActions()
  }
  
  override fun checkCameraPermission() {
    checkPermission(Manifest.permission.CAMERA, ::openCamera, ::requestCameraPermission)
  }
  
  override fun checkReadStoragePermission() {
    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, ::launchGallery, ::requestReadStoragePermission)
  }
  
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == RC_CAMERA_PERMISSION && permissions.isNotEmpty()) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) openCamera()
    } else {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) launchGallery()
    }
  }
  
  override fun openCamera() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      //check if there is component that can handle this intent
      resolveActivity(packageManager)?.also {
        startActivityForResult(this, RC_CAPTURE_IMAGE)
      }
    }
  }
  
  override fun launchGallery() {
    Intent(Intent.ACTION_PICK).apply {
      type = "image/*"
      putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
    }.run {
      resolveActivity(packageManager)?.also {
        startActivityForResult(this, RC_LOAD_IMAGE)
      }
    }
  }
  
  override fun showError() {
    Snackbar.make(welcomeLayout, getString(R.string.fake_signature), Snackbar.LENGTH_LONG).show()
  }
  
  override fun showContent() {
    startContentActivity(this)
    finish()
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        RC_CAPTURE_IMAGE -> {
          val image = data?.extras?.get("data") as? Bitmap
          image?.let {
            photoPreview.setImageBitmap(it.toGrayscale())
            this.image = it
            enableRecognizeButton()
          }
        }
        RC_LOAD_IMAGE -> {
          val imageUri = data?.data
          imageUri?.let {
            try {
              val inputStream = contentResolver.openInputStream(it)
              val selectedImage = BitmapFactory.decodeStream(inputStream)
              this.image = selectedImage
              photoPreview.setImageBitmap(selectedImage)
              enableRecognizeButton()
            } catch (ex: FileNotFoundException) {
              ex.printStackTrace()
            }
          }
        }
        RC_SIGN_IN -> {
          startContentActivity(this)
          finish()
        }
      }
    }
  }
  
  private fun initGoogleLogin() {
    currentUser?.let {
      startContentActivity(this)
      finish()
    } ?: googleSignInBtn.onClick { signIn() }
  }
  
  private fun signIn() {
    val intent = signInClient.signInIntent
    startActivityForResult(intent, RC_SIGN_IN)
  }
  
  private fun initClickActions() {
    capturePhoto.onClick { presenter.capturePhotoClicked() }
    loadFromGallery.onClick { presenter.loadImageClicked() }
    recognize.onClick { presenter.recognizeClicked(convertImageTo2DArray(image)) }
  }
  
  private fun requestCameraPermission() = ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                                                                            RC_CAMERA_PERMISSION)
  
  private fun requestReadStoragePermission() =
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        RC_STORAGE_PERMISSION)
  
  private fun disableRecognizeButton() = with(recognize) {
    isEnabled = false
  }
  
  private fun enableRecognizeButton() = with(recognize) {
    isEnabled = true
  }
}
