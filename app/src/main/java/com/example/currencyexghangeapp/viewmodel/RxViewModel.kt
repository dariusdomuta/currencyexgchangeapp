package com.example.currencyexghangeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyexghangeapp.viewmodel.Event.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class RxViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    private val dataLoadingLiveDataEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private val errorLiveDataEvent: MutableLiveData<Event<String>> = MutableLiveData()
    private val alertLiveDataEvent: MutableLiveData<Event<DisplayableAlert>> = MutableLiveData()
    private val dialogLiveDatEvent: MutableLiveData<Event<String>> = MutableLiveData()
    private val messagesLiveDataEvent: MutableLiveData<Event<Int>> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun subscribe(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun removeSubscriber(disposable: Disposable) {
        disposables.remove(disposable)
    }

    fun observeDataLoading(): LiveData<Event<Boolean>> {
        return dataLoadingLiveDataEvent
    }

    fun observeForErrors(): LiveData<Event<String>> {
        return errorLiveDataEvent
    }

    fun observeForAlerts(): LiveData<Event<DisplayableAlert>> {
        return alertLiveDataEvent
    }

    fun observeForMessages(): LiveData<Event<Int>> {
        return messagesLiveDataEvent
    }

    fun observeForDialogMessages(): LiveData<Event<String>> {
        return dialogLiveDatEvent
    }

    protected fun sendShowLoadingEvent() {
        dataLoadingLiveDataEvent.value = Event(true)
    }

    protected fun sendHideLoadingEvent() {
        dataLoadingLiveDataEvent.value = Event(false)
    }

    protected fun sendErrorEvent(error: String) {
        errorLiveDataEvent.value = Event(error)
    }

    protected fun sendMessageEvent(messageResId: Int) {
        messagesLiveDataEvent.value = Event(messageResId)
    }

    protected fun sendAlertEvent(titleResId: Int, messageResId: Int) {
        alertLiveDataEvent.value = Event(DisplayableAlert(titleResId, messageResId))
    }

    protected fun sendDialogMessageEvent(message: String) {
        dialogLiveDatEvent.value = Event(message)
    }
}

data class DisplayableAlert(
    val titleResId: Int,
    val messageResId: Int
)