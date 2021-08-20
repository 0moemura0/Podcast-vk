package com.example.podcast_vk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: PodcastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btnBottomSheetPersistent.setOnClickListener {
//            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            else
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }

        VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {

            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}