package myasm.com.register;

import java.util.ArrayList;
import java.util.List;

public class TemplateConfig {

    List<BaseTemplate> list = new ArrayList<>();

    public void register(BaseTemplate template){
        list.add(template);
    }

    public List<BaseTemplate> getList(){
        return list;
    }
}
