package com.oscar.benchfitness.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.Estadisticas
import com.oscar.benchfitness.navegation.Home
import com.oscar.benchfitness.navegation.MainExercises
import com.oscar.benchfitness.navegation.Perfil
import com.oscar.benchfitness.ui.theme.negroBench
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
    colorText: Color,
    onDone: () -> Unit = {},
    imeAction: ImeAction = ImeAction.Done
) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
            textAlign = textAlign,
            color = colorText
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
                keyboardController?.hide()
            },
            onSearch = {
                onDone()
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        ),
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
    backgroundColor: Color,
    colorText: Color = negroBench
) {
    // Eliminamos remember aquí, ya que se maneja con el estado del ViewModel
    var expanded by remember { mutableStateOf(false) }

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
                value = nombreSeleccion, // Ahora usamos el nombre desde el ViewModel
                onValueChange = {}, // No necesitamos cambiar el valor del TextField directamente
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
                    color = colorText,
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
                modifier = Modifier
                    .heightIn(max = 250.dp)
                    .background(negroOscuroBench)
            ) {
                // Aquí usamos directamente las opciones que vienen del ViewModel
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
                            // Ahora cambiamos directamente el valor en el ViewModel
                            onValueChange(opcion) // Esto actualiza el valor en el ViewModel
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
            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodySmall, fontSize = 20.sp,
                color = Color.White
            )
            Image(
                painter = painterResource(id = R.drawable.logo_bench),
                contentDescription = "Logo aplicación",
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
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
    aspectRatio: Float = 16f / 9f,  // Relación de aspecto por defecto (16:9)
    placeholder: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(negroOscuroBench),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = rojoBench)
        }
    }
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Mostrar el Placeholder mientras se carga
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        ) {
            // Este es el placeholder que se muestra mientras la imagen está cargando
            placeholder()
        }

        // Cargar la imagen (y reemplazar el placeholder cuando se cargue)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .decoderFactory(GifDecoder.Factory())
                .crossfade(true)  // Animación suave entre placeholder y la imagen cargada
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun InfoDialog(
    title: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    cuerpo: @Composable () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Text(
                    text = "Cerrar",
                    color = rojoBench,
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(10.dp),
                    fontSize = 15.sp
                )
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            text = {
                cuerpo()
            },
            containerColor = negroBench,
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}

@Composable
fun FlechitaAtras(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.flechita_atras),
            contentDescription = "Flechita atrás",
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}