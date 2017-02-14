Lady or Tiger?
--------------

Coisas a fazer
--------------
* Paralelizar o processamento

A expansão e fechamento de cada ramo pode ser paralelizada.
Para cada ramo:
    crie algo que vai executar a expansão do ramo
    agende a execução
O executor:
    age sobre o ramo
    notifica o controlador sobre que ação foi realizada

Situações do executor:
1) Encontra uma expansão alfa
    Modifica o ramo, acrescentando os itens necessários. Supondo que cada ramo só é tocado por um executor por vez, consigo fazer isso sem travar.
    Continua com a expansão.
2) Encontra uma expansão gama
    Igual expansão alfa.
3) Encontra uma expansão beta
    Modifica o ramo atual e cria um novo ramo.
    Notifica o controlador que um novo ramo foi criado e devolve os dois ramos para ele.
4) Fecha o ramo
    Notifica o controlador que o ramo foi fechado.
    Remover o ramo é mais complicado.
    Talvez guardar os ramos num Map em Tableau, cada um com um ID que serve de chave. Assim apagar um ramo não interfere na numeração dos demais.
5) Ramo aberto e não expansível
    Notifica o controlador que o ramo foi todo expandido e continua aberto.


Corrigir: Quando eu duplico um ramo, as afirmativas da cópia que já tinham sido expandidas na original estão sendo expandidas novamente?
Acho que um efeito colateral de ter removido os envelopes é esse.

Eu acredito que não há como especificar se as portas 3, 4 e 5 no problema 12 possuem um tigre ou estão vazias.
As sentenças não fazem menção a isso. O máximo que dá para saber é que a moça não está atrás delas.

Para tratar o caso de informação extra no problema 12, que tal uma nova linha com algo do tipo !(em(?, 8)), e aí
o solucionador gera um tableau para cada objeto e retorna a solução como a interseção das soluções não vazias?

  * Criar uma linguagem para especificar as consultas acerca dos resultados.
  * Talvez uma para questionar o caminho de execução? Por exemplo, perguntar por que determinada afirmativa tem que ser falsa?

Tradução de count()
-------------------

count(x) = 0
!x

count(x) = 1
x

count(x) > 0
x

count(x) < 0
erro

count(x,y) = 0
!x ^!y

count(x,y) = 1
x v y
x <=> !y
x y
0 0 F
0 1 T
1 0 T
1 1 F
(!x ^ y) v (x ^ !y) -> * cria apenas um ramo extra
 x <=> !y
 x -> !y
 !y -> x
 (!x v !y) ^ (y v x) -> * cria mais ramos
 * equivalentes logicamente

count(x,y) = 2
x ^ y

count(x,y) > 0
x v y

count(x,y) < 0
erro

count(x,y) > 1
x ^ y

count(x,y) < 1
!x ^!y

count(x,y) > 2
erro

count(x,y) < 2
!x v !y

count(x,y,z) = 1
x <=> !y^!z
y <=> !x^!z
z <=> !x^!y

count(x,y,z) = 2
!x <=> y^z
!y <=> x^z
!z <=> x^y

count(x,y,z) > 0
x v y v z

count(x,y,z) > 1
!x -> y^z
!y -> x^z
!z -> x^y

count(x,y,z) < 2
x -> !y^!z
y -> !x^!z
z -> !x^!y

Quine–McCluskey algorithm
não resultará nas implicações
mas se der todas as possibilidades, vai dar certo
é fácil montar a tabela da verdade

