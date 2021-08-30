package com.intellekta.linkedmassive.etalon;


import java.util.*;


public class Installer implements Subsystem {
    List<Subsystem> endpointList = new ArrayList<>();
    List<Subsystem> fullList = new ArrayList<>();
    List<Subsystem> fullPrerequisites = new ArrayList<>();
    private String name;
    private int version;
    private Subsystem[] prerequisites;

    public Queue<Subsystem> setUpPlan() {
        LinkedList<Subsystem> queue = new LinkedList<>(); //Итоговая очередь

        //1. Нужно развернуть исходный набор в полный список элементов и выявить среди них те, которые относятся к
        // конечным элементам графа
        endpointList = new ArrayList<>(); //endpoint-ы, то что ставится первым в цепочке
        fullList = new ArrayList<>(); // полный список всех элементов
        fullList.add(this);// добавляем в полный список сам элемент

        if (fillList(new ArrayList<>(), this)) { // пытаемся простроить граф из базовых пререквизитов, если удалось
            //2. Нужно построить дополненный граф на базе неявных требований (предварительная установка ранних версий компонента)
            createExtraGraph();
            //3. нужно сформировать итоговую очередь на базе решения задачи поиска в глубину по дополненному графу
            addToQueue(queue, this); //формируем очередь
        }

        return queue;
    }

    private void createExtraGraph() {
        for (Subsystem subsystem : fullList) { //Это цикл для создания дополненного графа, в нем пробегаем по известным узлам графа
            ((Installer) subsystem).fullPrerequisites = new ArrayList<>(); // Это список для хранения дополненного графа
            ((Installer) subsystem).fullPrerequisites.addAll(Arrays.asList(subsystem.getPrerequisites())); //создаем дополненный граф из базовых реквизитов
            int version = -1; //считаем, что базовых версий нет, поэтому версия -1
            Subsystem parentVersion = null;//сам базовый компонент неизвестен
            for (Subsystem subsystem2 : fullList) { //снова пробегаем по всем, ищем те, которые относятся к текущей подсистеме и имеют максимальную из меньших версий
                if (subsystem2.getName().equals(subsystem.getName()) && subsystem2.getVersion() < subsystem.getVersion() && subsystem2.getVersion() > version) {//если такие есть
                    version = subsystem2.getVersion();// запоминаем версию
                    parentVersion = subsystem2;// и сам объект
                }
            }
            if (parentVersion != null && !((Installer) subsystem).fullPrerequisites.contains(parentVersion)) { //если такие нашлись и объекта такого еще нет в дополненном графе
                ((Installer) subsystem).fullPrerequisites.add(parentVersion);//дописываем его в дополненный граф
                endpointList.remove(subsystem);
            }
        }
    }

    private void addToQueue(LinkedList<Subsystem> queue, Subsystem subsystem) {
        queue.push(subsystem); //очередь формируем в обратном порядке, т.е. как стек

        for (int i = 0; i < subsystem.getPrerequisites().length; i++) {// цикл пока все элементы базового графа не будут добавлены в очередь
            int maxPath = -1; //считаем, что максимальный путь -1, чтобы найти потом максимальный путь от пререквизита до листьев и установить последним тот, который имеет самый длинный путь
            Subsystem maxSubsystem = null; //считаем добавляемый элемент null-ом
            for (Subsystem prerequisit : subsystem.getPrerequisites()) { //пробегаем по каждому пререквизиту, вычисляем наибольшее расстояние до самого дальнего листа
                if (!queue.contains(prerequisit)) {//если этот элемент в очереди уже есть, его игнорируем
                    for (Subsystem endpoint : endpointList) {//вычисляем расстояние текущего элемента до каждого из листов, находим максимальный из них
                        int path = findLongestWay((Installer) prerequisit, (Installer) endpoint, 0); //ищем максимальный путь от текущего элемента до текущего листа
                        if (path >= maxPath) { //если он больше текущего максимума, считаем его новым максимумом
                            maxPath = path; //запоминаем длину пути
                            maxSubsystem = prerequisit; //и сам элемент
                        }
                    }
                }
            }
            if (maxSubsystem != null) //если максимум нашли - добавляем его в очередь рекурсивно. Иначе рекурсия прервется
                addToQueue(queue, maxSubsystem);//вызываем рекурсию
        }
    }

