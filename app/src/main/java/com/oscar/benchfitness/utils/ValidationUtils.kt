package com.oscar.benchfitness.utils


private val MAX_USERNAME_LENGTH = 15
private val MIN_PASSWORD_LENGTH = 6
private val REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+\$".toRegex()
private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

/**
 * Método para validar los datos del registro
 */
fun validateRegisterFields(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    acceptTerms: Boolean
): Pair<Boolean, String> {
    return when {
        // El nombre de usuario no puede estar en blanco
        username.isBlank() -> Pair(false, "El nombre de usuario no puede estar vacío.")

        // El nombre de usuario no puede tener mas longitud que la indicada
        username.length > MAX_USERNAME_LENGTH -> Pair(false, "El nombre de usuario no puede tener más de $MAX_USERNAME_LENGTH caracteres.")

        // El email no puede estar en blanco y tiene que pasar el regex
        email.isBlank() || !EMAIL_REGEX.matches(email) ->
            Pair(false, "Ingresa un email válido.")

        // La contraseña tiene que tener un minimo de caracteres
        password.length < MIN_PASSWORD_LENGTH ->
            Pair(false, "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres.")

        // Debe pasar el regex
        !REGEX_PASSWORD.matches(password) ->
            Pair(false, "La contraseña debe contener al menos una mayúscula, una minúscula y un número.")

        // Tienen que coincidir las contraseñas
        password != confirmPassword -> Pair(false, "Las contraseñas no coinciden.")

        // Tiene que aceptar los terminos
        !acceptTerms -> Pair(false, "Debes aceptar los términos y condiciones.")
        else -> Pair(true, "")
    }
}

/**
 * Método para validar los datos del login
 */
fun validateLoginFields(email: String, password: String): Pair<Boolean, String> {
    return when {
        // El email no puede estar en blanco y tiene que pasar el regex
        email.isBlank() || !EMAIL_REGEX.matches(email) -> Pair(
            false,
            "Ingresa un email válido"
        )

        // La contraseña tiene que tener mas de 6 caracteres
        password.length < MIN_PASSWORD_LENGTH -> Pair(false, "Completa correctamente los campos restantes")
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
    // La altura debe ser un número entero
    val alturaValida = altura.toIntOrNull()
    if (altura.isBlank()) return Pair(false, "La altura es obligatoria")
    if (alturaValida == null) return Pair(false, "La altura debe ser un número entero válido")

    // El género no puede estar vacío ni ser el valor predeterminado
    if (genero.isBlank() || genero == "Sexo") return Pair(false, "El género es obligatorio")

    // El nivel de actividad no puede estar vacío ni ser el valor predeterminado
    if (nivelActividad.isBlank() || nivelActividad == "Nivel de actividad física") {
        return Pair(false, "Debes seleccionar un nivel de actividad")
    }

    // El objetivo no puede estar vacío ni ser el valor predeterminado
    if (objetivo.isBlank() || objetivo == "Objetivo fitness") {
        return Pair(false, "Debes seleccionar un objetivo fitness")
    }

    // El peso debe ser un número decimal válido
    val pesoValido = peso.toDoubleOrNull()
    if (peso.isBlank()) return Pair(false, "El peso es obligatorio")
    if (pesoValido == null || peso.count { it == '.'} > 1) {
        return Pair(false, "El peso debe ser un número válido")
    }

    // La experiencia no puede estar vacío ni ser el valor predeterminado
    if (experiencia.isBlank() || experiencia == "Experiencia") {
        return Pair(false, "Debes seleccionar tu nivel de experiencia")
    }

    // El cumpleaños no puede estar vacío
    if (birthday.isBlank()) return Pair(false, "Selecciona una fecha de nacimiento.")

    // Si todo es valido
    return Pair(true, "")
}

/**
 * Valida el objetivo del usuario al cambiarlo en el perfil
 */
fun validarObjetivo(newObjetivo: String): String? {
    return if (newObjetivo.isBlank()) "Por favor selecciona un objetivo válido." else null
}

/**
 * Valida la altura del usuario al cambiarla en el perfil
 */
fun validarAltura(newAltura: String): String? {
    val alturaInt = newAltura.toIntOrNull()
    return if (alturaInt == null || alturaInt !in 80..250)
        "Introduce una altura válida entre 80 y 250 cm."
    else null
}

/**
 * Valida la contraseña del usuario al cambiarla en el perfil
 */
fun validatePassword(password: String): Pair<Boolean, String> {
    return when {
        password.length < MIN_PASSWORD_LENGTH ->
            Pair(false, "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres.")
        !REGEX_PASSWORD.matches(password) ->
            Pair(false, "La contraseña debe contener al menos una mayúscula, una minúscula y un número.")
        else -> Pair(true, "")
    }
}
