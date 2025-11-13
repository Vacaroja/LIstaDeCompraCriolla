package com.ccc.listadecompracriolla.coutes

import com.ccc.listadecompracriolla.Core.clases.coutes.funtioncoutes
import org.junit.Assert.assertEquals
import org.junit.Test

class FuntioncoutesTest {

    val coutes = funtioncoutes()

    @Test
    fun coutesIsCorrects(){
        assertEquals(60f,coutes.createCoutes(240f,3,0.25f))
    }
    @Test
    fun firstCouteIsCorrects(){
        assertEquals(60f,coutes.firstCoute(240f,0.25f))
    }

}