    private int findLongestWay(Installer from, Installer to, int currentResult) {
        int result = 0; //текущее расстояние от from до to считаем нулевым - гипотеза from равно to
        if (from.fullPrerequisites.contains(to)) //если to стоит в списке пререквизитов from
            result = 1;//расстояние равно 1
        else if (from.fullPrerequisites.size() == 0 && from != to) //если нет и список пререквизитов from пустой
            result = Integer.MIN_VALUE;//значит от from до to недобраться
        else if (from != to) { //в противном случае если from это не to, нужно искать дальше
            int maxResult = 0; //считаем, что расстояние будет 0
            for (Subsystem installer : from.fullPrerequisites) { //бежим по дополненному подграфу from и ищем самый длинный путь через пререквизиты from
                int subResult = findLongestWay((Installer) installer, to, currentResult + 1); //берем первый пререквизит from и вычисляем расстояние от него до to, запоминая, что провалились на 1 шаг ниже (+1)
                if (subResult >= 0 && maxResult < subResult) { //если расстояние больше или рано нулю и больше текущего максимального пути
                    maxResult = subResult;// запоминаем максимальное расстояние
                }
            }
            result = maxResult; //максимальное расстояние от from до to
        }
        return result + currentResult; //таким образом полное расстояние по текущему пути - длина пути до from и от него до to
    }

    private boolean fillList(List<Subsystem> currentList, Subsystem current) {
        currentList.add(current); //это текущий путь графа, добавляем в неё текущий элемент
        boolean isOk = current.getPrerequisites() != null;//если список пререквизитов не определен - сразу ошибка
        boolean okRecursive = true;//переменная, которая будет использоваться для определения того, обработал ли предыдущий проход сообщение об ошибке
        if (isOk && current.getPrerequisites().length > 0) { // Если пока ошибок нет и в списке пререквизитов что-то есть
            for (int i = 0; i < current.getPrerequisites().length && isOk; i++) {//пробегаем по всем пререквизитам, если не встретим ошибки
                if (isOk = current.getPrerequisites()[i] != null) {//если в пререквизите пустой список пререквизитов - запоминаем ошибку
                    if (!fullList.contains(current.getPrerequisites()[i])) //проверяем, если ли текущий элемент в списке элементов графа
                        fullList.add(current.getPrerequisites()[i]); //если его там нет - добавляем
                    if (isOk = !currentList.contains(current.getPrerequisites()[i])) //проверяем есть ли циклические ссылки (элемент ссылается на элемент, который уже есть в текущем пути графа)
                        isOk = okRecursive = fillList(currentList, current.getPrerequisites()[i]); //если циклических нет, пытаемся пройти дальше вглубь графа по текущему пути, если пришла ошибка, значит раньше обработали
                }
            }
        } else if (isOk && !endpointList.contains(current))//если список пререквизитов пуст, нет ошибок и элемента нет в списке эндпоинтов - добавляем его туда
            endpointList.add(current);

        if (!isOk && okRecursive) //если была ошибка, и она возникла только в этом проходе
            System.out.println(String.format("SetUp plan calculation failed. Wrong prerequisite description at %s %d.", current.getName(), current.getVersion()));

        currentList.remove(currentList.size() - 1); //исключаем из текущей части пути текущий элемент (делаем шаг назад)

        return isOk; // возвращаем в качестве результата успех или неуспех формирования графа
    }

    public void setUp(Queue<Subsystem> subsystems) {
        while (subsystems != null && !subsystems.isEmpty())
            subsystems.poll().install();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public void install() {
        System.out.println(String.format("%s version %d installed successfully", name, version));
    }

    @Override
    public Subsystem[] getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Subsystem[] prerequisites) {
        this.prerequisites = prerequisites;
    }
}
