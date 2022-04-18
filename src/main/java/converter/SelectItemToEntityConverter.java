package converter;

import entities.*;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;
import java.util.function.Predicate;

@FacesConverter(value = "SelectItemToEntityConverter")
public class SelectItemToEntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Object o = null;
        if (!(value == null || value.isEmpty())) {
            o = this.getSelectedItemAsEntity(component, value);
        }
        return o;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String s = "";
        if (value != null) {
            if(value instanceof Book) s = ((Book) value).getId().toString();
            else if(value instanceof Reader) s = ((Reader) value).getId().toString();
        }
        return s;
    }
    
    private BaseEntity getSelectedItemAsEntity(UIComponent comp, String value) {
        BaseEntity item = null;
        List<BaseEntity> selectItems;
        for (UIComponent uic : comp.getChildren()) {
            if (uic instanceof UISelectItems) {
                Integer itemId = Integer.valueOf(value);
                selectItems = (List<BaseEntity>) ((UISelectItems) uic).getValue();

                if (itemId != null && selectItems != null && !selectItems.isEmpty()) {
                    Predicate<BaseEntity> predicate = i -> i.getId().equals(itemId);
                    item = selectItems.stream().filter(predicate).findFirst().orElse(null);
                }
            }
        }
        return item;
    }
    
}
