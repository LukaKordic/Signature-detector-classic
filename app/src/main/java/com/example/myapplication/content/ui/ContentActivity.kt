package com.example.myapplication.content.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.content.ContentContract
import com.example.myapplication.content.ui.list.RepoAdapter
import com.example.myapplication.networking.model.Repository
import com.example.myapplication.welcome.ui.WelcomeActivity
import com.example.myapplication.welcome.ui.startWelcomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_content.*
import org.koin.android.ext.android.inject

fun startContentActivity(from: Context) = from.startActivity(Intent(from, ContentActivity::class.java))

class ContentActivity : AppCompatActivity(), ContentContract.View {
  
  private val signInOptions by lazy { GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build() }
  private val signInClient by lazy { GoogleSignIn.getClient(this, signInOptions) }
  private val presenter: ContentContract.Presenter by inject()
  private val repoAdapter by lazy { RepoAdapter(this::showRepoOnline) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_content)
    
    initList()
    presenter.setView(this)
    presenter.fetchRepositories()
  }
  
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.overflow_menu, menu)
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.signOut -> signInClient.signOut().addOnCompleteListener {
        startWelcomeActivity(this)
        finish()
      }
    }
    return false
  }
  
  override fun showRepositories(repos: List<Repository>) {
    repoAdapter.setData(repos)
  }
  
  override fun showError(error: String) {
    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
  }
  
  override fun showLoading() {
    loading.visibility = View.VISIBLE
  }
  
  override fun hideLoading() {
    loading.visibility = View.GONE
  }
  
  private fun showRepoOnline(url: String) {
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(this, Uri.parse(url))
  }
  
  private fun initList() = with(repoList) {
    adapter = repoAdapter
    layoutManager = LinearLayoutManager(this@ContentActivity)
    setHasFixedSize(true)
  }
}