package com.example.practicarxjava

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction

fun main(args: Array<String>) {
    //Single
    singleExample(5000.0).subscribe({
        println("El resultado es $it")
    },{
        print("error en el cálculo")
    })

    //Single Zip
    singleZipExample().subscribe({ groceriesList ->
        print("single zip new list: $groceriesList")
    }, {

    })

}

private fun singleExample(numberValue: Double): Single<Double> {
    return Single.create{
        val operation = numberValue * 50
        val newValue = if (operation >= 500000) 2000.0 else operation
        it.onSuccess(newValue)
    }
}

private fun singleZipExample(): Single<List<String>>{
    val fruits = Single.just(listOf("banano", "manzana", "piña", "fresa"))
    val vegetables = Single.just(listOf("tomate", "cebolla", "lechuga", "brócoli", "zanahoria"))
    val groceries = mutableListOf<String>()

    return Single.zip(
        fruits,
        vegetables,
        BiFunction{ t1, t2 ->
            groceries.addAll(t1)
            groceries.addAll(t2)
            return@BiFunction groceries
        }
    )
}