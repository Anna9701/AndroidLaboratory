package com.example.anwyr1.calculatorzad1.Interfaces;

import java.util.Queue;

public interface IReversePolishNotationConverter {
    void convertToReversePolishNotationSequence();
    Queue<IRPNSCharacter> getRPNSSequence();
}
