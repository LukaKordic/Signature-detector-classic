package com.example.myapplication

import com.example.myapplication.content.ContentContract
import com.example.myapplication.content.interaction.GithubInteractor
import com.example.myapplication.content.presentation.ContentPresenter
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

class ContentPresenterTest {
  private var view: ContentContract.View = mock()
  private val interactor: GithubInteractor = mock()
  private val presenter = ContentPresenter(interactor)
  
  @Before
  fun setUp() {
    presenter.setView(view)
  }
}