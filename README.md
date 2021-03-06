# Частично персистентный связный список 

Частично персистентный связный список с методами path coping и fat node.

## Общие понятия
Связный список — базовая динамическая структура данных в информатике, состоящая из узлов, 
каждый из которых содержит как собственно данные, так и одну или две ссылки («связки») на 
следующий и/или предыдущий узел списка. 

Частично персистентная структура данных - это структуры данных, которые при 
внесении в них каких-то изменений сохраняют все свои предыдущие состояния 
и доступ к этим состояниям. В частично персистентных структурах данных к каждой версии можно 
делать запросы, но изменять можно только последнюю версию структуры данных. 

## Интерфейс структуры данных

### Интерфейс персистентного списка 

Операции делятся на два типа:
1. Операции по индексу. 
2. Операции с началом или концом списка. 

Во всех операциях по индексу сначала выполняется поиск узла по индексу за O(n), и 
далее выполняется операция. 

В операциях с началом или концом списка поиск выполнять не нужно, 
следовательно, время будет равно времени обновлению ссылок, амортизационная стоимость обновления - O(1).  
Есть добавление/изменение/удаление происходило бы по ссылке, которая указывает на середину списка, то 
операция добавления/изменения/удаления в целом бы занимала O(1) времени. 

Амортизационная стоимость затрат по памяти на любую операцию O(1).

Методы изменения за O(n) времени:
   
    void add(int index, E value);
    void set(int index, E newValue);
    void remove(int index);
Методы изменения за O(1) времени:
    
    void addFirst(E value);
    void addLast(E value);
    void removeTail();
    void removeLast();
    void setTail(E value);
    void setLast(E value);
Получение значения за O(1) времени:
   
    E getFirst(int version);
    E getLast(int version);
    E getFirst();
    E getLast();
Получение значения за O(n) времени:
    
    E get(int index, int version);
    E get(int index);
Получение информации за O(1) времени:

    int getCurrentVersion();
    int getCurrentSize();
    boolean isEmpty(int version);
    boolean isEmpty();
Получение информации за O(n) времени:

    int getSize(int version);
Получение итератора за O(1) времени:
    
    IIterator<E> getHeadIterator(int version);
    IIterator<E> getTailIterator(int version);
    IIterator<E> getHeadIterator();
    IIterator<E> getTailIterator();    
Получение итератора за O(n) времени:

    IIterator<E> getIterator(int index, int version);
    IIterator<E> getIterator(int index);
См. документацию для деталей.

### Интерфейс итератора

По итератору можно получить текущее значение.
Итератор можно сдвинуть вперед, назад. 

    boolean hasNext();
    boolean hasPrev();
    void next();
    void prev();
    E getValue();

См. документацию для деталей.

## Описание структуры данных

Есть два вида вершин: 
* Fat nodes (Большие вершины)
* Nodes (Подвершины)

Большие вершины содержат:
* одну или две подвершины

Подвершины содержат: 
* значение
* номер версии, на которой была создана подвершина
* ссылку на следующую большую вершину
* ссылку на предыдущую большую вершину

Если следующего/предыдущего элемента нет, то ссылка = null.

Есть массив голов(heads) и хвостов(tails), который представляет собой список 
больших вершин, по большой вершине можно получить первый Node списка. 

Выбор Node из FatNode по версии v:
* Если большая вершина имеет одну подвершину - выбираем эту подвершину.
* Если несколько, то выбираем вершину, версия создания которой меньше или равна v.   

## Описание операций 

### Связка вершин
Для начала введем понятие связки вершины(node) в большой вершине(fatNode) с правой
большой вершиной(rightFatNode) (для левой все делает аналогично) 
* Если rightFatNode имеет одну подвершину, то создаем вторую подвершину в rightFatNode
и ссылка на предыдущую будет указывать на node, а следующая у node будет указывать 
на rightFatNode.
* Если rightFatNode имеет две подвершины, то создаем новую большую правую вершину
(newRightFatNode), где будет одна подвершина, у которой ссылка на предыдущюю будет 
указывать на node. Далее запустим этот процесс для newRightFatNode. 

### Добавление
Для добавления нового значения value в список надо:
1. Создать новую большую вершину
2. Добавить в нее одну подвершину со значением value
3. Далее надо связать ссылками текущую подвершину в правую и левую стороны.  
 
### Изменение
* Если текущая большая вершина имеет одну подвершину, то надо добавить вторую 
подвершину с новым значением и связать в правую и левую стороны. 
* Если текущая большая вершина имеет одну подвершины, то надо добавить большую вершину
с одной подвершиной и запустить связку в правую и левую стороны. 

### Удаление
Надо добавить по одной новой подвершине(или создать одну новую большую вершину 
с подвершиной) у правого и левого соседа вершины, 
которую надо удалить, и связать в правую и левую стороны новые подвершины 
правого и левого соседа.  

### Получение итератора и проход по списку
Для получение итератора и проход по списку надо 
1. Извлечь FatNode из массива heads[version]. 
2. Получить Node по FatNode.
3. Пройти по ссылку next в следующую FatNode.
4. Повторить с пункта 2, пока есть следующий элемент. 

