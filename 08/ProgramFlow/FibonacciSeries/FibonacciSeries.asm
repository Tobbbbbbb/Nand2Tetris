@1
D=A
@ARG
A=M+D
D=M
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
@R13
M=D
@1
D=A
@3
A=A+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@0
D=A
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
@R13
M=D
@0
D=A
@THAT
A=M+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@1
D=A
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
@R13
M=D
@1
D=A
@THAT
A=M+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@0
D=A
@ARG
A=M+D
D=M
@SP
M=M+1
A=M-1
M=D
@2
D=A
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
@SP
M=M-1
A=M
D=M
@R13
M=D
@0
D=A
@ARG
A=M+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
(MAIN_LOOP_START)
@0
D=A
@ARG
A=M+D
D=M
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
@R13
M=D
@0
D=A
@5
A=A+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@5
D=M
@COMPUTE_ELEMENT
D;JNE
@END_PROGRAM
0;JMP
(COMPUTE_ELEMENT)
@0
D=A
@THAT
A=M+D
D=M
@SP
M=M+1
A=M-1
M=D
@1
D=A
@THAT
A=M+D
D=M
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
@SP
M=M-1
A=M
D=M
@R13
M=D
@2
D=A
@THAT
A=M+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@1
D=A
@3
A=A+D
D=M
@SP
M=M+1
A=M-1
M=D
@1
D=A
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
A=A-1
M=D+M
@SP
M=M-1
A=M
D=M
@R13
M=D
@1
D=A
@3
A=A+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@0
D=A
@ARG
A=M+D
D=M
@SP
M=M+1
A=M-1
M=D
@1
D=A
@SP
M=M+1
A=M-1
M=D
@SP
M=M-1
A=M
D=M
A=A-1
M=M-D
@SP
M=M-1
A=M
D=M
@R13
M=D
@0
D=A
@ARG
A=M+D
D=M
D=A
@R14
M=D
@R13
D=M
@R14
A=M
M=D
@MAIN_LOOP_START
0;JMP
(END_PROGRAM)
(LOOP)
@LOOP
0;JMP
