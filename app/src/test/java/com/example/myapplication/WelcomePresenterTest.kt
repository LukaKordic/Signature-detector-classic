package com.example.myapplication

import com.example.myapplication.common.Resources
import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.ml.CustomKNN
import com.example.myapplication.welcome.presentation.WelcomePresenterImpl
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Before
import org.junit.Test

class WelcomePresenterTest {
  private val resources: Resources = mock()
  private val view: WelcomeContract.WelcomeView = mock()
  private val presenter = WelcomePresenterImpl(resources)
  private val classifier: CustomKNN = mock()
  
  @Before
  fun setUp() {
    presenter.setView(view)
    classifier.fit(testSignatureFeatures, testSignatureLabels)
  }
  
  @Test
  fun `check for permissions when capture photo is clicked`() {
    presenter.capturePhotoClicked()
    
    verify(view).checkCameraPermission()
    verifyNoMoreInteractions(view)
  }
  
  @Test
  fun `check read storage permissions when load image is clicked`() {
    presenter.loadImageClicked()
    
    verify(view).checkReadStoragePermission()
    verifyNoMoreInteractions(view)
  }
  
  @Test
  fun `calling fit trains classifier`() {
    presenter.loadData(R.raw.signature_features, R.raw.signature_labels)
    
    verify(classifier).fit(testSignatureFeatures, testSignatureLabels)
  }
  
  @Test
  fun `recognize clicked returns a prediction`() {
    presenter.recognizeClicked(testImageMatrix)
    
    val result = classifier.predict(testLbpHistogram)
    assert(result.isNotBlank())
    verifyNoMoreInteractions(classifier)
  }
}