package com.oscar.benchfitness.user

import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.models.user.userData
import com.oscar.benchfitness.utils.validateLoginFields
import com.oscar.benchfitness.utils.validateRegisterFields
import org.junit.Assert.assertEquals
import org.junit.Test

class UserTests {

    private val USER_PLANTILLA = userData(
        username = "Oscar",
        peso = "",
        email = "oscar11luquexd@gmail.com",
        altura = "",
        genero = "",
        nivelActividad = "",
        objetivo = "",
        birthday = "",
        uid = "60xDyKtSv3hGuVG57tjk74JsHZb4",
        datosCompletados = false,
        rutinaAsignada = Routine(),
        experiencia = ""
    )

    @Test
    fun creacionUsuarioSinDatos() {
        /**
         * Caso en el que se crea un usuario con los datos mínimos y se comprueba que se ha creado bien
         */
        val userACrear = userData(
            username = "Oscar",
            email = "oscar11luquexd@gmail.com",
            uid = "60xDyKtSv3hGuVG57tjk74JsHZb4",
        )

        assertEquals(userACrear, USER_PLANTILLA)
    }

    @Test
    fun creacionUsuarioConDatos() {
        /**
         * Caso en el que se crea un usuario con todos sus datos y se comprueba que se ha creado bien
         */
        val userACrear = userData(
            username = "Oscar",
            peso = "80",
            email = "oscar11luquexd@gmail.com",
            altura = "167",
            genero = "Hombre",
            nivelActividad = "Sedentario (poco o ningún ejercicio)",
            objetivo = "Masa muscular",
            birthday = "08/09/2006",
            uid = "60xDyKtSv3hGuVG57tjk74JsHZb4",
            datosCompletados = true,
            rutinaAsignada = Routine(),
            experiencia = "Principiante"
        )

        val userEsperado = USER_PLANTILLA.copy(
            peso = "80",
            altura = "167",
            genero = "Hombre",
            nivelActividad = "Sedentario (poco o ningún ejercicio)",
            objetivo = "Masa muscular",
            birthday = "08/09/2006",
            datosCompletados = true,
            rutinaAsignada = Routine(),
            experiencia = "Principiante"
        )

        assertEquals(userACrear, userEsperado)
    }

    @Test
    fun cambiarPesoUsuario() {
        val userOriginal = USER_PLANTILLA
        val userModificado = userOriginal.copy(peso = "77")

        assertEquals("77", userModificado.peso)
    }

    @Test
    fun cambiarAlturaUsuario() {
        val userOriginal = USER_PLANTILLA
        val userModificado = userOriginal.copy(altura = "175")

        assertEquals("175", userModificado.altura)
    }

    @Test
    fun cambiarObjetivoUsuario() {
        val userOriginal = USER_PLANTILLA
        val userModificado = userOriginal.copy(objetivo = "Perder peso")

        assertEquals("Perder peso", userModificado.objetivo)
    }

    @Test
    fun cambiarRutinaAsignadaUsuario() {
        val userOriginal = USER_PLANTILLA
        val nuevaRutina = Routine(nombre = "Rutina Fuerza")
        val userModificado = userOriginal.copy(rutinaAsignada = nuevaRutina)

        assertEquals(nuevaRutina, userModificado.rutinaAsignada)
    }

    @Test
    fun registrarUsuarioCorrecto() {
        val userOriginal = USER_PLANTILLA

        val (isValid) = validateRegisterFields(
            username = userOriginal.username,
            email = userOriginal.email,
            acceptTerms = true,
            password = "Lukas123",
            confirmPassword = "Lukas123"
        )

        assertEquals(true, isValid)
    }

    @Test
    fun registrarUsuarioIncorrecto() {
        val userOriginal = USER_PLANTILLA

        val (isValid) = validateRegisterFields(
            username = userOriginal.username,
            email = userOriginal.email,
            acceptTerms = true,
            password = "Lukas123",
            confirmPassword = "Lukas12"
        )

        assertEquals(false, isValid)
    }

    @Test
    fun loginUsuarioCorrecto() {
        val userOriginal = USER_PLANTILLA

        val (isValid) = validateLoginFields(
            email = userOriginal.email,
            password = "Lukas123",
        )

        assertEquals(true, isValid)
    }

    @Test
    fun loginUsuarioIncorrecto() {
        val userOriginal = USER_PLANTILLA

        val (isValid) = validateLoginFields(
            email = userOriginal.email,
            password = "123",
        )

        assertEquals(false, isValid)
    }
}