package sample;

/**
 * Интерфейс Observer.
 * На его основе построен класс представления View.
 * Выполняет роль слушателя для зарегистрированных классов.
 *
 * @author A. Nosov
 * @version 1.0
 */
//Слушатель для всех зарегистрированных классов View
interface Observer {
    void notification(String message);

}

