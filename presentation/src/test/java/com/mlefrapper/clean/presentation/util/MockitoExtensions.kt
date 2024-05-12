@file:Suppress("unused")

package com.mlefrapper.clean.presentation.util

import org.mockito.Mockito

/**
 * Returns Mockito.any() as nullable type to avoid java.lang.IllegalStateException when
 * null is returned.
 */
fun <T> any(): T = Mockito.any()

/**
 * Gives you the ability to mock observes programmatically
 * **/
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)