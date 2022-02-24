package com.example.currencyexghangeapp.util

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

fun <T> Observable<T>.defaultSchedulers(): Observable<T> = this.compose(ApplySchedulersTransformer())

fun <T> Flowable<T>.defaultSchedulers(): Flowable<T> = this.compose(ApplyFlowableSchedulersTransformer())

class ApplySchedulersTransformer<T : Any?> : ObservableTransformer<T, T> {
    override fun apply(source: Observable<T>): ObservableSource<T> {
        return source
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}

class ApplyFlowableSchedulersTransformer<T : Any?> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
