package dao;

public interface InterfaceWriteAdmin<Y> {

    public String writeFile(String x);
    public String writeFile(List<Y> x);
    public String writeFile(String x, String filePath);
    public String writeFile(List<Y> x, String filePath);
    
}