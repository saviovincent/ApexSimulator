# ApexSimulator
Decoupled multi stage Pipeline Simulator

This is a implementation of a cycle-by-cycle simulator for an out-of-order APEX pipeline with multiple different function units
The instruction set used is LOAD-STORE architecture
The instruction stalls in case of flow and output dependencies
Out of order writebacks are implemented and priority is set as Div > Mul> Int
16 architectural registers used
Instructions supported are: ADD, SUB, LOAD, STORE, MOVC, AND, OR, EXOR, MUL, DIV


