package com.example.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val button = findViewById<Button>(R.id.btnClick)
//        simpleObserver();
//        createObservable();

        button.clicks()
            .throttleFirst(5000 , TimeUnit.MILLISECONDS)
            .subscribe {
            Log.d("RxJava", "Button clicked")
        }
        implementNetworkCall()
    }

    private fun implementNetworkCall() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val productService = retrofit.create(ProductService::class.java)
        productService.getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            Log.d("RxJava", it.toString())
        }
    }

    private fun createObservable() {
    val observable = Observable.create<String> {
        it.onNext("One")
        it.onError(IllegalArgumentException("Error in Observable"))
        it.onNext("Two")
        it.onComplete()

    }
        observable.subscribe(object :Observer<String>{
            override fun onSubscribe(d: Disposable) {
                Log.d("Rxjava", "onSubscribe called")
            }

            override fun onNext(t: String) {
                Log.d("Rxjava", "onNext: - $t"  )
            }

            override fun onError(e: Throwable) {
                Log.d("Rxjava", "onError: - ${e.message}")
            }

            override fun onComplete() {
                Log.d("Rxjava", "onComplete is called")
            }

        });
    }

    private fun simpleObserver() {

        val list = listOf<String>("A","B","C")
        val observable = Observable.fromIterable(list)


     /*   observable.subscribe(object:Observer<String>{
            override fun onSubscribe(d: Disposable) {

                Log.d("Rxjava", "onSubscribe called")
            }

            override fun onNext(t: String) {

                Log.d("Rxjava", "onNext: - $t"  )
            }

            override fun onError(e: Throwable) {

                Log.d("Rxjava", "onError: - ${e.message}")
            }

            override fun onComplete() {

                Log.d("Rxjava", "onComplete is called")
            }

        })*/
    }
}