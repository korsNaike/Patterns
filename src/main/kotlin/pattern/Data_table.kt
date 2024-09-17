package org.korsnaike.pattern

class Data_table<T> private constructor(private val data: Array<Array<T>>) {
    companion object {
        fun <T> create(data: Array<Array<T>>): Data_table<T> {
            return Data_table(data)
        }
    }
}