
		.data
	
seta:	.asciiz "-> "
input:	.space 64
pilha:  .space 40
nxtln:  .asciiz "\n"
tab:	.asciiz "	"
stcktxt:.asciiz	"Stack:\n"
error:	.asciiz "Unkown command. Type 'help' for available commands\n"
bye:	.asciiz "Bye!"
hlptxt:	.asciiz "\n     + | Adicao (operador binario)\n     - | Subtracao (operador binario)\n     * | Multiplicacao (operador binario)\n     / | Divisao (operador binario)\n   neg | Calcula o simetrico (operador unario)\n  swap | Troca a posicao dos dois operandos do topo da pilha\n   dup | Duplica (clone) o operando do topo da stack\n  drop | Elimina o operando do topo da stack\n clear | Limpa toda a stack\n   off | Desliga a calculadora\n\n"
intro:	.asciiz "\n**************************************************\n**** RPN - Reverse Polish Notation Calculator ****\n** Andre Rato (45517) && Jose Alexandre (45223) **\n**** Arquitetura de Sistemas e Computadores 1 ****\n**************************************************\nType 'help' for available commands\n\n"
vazia:	.asciiz "(empty)\n"
byzero:	.asciiz "Error! Division by 0 is impossible!\n"
elmnts: .asciiz "Not enought values in the stack!\n"
sempty: .asciiz "The stack is already empty!\n"
elemms: .asciiz "Stack is full\n"

	
	.text
main:
	la $s7, pilha					# pilha - $s7
	la $s4, pilha					# variável que guarda a posição incial da pilha
	addi $s2, $s4, 4				# pilha com 1 elemento (s2)
	addi $s3, $s2, 4				# pilha com 2 elementos (s3)
	addi $s5, $s4, 40				# pilha com 10 elementos (s5)
	
	# print do cabeçalho
	li $v0, 4
	la $a0, intro
	syscall
	
	j print
	nop
	
	ciclo:
		# print da string seta
		li $v0, 4
		la $a0, seta
		syscall
		
		# leitura da string para input
		li $v0, 8
		la $a0, input
		li $a1, 64
		syscall
		
		# endereço da input em $s6
		move $s6, $a0
		
		controlar:
			# colocar auxiliar a 0
			add $t1, $zero, $zero		# variável de controlo dos números (auxiliar)
			add $t0, $zero, $zero		# variável numérica
			
		
		# análise da string input
		loop:
			lb $t2, 0($s6)			# load do byte para t2 (caractere)
			addi $s6, $s6, 1		# próxima posição
			
			beq $t2, 0, print		# se for o fim da string, executa print
			nop			
			
			beq $t2, '+', soma		# se t2 = '+', executa soma
			nop
			
			beq $t2, '-', subt		# se t2 = '-', executa subt
			nop
			
			beq $t2, '*', prod		# se t2 = '*', executa prod
			nop
			
			beq $t2, '/', divi		# se t2 = '/', executa divi
			nop
			
			beq $t2, 'n', nega		# se t2 = 'n', executa nega
			nop
			
			beq $t2, 's', swap		# se t2 = 's', executa swap
			nop
			
			beq $t2, 'c', clear		# se t2 = 'c', executa clear
			nop
			
			beq $t2, 'd', funcoesd		# se t2 = 'd', executa funcoesd
			nop
			
			beq $t2, 'o', off		# se t2 = 'o', executa off
			nop
			
			beq $t2, 'h', help		# se t2 = 'h', executa help
			nop		
			
			beq $t2, ' ', espaco		# se t2 = ' ', executa espaco
			nop
			
			beq $t2, '\n', barraN		# se t2 = '\n', executa barraN
			nop
			
			bgt $t2, 47, numeros		# se t2 > 47, executa numeros
			nop
			
			erro:
			# print mensagem "ERRO"
			li $v0, 4
			la $a0, error
			syscall
			
		j ciclo
		nop

soma:
	blt $s7, $s3, elementosInsuficientes
	nop
	subi $s7, $s7, 4				# posição anterior (último elemento da stack)
	lw $t3, -4($s7)					# t3 - penúltimo elemento da stack
	lw $t4, 0($s7)					# t4 - último elemento da stack
	subi $s7, $s7, 4				# decrementa a posição da pilha
	add $t3, $t3, $t4				# t3 = t3 + t4
	sw $t3, 0($s7)					# coloca t3 no topo da pilha
	addi $s7, $s7, 4				# pilha pronta a receber valores
	j loop
	nop
	
