package com.ccc.listadecompracriolla.Core.clases.coutes

class funtioncoutes {



    fun firstCoute(total: Float = 1f,percentInitial:Float= 1f): Float{
        return total *  percentInitial}

    fun createCoutes(total: Float= 1f,numCoutes:Int= 1,percentInitial:Float= 1f): Float{
        val initial= firstCoute(total,percentInitial)
        return (total - initial) / numCoutes

    }
}