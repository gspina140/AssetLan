li $s0 0
addi $sp $sp -16
move $fp $sp
li $a0 0
sw $a0 8($fp)
li $t1 0
sw $t1 12($fp)
sw $fp 0($sp)
li $a0 2
push $a0
li $a0 1
push $a0
move $al $fp
push $al
jal function_f
addi $sp $sp 16
halt

function_g:
move $fp $sp
push $ra
move $al $fp
lw $al 0($al)
lw $a0 12($al)
add $s0 $s0 $a0
li $t1 0
sw $t1 12($al)
lw $ra 0($sp)
addi $sp $sp 8
lw $fp 0($sp)
pop
jr $ra

function_f:
move $fp $sp
push $ra
move $al $fp
lw $a0 8($al)
move $al $fp
lw $al 0($al)
sw $a0 4($al)
move $al $fp
lw $al 0($al)
lw $a0 8($al)
push $a0
move $al $fp
lw $a0 4($al)
lw $t1 0($sp)
add $a0 $t1 $a0
pop
move $al $fp
lw $al 0($al)
sw $a0 8($al)
move $al $fp
lw $a0 8($al)
push $a0
li $t1 0
sw $t1 8($al)
move $al $fp
lw $al 0($al)
lw $a0 12($al)
lw $t1 0($sp)
add $a0 $a0 $t1
sw $a0 12($al)
pop
push $fp
move $al $fp
move $al $fp
move $al $fp
lw $al 0($al)
lw $al 0($al)
push $al
jal function_g
lw $ra 0($sp)
addi $sp $sp 16
lw $fp 0($sp)
pop
jr $ra