subt:
	blt $s7, $s3, elementosInsuficientes
	nop
	subi $s7, $s7, 4				# posição anterior (último elemento da stack)
	lw $t3, -4($s7)					# t3 - penúltimo elemento da stack
	lw $t4, 0($s7)					# t4 - último elemento da stack
	subi $s7, $s7, 4				# decrementa a posição da pilha
	sub $t3, $t3, $t4				# t3 = t3 - t4
	sw $t3, 0($s7)					# coloca t3 no topo da pilha
	addi $s7, $s7, 4				# pilha pronta a receber valores
	j loop
	nop
	
prod:
	blt $s7, $s3, elementosInsuficientes
	nop
	subi $s7, $s7, 4				# posição anterior (último elemento da stack)
	lw $t3, -4($s7)					# t3 - penúltimo elemento da stack
	lw $t4, 0($s7)					# t4 - último elemento da stack
	subi $s7, $s7, 4				# decrementa a posição da pilha
	mult $t3, $t4					# t3 = t3 * t4
	mflo $t3
	sw $t3, 0($s7)					# coloca t3 no topo da pilha
	addi $s7, $s7, 4				# pilha pronta a receber valores
	j loop
	nop
	
divi:
	blt $s7, $s3, elementosInsuficientes
	nop
	subi $s7, $s7, 4				# posição anterior (último elemento da stack)
	lw $t3, -4($s7)					# t3 - penúltimo elemento da stack
	lw $t4, 0($s7)					# t4 - último elemento da stack
	beq $t4, $t0, errodivisao
	nop
	subi $s7, $s7, 4				# decrementa a posição da pilha
	div $t3, $t4					# t3 = t3 / t4
	mflo $t3
	sw $t3, 0($s7)					# coloca t3 no topo da pilha
	addi $s7, $s7, 4				# pilha pronta a receber valores
	j loop
	nop
	errodivisao:
		addi $s7, $s7, 4
		li $v0, 4
		la $a0, byzero
		syscall
		
		j loop
		nop
		
nega:
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'e', erro				# se t2 != e, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'g', erro				# se t2 != g, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1
	bne $t2, ' ', controlneg
	nop
	contneg:
	blt $s7, $s2, elementosInsuficientes
	nop
	subi $s7, $s7, 4				# decrementa a posição da pilha
	lw $t3, 0($s7)					# t3 - último elemento da stack
	sub $t3, $zero, $t3				# t3 = -t3
	sw $t3, 0($s7)					# coloca t3 no topo da pilha
	addi $s7, $s7, 4				# pilha pronta a receber valores
	j loop
	nop
	controlneg:
		beq $t2, '\n', contneg
		addi $s6, $s6, 1
		j erro
		nop
		
swap:
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'w', erro				# se t2 != w, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'a', erro				# se t2 != a, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'p', erro				# se t2 != p, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, ' ', controlswap			# se t2 != ' ', executa erro
	nop
	contswap:
	blt $s7, $s3, elementosInsuficientes
	nop
	subi $s7, $s7, 4				# decrementa a posição da pilha
	lw $t3, 0($s7)					# tira o ultimo elemento da pilha
	lw $t4, -4($s7)					# tira o penultimo elemento da pilha
	sw $t3, -4($s7)                    		# coloca t3 no topo da pilha
	sw $t4, 0($s7)                    		# coloca t3 no topo da pilha
	addi $s7, $s7, 4                		# pilha pronta a receber valores
    	j loop
    	nop
	
	controlswap:
		beq $t2, '\n', contswap
        	addi $s6, $s6, 1
        	j erro
        	nop
        	
clear:
	lb $t2, 0($s6)                    		# load do byte para t2 (caractere)
    	addi $s6, $s6, 1               			# próxima posição
    	bne $t2, 'l', erro                		# se t2 != l, executa erro
    	nop
    	lb $t2, 0($s6)                    		# load do byte para t2 (caractere)
    	addi $s6, $s6, 1               			# próxima posição
    	bne $t2, 'e', erro                		# se t2 != e, executa erro
    	nop
    	lb $t2, 0($s6)                    		# load do byte para t2 (caractere)
    	addi $s6, $s6, 1               			# próxima posição
    	bne $t2, 'a', erro                		# se t2 != a, executa erro
    	nop
    	lb $t2, 0($s6)                    		# load do byte para t2 (caractere)
    	addi $s6, $s6, 1               			# próxima posição
    	bne $t2, 'r', erro                		# se t2 != r, executa erro
    	nop
    	lb $t2, 0($s6)                    		# load do byte para t2 (caractere)
    	addi $s6, $s6, 1               			# próxima posição
    	bne $t2, '\n', erro                		# se t2 != '\n', executa erro
    	nop
    	
    	beq $s7, $s4, emptys
    	nop
    	la $s7, pilha					# reset da pilha (volta à posição inicial)
    	
    	j print
    	nop
    	
funcoesd:
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	beq $t2, 'u', dup				# se t2 != u, executa erro
	nop
	beq $t2, 'r', drop				# se t2 = r, executa drop
	nop
	j erro
	nop
	
