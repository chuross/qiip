package com.github.chuross.qiiip.ui.viewmodel.fragment.component

import android.content.Context
import com.github.chuross.qiiip.Settings
import com.github.chuross.qiiip.domain.item.Item
import com.github.chuross.qiiip.domain.tag.Tag
import com.github.chuross.qiiip.ui.viewmodel.fragment.FragmentViewModel
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.functions.Consumer
import jp.keita.kagurazaka.rxproperty.RxProperty
import timber.log.Timber

class TagItemListFragmentViewModel(context: Context, val tag: Tag) : FragmentViewModel(context) {

    val items: RxProperty<List<Item>> = RxProperty()
    val currentPage: RxProperty<Int> = RxProperty(1)
    val isLoading: RxProperty<Boolean> = RxProperty(false)
    val hasError: RxProperty<Boolean> = RxProperty(false)

    init {
        isLoading.filter { it }
                .subscribe({ hasError.set(false) })
                .apply { disposables.add(this) }
    }

    fun fetchItems() = fetchItems(1, Consumer {
        items.set(it)
    })

    fun fetchNextItems() = fetchItems(currentPage.get()!!.inc(), Consumer { result ->
        items.get()?.let { items.set(it.plus(result)) }
        currentPage.get()?.let { currentPage.set(it.inc()) }
    })

    private fun fetchItems(page: Int, success: Consumer<List<Item>>) {
        isLoading.set(true)
        application.itemRepository.findAllByTagIdentity(tag.identity, page, Settings.app.perPage)
                .bindUntilEvent(this, FragmentEvent.DESTROY_VIEW)
                .subscribeOn(application.serialScheduler)
                .observeOn(application.mainThreadScheduler)
                .subscribe({
                    success.accept(it)
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