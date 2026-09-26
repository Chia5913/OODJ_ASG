package dao;
import java.util.List;


public interface InterfaceLoadAdmin<X> {

    public String readLoadFile();
    public List<X> getObjectList();

}