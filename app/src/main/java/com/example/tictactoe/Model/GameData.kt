package com.example.tictactoe.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object GameData {
    private val _gameModel: MutableLiveData<GameModel> = MutableLiveData()
    val gameModel: LiveData<GameModel> get() = _gameModel

    fun saveGameModel(model: GameModel) {
        _gameModel.postValue(model)
    }
}