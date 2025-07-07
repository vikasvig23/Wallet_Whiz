package com.example.expensestracker.Components


import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.expensestracker.R
import com.example.expensestracker.data.NavigationItem
import com.example.expensestracker.data.SignUpUIEvent
import com.example.expensestracker.data.SignUpViewModel
//import com.example.expensestracker.data.Expense
//import com.example.expensestracker.data.currenccy
import com.example.expensestracker.data.currency
import com.example.expensestracker.navigation.BottomBarScreen
//import com.example.expensestracker.preferences.CurrencyPref
//import com.example.expensestracker.preferences.MainViewModel
import com.example.expensestracker.screens.AddCurrency

import com.example.expensestracker.screens.AddExpenses
import com.example.expensestracker.screens.Categories
import com.example.expensestracker.screens.Expenses
import com.example.expensestracker.screens.Reports
//import com.example.expensestracker.screens.categories
import com.example.expensestracker.screens.Setting
import com.example.expensestracker.screens.saveNameToSharedPreferences
import com.example.expensestracker.ui.theme.AccentColor
import com.example.expensestracker.ui.theme.GrayCoor
import com.example.expensestracker.ui.theme.Primary
import com.example.expensestracker.ui.theme.Secondary
import com.example.expensestracker.ui.theme.TextColor
import kotlinx.coroutines.delay

@Composable
fun NormalTextComponents(value:String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        color = colorResource(id = R.color.colorText),
        textAlign = TextAlign.Left

        )
}

@Composable
fun UnderlinedTextComponents(value:String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(40.dp),
        style = TextStyle(

            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        color = colorResource(id = R.color.colorGray),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline

    )
}

@Composable
fun HeadingTextComponents(value:String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        color = colorResource(id = R.color.black),
       // textAlign = TextAlign.Center

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    labelValue: String, painterResource: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean=false){

    val textValue= remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier= Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small),
        label ={ Text(text=labelValue) },

        colors= OutlinedTextFieldDefaults.colors(
            cursorColor = Primary,
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value=textValue.value,
        onValueChange ={
            textValue.value=it
            onTextSelected(it)
        },
        leadingIcon={
            Icon(painter = painterResource, contentDescription ="null" )
        },
        isError = !errorStatus)
    /*ButtonComponent(value = String()) {
       Button(onClick = { saveTextToSharedPreferences(context, textValue.value) }) {
        }

     */

    }




@Composable
fun MyPasswordField(
    labelValue: String, painterResource: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean=false
){
    val localFocusManager= LocalFocusManager.current
    val password= remember {
        mutableStateOf("")
    }
    val passwordVisible= remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small),
        label = { Text(text = labelValue) },

        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Primary,
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,


            ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else Icons.Filled.VisibilityOff
            var Description = if (passwordVisible.value) {
                stringResource(id = R.string.hidePassword)
            } else
                stringResource(id = R.string.showPassword)
            IconButton(onClick = { passwordVisible.value=!passwordVisible.value }) {

                Icon(imageVector = iconImage, contentDescription = "description")
                
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus

    )
}

@Composable
fun CheckboxComponent(value:String,  onTextSelected:(String)->Unit, onCheckedChange:(Boolean)->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        val checkedState= remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value, onCheckedChange = {
            checkedState.value=!checkedState.value
            onCheckedChange.invoke(it)
        })
        ClickableTextComponent(value = value,onTextSelected)
    }
}

@Composable
fun ClickableTextComponent(value:String, onTextSelected:(String)->Unit){
    val initialText="By continuing you accept our"
    val privacyPolicyText=" Privacy Policy "
    val andText=" and "
    val termsAndConditionText="Term of Use"
    val annotatedString= buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color= Primary)){
            pushStringAnnotation(tag=privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style = SpanStyle(color= Primary)){
            pushStringAnnotation(tag=termsAndConditionText, annotation = termsAndConditionText)
            append(termsAndConditionText)
        }
    }
    ClickableText(text = annotatedString, onClick ={ offset->
        annotatedString.getStringAnnotations(offset,offset)
            .firstOrNull()?.also{span->
                Log.d("ClickableTextComponent","{${span.item}}")

                if((span.item==termsAndConditionText) || (span.item==privacyPolicyText)){
                    onTextSelected(span.item)
                }
            }

} )
}

