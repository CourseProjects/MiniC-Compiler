.data
T1: .word
a: .word
b: .word
j: .word
.text
.globl main
main:
	la $a0, T1
	la $t1, a
	la $t2, b
	add $t3, $t1, $t2
	sw $t3, 0($a0)
	la $a0, j
	la $v1, T1
	sw $v1, 0($v0)
	sw $v0, 0($a0)
