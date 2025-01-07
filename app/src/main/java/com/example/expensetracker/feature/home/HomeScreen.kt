package com.example.expensetracker.feature.home

import android.app.AlertDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.HomeViewModel
import com.example.expensetracker.viewmodel.HomeViewModelFactory
import com.example.expensetracker.widget.MyTextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight



@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topbar, add) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            {
                Column {
                    MyTextView(text = "Welcome Back", fontSize = 16.sp, color = Color.White)
                    MyTextView(
                        text = "User",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

//          for view model
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            // our function
            CardItem(modifier = Modifier.constrainAs(card) {
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, balance, income, expenses)

            // Our List
            // Our List
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                list = state.value, // Pass the list from state.value correctly
                onDeleteExpense = { expense ->
                    GlobalScope.launch {
                        viewModel.deleteExpense(expense) // Ensure delete operation is handled
                    }
                }
            )


            Image(
                painter = painterResource(id = R.drawable.ic_addbutton),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 20.dp , end = 20.dp).constrainAs(add) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }.size(68.dp).clip(CircleShape).clickable {
                    navController.navigate("/add")
                })
        }
    }
}

@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expenses: String) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                MyTextView(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                MyTextView(
                    text = balance,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            // our created function
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                "Income",
                income,
                R.drawable.ic_income
            )

            // our created funtion
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                "Expense",
                expenses,
                R.drawable.ic_expense
            )
        }
    }
}

// function of row item in a card of income expense
@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, image: Int) {

    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            MyTextView(text = title, fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        MyTextView(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    list: List<ExpenseEntity>,
    title: String = "Recent Transaction",
    onDeleteExpense: (ExpenseEntity) -> Unit
) {
    // State to track the selected item and dialog visibility
    val (itemToDelete, setItemToDelete) = remember { mutableStateOf<ExpenseEntity?>(null) }

    // Confirmation dialog
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { setItemToDelete(null) },
            title = { Text(text = "Delete") },
            text = { Text(text = "Are you sure you want to delete ?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteExpense(itemToDelete)
                    setItemToDelete(null)
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { setItemToDelete(null) }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                MyTextView(text = title, fontSize = 20.sp)
                if (title == "Recent Transaction") {
                    MyTextView(
                        text = "See All",
                        fontSize = 16.sp,
                        color = Zinc,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }

        // Iterating through the items in the list
        items(list) { item ->
            TransationItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = if (item.type == "Income") R.drawable.aaya else R.drawable.gya,
                date = item.date.toString(),
                color = if (item.type == "Income") Color.Green else Color.Red,
                onLongPress = { setItemToDelete(item) } // Set item for deletion
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransationItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,
    onLongPress: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .combinedClickable(
                onClick = { /* Handle regular click if needed */ },
                onLongClick = onLongPress // Trigger on long press
            )
    ) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                MyTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                MyTextView(text = date, fontSize = 12.sp)
            }
        }

        MyTextView(
            text = amount,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}



@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen() {
    HomeScreen(rememberNavController())
}