## Тесты
Для проверки был написан наивный алгоритм частично персистентного списка, который 
работает на массиве массивов.  
Сравнение операций: 
 
|            Операция           	| Наивная реализация: время 	| Наивная реализация: память 	| Персистентный список: время 	| Персистентный список: память 	|
|:-----------------------------:	|:-------------------------:	|----------------------------	|:---------------------------:	|------------------------------	|
| Добавление по индексу         	|            O(n)           	|            O(n)            	|             O(n)            	|             O(1)             	|
| Изменение по индексу          	|            O(n)           	|            O(n)            	|             O(n)            	|             O(1)             	|
| Удаление по индексу           	|            O(n)           	|            O(n)            	|             O(n)            	|             O(1)             	|
| Добавление в начало или конец 	|            O(n)           	|            O(n)            	|             O(1)            	|             O(1)             	|
| Изменение в начало или конец  	|            O(n)           	|            O(n)            	|             O(1)            	|             O(1)             	|
| Удаление в начало или конец   	|            O(n)           	|            O(n)            	|             O(1)            	|             O(1)             	|
 
 
Тесты покрывают все виды операций. Поскольку наивный алгоритм 
работает на обычном массиве в java, будем считать, что он работает правильно. Значит для проверки 
корректности алгоритма достаточно проверить совпадение значений при обходе списков всех версий. 

## Сравнение времени работы и памяти 

### Время работы 

Время работы оценивалось на n = 5000, если взять больше, то наивная реализация получала 
OutOfMemoryError: Java heap space. Для операций удаления и изменения сначала было 
добавлено n элементов, затем производился замер времени. 

|            Операция           	| Наивная реализация, мс        | Персистентный список, мс     	|
|:-----------------------------:    |:-------------------------:	|:---------------------------:	|
| Добавление по индексу          	|            1279           	|             98            	|
| Изменение по индексу          	|            1875           	|             83            	|
| Удаление по индексу           	|            724               	|             93            	|
| Добавление в начало            	|            3881           	|             2             	|
| Добавление в конец            	|            453               	|             2             	|
| Изменение в начале              	|            778               	|             1             	|
| Изменение в конце             	|            1195              	|             2             	|
| Удаление в начале                	|            1534              	|             3             	|
| Удаление в конце                	|            1056              	|             1                	|

### Память

Время используемая оценивалась на n = 5000, если взять больше, то наивная реализация получала 
OutOfMemoryError: Java heap space. Для операций удаления и изменения сначала было 
добавлено n элементов, затем производился замер памяти. 

|            Операция           	| Наивная реализация, MiB       | Персистентный список, MiB    	|
|:-----------------------------:    |:-------------------------:	|:---------------------------:	|
| Добавление по индексу          	|            288            	|             1             	|
| Изменение по индексу          	|            568               	|             1               	|
| Удаление по индексу           	|            290               	|             1              	|
| Добавление в начало            	|            288            	|             1             	|
| Добавление в конец            	|            288               	|             1             	|
| Изменение в начале              	|            568               	|             1             	|
| Изменение в конце             	|            579              	|             1             	|
| Удаление в начале                	|            290              	|             1             	|
| Удаление в конце                	|            289              	|             1                	|

## Графики 

Графики для всех функций по времени. График отображает суммарное время до n-ой операции 
для двух реализаций персистентного списка. Naive Persistent List - наивная реализация,
Persistent List - реализация с методами path coping и fat node. Замечание: в графиках 
пренебрегается иногда 
возникающим задержкам, где на одну операцию тратится в 100-1000 раз больше времени, чем 
на операции рядом, скорее всего в этот момент происходить выделение памяти. 


![](./src/test/resources/randomAdd.png)
![](./src/test/resources/randomSet.png)
![](./src/test/resources/randomRemove.png)
![](./src/test/resources/addFirst.png)
![](./src/test/resources/addLast.png)
![](./src/test/resources/setFirst.png)
![](./src/test/resources/setLast.png)
![](./src/test/resources/removeFirst.png)
![](./src/test/resources/removeLast.png)

Графики для всех функий по памяти. График отображает суммарные затраты памяти 
до n-ой операции для двух реализаций персистентного списка. 
Naive Persistent List - наивная реализация,
Persistent List - реализация с методами path coping и fat node.

![](./src/test/resources/memoryRandomAdd.png)
![](./src/test/resources/memoryRandomSet.png)
![](./src/test/resources/memoryRandomRemove.png)
![](./src/test/resources/memoryAddFirst.png)
![](./src/test/resources/memoryAddLast.png)
![](./src/test/resources/memorySetFirst.png)
![](./src/test/resources/memorySetLast.png)
![](./src/test/resources/memoryRemoveFirst.png)
![](./src/test/resources/memoryRemoveLast.png)

## Сборка
* [Maven](https://maven.apache.org/) - Dependency Management

## Запуск тестов
Тесты можно запустить используя maven.
```
maven test
```

## Автор
* **Маргарита Маркина**(ИТМО, M4239) - [Margarita Markina](https://github.com/markina) 
