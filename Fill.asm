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

// pseudo code solution below
// brushLimit = 8192 (amount of spots to paint/fill)
// brushPos = 0
// while true
// 	 if KBD > 0
// 	   if brush + 1 > SCREEN
//        SCREEN[brushPos] = -1
//        brushPos = brushPos + 1
//     else
//        do nothing
//   else 
//     if brush - brushLimit < 0
//        SCREEN[brushPos] = 0
//        brushPos = brushPos - 1
//     else
//        do nothing
// 
	@8192
	D=A
	@brushLimit
	M=D                 // the limit, otherwise we brush onto the keyboard
	@brushPos
	M=0                 // set brushPos to 0
(LOOP)
	@KBD
	D=M                 // D = value of KBD
	@IF_KBD_PRESSED     // if KBD > 0 then goto IF_KBD_PRESSED
	D;JGT
	@brushPos
	D=M+1 				// D = brushPos value + 1 
	@ARR_BEYOND_SCREEN  // if brushPos + 1 > 0 goto ARR_BEYOND_SCREEN 
	D;JGT
	@LOOP               
	0;JMP               // else if brushPos + 1 <= 0, goto LOOP (start loop over)
(IF_KBD_PRESSED)
	@brushPos
	D=M                 // set D = brushPos value
	@brushLimit
	D=D-M               // set D = D - brushLimit value
	@ARR_BEFORE_KBD
	D;JLT               // if brushPos - brushLimit < 0, goto ARR_BEFORE_KBD (keep brushing)
	@LOOP
	0;JMP               // else if brushPos - brushLimit >= 0, goto LOOP (hit KBD, start loop over)
(ARR_BEFORE_KBD)
	@SCREEN
	D=A                 // D = address of SCREEN
	@brushPos
	D=D+M               // add brushPos value to address of SCREEN
	A=D                 // set address to D
	M=-1                // set value at address to -1 (black)
	@brushPos
	M=M+1               // increment brushPos
	@LOOP
	0;JMP               // goto LOOP (start loop over)
(ARR_BEYOND_SCREEN)
	@SCREEN
	D=A                 // D = address of SCREEN
	@brushPos
	D=D+M               // add brushPos value to address of SCREEN
	A=D                 // set address to D
	M=0                 // set value at address to 0 (white)
	@brushPos
	M=M-1               // decrement brushPos
	@LOOP
	0;JMP               // goto LOOP (start loop over)
