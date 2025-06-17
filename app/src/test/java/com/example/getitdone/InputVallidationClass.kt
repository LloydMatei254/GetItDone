package com.example.getitdone

import com.example.getitdone.util.InputValidator
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class InputValidationClass {


    @Test
    fun inputValidator_returnsFalseWhenEmpty(){


        val result = InputValidator.isInputValid("")

        assertFalse(result)


    }

    @Test
    fun inputValidator_returnsFalseWhenOnlyWhiteSpace(){


        val result = InputValidator.isInputValid("   ")

        assertFalse(result)


    }

    @Test
    fun inputValidator_returnsTrueWhenMoreThanOneNonWhiteSpaceCharacter(){


        val result = InputValidator.isInputValid("more than one character")

       assertTrue(result)


    }
}