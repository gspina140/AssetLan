li $s0 0
addi $sp $sp -8
move $fp $sp
li $t1 0
sw $t1 4($fp)
sw $fp 0($sp)
li $a0 2
push $a0
li $a0 1
push $a0
move $al $fp
push $al
jal function_main
addi $sp $sp 8
halt

function_f:
move $fp $sp
push $ra
move $al $fp
lw $a0 4($al)
push $a0
li $a0 0
lw $t1 0($sp)
beq $t1 $a0 label2
li $a0 0
b label3
label2:
li $a0 1
label3:
pop
li $t1 0
beq $a0 $t1 label0
move $al $fp
lw $a0 8($al)
push $a0
li $t1 0
sw $t1 8($al)
move $al $fp
lw $al 0($al)
lw $a0 4($al)
lw $t1 0($sp)
add $a0 $a0 $t1
sw $a0 4($al)
pop
b label1
label0:
move $al $fp
lw $a0 8($al)
push $a0
li $t1 0
sw $t1 8($al)
move $al $fp
lw $al 0($al)
lw $a0 4($al)
lw $t1 0($sp)
add $a0 $a0 $t1
sw $a0 4($al)
pop
move $al $fp
lw $a0 12($al)
push $a0
li $t1 0
sw $t1 12($al)
move $al $fp
lw $al 0($al)
lw $a0 4($al)
lw $t1 0($sp)
add $a0 $a0 $t1
sw $a0 4($al)
pop
label1:
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
addi $sp $sp -8
lw $a0 4($al)
sw $a0 0($sp)
li $t1 0
sw $t1 4($al)
lw $a0 8($al)
sw $a0 4($sp)
li $t1 0
sw $t1 8($al)
move $al $fp
li $a0 0

push $a0
move $al $fp
lw $al 0($al)
lw $al 0($al)
push $al
jal function_f
move $al $fp
lw $al 0($al)
lw $a0 4($al)
print $a0
move $al $fp
lw $al 0($al)
lw $a0 4($al)
add $s0 $s0 $a0
li $t1 0
sw $t1 4($al)
lw $ra 0($sp)
addi $sp $sp 16
lw $fp 0($sp)
pop
jr $ra
