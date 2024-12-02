package org.korsnaike.pattern

import org.korsnaike.db.Pagination

abstract class Data_list<T : Comparable<T>>(protected var elements: List<T>) {
    private val selectedIndices = mutableSetOf<Int>()

    val pagination: Pagination = Pagination()

    init {
        elements.sorted()
    }

    fun getElement(index: Int): T {
        require(index in elements.indices) { "Индекс выходит за пределы списка" }
        return elements[index]
    }

    fun getSize(): Int = elements.size

    fun toList(): List<T> = elements.toList()

    fun select(number: Int) {
        require(number in elements.indices) { "Номер выходит за пределы списка" }
        selectedIndices.add(number)
    }

    fun getSelected(): List<Int> = selectedIndices.toList()

    fun getNames(): List<String> {
        return listOf("№") + getEntityFields()
    }

    abstract fun getEntityFields(): List<String>

    fun getData(): Data_table<Any> {
        val data = elements.mapIndexed { index, entity ->
            listOf(index) + getDataRow(entity)
        }
        return Data_table.create(data) as Data_table<Any>
    }

    abstract fun getDataRow(entity: T): List<Any>

    protected fun generateOrderNumbers(): List<Int> = elements.indices.toList()

    override fun toString(): String = elements.toString()
}