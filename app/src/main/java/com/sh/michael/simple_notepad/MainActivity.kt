package com.sh.michael.simple_notepad

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sh.michael.simple_notepad.common.DrawerHandle
import com.sh.michael.simple_notepad.common.disableGlobalGestures
import com.sh.michael.simple_notepad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // todo: disable swipe gesture for this view
        DrawerHandle.attach(
            binding.drawerNavigationView,
            R.layout.menu_handle,
            HANDLE_OFFSET
        )

        findViewById<ImageView>(R.id.handleImageView).disableGlobalGestures()
    }

    companion object {

        const val HANDLE_OFFSET = 0.7f
    }
}