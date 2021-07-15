package com.example.practicarxjava

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.practicarxjava.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCompletableTrue.setOnClickListener {
            completableExample(true)
        }

        binding.buttonCompletableFalse.setOnClickListener {
            completableExample(false)
        }

        binding.buttonSuscribe.setOnClickListener {
            initSusbcribe()
        }

        binding.buttonNextActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun completableExample(booleanValue: Boolean) {
        Completable.create {
            if (booleanValue) {
                Thread.sleep(3000)
                it.onComplete()
            } else {
                Thread.sleep(2000)
                it.onError(Throwable("ocurrió un error"))
            }
        }.subscribe(
            {
                Toast.makeText(applicationContext, "Proceso exitoso", Toast.LENGTH_LONG).show()
            },
            {   error ->
                Toast.makeText(applicationContext, "Proceso falló: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun initSusbcribe() {
        val observable = Observable.interval(1, TimeUnit.SECONDS)
        val observer = object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: Long) {
                println("onNext: $t")

                if(t > 5 && !disposable.isDisposed) {
                    disposable.dispose()
                }
            }

            override fun onError(e: Throwable) {
                println("onError: ${e.message}")
            }

            override fun onComplete() {
                println("onComplete")
            }

        }
        observable.subscribe(observer)
    }
}
