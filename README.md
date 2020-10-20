# Travelling Salesman Problem - Solução com algoritmo genético

A aplicação consiste em uma solução concorrente para o Problema do Caixeiro Viajante, utilizando um algoritmo genético.
Para otimizar o tempo de execução, o cálculo do "fitness" para cada solução (lista ordenada das cidades) está sendo dividido entre várias threads.
Dessa forma, se a população conter 1000 soluções e forem utilizadas 4 threads, cada thread será responsável por calcular o "fitness" de 250 soluções.

Para sincronizar a execução de cada thread está sendo utilizado um `CountDownLatch`, inicializado com um valor correspondente ao número de threads. Quando uma thread termina de executar, invoca o método `countDown`, decrementando o contador. Quando o contador chegar a 0, a execução do algoritmo genético continua. 