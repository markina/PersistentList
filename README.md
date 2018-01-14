# Частично персистентный связный список с O(1) на операцию. 

Частично персистентный связный список с O(1) на операцию с методами path coping и fat node.

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

Во всех операциях по индексу сначала выполняется поиск узла по интексу за O(n), и 
далее выполняется операция. 

В операциях с началом или концом списка поиск выполнять не нужно, 
следовательно, время будет равно времи обновлению ссылок, амортизационная стоимость обновления - O(1).  
Есть добаление/изменение/удаление происходило бы по ссылке, которая указывает на середину списка, то 
операция добаления/изменения/удаления в целом бы занимала O(1) времени. 

Амортизационная стоимость затрат по памяти на любую опарацию O(1).

Методы измнения за O(n) времени:
   
    void add(int index, E value);
    void set(int index, E newValue);
    void remove(int index);
Методы измнения за O(1) времени:
    
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

Есть массив голов(heads) и хвостов(tails), который представляет из себя список 
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
3. Делее надо связать ссылками текущуюю подвершину в парую и левую стороны.  
 
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
1. Извлеч FatNode из массива heads[version]. 
2. Получить Node по FatNode.
3. Пройти по ссылку next в следующую FatNode.
4. Повторить с пункта 2, пока есть слудующий элемент. 

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
работает на обычном массиву в java, будем считать, что он работает правильно. Значит для пороверки 
корректности алгоритма достаточно проверить совпадение значений при обходе списков всех версий. 

## Сравнение времи работы и памяти 


## Сборка
* [Maven](https://maven.apache.org/) - Dependency Management

## Запуск тестов
Тесты можно запустить используя maven.
```
maven test
```

## Автор
* **Маргарита Маркина**(ИТМО, M4239) - [Margarita Markina](https://github.com/markina) 
