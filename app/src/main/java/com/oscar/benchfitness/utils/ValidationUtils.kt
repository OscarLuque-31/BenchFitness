package com.oscar.benchfitness.utils

fun validateFields(
    username: String, email: String, password: String, confirmPassword: String,
    birthday: String, acceptTerms: Boolean
): Pair<Boolean, String> {
    return when {
        username.isBlank() -> Pair(false, "El nombre de usuario no puede estar vacío.")
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() -> Pair(
            false,
            "Ingresa un email válido."
        )

        password.length < 6 -> Pair(false, "La contraseña debe tener al menos 6 caracteres.")
        password != confirmPassword -> Pair(false, "Las contraseñas no coinciden.")
        birthday.isBlank() -> Pair(false, "Selecciona una fecha de nacimiento.")
        !acceptTerms -> Pair(false, "Debes aceptar los términos y condiciones.")
        else -> Pair(true, "")
    }
}


// Función para validar los campos de inicio de sesión
fun validateLoginFields(email: String, password: String): Pair<Boolean, String> {
    return when {
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(
            false,
            "Ingresa un email válido"
        )

        password.length < 6 -> Pair(false, "Se debe completar el campo")
        else -> Pair(true, "")
    }
}

fun validateFieldsDatos(
    altura: String, genero: String, nivelActividad: String, objetivo: String,
    peso: String, experiencia: String
): Pair<Boolean, String> {
    return when {
        altura.isBlank() -> Pair(false, "La altura es obligatoria")
        genero.isBlank() -> Pair(false, "El género es obligatorio")
        nivelActividad.isBlank() -> Pair(false, "Debes seleccionar un nivel de actividad")
        objetivo.isBlank() -> Pair(false, "Debes seleccionar un objetivo fitness")
        peso.isBlank() -> Pair(false, "El peso es obligatorio")
        experiencia.isBlank() -> Pair(false, "Debes seleccionar tu nivel de experiencia")
        else -> Pair(true, "")
    }
}