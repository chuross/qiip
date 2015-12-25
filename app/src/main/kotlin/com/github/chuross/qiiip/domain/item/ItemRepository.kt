package com.github.chuross.qiiip.domain.item

import com.github.chuross.qiiip.domain.item.converter.ItemConverter
import com.github.chuross.qiiip.domain.tag.TagIdentity
import com.github.chuross.qiiip.domain.user.UserIdentity
import com.github.chuross.qiiip.infrastructure.qiita.QiitaV2Api
import rx.Observable
import javax.inject.Inject

class ItemRepository {

    @Inject
    lateinit var api: QiitaV2Api

    fun findByIdentity(identity: ItemIdentity): Observable<Item> {
        return api.getItemById(identity.value)
                .map { result -> ItemConverter.convertToModel(result) }
    }

    fun findAll(page: Int, perPage: Int): Observable<List<Item>> {
        return api.getItems(page, perPage)
                .map { result -> ItemConverter.convertToModels(result) }
    }

    fun findAllByKeyword(query: String, page: Int, perPage: Int): Observable<List<Item>> {
        return api.getItemsByKeyword(query, page, perPage)
                .map { result -> ItemConverter.convertToModels(result) }
    }

    fun findAllByTagIdentity(identity: TagIdentity, page: Int, perPage: Int): Observable<List<Item>> {
        return api.getItemsByTagId(identity.value, page, perPage)
                .map { result -> ItemConverter.convertToModels(result) }
    }

    fun findAllByUserIdentity(identity: UserIdentity, page: Int, perPage: Int): Observable<List<Item>> {
        return api.getItemsByKeyword("user:${identity.value}", page, perPage)
                .map { result -> ItemConverter.convertToModels(result) }
    }

}