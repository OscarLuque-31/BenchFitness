package com.oscar.benchfitness.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.Ejercicios
import com.oscar.benchfitness.navegation.Estadisticas
import com.oscar.benchfitness.navegation.Home
import com.oscar.benchfitness.navegation.MainExercises
import com.oscar.benchfitness.navegation.Perfil
import com.oscar.benchfitness.screens.exercises.EjerciciosScreen
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.convertMillisToDate
import java.util.Calendar

@Composable
fun GlobalTextField(
    nombre: String,
    text: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier,
    backgroundColor: Color,
    colorText: Color
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp,
                color = colorText
            )
        },
        trailingIcon = trailingIcon,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 14.sp,
            textAlign = textAlign
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun GlobalButton(
    text: String,
    backgroundColor: Color,
    colorText: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = colorText
        ),
        modifier = modifier
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(onDateSelected: (String) -> Unit) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val state = rememberDatePickerState(
        yearRange = IntRange(
            1950,
            Calendar.getInstance().get(Calendar.YEAR)
        )
    )
    val selectedDate = state.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""


    TextField(
        value = selectedDate,
        onValueChange = { },
        placeholder = {
            Text(
                "Fecha de nacimiento",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp,
                color = negroBench
            )
        },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePickerDialog = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        val formattedDate = convertMillisToDate(it)
                        onDateSelected(formattedDate)
                    }
                    showDatePickerDialog = false
                }) {
                    Text(
                        "Confirmar",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = negroBench, // Fondo del DatePicker
                titleContentColor = rojoBench, // Color del título (Mes y Año en encabezado)
                headlineContentColor = rojoBench, // Color del mes/año seleccionado
                weekdayContentColor = rojoBench, // Color de los días de la semana (Lun, Mar, etc.)
                subheadContentColor = rojoBench, // Color de la cabecera secundaria
                dayContentColor = rojoBench, // Color de los días
                selectedDayContentColor = Color.White, // Día seleccionado (texto)
                selectedDayContainerColor = rojoBench, // Día seleccionado (fondo)
                todayDateBorderColor = rojoBench, // Borde del día actual
                yearContentColor = rojoBench, // Año
                currentYearContentColor = Color.White,
                selectedYearContentColor = rojoBench,// Año seleccionado
            )
        ) {
            DatePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    containerColor = negroBench, // Fondo del DatePicker
                    titleContentColor = rojoBench, // Color del título (Mes y Año en encabezado)
                    headlineContentColor = rojoBench, // Color del mes/año seleccionado
                    weekdayContentColor = rojoBench, // Color de los días de la semana (Lun, Mar, etc.)
                    subheadContentColor = rojoBench, // Color de la cabecera secundaria
                    dayContentColor = rojoBench, // Color de los días
                    selectedDayContentColor = Color.White, // Día seleccionado (texto)
                    selectedDayContainerColor = rojoBench, // Día seleccionado (fondo)
                    todayDateBorderColor = rojoBench,// Borde del día actual
                    todayContentColor = Color.White,
                    yearContentColor = rojoBench, // Año
                    selectedYearContentColor = rojoBench,// Año seleccionado
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalDropDownMenu(
    nombreSeleccion: String,
    opciones: List<String>,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    backgroundColor: Color
) {
    // Variable para controlar la visibilidad
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(nombreSeleccion) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp))
        ) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Flecha desplegar menú"
                        )
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    color = negroBench,
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = modifier
                    .menuAnchor()
                    .background(backgroundColor)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 250.dp)
                    .background(negroOscuroBench)
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                opcion,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            )
                        },
                        onClick = {
                            selectedOption = opcion
                            onValueChange(opcion)
                            expanded = false
                        },
                        modifier = modifier.background(negroOscuroBench)
                    )
                }
            }
        }
    }
}

@Composable
fun GlobalHeader(text: String) {
    CabeceraBenchFitness(text)
}


@Composable
fun CabeceraBenchFitness(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text,
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodySmall, fontSize = 20.sp,
                color = Color.White
            )
            Image(
                painter = painterResource(id = R.drawable.logo_marca),
                contentDescription = "Logo aplicación",
                modifier = Modifier.size(100.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(rojoBench)
        )
    }
}

@Composable
fun GlobalBarraNavegacion(navController: NavController) {
    var selectedItem by remember { mutableStateOf("Home") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(negroOscuroBench),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NavigationIcon("Home", R.drawable.home, selectedItem) {
            selectedItem = "Home"
            navController.navigate(Home)
        }
        NavigationIcon("Ejercicios", R.drawable.iconopesas, selectedItem) {
            selectedItem = "Ejercicios"
            navController.navigate(MainExercises)
        }
        NavigationIcon("Estadisticas", R.drawable.bascula, selectedItem) {
            selectedItem = "Estadisticas"
            navController.navigate(Estadisticas)
        }
        NavigationIcon("Perfil", R.drawable.user, selectedItem) {
            selectedItem = "Perfil"
            navController.navigate(Perfil)
        }
    }
}

@Composable
fun NavigationIcon(
    label: String,
    iconRes: Int,
    selectedItem: String,
    onClick: () -> Unit,
) {
    val color by animateColorAsState(
        targetValue = if (selectedItem == label) rojoBench else negroOscuroBench,
        label = ""
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun AdaptiveGifRow(
    url: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = rojoBench)
        }
    }
) {
    var imageSize by remember { mutableStateOf<IntSize?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ) {
        if (isLoading) {
            placeholder()
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .onGloballyPositioned {
                    if (imageSize == null) {
                        imageSize = it.size
                        isLoading = false
                    }
                },
            contentScale = ContentScale.FillWidth
        )
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { (imageSize?.height?.toDp() ?: 0.dp) })
            .padding(top = 10.dp)
    )
}