@Composable
fun ClickableLoginTextComponent(tryingToLogin:Boolean, onTextSelected:(String)->Unit){
    val initialText=if(tryingToLogin)"Already have an Account?" else "Don't have a Account yet?"
    val LoginText=if(tryingToLogin)"Login" else " Register"

    val annotatedString= buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color= Primary)){
            pushStringAnnotation(tag=LoginText, annotation = LoginText)
            append(LoginText)
        }
    }
    ClickableText( modifier = Modifier
        .fillMaxWidth()
        .heightIn(40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString, onClick ={ offset->
            annotatedString.getStringAnnotations(offset,offset)
                .firstOrNull()?.also{span->
                    Log.d("ClickableTextComponent","{${span.item}}")

                    if(span.item==LoginText){
                        onTextSelected(span.item)
                    }
                }

        } )
}
@Composable
fun AnimatedMessageBar(
    message: String,
    isSuccess: Boolean,
    show: Boolean,
    onDismiss: () -> Unit
) {
    val transition = updateTransition(targetState = show, label = "messageTransition")
    val offsetY by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "offsetAnimation"
    ) { visible -> if (visible) 0.dp else (-100).dp }

    if (show) {
        LaunchedEffect(Unit) {
            delay(3000)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetY)
            .background(
                color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            )
            .padding(16.dp)
    ) {
        androidx.compose.material3.Text(
            text = message,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AnimatedSwitchButton(
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Press effect scale with spring
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "switchButtonScale"
    )

    // Glow effect (like toggle button)
    val glowColor by animateColorAsState(
        targetValue = when {
            !isEnabled -> Color.Gray
            isPressed -> Color(0xFF00C853) // active press green
            else -> Color(0xFF0F9D58) // default green
        },
        animationSpec = tween(250),
        label = "switchButtonColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(48))
            .background(
                brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                shape = RoundedCornerShape(50.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null, // remove default ripple
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.2.sp
        )
    }
}



@Composable
fun ButtonComponent(value: String, isEnabled:Boolean=false, onButtonClicked:()->Unit){
    val context = LocalContext.current
    Button(onClick = {
                     onButtonClicked.invoke()
    },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding= PaddingValues(), //for default values
        colors=ButtonDefaults.buttonColors(Color.Transparent),
        enabled=isEnabled
  ){
        Box(
            modifier= Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Secondary, Primary)),
                    shape = RoundedCornerShape(50.dp),
                    //ena=isEnabled
                ),
            contentAlignment = Alignment.Center
        ){
            Text(text=value,
                fontSize = 18.sp,
                color=Color.White,
                fontWeight =FontWeight.Bold)
        }

    }
}

@Composable
fun DividerTextComponent(){
    Row(modifier = Modifier.
        fillMaxWidth()

    ){
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            thickness = 1.dp,
            color = GrayCoor
        )
        Text(modifier = Modifier.padding(8.dp), text= stringResource(R.string.or),
            fontSize=18.sp, color= TextColor)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            thickness = 1.dp,
            color = GrayCoor
        )
    }
}

@Composable
fun ClickableLoginTextComponent(onTextSelected:(String)->Unit){
    val initialText="Already have an Account?"
    val LoginText=" Login"

    val annotatedString= buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color= Primary)){
            pushStringAnnotation(tag=LoginText, annotation = LoginText)
            append(LoginText)
        }
    }
    ClickableText( modifier = Modifier
        .fillMaxWidth()
        .heightIn(40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString, onClick ={ offset->
        annotatedString.getStringAnnotations(offset,offset)
            .firstOrNull()?.also{span->
                Log.d("ClickableTextComponent","{${span.item}}")

                if(span.item==LoginText){
                    onTextSelected(span.item)
                }
            }

    } )
}


//+ "/{currencyCode}"
//@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(navController: NavHostController) {
    //val currencyPref: CurrencyPref
   // val viewModel:MainViewModel by viewModel()
    NavHost(navController = navController, startDestination = BottomBarScreen.Expense.route) {

        composable(route=BottomBarScreen.Expense.route) {
         //   val currencyPref: CurrencyPref
                Expenses(navController)
            }

        composable(route=BottomBarScreen.Reports.route){
            Reports()
        }
        composable(route=BottomBarScreen.Add.route){
           AddExpenses(navController)
        }
        composable(route=BottomBarScreen.Setting.route){
          Setting(navController)
        }
        composable(route="setting/categories") {
               Categories(navController)
            }
        composable(route="setting/currency") {
            AddCurrency( navController)
        }


    }
}

@Composable
fun NavigationDrawerHeader(value: String?) {
    Box(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    listOf(Primary, Secondary)
                )
            )
            .fillMaxWidth()
            .height(180.dp)
            .padding(32.dp)
    ) {

        NavigationDrawerText(
            title = value?:stringResource(R.string.navigation_header), 28.sp , AccentColor
        )

    }
}

@Composable
fun NavigationDrawerBody(navigationDrawerItems: List<NavigationItem>,
                         onNavigationItemClicked:(NavigationItem) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        items(navigationDrawerItems) {
            NavigationItemRow(item = it,onNavigationItemClicked)
        }

    }
}

@Composable
fun NavigationItemRow(item: NavigationItem,
                      onNavigationItemClicked:(NavigationItem) -> Unit) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigationItemClicked.invoke(item)
            }
            .padding(all = 16.dp)
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.description,
        )

        Spacer(modifier = Modifier.width(18.dp))

        NavigationDrawerText(title = item.title, 18.sp, Primary)


    }
}

@Composable
fun NavigationDrawerText(title: String, textUnit: TextUnit, color: Color) {

    val shadowOffset = Offset(4f, 6f)

    Text(
        text = title, style = TextStyle(
            color = Color.Black,
            fontSize = textUnit,
            fontStyle = FontStyle.Normal,
            shadow = Shadow(
                color = Primary,
                offset = shadowOffset, 2f
            )
        )
    )
}











