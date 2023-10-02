// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
@kbd
M=0
(LOOP1)
@KBD
D=M
@LOOP1
D; JEQ
@SCREEN
D=A
M=-1
@R0
M=D
@8191
D=A
(LOOP2)
@R0
M=M+1
A=M
M=-1
D=D-1
@LOOP2
(LOOP3)
D; JGE
@KBD
D=M
@LOOP3
D; JNE
@SCREEN
D=A
M=0
@R0
M=D
@8191
D=A
(LOOP4)
@R0
M=M+1
A=M
M=0
D=D-1
@LOOP4
D; JGE
0; JMP


