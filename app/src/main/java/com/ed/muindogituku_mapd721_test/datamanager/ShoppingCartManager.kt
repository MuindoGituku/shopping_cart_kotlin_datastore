package com.ed.muindogituku_mapd721_test.datamanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingCartManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("ShoppingCart")

        val CART_PRODUCTS_IDS = stringSetPreferencesKey("product_ids")
    }

    suspend fun saveProductsToCart(stringList: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[CART_PRODUCTS_IDS] = stringList.toSet()
        }
    }

    suspend fun clearCart() {
        context.dataStore.edit { preferences ->
            preferences[CART_PRODUCTS_IDS] = emptySet()
        }
    }

    val getProductsInCart: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[CART_PRODUCTS_IDS]?.toList() ?: emptyList()
        }
}