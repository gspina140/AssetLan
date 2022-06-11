li $s0 0
addi $sp $sp -12
move $fp $sp
li $t1 0
sw $t1 8($fp)
sw $fp 0($sp)
li $a0 3
push $a0
li $a0 2
push $a0
li $a0 1
push $a0
move $al $fp
push $al
jal function_main
addi $sp $sp 12
halt

function_f:
move $fp $sp
push $ra
move $al $fp
lw $a0 4($al)
push $a0
li $t1 0
sw $t1 4($al)
move $al $fp
lw $al 0($al)
lw $a0 8($al)
lw $t1 0($sp)
add $a0 $a0 $t1
sw $a0 8($al)
pop
push $fp
move $al $fp
move $al $fp
push $al
jal function_f
lw $ra 0($sp)
addi $sp $sp 20
lw $fp 0($sp)
pop
jr $ra

function_main:
move $fp $sp
push $ra
push $fp
move $al $fp
move $al $fp
push $al
jal function_f
move $al $fp
lw $al 0($al)
lw $al 0($al)
lw $a0 8($al)
add $s0 $s0 $a0
li $t1 0
sw $t1 8($al)
lw $ra 0($sp)
addi $sp $sp 20
lw $fp 0($sp)
pop
jr $ra
