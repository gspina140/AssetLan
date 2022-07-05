li $s0 0
addi $sp $sp -8
move $fp $sp
li $a0 1
sw $a0 4($fp)
sw $fp 0($sp)
li $a0 10
push $a0
move $al $fp
push $al
jal function_f
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
lw $al 0($al)
lw $a0 4($al)
print $a0
b label1
label0:
move $al $fp
lw $al 0($al)
lw $a0 4($al)
push $a0
move $al $fp
lw $a0 4($al)
lw $t1 0($sp)
mult $a0 $t1 $a0
pop
move $al $fp
lw $al 0($al)
sw $a0 4($al)
push $fp
move $al $fp
move $al $fp
move $al $fp
lw $a0 4($al)
push $a0
li $a0 1
lw $t1 0($sp)
sub $a0 $t1 $a0
pop

push $a0
move $al $fp
lw $al 0($al)
lw $al 0($al)
push $al
jal function_f
label1:
lw $ra 0($sp)
addi $sp $sp 12
lw $fp 0($sp)
pop
jr $ra
