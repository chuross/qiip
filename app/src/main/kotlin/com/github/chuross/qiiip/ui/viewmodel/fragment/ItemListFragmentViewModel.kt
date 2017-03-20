package com.github.chuross.qiiip.ui.viewmodel.fragment

import android.content.Context
import com.github.chuross.qiiip.Settings
import com.github.chuross.qiiip.domain.item.Item
import com.github.chuross.qiiip.domain.item.ItemRepository
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import jp.keita.kagurazaka.rxproperty.RxProperty
import timber.log.Timber

class ItemListFragmentViewModel(context: Context) : FragmentViewModel(context) {

    val items: RxProperty<List<Item>> = RxProperty()
    var currentPage: RxProperty<Int> = RxProperty(Settings.app.defaultPage)
    var isLoading: RxProperty<Boolean> = RxProperty(false)
    var hasError: RxProperty<Boolean> = RxProperty(false)
    private val itemRepository by lazy { ItemRepository().apply { application.component.inject(this) } }

    init {
        isLoading.filter { it }
                .subscribe({ hasError.set(false) })
                .apply { disposables.add(this) }
    }

    fun fetchItems() = fetchItems(Settings.app.defaultPage)

    fun fetchNextItems() = fetchItems(currentPage.get()!!.inc())

    private fun fetchItems(page: Int) {
        isLoading.set(true)
        itemRepository.findAll(page, Settings.app.perPage)
                .bindUntilEvent(this, FragmentEvent.DESTROY_VIEW)
                .subscribeOn(application.serialScheduler)
                .observeOn(application.mainThreadScheduler)
                .subscribe({
                    items.set(it)
                    hasError.set(false)
                    isLoading.set(false)
                }, {
                    Timber.e(it)
                    hasError.set(true)
                    isLoading.set(false)
                })
                .apply { disposables.add(this) }
    }
}