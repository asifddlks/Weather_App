package com.asifddlks.weatherapp.ui.activities

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asifddlks.weatherapp.R
import com.asifddlks.weatherapp.databinding.ActivitySplashScreenBinding


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class SplashScreenActivity : AppCompatActivity() {

    private val binding: ActivitySplashScreenBinding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                
            }

            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
                
            }

            override fun onAnimationRepeat(animation: Animator) {
                
            }

        })


    }
}