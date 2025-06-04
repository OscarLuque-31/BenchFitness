package com.oscar.benchfitness.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.MainHome
import com.oscar.benchfitness.navegation.MainPerfil
import com.oscar.benchfitness.navegation.MainStatistics
import com.oscar.benchfitness.navegation.MainWorkout
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.convertMillisToDate
import com.oscar.benchfitness.viewModels.statistics.CalculosViewModel
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
    imeAction: ImeAction = ImeAction.Done,
    onFocusChange: (FocusState) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
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
        modifier = modifier.fillMaxWidth(),
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
        keyboardOptions = keyboardOptions,

        )
}


@Composable
fun GlobalButton(
    text: String,
    backgroundColor: Color,
    colorText: Color,
    modifier: Modifier,
    tamanyoLetra: TextUnit = 14.sp,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = colorText
        ),
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = tamanyoLetra
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
            colors = createDatePickerColors()
        ) {
            DatePicker(
                state = state,
                colors = createDatePickerColors()
            )
        }
    }
}

// Función auxiliar para crear los colores del DatePicker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun createDatePickerColors() = DatePickerDefaults.colors(
    containerColor = negroBench,
    titleContentColor = rojoBench,
    headlineContentColor = rojoBench,
    subheadContentColor = rojoBench,
    weekdayContentColor = rojoBench,
    dayContentColor = rojoBench,
    disabledDayContentColor = rojoBench.copy(alpha = 0.5f),
    selectedDayContentColor = Color.White,
    selectedDayContainerColor = rojoBench,
    todayContentColor = Color.White,
    todayDateBorderColor = rojoBench,
    navigationContentColor = rojoBench,
    yearContentColor = rojoBench,
    disabledYearContentColor = rojoBench.copy(alpha = 0.5f),
    currentYearContentColor = Color.White,
    selectedYearContentColor = rojoBench,
    selectedYearContainerColor = negroBench,
    dividerColor = rojoBench,
    dateTextFieldColors = createTextFieldColors()
)

// Función auxiliar para crear los colores del TextField
@Composable
private fun createTextFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = rojoBench,
    disabledTextColor = rojoBench.copy(alpha = 0.5f),
    focusedContainerColor = negroBench,
    unfocusedContainerColor = negroBench,
    disabledContainerColor = negroBench.copy(alpha = 0.5f),
    cursorColor = rojoBench,
    selectionColors = TextSelectionColors(
        handleColor = rojoBench,
        backgroundColor = rojoBench.copy(alpha = 0.4f)
    ),
    focusedIndicatorColor = rojoBench,
    unfocusedIndicatorColor = rojoBench.copy(alpha = 0.5f),
    disabledIndicatorColor = rojoBench.copy(alpha = 0.2f),
    focusedLabelColor = rojoBench,
    unfocusedLabelColor = rojoBench,
    disabledLabelColor = rojoBench.copy(alpha = 0.5f),
    focusedPlaceholderColor = rojoBench.copy(alpha = 0.7f),
    unfocusedPlaceholderColor = rojoBench.copy(alpha = 0.7f),
    disabledPlaceholderColor = rojoBench.copy(alpha = 0.3f),
    focusedLeadingIconColor = rojoBench,
    unfocusedLeadingIconColor = rojoBench,
    disabledLeadingIconColor = rojoBench.copy(alpha = 0.5f),
    focusedTrailingIconColor = rojoBench,
    unfocusedTrailingIconColor = rojoBench,
    disabledTrailingIconColor = rojoBench.copy(alpha = 0.5f)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GlobalDropDownMenu(
    selectedItem: T,
    opciones: List<T>,
    onValueChange: (T) -> Unit,
    itemText: (T) -> String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = negroOscuroBench,
    colorText: Color = Color.White,
    colorFlechita: Color = rojoBench,
    colorItemPulsado: Color = negroBench.copy(alpha = 0.7f)
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldWidth by remember { mutableIntStateOf(0) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = itemText(selectedItem),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Flecha desplegar menú",
                    tint = colorFlechita
                )
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
                .clip(RoundedCornerShape(12.dp))
                .menuAnchor()
                .onGloballyPositioned { coordinates ->
                    textFieldWidth = coordinates.size.width
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldWidth.toDp() })
                .heightIn(max = 250.dp)
                .background(backgroundColor)
        ) {
            opciones.forEach { opcion ->
                val isSelected = opcion == selectedItem
                val itemBackground = if (isSelected) colorItemPulsado else backgroundColor

                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = itemText(opcion),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = colorText,
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onValueChange(opcion)
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(itemBackground),
                    contentPadding = PaddingValues(0.dp)
                )
            }
        }
    }
}

