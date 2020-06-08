package com.echrisantus.buzz.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//Synchronous
fun foo0(): List<Int> {
    Thread.sleep(500)
    return listOf(1, 2, 3, 4, 5)
}
//fun main() {
//    foo0().forEach { value -> println(value) }
//}

fun foo1(): Sequence<Int> = sequence {
    for (i in 1..5) {
        Thread.sleep(500)
        yield(i)
    }
}
//fun main() {
//    foo1().forEach { value -> println(value) }
//}


//Asynchronous
suspend fun foo2(): List<Int> {
    Thread.sleep(500)
    return listOf(1, 2, 3, 4, 5)
}
//fun main() = runBlocking {
//    foo2().forEach { value -> println(value) }
//}

fun foo3(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(500)
        emit(i)
    }
}

//fun main() = runBlocking {
//    launch {
//        for (k in 1..5) {
//            println("I'm not blocked $k")
//            delay(500)
//        }
//    }
//    foo3().collect{ value -> println(value) }
//}

fun main() {
    println(getList())
}

fun getList(): List<String> {
    val list = mutableListOf<String>()

    for (i in 1..5) list.add("$i")
    return list.reversed()
}