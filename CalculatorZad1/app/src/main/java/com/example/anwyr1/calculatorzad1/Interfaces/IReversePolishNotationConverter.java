package com.example.anwyr1.calculatorzad1.Interfaces;

import com.example.anwyr1.calculatorzad1.Services.RPNSCharacter;

import java.util.Queue;

/**
 * Created by anwyr1 on 22/03/2018.
 */

public interface IReversePolishNotationConverter {
    void convertToReversePolishNotationSequence();
    Queue<IRPNSCharacter> getRPNSSequence();
}
