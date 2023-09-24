package com.sh.michael.simple_notepad

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import com.sh.michael.simple_notepad.common.DrawerHandle
import com.sh.michael.simple_notepad.common.disableGlobalGestures
import com.sh.michael.simple_notepad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DrawerHandle.attach(
            binding.drawerNavigationView,
            R.layout.menu_handle,
            HANDLE_OFFSET
        )

        findViewById<ImageView>(R.id.handleImageView).disableGlobalGestures()
    }

    fun closeDrawer() {
        binding.mainContainer.closeDrawer(GravityCompat.START)
    }

    companion object {

        const val HANDLE_OFFSET = 0.7f
    }
}