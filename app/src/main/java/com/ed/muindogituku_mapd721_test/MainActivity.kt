package com.ed.muindogituku_mapd721_test

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ed.muindogituku_mapd721_test.datamanager.ShoppingCartManager
import com.ed.muindogituku_mapd721_test.model.Product
import com.ed.muindogituku_mapd721_test.template.ProductCard
import com.ed.muindogituku_mapd721_test.template.ProductInCartCard
import com.ed.muindogituku_mapd721_test.ui.theme.MuindoGituku_MAPD721_TestTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/*
* Created by Muindo Gituku on 21st February 2024
* Activity to list the products from a static list and update the cart depending on the interaction
* there is also a button to view the cart in a dialog view
* */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuindoGituku_MAPD721_TestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProductListView()
                }
            }
        }
    }
}

@Composable
fun ProductListView(){
    val productsList = listOf<Product>(
        Product("1", "Apple", 5.0),
        Product("2", "Banana", 5.0),
        Product("3", "Orange", 5.0),
        Product("4", "Guava", 5.0),
        Product("5", "Dates", 5.0),
        Product("6", "Potatoes", 5.0),
        Product("7", "Cabbage", 5.0),
        Product("8", "Biscuits", 5.0),
        Product("9", "Juice", 5.0),
        Product("10", "Salad", 5.0),
        Product("11", "Milk", 5.0),
        Product("12", "Yoghurt", 5.0),
        Product("13", "Pineapple", 5.0),
        Product("14", "Watermelon", 5.0),
        Product("15", "Grapes", 5.0),
        Product("16", "Peanut butter", 5.0),
    )

    var showDialog by remember { mutableStateOf(false) } //updated the visibility of the dialog

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = ShoppingCartManager(context)

    val savedProductIDs = dataStore.getProductsInCart.collectAsState(initial = emptyList())

    //this list will be rebuilt everytime there is a change in the list of IDs saved in store
    val productsInCart = productsList.filter { savedProductIDs.value.contains(it.productId) }

    Scaffold (
        topBar = {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(15.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = "Shopping App",
                    style = MaterialTheme
                        .typography.titleLarge
                        .copy(
                            fontWeight = FontWeight.Black,
                            color = colorResource(id = R.color.white)
                        )
                )
                Row (horizontalArrangement = Arrangement.Start){
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = colorResource(id = R.color.white),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.cart),
                                contentDescription = "Cart",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp)),
                                colorFilter = ColorFilter.tint(
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                dataStore.clearCart()
                                Toast.makeText(context, "Cart emptied!!", Toast.LENGTH_LONG).show()
                            }
                        }
                    ) {
                        Text(
                            text = "Clear",
                            style = MaterialTheme
                                .typography.titleSmall
                                .copy(
                                    fontWeight = FontWeight.Black,
                                    color = colorResource(id = R.color.white)
                                )
                        )
                    }
                }
            }
        }
    ){
        Column(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(productsList) { product ->
                    ProductCard(
                        product = product,
                        onClickHandler = {
                            scope.launch {
                                val currentIDs = savedProductIDs.value.toMutableList()
                                //we have created a mutable list that we can update then check if the
                                //id of selected product is present, if it is we remove it otherwise
                                //we add it to the list then overwrite the existing list in the store
                                if (!currentIDs.contains(product.productId)) {
                                    currentIDs.add(product.productId)
                                    dataStore.saveProductsToCart(currentIDs)
                                    Toast.makeText(context, "Added ${product.productName}!!", Toast.LENGTH_LONG).show()
                                } else {
                                    currentIDs.remove(product.productId)
                                    dataStore.saveProductsToCart(currentIDs)
                                    Toast.makeText(context, "Removed ${product.productName}!!", Toast.LENGTH_LONG).show()
                                }

                            }
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(corner = CornerSize(10))
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 15.dp),
                        isPresentInCart = productsInCart.contains(product) //check to show relevant text in button
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cart") },
            text = {
                if (productsInCart.isEmpty()) {
                    Text("Empty shopping cart!!")
                } else {
                    LazyColumn {
                        items(productsInCart) { product ->
                            ProductInCartCard(
                                product = product,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingCartPreview() {
    MuindoGituku_MAPD721_TestTheme {
        ProductListView()
    }
}