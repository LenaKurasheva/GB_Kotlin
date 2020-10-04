package ru.geekbrains.gb_kotlin

class Model: IModel {

    override fun getData(): String {
        //Очень долгие вычисления
        return "Hello"
    }
}