// Version para strings
@Composable
fun GlobalDropDownMenu(
    nombreSeleccion: String,
    opciones: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = negroOscuroBench,
    colorText: Color = Color.White,
    colorFlechita: Color = rojoBench,
    colorItemPulsado: Color = negroBench.copy(alpha = 0.7f)
) {
    GlobalDropDownMenu(
        selectedItem = nombreSeleccion,
        opciones = opciones,
        onValueChange = onValueChange,
        itemText = { it },
        modifier = modifier,
        backgroundColor = backgroundColor,
        colorText = colorText,
        colorFlechita = colorFlechita,
        colorItemPulsado = colorItemPulsado
    )
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
                color = Color.White, fontWeight = FontWeight.Medium
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
        NavigationIcon("Home", R.drawable.home, currentRoute == MainHome.route) {
            navController.navigate(MainHome.route)
        }
        NavigationIcon(
            "Ejercicios",
            R.drawable.iconopesas,
            currentRoute == MainWorkout.route
        ) {
            navController.navigate(MainWorkout.route)
        }
        NavigationIcon(
            "Estadisticas",
            R.drawable.bascula,
            currentRoute == MainStatistics.route
        ) {
            navController.navigate(MainStatistics.route)
        }
        NavigationIcon("Perfil", R.drawable.user, currentRoute == MainPerfil.route) {
            navController.navigate(MainPerfil.route)
        }
    }
}

@Composable
fun NavigationIcon(
    label: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) rojoBench else negroOscuroBench,
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
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
        )
    }
}

@Composable
fun FormularioCalorias(
    viewModel: CalculosViewModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PesoAlturaInputs(viewModel)
        GlobalDropDownMenu(
            viewModel.nivelActividad,
            viewModel.opcionesNivelActividad,
            onValueChange = { viewModel.nivelActividad = it },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = negroBench,
            colorText = rojoBench,
            colorFlechita = rojoBench
        )
        EdadGeneroInputs(viewModel, viewModel.opcionesGenero)

        GlobalButton(
            text = "Calcular",
            colorText = if (viewModel.esFormularioCompletado()) Color.White else Color.Gray,
            backgroundColor = if (viewModel.esFormularioCompletado()) negroBench else Color.DarkGray,
            onClick = {
                when {
                    !viewModel.esEdadValida() -> {
                        viewModel.mensajeError =
                            "Edad inválida. Ingresa una edad entre 5 y 120 años."
                        viewModel.showDialog = true
                    }

                    !viewModel.esPesoValido() -> {
                        viewModel.mensajeError = "Peso inválido. Ingresa un peso entre 20 y 300 kg."
                        viewModel.showDialog = true
                    }

                    !viewModel.esAlturaValida() -> {
                        viewModel.mensajeError =
                            "Altura inválida. Ingresa una altura entre 80 y 250 cm."
                        viewModel.showDialog = true
                    }

                    viewModel.esFormularioValido() -> {
                        onClick()
                    }

                    else -> {
                        viewModel.mensajeError = "Completa todos los campos correctamente."
                        viewModel.showDialog = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        ResultadoCaloriasYLegal(viewModel.calorias)

        InfoDialog(
            title = "Datos no válidos",
            showDialog = viewModel.showDialog,
            onDismiss = { viewModel.showDialog = false }
        ) {
            Text(viewModel.mensajeError, color = rojoBench)
        }
    }
}


@Composable
fun PesoAlturaInputs(viewModel: CalculosViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf(
            Triple("Peso", viewModel.peso, "kg"),
            Triple("Altura", viewModel.altura, "cm")
        ).forEach { (label, value, unit) ->
            GlobalTextField(
                nombre = "",
                text = value,
                onValueChange = {
                    when (label) {
                        "Peso" -> viewModel.peso = it
                        "Altura" -> viewModel.altura = it
                    }
                },
                trailingIcon = { Text(unit, fontSize = 14.sp, color = rojoBench) },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.5f)
                    .height(55.dp),
                backgroundColor = negroBench,
                colorText = rojoBench,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun EdadGeneroInputs(viewModel: CalculosViewModel, opcionesGenero: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GlobalTextField(
            nombre = "",
            text = viewModel.edad,
            onValueChange = { viewModel.edad = it },
            trailingIcon = { Text("años", fontSize = 14.sp, color = rojoBench) },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(120.dp)
                .height(55.dp),
            backgroundColor = negroBench,
            colorText = rojoBench,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        GlobalDropDownMenu(
            viewModel.genero,
            opcionesGenero,
            onValueChange = { viewModel.genero = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = negroBench,
            colorText = rojoBench,
            colorFlechita = rojoBench
        )
    }
}


@Composable
fun ResultadoCaloriasYLegal(calorias: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        if (calorias.isNotBlank()) {
            Text(
                "$calorias kcal",
                color = rojoBench,
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Este cálculo es solo una aproximación. Consulta a un profesional de la salud para un cálculo más específico.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp)
        )
    }
}