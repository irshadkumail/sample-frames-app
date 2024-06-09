package com.example.perftester.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perftester.client.Product
import com.example.perftester.data.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

sealed class ProductUIState {
    data object Loading : ProductUIState()
    data class Success(val products: List<Product>, val visibleIndex: Int = 0) : ProductUIState()
    data object Error : ProductUIState()

}

@HiltViewModel
class MainViewModel @Inject constructor(private val apiRepository: ProductRepository) :
    ViewModel() {

    var uiState by mutableStateOf<ProductUIState>(ProductUIState.Loading)

    var selectedProperty by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {

            val response = apiRepository.getProducts()

            uiState = if (response.isSuccessful) {
                response.body()?.let {
                    ProductUIState.Success(it)
                } ?: ProductUIState.Error
            } else {
                ProductUIState.Error
            }
        }
    }

    fun findSelectedItem(index: Int): Product? {
        return (uiState as? ProductUIState.Success)?.products?.get(index)
    }

    fun updateStateIndex(index: Int) {
        Thread.sleep(600)
        uiState =
            ProductUIState.Success(
                (uiState as? ProductUIState.Success)?.products ?: emptyList(),
                index
            )
    }
}