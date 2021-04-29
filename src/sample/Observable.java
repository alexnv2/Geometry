package sample;
//Наблюдатель для класса Model
interface  Observable{
    void registerObserver(Observer o);//регистрация слушателей
    void  notifyObservers(String message);//Передача сообщений для слушутелей
}