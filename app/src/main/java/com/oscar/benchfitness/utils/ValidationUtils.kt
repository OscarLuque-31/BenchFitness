package com.oscar.benchfitness.utils


private val MAX_USERNAME_LENGTH = 15
private val MIN_PASSWORD_LENGTH = 6
private val REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+\$".toRegex()

/**
 * Método para validar los datos del registro con validación de contraseña mejorada
 */
fun validateRegisterFields(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    acceptTerms: Boolean
): Pair<Boolean, String> {
    return when {
        username.isBlank() -> Pair(false, "El nombre de usuario no puede estar vacío.")
        username.length > MAX_USERNAME_LENGTH -> Pair(false, "El nombre de usuario no puede tener más de $MAX_USERNAME_LENGTH caracteres.")
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
            Pair(false, "Ingresa un email válido.")
        password.length < MIN_PASSWORD_LENGTH ->
            Pair(false, "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres.")
        !REGEX_PASSWORD.matches(password) ->
            Pair(false, "La contraseña debe contener al menos una mayúscula, una minúscula y un número.")
        password != confirmPassword -> Pair(false, "Las contraseñas no coinciden.")
        !acceptTerms -> Pair(false, "Debes aceptar los términos y condiciones.")
        else -> Pair(true, "")
    }
}


/**
 * Método para validar los datos del login
 */
fun validateLoginFields(email: String, password: String): Pair<Boolean, String> {
    return when {
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(
            false,
            "Ingresa un email válido"
        )

        password.length < 6 -> Pair(false, "Completa los campos restantes")
        else -> Pair(true, "")
    }
}

/**
 * Método para validar los datos de la pantalla de datos
 */
fun validateFieldsDatos(
    altura: String,
    genero: String,
    nivelActividad: String,
    objetivo: String,
    peso: String,
    experiencia: String,
    birthday: String
): Pair<Boolean, String> {
    // Validación de la altura: debe ser un número entero
    val alturaValida = altura.toIntOrNull()
    if (altura.isBlank()) return Pair(false, "La altura es obligatoria")
    if (alturaValida == null) return Pair(false, "La altura debe ser un número entero válido")

    // Validación del género: no puede estar vacío ni ser el valor predeterminado
    if (genero.isBlank() || genero == "Sexo") return Pair(false, "El género es obligatorio")

    // Validación del nivel de actividad: no puede estar vacío ni ser el valor predeterminado
    if (nivelActividad.isBlank() || nivelActividad == "Nivel de actividad física") {
        return Pair(false, "Debes seleccionar un nivel de actividad")
    }

    // Validación del objetivo: no puede estar vacío ni ser el valor predeterminado
    if (objetivo.isBlank() || objetivo == "Objetivo fitness") {
        return Pair(false, "Debes seleccionar un objetivo fitness")
    }

    // Validación del peso: debe ser un número decimal válido (float o double)
    val pesoValido = peso.toDoubleOrNull()
    if (peso.isBlank()) return Pair(false, "El peso es obligatorio")
    if (pesoValido == null || peso.count { it == '.'} > 1) {
        return Pair(false, "El peso debe ser un número válido")
    }

    // Validación de la experiencia: no puede estar vacío ni ser el valor predeterminado
    if (experiencia.isBlank() || experiencia == "Experiencia") {
        return Pair(false, "Debes seleccionar tu nivel de experiencia")
    }

    // Validación del cumpleaños: no puede estar vacío
    if (birthday.isBlank()) return Pair(false, "Selecciona una fecha de nacimiento.")

    // Si todo es válido
    return Pair(true, "")
}

fun validarObjetivo(newObjetivo: String): String? {
    return if (newObjetivo.isBlank()) "Por favor selecciona un objetivo válido." else null
}

fun validarAltura(newAltura: String): String? {
    val alturaInt = newAltura.toIntOrNull()
    return if (alturaInt == null || alturaInt !in 80..250)
        "Introduce una altura válida entre 80 y 250 cm."
    else null
}

fun validatePassword(password: String): Pair<Boolean, String> {
    return when {
        password.length < MIN_PASSWORD_LENGTH ->
            Pair(false, "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres.")
        !REGEX_PASSWORD.matches(password) ->
            Pair(false, "La contraseña debe contener al menos una mayúscula, una minúscula y un número.")
        else -> Pair(true, "")
    }
}
