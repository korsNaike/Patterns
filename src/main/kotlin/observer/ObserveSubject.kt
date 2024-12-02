package org.korsnaike.observer

interface ObserveSubject {
    val observers: MutableList<Observer>

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    fun notify() {
        observers.forEach { it.update() }
    }
}