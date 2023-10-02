// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

@R0
D = M
@25
D; JEQ
@R1
D = M
@25
D; JEQ
@R2
M = 0
@R1
D = M
@R3
M = D
@R0
D = M
@R2
M = M+D
@R3
M = M-1
D = M
@14
D; JGT
@27
0; JMP
@R2
M = 0
@27
0; JMP

//Set the value of R3 to the number of times left that we have to add R0 to itself
