package com.ed.muindogituku_mapd721_test.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ed.muindogituku_mapd721_test.R
import com.ed.muindogituku_mapd721_test.model.Product

@Composable
fun ProductCard(
    product: Product,
    onClickHandler:() -> Unit,
    modifier: Modifier,
    isPresentInCart: Boolean
){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Column {
            Text(
                text = "Product Name: ${product.productName}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )
            )
            Text(text = "Price: $${product.price}")
        }
        Button(
            onClick = { onClickHandler() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .height(35.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(corner = CornerSize(10))
                )
        ) {
            Text(
                text = if (isPresentInCart) "Remove" else "Add to Cart"
            )
        }
    }
}

@Composable
fun ProductInCartCard(
    product: Product,
    modifier: Modifier,
){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column {
            Text(text = "Product Name: ${product.productName}")
            Text(text = "Price: $${product.price}")
        }
    }
}