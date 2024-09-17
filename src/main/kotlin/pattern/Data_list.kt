package org.korsnaike.pattern

abstract class Data_list<T : Comparable<T>>(protected val elements: List<T>) {
    private val selectedIndices = mutableSetOf<Int>()

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

    abstract fun getNames(): List<String>

    abstract fun getData(): Data_table<Any>

    protected fun generateOrderNumbers(): List<Int> = elements.indices.toList()
}