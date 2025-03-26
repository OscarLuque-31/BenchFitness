package com.oscar.benchfitness.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscar.benchfitness.screens.convertMillisToDate
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import java.util.Calendar

@Composable
fun GlobalTextField(
    nombre: String,
    text: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp,
                color = negroBench
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
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
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
    funcion: () -> Unit
) {
    Button(
        onClick = funcion,
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
            1900,
            Calendar.getInstance().get(Calendar.YEAR)
        )
    )
    val selectedDate = state.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""


    TextField(
        value = selectedDate,
        onValueChange = { }, // No cambia el estado aquí ya que el DatePicker manejará el estado
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
            .width(310.dp)
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
fun GlobalDropDownMenu(nombreSeleccion: String, opciones: List<String>, modifier: Modifier) {
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
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = modifier
                    .menuAnchor()
                    .background(Color.White) // Fondo blanco del TextField
                // Fondo blanco del TextField
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(negroClaroBench) // Ancho fijo del menú
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
                            expanded = false
                        },
                        modifier = modifier.background(negroClaroBench)
                    )
                }
            }
        }
    }
}

