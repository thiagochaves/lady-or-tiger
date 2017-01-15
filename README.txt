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