dup:
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'p', erro				# se t2 != p, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, ' ', controldup			# se t2 != ' ', executa erro
	nop
	contdup:
	blt $s7, $s2, elementosInsuficientes
	nop
	subi $s7, $s7, 4
	lw $t3, 0($s7)                    		# t3 - último elemento da stack
	addi $s7, $s7, 4
	sw $t3, 0($s7)
	addi $s7, $s7, 4
	
	j loop
	nop
	
	controldup:
		beq $t2, '\n', contdup			# se t2 = ' ', executa contdup
		nop
		j erro
		nop
		
drop:
        lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'o', erro				# se t2 != o, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'p', erro				# se t2 != p, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, ' ', controldrop			# se t2 != ' ', executa erro
	nop	
	contdrop:
	blt $s7, $s2, elementosInsuficientes
	nop
	subi $s7, $s7, 4
	
	j loop
	nop
	
	controldrop:
		beq $t2, '\n', contdrop			# se t2 = ' ', executa contdrop
		nop
		j erro
		nop
	
off:
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'f', erro				# se t2 != f, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'f', erro				# se t2 != f, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, '\n', erro
	nop
	
	j sair
	nop
	
help:
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'e', erro				# se t2 != e, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'l', erro				# se t2 != l, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, 'p', erro				# se t2 != p, executa erro
	nop
	lb $t2, 0($s6)					# load do byte para t2 (caractere)
	addi $s6, $s6, 1				# próxima posição
	bne $t2, '\n', erro
	nop
	
	li $v0, 4
	la $a0, hlptxt
	syscall
	
	j ciclo
	nop
	
espaco:
	beq $t1, $zero, loop				# se t1 = 0 (caractere anterior não é número), executa loop
	nop
	beq $s7, $s5, maiselem
	nop
	sw $t0, 0($s7)					# coloca o numero na pilha
	addi $s7, $s7, 4				# pilha pronta para receber mais valores
	add $t1, $zero, $zero				# t1 = 0 (reset da variável de controlo)
	add $t0, $zero, $zero				# t0 = 0 (reset da variável numérica)
	j loop
	nop
	
barraN:
	beq $t1, $zero, darPrint
	nop
	beq $s7, $s5, maiselem
	nop
	sw $t0, 0($s7)					# coloca o numero na pilha
	addi $s7, $s7, 4				# pilha pronta para receber mais valores
	add $t1, $zero, $zero				# t1 = 0 (reset da variável de controlo)
	add $t0, $zero, $zero				# t0 = 0 (reset da variável numérica)
	darPrint:
		j print
		nop
		
numeros:
	bgt $t2, 57, erro				# se t2 > 57, executa erro
	nop
	addi $t1, $t1, 1				# incrementa a variável de controlo
	subi $t2, $t2, 48				# conversão de caractere para inteiro
	addi $t5, $zero, 10				# t5 = 10
	mult $t0, $t5					# t0 = t0 * t5 ( num = num * 10 )
	mflo $t0
	add $t0, $t0, $t2				# t0 = t0 + t2
	j loop
	nop
	
print:
	la $s0, 0($s7)					# ultima posição da pilha (onde será colocado o próximo valor)
	la $s1, pilha					# s1 - endereço do primeiro elemento da pilha
	
	# print de "Stack:\n"
	la $a0, stcktxt
	li $v0, 4
	syscall
	
	# stack vazia
	beq $s0, $s1, empty
	nop
	
	printLoop:
		# print de TAB
		la $a0, tab
		syscall
	
		# print do elemento da pilha
		lw $a0, 0($s1)
		li $v0, 1
		syscall
		
		addi $s1, $s1, 4			# próximo elemento

		# print de '\n'
		la $a0, nxtln
		li $v0, 4
		syscall
		
		blt $s1, $s0, printLoop			# se s0 < s1, executa printLoop
		nop
	
	j ciclo
	nop
	
	empty:
		# print de TAB
		la $a0, tab
		syscall
		
		# print de (empty)
		la $a0, vazia
		li $v0, 4
		syscall
		
		# print de '\n'
		la $a0, nxtln
		li $v0, 4
		syscall
		
		j ciclo
		nop
		
elementosInsuficientes:
	# print da mensagem de erro
	li $v0, 4
	la $a0, elmnts
	syscall
	
	j print
	nop
	
emptys:
	# print da mensagem sempty
	li $v0, 4
	la $a0, sempty
	syscall
	
	j print
	nop
	
maiselem:
	li $v0, 4
	la $a0, elemms
	syscall
	
	j controlar
	nop
			
			
sair:
	# print de mensagem
	la $a0, bye
	li $v0, 4
	syscall
	
	# programa termina
	li $v0, 10
	syscall
