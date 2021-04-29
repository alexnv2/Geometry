package sample;
//Второй класс для вывода на экран
class View1 implements Observer{
    Model model=new Model();

    @Override
    //Какую информацию надо вывести, переопредеояем класс интерфейса
    public  void  notification(String message){
        System.out.println(message+" View1");
    }
}